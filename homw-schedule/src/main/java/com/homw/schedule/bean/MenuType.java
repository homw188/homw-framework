package com.homw.schedule.bean;

/**
 * @description 菜单类型
 * @author Hom
 * @version 1.0
 * @since 2020-04-02
 */
public enum MenuType {
	/**
	 * 目录
	 */
	CATALOG(0),
	/**
	 * 菜单
	 */
	MENU(1),
	/**
	 * 按钮
	 */
	BUTTON(2);

	private int value;

	private MenuType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
