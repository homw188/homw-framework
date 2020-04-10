package com.homw.tool.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homw.common.util.DateUtil;
import com.homw.tool.dao.PayRecordGenerateDao;
import com.homw.tool.entity.AgreementEntity;
import com.homw.tool.entity.PayRecordEntity;
import com.homw.tool.service.IPayRecordGenerateService;

@Service
public class PayRecordGenerateService implements IPayRecordGenerateService {
	@Autowired
	private PayRecordGenerateDao payRecordGenerateDao;

	private static Logger logger = LoggerFactory.getLogger(PayRecordGenerateService.class);

	@Override
	public void generate(Long spaceId) throws Exception {
		// 查找该项目下无支付记录的合同
		List<AgreementEntity> agreementList = payRecordGenerateDao.queryListMissingPayRecord(spaceId);
		if (CollectionUtils.isNotEmpty(agreementList)) {
			logger.info("found agreements size:{}", agreementList.size());

			List<PayRecordEntity> recordList = new ArrayList<>();
			for (AgreementEntity agreement : agreementList) {
				Pair<List<Pair<Long, Long>>, Long> pair = calculateEveryMonthRentTime(agreement);
				for (Pair<Long, Long> triple : pair.getLeft()) {
					PayRecordEntity record = new PayRecordEntity();
					record.setPayerName(agreement.getApptName());
					record.setAgreementId(agreement.getAgreementId());
					record.setRecordStatus("NEW");
					record.setTotalAmt(BigDecimal.ZERO);
					record.setPayAmt(BigDecimal.ZERO);
					record.setSpaceId(spaceId);
					record.setOrderType("RENT");
					record.setPeriod(agreement.getPeriod());
					record.setYear(DateUtil.format(triple.getLeft(), "yyyy"));
					record.setMonth(DateUtil.format(triple.getLeft(), "MM"));
					record.setCreateTime(System.currentTimeMillis());
					record.setStartTime(triple.getLeft());
					record.setEndTime(triple.getRight());
					record.setStatus((short) 1);
					record.setVersion(0);
					recordList.add(record);
				}
			}
			// 批量保存支付记录
			payRecordGenerateDao.saveBatchRecord(recordList);
		} else {
			logger.info("not found agreements missing paying records.");
		}
	}

	/**
	 * 计算每个月的租赁周期
	 * 
	 * @param agreement
	 * @return
	 * @throws IllegalArgumentException
	 */
	private Pair<List<Pair<Long, Long>>, Long> calculateEveryMonthRentTime(AgreementEntity agreement) {
		Integer periodNum = agreement.getPeriodNum().intValue();
		// 若是初始建立的合同
		List<Pair<Long, Long>> pairList = new ArrayList<>();
		Long rentEndTime = 0l;

		logger.info("calculateRentTime start agreementId:{}", agreement.getAgreementId());

		if (agreement.getIsIncludeProFee() == null) {
			agreement.setIsIncludeProFee(true);
		}
		boolean flag = false;
		if (agreement.getFreeStartTime() != null || agreement.getFreeEndTime() != null) {
			flag = true;
		}
		// 若有免租期，租赁总时间是否小于单位时间
		int totalRentMonth = 0;
		if (flag) {
			if (agreement.getFreeStartTime().equals(agreement.getStartTime())) {
				totalRentMonth = calculateMonth(agreement.getFreeEndTime() + 1, agreement.getEndTime());
				logger.info("agreement free start time equals");
				if (totalRentMonth < agreement.getPeriodNum() /* && tempTime > agreement.getEndTime() */) {
					logger.info("totalRentMonth greater than periodNum");
					throw new IllegalArgumentException("agreement period fail");
				}
			} else if (agreement.getFreeEndTime().equals(agreement.getEndTime())) {
				totalRentMonth = calculateMonth(agreement.getStartTime(), agreement.getFreeStartTime() - 1);
				logger.info("agreement free end time equals");
				if (totalRentMonth < agreement.getPeriodNum() /* && tempTime > agreement.getFreeStartTime() */) {
					logger.info("totalRentMonth greater than periodNum");
					throw new IllegalArgumentException("agreement period fail");
				}
			}
		} else {
			totalRentMonth =calculateMonth(agreement.getStartTime(), agreement.getEndTime());
			logger.info("agreement no freetime equals");
		}

		logger.info("calculate totalRentMonth:{}", totalRentMonth);
		if (totalRentMonth < agreement.getPeriodNum()) {
			throw new IllegalArgumentException("agreement period fail");
		}
		Long startTime = agreement.getStartTime();
		if (flag) {
			if (agreement.getFreeStartTime().equals(agreement.getStartTime())) {
				startTime = agreement.getFreeEndTime() + 1;
			}
		}
		String startDay = DateUtil.format(startTime, "dd");
		if (!"01".equals(startDay)) {
			rentEndTime = DateUtil.parseTimeMillis(
					DateUtil.format(DateUtil.addMonth(startTime, periodNum + 1), "yyyyMM") + "01 00:00:00.000",
					"yyyyMMdd HH:mm:ss.SSS");
			rentEndTime = rentEndTime - 1;
			logger.info("calculate startDay != 01 rentEndTime:{}", rentEndTime);
		} else {
			rentEndTime = DateUtil.addMonth(startTime, periodNum) - 1;
			logger.info("calculate startDay == 01 rentEndTime:{}", rentEndTime);
		}
		if (rentEndTime > agreement.getEndTime()) {
			rentEndTime = agreement.getEndTime();
		}
		pairList.addAll(calculatePreRentTime(agreement.getStartTime(), agreement.getEndTime(), BigDecimal.ZERO));
		return Pair.of(pairList, rentEndTime);
	}
	
