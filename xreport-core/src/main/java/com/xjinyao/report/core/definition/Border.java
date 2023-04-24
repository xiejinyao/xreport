package com.xjinyao.report.core.definition;

import java.io.Serializable;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class Border implements Serializable {
	private static final long serialVersionUID = 5320929211828633858L;
	private int width;
	private String color;
	private BorderStyle style;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public BorderStyle getStyle() {
		return style;
	}

	public void setStyle(BorderStyle style) {
		this.style = style;
	}
}
