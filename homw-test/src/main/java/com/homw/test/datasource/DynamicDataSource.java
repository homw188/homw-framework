package com.homw.test.datasource;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.homw.common.util.ReflectUtil;

/**
 * @description 动态数据源
 * @author Hom
 * @version 1.0
 * @since 2020-03-19
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

	int slaveCount;
	AtomicInteger counter = new AtomicInteger(-1);
	List<Object> slaveDataSourceList = new ArrayList<>();
	
	@Override
	protected Object determineCurrentLookupKey() {
		if (DynamicDataSourceHolder.isMaster()) {
			return DynamicDataSourceHolder.getDataSourceKey();
		}
		return getSlaveKey();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		
		Field field = ReflectUtil.getAccessibleField(AbstractRoutingDataSource.class, "resolvedDataSources");
		try {
			Map<Object, DataSource> resolvedDataSources = (Map<Object, DataSource>) field.get(this);
			slaveCount = resolvedDataSources.size() - 1; // 过滤主库
			for (Entry<Object, DataSource> entry : resolvedDataSources.entrySet()) {
				if (DynamicDataSourceHolder.MASTER.equals(entry.getKey())) {
					continue;
				}
				// 添加从库数据源
				slaveDataSourceList.add(entry.getKey());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 轮询从库数据源
	 * @return
	 */
	public Object getSlaveKey() {
		int index = counter.incrementAndGet() % slaveCount;
		if (counter.get() > 9999) {
			counter.set(-1);
		}
		return slaveDataSourceList.get(index);
	}

}
