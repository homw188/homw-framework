package com.homw.tool.api.kede.serial;

public interface ICommPortService {

	void sendOpenElecMsg(String elecAddr) throws Exception;

	void sendCloseElecMsg(String elecAddr) throws Exception;

	void sendSearchElecMsg(String elecAddr) throws Exception;
}
