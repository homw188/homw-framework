package com.homw.tool.service.impl;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homw.common.bean.ImportExcel;
import com.homw.tool.dao.SpmsNoDao;
import com.homw.tool.entity.SpmsNoEntity;
import com.homw.tool.service.ISpmsNoService;

@Service
public class SpmsNoService implements ISpmsNoService {
	@Autowired(required = false)
	private SpmsNoDao spmsNoDao;

	@Override
	public void import2Database(String tableName, String idColumn, String fileName) throws Exception {
		// Excel解析
		ImportExcel parser = new ImportExcel(fileName, 0);
		List<SpmsNoEntity> dataList = parser.getDataList(SpmsNoEntity.class, null, null);

		// 参数封装
		Iterator<SpmsNoEntity> it = dataList.iterator();
		while (it.hasNext()) {
			SpmsNoEntity data = it.next();

			// 剔除字段为空记录
			if (data.getDataId() == null || StringUtils.isEmpty(data.getSpmsNo())) {
				it.remove();
				continue;
			}
			data.setTableName(tableName);
			data.setIdColumn(idColumn);

			spmsNoDao.update(data);
		}
	}
}