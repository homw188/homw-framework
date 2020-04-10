package com.homw.tool.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homw.common.bean.ImportExcel;
import com.homw.tool.dao.DeviceInfoDao;
import com.homw.tool.entity.DeviceInfoEntity;
import com.homw.tool.service.IDeviceInfoService;

@Service
public class DeviceInfoService implements IDeviceInfoService {
	@Autowired
	private DeviceInfoDao deviceInfoDao;

	@Override
	public DeviceInfoEntity queryObject(Long deviceId) {
		return deviceInfoDao.queryObject(deviceId);
	}

	@Override
	public List<DeviceInfoEntity> queryList(Map<String, Object> map) {
		return deviceInfoDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return deviceInfoDao.queryTotal(map);
	}

	@Override
	public void save(DeviceInfoEntity deviceInfo) {
		deviceInfoDao.save(deviceInfo);
	}

	@Override
	public void update(DeviceInfoEntity deviceInfo) {
		deviceInfoDao.update(deviceInfo);
	}

	@Override
	public void delete(Long deviceId) {
		deviceInfoDao.delete(deviceId);
	}

	@Override
	public void deleteBatch(Long[] deviceIds) {
		deviceInfoDao.deleteBatch(deviceIds);
	}

	@Override
	public void import2Database(String tableName, String fileName) throws Exception {
		// Excel解析
		ImportExcel parser = new ImportExcel(fileName, 1);
		List<DeviceInfoEntity> dataList = parser.getDataList(DeviceInfoEntity.class, null, null);

		// 参数封装
		Iterator<DeviceInfoEntity> it = dataList.iterator();
		while (it.hasNext()) {
			DeviceInfoEntity device = it.next();

			// 剔除设备名称或设备号为空的设备
			if (StringUtils.isEmpty(device.getDoorName()) || StringUtils.isEmpty(device.getOuterNo())) {
				it.remove();
				continue;
			}

			// 设置默认参数
			device.setDoorPort(0);
			device.setElecStatus("0100");
			device.setElecUsePoint(0);
			device.setElecLeftPoint(0);
			device.setStatus(1);
			device.setVersion(0);
			device.setIsReferNode(0);

			// 设置转换参数
			device.setDeviceType("ELECTRIC");
			device.setDoorPort(10001);
			int readNo = device.getDoorReadno();
			device.setElecAddr(device.getDoorAddr() + (readNo < 10 ? "0" + readNo : readNo));
		}
		// 批量存储
		deviceInfoDao.saveBatch(dataList);
	}

}
