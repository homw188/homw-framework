package com.homw.tool.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homw.tool.dao.LampsKeyUpdateDao;
import com.homw.tool.entity.LightSerialLocationEntity;
import com.homw.tool.entity.LightlampsEntity;
import com.homw.tool.service.ILampsKeyUpdateService;

@Service
public class LampsKeyUpdateService implements ILampsKeyUpdateService {
	@Autowired
	private LampsKeyUpdateDao lampsKeyUpdateDao;

	@Override
	public void updateBatch() throws Exception {
		List<LightlampsEntity> lighlampsList = lampsKeyUpdateDao.selectLampList();
		Map<String, String> lampTypeMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(lighlampsList)) {
			for (LightlampsEntity lamp : lighlampsList) {
				lampTypeMap.put(lamp.getLampsName(), lamp.getLampsKey());
			}
		}

		List<LightSerialLocationEntity> locationList = lampsKeyUpdateDao.selectLocationList();
		if (CollectionUtils.isNotEmpty(locationList)) {
			for (LightSerialLocationEntity location : locationList) {
				Pattern pattern = Pattern.compile("\"?lampsName\"?\\s*:\\s*\"?[\\u4e00-\\u9fa5\\w]*\"?");
				Matcher matcher = pattern.matcher(location.getLocationData());
				if (matcher.find()) {
					String lampsName = matcher.group().replaceAll("\"", "").replaceAll("lampsName\\s*:", "").trim();
					String lampsKey = lampTypeMap.get(lampsName);
					location.setLampsKey(lampsKey);
				}
			}
			lampsKeyUpdateDao.updateBatch(locationList);
		}
	}
}