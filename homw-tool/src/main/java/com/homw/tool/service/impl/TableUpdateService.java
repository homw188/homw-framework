package com.homw.tool.service.impl;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homw.common.bean.ImportExcel;
import com.homw.tool.dao.TableUpdateDao;
import com.homw.tool.entity.TableUpdateEntity;
import com.homw.tool.service.ITableUpdateService;

@Service
public class TableUpdateService implements ITableUpdateService {
	@Autowired(required = false)
	private TableUpdateDao tableUpdateDao;

	@Override
	public void update2Database(String tableName, String idColumn, String updateColumn, String fileName)
			throws Exception {
		// Excel解析
		ImportExcel parser = new ImportExcel(fileName, 0);
		List<TableUpdateEntity> dataList = parser.getDataList(TableUpdateEntity.class, null, null);

		// 参数封装
		Iterator<TableUpdateEntity> it = dataList.iterator();
		while (it.hasNext()) {
			TableUpdateEntity data = it.next();

			// 剔除字段为空记录
			if (data.getDataId() == null || StringUtils.isEmpty(data.getDataValue())) {
				it.remove();
				continue;
			}
			data.setTableName(tableName);
			data.setIdColumn(idColumn);
			data.setUpdateColumn(updateColumn);

			tableUpdateDao.update(data);
		}
	}
}