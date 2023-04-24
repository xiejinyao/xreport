package com.xjinyao.report.core.chart;

import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.chart.axes.impl.XAxes;
import com.xjinyao.report.core.chart.axes.impl.YAxes;
import com.xjinyao.report.core.chart.dataset.Dataset;
import com.xjinyao.report.core.chart.dataset.impl.BubbleDataset;
import com.xjinyao.report.core.chart.dataset.impl.ScatterDataset;
import com.xjinyao.report.core.chart.dataset.impl.category.BarDataset;
import com.xjinyao.report.core.chart.dataset.impl.category.LineDataset;
import com.xjinyao.report.core.chart.option.Option;
import com.xjinyao.report.core.chart.plugins.Plugin;
import com.xjinyao.report.core.model.Cell;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class Chart {
	private Dataset dataset;
	private XAxes xaxes;
	private YAxes yaxes;
	private List<Option> options = new ArrayList<>();
	private List<Plugin> plugins = new ArrayList<>();

	public ChartData doCompute(Cell cell, Context context) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"type\":\"" + dataset.getType() + "\",");
		sb.append("\"data\":" + dataset.buildDataJson(context, cell) + ",");
		sb.append("\"options\":{");
		boolean withoption = false;
		if (options != null && options.size() > 0) {
			for (int i = 0; i < options.size(); i++) {
				Option option = options.get(i);
				if (i > 0) {
					sb.append(",");
				}
				sb.append(option.buildOptionJson());
				withoption = true;
			}
		}
		if (plugins != null && plugins.size() > 0) {
			if (withoption) {
				sb.append(",");
			}
			withoption = true;
			sb.append("\"plugins\": {");
			for (Plugin plugin : plugins) {
				String pluginJson = plugin.toJson(dataset.getType());
				if (pluginJson != null) {
					sb.append(pluginJson);
				}
			}
			sb.append("}");
		} else {
			withoption = true;
			sb.append("\"plugins\": {");
			sb.append("\"datalabels\":{\"display\":false}");
			sb.append("}");
		}
		if (xaxes != null || yaxes != null) {
			if (withoption) {
				sb.append(",");
			}
			withoption = true;
			sb.append("\"scales\":{");
			if (xaxes != null) {
				sb.append("\"xAxes\":[");
				sb.append(xaxes.toJson());
				sb.append("]");
			}
			if (yaxes != null) {
				if (xaxes != null) {
					sb.append(",\"yAxes\":[");
				} else {
					sb.append("\"yAxes\":[");
				}
				sb.append(yaxes.toJson());
				sb.append("]");
			} else {
				if (hasYAxes(dataset)) {
					sb.append(",\"yAxes\":[{\"ticks\":{\"min\":0}}]");
				}
			}
			sb.append("}");
		} else {
			if (withoption && hasYAxes(dataset)) {
				sb.append(",");
				sb.append("\"scales\":{\"yAxes\":[]}");
			}
		}
		sb.append("}");
		sb.append("}");
		ChartData chartData = new ChartData(sb.toString(), cell);
		context.addChartData(chartData);
		return chartData;
	}

	private boolean hasYAxes(Dataset dataset) {
		if (dataset instanceof BarDataset) {
			return true;
		}
		if (dataset instanceof LineDataset) {
			return true;
		}
		if (dataset instanceof BubbleDataset) {
			return true;
		}
		if (dataset instanceof ScatterDataset) {
			return true;
		}
		return false;
	}

	public List<Option> getOptions() {
		return options;
	}

	public void setOptions(List<Option> options) {
		this.options = options;
	}

	public List<Plugin> getPlugins() {
		return plugins;
	}

	public void setPlugins(List<Plugin> plugins) {
		this.plugins = plugins;
	}

	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	public XAxes getXaxes() {
		return xaxes;
	}

	public void setXaxes(XAxes xaxes) {
		this.xaxes = xaxes;
	}

	public YAxes getYaxes() {
		return yaxes;
	}

	public void setYaxes(YAxes yaxes) {
		this.yaxes = yaxes;
	}
}
