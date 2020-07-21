package com.homw.tool.api.kede.serial;

public interface ICommPortService {

	void openElec(String addr) throws Exception;

	void closeElec(String addr) throws Exception;

	void searchElec(String addr) throws Exception;
}
