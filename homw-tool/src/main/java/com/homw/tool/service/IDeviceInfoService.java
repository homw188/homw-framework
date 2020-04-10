package com.homw.tool.service;

import java.util.List;
import java.util.Map;

import com.homw.tool.entity.DeviceInfoEntity;

/**
 * 设备信息表
 * 
 * @author System
 * @email 
 * @since 2019-05-20
 */
public interface IDeviceInfoService {
	
	DeviceInfoEntity queryObject(Long deviceId);
	
	List<DeviceInfoEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(DeviceInfoEntity deviceInfo);
	
	void update(DeviceInfoEntity deviceInfo);
	
	void delete(Long deviceId);
	
	void deleteBatch(Long[] deviceIds);
	
	/**
	 * 导入设备参数至数据库
	 * @param tableName
	 * @param fileName
	 * @throws Exception
	 */
	void import2Database(String tableName, String fileName) throws Exception ;
}
