package com.homw.tool.dao;

import java.util.List;

import com.homw.dao.support.BaseDao;
import com.homw.tool.entity.AgreementEntity;
import com.homw.tool.entity.PayRecordEntity;

/**
 * PayRecordGenerate
 * 
 * @author System
 * @email 
 * @since 2019-09-23
 */
public interface PayRecordGenerateDao extends BaseDao<AgreementEntity> {
	List<AgreementEntity> queryListMissingPayRecord(Long spaceId);
	
	int saveBatchRecord(List<PayRecordEntity> recordList);
}