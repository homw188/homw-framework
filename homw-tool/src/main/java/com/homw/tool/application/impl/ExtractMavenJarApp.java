package com.homw.tool.application.impl;

import java.io.File;
import java.util.Iterator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Controller;

import com.homw.tool.annotation.Application;
import com.homw.tool.application.AbstractApplication;

import cn.hutool.core.io.FileUtil;

/**
 * @description 提取maven仓库jar应用
 * @author Hom
 * @version 1.0
 * @since 2020-10-23
 */
@Controller
@Application("extractMavenJarApp")
public class ExtractMavenJarApp extends AbstractApplication {
	
	private String pom;
	
	@Override
	protected void configArgs(Options options) {
		options.addOption(Option.builder("s").longOpt("src").hasArg().required().desc("maven repo dir").build());
		options.addOption(Option.builder("d").longOpt("dest").hasArg().desc("dest dir").build());
		options.addOption(Option.builder("p").longOpt("pom").hasArg().desc("pom.xml file").build());
	}

	@Override
	protected void validateArgs(CommandLine params) {
		String repo = params.getOptionValue("s");
		if (!FileUtil.exist(repo)) {
			throw new IllegalArgumentException("repo dir [" + repo + "] not existed.");
		}
		pom = getPom(params);
		if (StringUtils.isEmpty(pom)) {
			throw new IllegalArgumentException("not found pom.xml.");
		}
	}

	@Override
	protected void execute(CommandLine params) throws Exception {
		String repo = params.getOptionValue("s");
		String dest = getDestination(params);
		
		SAXReader reader = new SAXReader();
		Document document = reader.read(new File(pom));
		Element rootEle = document.getRootElement();
		Element depsEle = rootEle.element("dependencies");
		if (depsEle == null) {
			logger.info("dependencies tag not existed.");
			return;
		}
		Element propsEle = rootEle.element("properties");
		
		Element depEle;
		String jar, version, groupId, artifactId;
		Iterator<?> depsIter = depsEle.elementIterator();
		for (int i = 1; depsIter.hasNext(); i++) {
			depEle = (Element) depsIter.next();
			groupId = depEle.element("groupId").getText().trim();
			artifactId = depEle.element("artifactId").getText().trim();
			
			version = getVersion(propsEle, depEle, i);
			if (StringUtils.isEmpty(version)) {
				continue;
			}
			
			jar = repo + File.separator + groupId.replace(".", File.separator) + File.separator + artifactId
					+ File.separator + version + File.separator + artifactId + "-" + version + ".jar";
			if (!FileUtil.exist(jar)) {
				logger.warn(jar + " not existed.");
				continue;
			}
			FileUtil.copy(jar, dest, true);
		}
	}
	
	private String getPom(CommandLine params) {
		String pom = params.getOptionValue("p");
		if (StringUtils.isNotEmpty(pom)) {
			if (!pom.endsWith("pom.xml")) {
				pom += File.separator + "pom.xml";
			}
			if (FileUtil.exist(pom)) {
				return pom;
			}
		}
		pom = FileUtil.getAbsolutePath(".") + File.separator + "pom.xml";
		if (!FileUtil.exist(pom)) {
			pom = FileUtil.getUserHomePath() + File.separator + "pom.xml";
			if (!FileUtil.exist(pom)) {
				return null;
			}
		}
		return pom;
	}
	
	private String getDestination(CommandLine params) {
		String dest = params.getOptionValue("d");
		if (StringUtils.isNotEmpty(dest) && FileUtil.exist(dest)) {
			return dest;
		}
		return FileUtil.getAbsolutePath(".") + File.separator + "lib";
	}
	
	private String getVersion(Element propsEle, Element depEle, int i) {
		String version = null;
		Element versionEle = depEle.element("version");
		if (versionEle == null) {
			logger.warn("dependency->version tag " + i + " not existed.");
			return null;
		}
		version = versionEle.getText().trim();
		if (version.startsWith("${") && version.endsWith("}")) {
			version = version.substring(2, version.length() - 1);
			Element propEle = propsEle.element(version);
			if (propEle != null) {
				version = propEle.getText().trim();
			} else {
				logger.warn("not found " + version + " propertie.");
				return null;
			}
		}
		return version;
	}
}