	public static int calculateMonth(Long startTime, Long endTime) {
		endTime = endTime + 1;
		Calendar start = Calendar.getInstance();
		start.setTimeInMillis(startTime);
		
		Calendar end = Calendar.getInstance();
		end.setTimeInMillis(endTime);
		
		if (end.equals(start))
			return 0;
		
		int num = 0;
		if (end.get(Calendar.YEAR) == start.get(Calendar.YEAR)
				&& end.get(Calendar.MONTH) == start.get(Calendar.MONTH)) {
			num = 1;
		} else if (end.get(Calendar.YEAR) > start.get(Calendar.YEAR)) {
			num = (end.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 12
					+ end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
		} else {
			num = end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
		}
		return num;
	}

	/**
	 * 计算之前的租赁周期
	 * 
	 * @param startTime
	 * @param endTime
	 * @param rentAmt
	 * @return
	 */
	private List<Pair<Long, Long>> calculatePreRentTime(Long startTime, Long endTime, BigDecimal rentAmt) {
		String freeStartDay = DateUtil.format(startTime, "dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(startTime);
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);

		BigDecimal curRentAmt = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
		List<Pair<Long, Long>> pairList = new ArrayList<>();
		if ("01".equals(freeStartDay)) {
			if (calendar.getTimeInMillis() >= endTime) {
				String endDay = DateUtil.format(endTime, "dd");
				BigDecimal propertyFee = BigDecimal.ZERO;
				if (rentAmt != null) {
					propertyFee = rentAmt;
				}
				BigDecimal preDayAmt = propertyFee.setScale(4).multiply(new BigDecimal(endDay))
						.multiply(new BigDecimal(12)).divide(new BigDecimal(365), 2);
				curRentAmt = curRentAmt.add(preDayAmt).setScale(2, BigDecimal.ROUND_HALF_UP);
			} else {
				BigDecimal propertyFee = BigDecimal.ZERO;
				if (rentAmt != null) {
					propertyFee = rentAmt;
				}
				curRentAmt = curRentAmt.add(propertyFee).setScale(2, BigDecimal.ROUND_HALF_UP);

				Pair<Long, Long> triple = Pair.of(startTime, calendar.getTimeInMillis());
				pairList.add(triple);
				pairList.addAll(calculatePreRentTime(calendar.getTimeInMillis() + 1, endTime, propertyFee));
				return pairList;
			}
		} else {
			if (calendar.getTimeInMillis() >= endTime) {
				String freeEndDay = DateUtil.format(endTime, "dd");
				BigDecimal propertyFee = BigDecimal.ZERO;
				if (rentAmt != null) {
					propertyFee = rentAmt;
				}
				BigDecimal preDayAmt = propertyFee.setScale(4).multiply(
						new BigDecimal(freeEndDay).subtract(new BigDecimal(freeStartDay)).add(new BigDecimal(1)))
						.multiply(new BigDecimal(12)).divide(new BigDecimal(365), 2);
				curRentAmt = curRentAmt.add(preDayAmt).setScale(2, BigDecimal.ROUND_HALF_UP);
			} else {
				BigDecimal propertyFee = BigDecimal.ZERO;
				if (rentAmt != null) {
					propertyFee = rentAmt;
				}
				BigDecimal preDayAmt = propertyFee.setScale(4)
						.multiply(new BigDecimal(freeStartDay).subtract(new BigDecimal(1))).multiply(new BigDecimal(12))
						.divide(new BigDecimal(365), 2);
				curRentAmt = curRentAmt.add(propertyFee.subtract(preDayAmt)).setScale(2, BigDecimal.ROUND_HALF_UP);

				Pair<Long, Long> pair = Pair.of(startTime, calendar.getTimeInMillis());
				pairList.add(pair);
				pairList.addAll(calculatePreRentTime(calendar.getTimeInMillis() + 1, endTime, rentAmt));
				return pairList;
			}
		}
		Pair<Long, Long> pair = Pair.of(startTime, endTime);
		pairList.add(pair);
		return pairList;
	}
}