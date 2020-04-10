package com.homw.tool.dao;

import java.util.List;

import com.homw.dao.support.BaseDao;
import com.homw.tool.entity.LightSerialLocationEntity;
import com.homw.tool.entity.LightlampsEntity;
import com.homw.tool.entity.TableUpdateEntity;

/**
 * TableUpdate
 * 
 * @author System
 * @email 
 * @since 2019-07-18
 */
public interface LampsKeyUpdateDao extends BaseDao<TableUpdateEntity> {
	List<LightSerialLocationEntity> selectLocationList();
	
	List<LightlampsEntity> selectLampList();
	
	int updateBatch(List<LightSerialLocationEntity> list);
}