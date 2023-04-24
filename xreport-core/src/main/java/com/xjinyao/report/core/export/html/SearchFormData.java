package com.xjinyao.report.core.export.html;

import com.xjinyao.report.core.definition.searchform.FormPosition;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class SearchFormData {
	private String html;
	private String js;
	private String searchFormXml;
	private FormPosition formPosition;

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getJs() {
		return js;
	}

	public void setJs(String js) {
		this.js = js;
	}

	public FormPosition getFormPosition() {
		return formPosition;
	}

	public void setFormPosition(FormPosition formPosition) {
		this.formPosition = formPosition;
	}

	public String getSearchFormXml() {
		return searchFormXml;
	}

	public void setSearchFormXml(String searchFormXml) {
		this.searchFormXml = searchFormXml;
	}
}
