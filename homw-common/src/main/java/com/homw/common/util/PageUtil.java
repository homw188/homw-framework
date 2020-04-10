package com.homw.common.util;

import java.io.Serializable;
import java.util.List;

/**
 * @description 分页工具类
 * @author Hom
 * @version 1.0
 * @since 2020-04-09
 */
public class PageUtil implements Serializable {
	private static final long serialVersionUID = 1L;
	// 总记录数
	private int totalCount;
	// 每页记录数
	private int pageSize;
	// 总页数
	private int totalPage;
	// 当前页数
	private int currPage;
	// 列表数据
	private List<?> list;

	public PageUtil(List<?> list, int totalCount, int pageSize, int currPage) {
		this.list = list;
		this.totalCount = totalCount;
		this.pageSize = pageSize;
		this.currPage = currPage;
		this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getCurrPage() {
		return currPage;
	}

	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}
}