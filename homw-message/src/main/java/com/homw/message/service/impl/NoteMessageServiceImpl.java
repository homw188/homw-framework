package com.homw.message.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.homw.message.note.INoteProxy;
import com.homw.message.service.INoteMessageService;

/**
 * @description 短信服务
 * @author Hom
 * @version 1.0
 * @since 2020-04-03
 */
@Service
public class NoteMessageServiceImpl implements INoteMessageService {

	@Autowired
	private INoteProxy noteProxy;

	@Override
	public void sendNote(String mobile, String templateId, String[] args) {
		if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(templateId)) {
			return;
		}
		noteProxy.sendNote(mobile, templateId, args);
	}
}