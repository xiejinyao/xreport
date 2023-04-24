package com.xjinyao.report.core.chart.dataset.impl.category;

import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.model.Cell;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class LineDataset extends CategoryDataset {
	private double lineTension = 0.2;

	@Override
	public String buildDataJson(Context context, Cell cell) {
		String props = "\"lineTension\":" + lineTension;
		String datasetJson = buildDatasetJson(context, cell, props);
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		String labels = getLabels();
		sb.append("\"labels\":" + labels + ",");
		sb.append("\"datasets\":[" + datasetJson + "]");
		sb.append("}");
		return sb.toString();
	}

	public String toMixJson(Context context, Cell cell, int index) {
		String props = "\"type\":\"line\",\"lineTension\":" + lineTension + ",\"fill\":false";
		String datasetJson = buildDatasetJson(context, cell, props);
		return datasetJson;
	}

	@Override
	public String getType() {
		return "line";
	}

	public double getLineTension() {
		return lineTension;
	}

	public void setLineTension(double lineTension) {
		this.lineTension = lineTension;
	}
}
