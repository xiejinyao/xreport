package com.xjinyao.report.core.build.paging;

import com.xjinyao.report.core.model.Column;
import com.xjinyao.report.core.model.Row;

import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class Page {
	private List<Row> rows;
	private List<Column> columns;
	private HeaderFooter header;
	private HeaderFooter footer;

	public Page(List<Row> rows, List<Column> columns) {
		this.rows = rows;
		this.columns = columns;
	}

	public List<Row> getRows() {
		return rows;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public HeaderFooter getHeader() {
		return header;
	}

	public void setHeader(HeaderFooter header) {
		this.header = header;
	}

	public HeaderFooter getFooter() {
		return footer;
	}

	public void setFooter(HeaderFooter footer) {
		this.footer = footer;
	}
}
