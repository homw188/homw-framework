package com.homw.tool.service;

/**
 * 生成支付记录服务
 * 
 * @author System
 * @email 
 * @date 2019-09-23 15:53:25
 */
public interface IPayRecordGenerateService {
	/**
	 * 将该项目下缺失支付记录的合同，批量生成（仅支持按月租赁方式）
	 * 
	 * @param spaceId 项目id
	 * @throws Exception
	 */
	void generate(Long spaceId) throws Exception;
}
