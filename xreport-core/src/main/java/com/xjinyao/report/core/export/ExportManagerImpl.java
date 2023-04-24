package com.xjinyao.report.core.export;

import com.xjinyao.report.core.build.paging.Page;
import com.xjinyao.report.core.cache.CacheUtils;
import com.xjinyao.report.core.chart.ChartData;
import com.xjinyao.report.core.definition.ReportDefinition;
import com.xjinyao.report.core.export.excel.high.ExcelProducer;
import com.xjinyao.report.core.export.excel.low.Excel97Producer;
import com.xjinyao.report.core.export.html.HtmlProducer;
import com.xjinyao.report.core.export.html.HtmlReport;
import com.xjinyao.report.core.export.pdf.PdfProducer;
import com.xjinyao.report.core.export.word.high.WordProducer;
import com.xjinyao.report.core.model.Report;

import java.util.List;
import java.util.Map;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ExportManagerImpl implements ExportManager {
	private ReportRender reportRender;
	private HtmlProducer htmlProducer = new HtmlProducer();
	private WordProducer wordProducer = new WordProducer();
	private ExcelProducer excelProducer = new ExcelProducer();
	private Excel97Producer excel97Producer = new Excel97Producer();
	private PdfProducer pdfProducer = new PdfProducer();

	@Override
	public HtmlReport exportHtml(String file, String contextPath, Map<String, Object> parameters) {
		ReportDefinition reportDefinition = reportRender.getReportDefinition(file);
		Report report = reportRender.render(reportDefinition, parameters);
		Map<String, ChartData> chartMap = report.getContext().getChartDataMap();
		if (chartMap.size() > 0) {
			CacheUtils.storeChartDataMap(chartMap);
		}
		HtmlReport htmlReport = new HtmlReport();
		String content = htmlProducer.produce(report);
		htmlReport.setContent(content);
		if (reportDefinition.getPaper().isColumnEnabled()) {
			htmlReport.setColumn(reportDefinition.getPaper().getColumnCount());
		}
		htmlReport.setStyle(reportDefinition.getStyle());
		htmlReport.setSearchFormData(reportDefinition.buildSearchFormData(report.getContext().getDatasetMap(), parameters));
		htmlReport.setReportAlign(report.getPaper().getHtmlReportAlign().name());
		htmlReport.setChartDatas(report.getContext().getChartDataMap().values());
		htmlReport.setHtmlIntervalRefreshValue(report.getPaper().getHtmlIntervalRefreshValue());
		return htmlReport;
	}

	@Override
	public HtmlReport exportHtml(String file, String contextPath, Map<String, Object> parameters, int pageIndex) {
		ReportDefinition reportDefinition = reportRender.getReportDefinition(file);
		Report report = reportRender.render(reportDefinition, parameters);
		Map<String, ChartData> chartMap = report.getContext().getChartDataMap();
		if (chartMap.size() > 0) {
			CacheUtils.storeChartDataMap(chartMap);
		}
		SinglePageData pageData = PageBuilder.buildSinglePageData(pageIndex, report);
		List<Page> pages = pageData.getPages();
		String content = null;
		if (pages.size() == 1) {
			content = htmlProducer.produce(report.getContext(), pages.get(0), false);
		} else {
			content = htmlProducer.produce(report.getContext(), pages, pageData.getColumnMargin(), false);
		}
		HtmlReport htmlReport = new HtmlReport();
		htmlReport.setContent(content);
		if (reportDefinition.getPaper().isColumnEnabled()) {
			htmlReport.setColumn(reportDefinition.getPaper().getColumnCount());
		}
		htmlReport.setStyle(reportDefinition.getStyle());
		htmlReport.setSearchFormData(reportDefinition.buildSearchFormData(report.getContext().getDatasetMap(), parameters));
		htmlReport.setPageIndex(pageIndex);
		htmlReport.setTotalPage(pageData.getTotalPages());
		htmlReport.setReportAlign(report.getPaper().getHtmlReportAlign().name());
		htmlReport.setChartDatas(report.getContext().getChartDataMap().values());
		htmlReport.setHtmlIntervalRefreshValue(report.getPaper().getHtmlIntervalRefreshValue());
		return htmlReport;
	}

	@Override
	public void exportPdf(ExportConfigure config) {
		String file = config.getFile();
		Map<String, Object> parameters = config.getParameters();
		ReportDefinition reportDefinition = reportRender.getReportDefinition(file);
		Report report = reportRender.render(reportDefinition, parameters);
		pdfProducer.produce(report, config.getOutputStream());
	}

	@Override
	public void exportWord(ExportConfigure config) {
		String file = config.getFile();
		Map<String, Object> parameters = config.getParameters();
		ReportDefinition reportDefinition = reportRender.getReportDefinition(file);
		Report report = reportRender.render(reportDefinition, parameters);
		wordProducer.produce(report, config.getOutputStream());
	}

	@Override
	public void exportExcel(ExportConfigure config) {
		String file = config.getFile();
		Map<String, Object> parameters = config.getParameters();
		ReportDefinition reportDefinition = reportRender.getReportDefinition(file);
		Report report = reportRender.render(reportDefinition, parameters);
		excelProducer.produce(report, config.getOutputStream());
	}

	@Override
	public void exportExcel97(ExportConfigure config) {
		String file = config.getFile();
		Map<String, Object> parameters = config.getParameters();
		ReportDefinition reportDefinition = reportRender.getReportDefinition(file);
		Report report = reportRender.render(reportDefinition, parameters);
		excel97Producer.produce(report, config.getOutputStream());
	}

	@Override
	public void exportExcelWithPaging(ExportConfigure config) {
		String file = config.getFile();
		Map<String, Object> parameters = config.getParameters();
		ReportDefinition reportDefinition = reportRender.getReportDefinition(file);
		Report report = reportRender.render(reportDefinition, parameters);
		excelProducer.produceWithPaging(report, config.getOutputStream());
	}

	@Override
	public void exportExcel97WithPaging(ExportConfigure config) {
		String file = config.getFile();
		Map<String, Object> parameters = config.getParameters();
		ReportDefinition reportDefinition = reportRender.getReportDefinition(file);
		Report report = reportRender.render(reportDefinition, parameters);
		excel97Producer.produceWithPaging(report, config.getOutputStream());
	}

	@Override
	public void exportExcelWithPagingSheet(ExportConfigure config) {
		String file = config.getFile();
		Map<String, Object> parameters = config.getParameters();
		ReportDefinition reportDefinition = reportRender.getReportDefinition(file);
		Report report = reportRender.render(reportDefinition, parameters);
		excelProducer.produceWithSheet(report, config.getOutputStream());
	}

	@Override
	public void exportExcel97WithPagingSheet(ExportConfigure config) {
		String file = config.getFile();
		Map<String, Object> parameters = config.getParameters();
		ReportDefinition reportDefinition = reportRender.getReportDefinition(file);
		Report report = reportRender.render(reportDefinition, parameters);
		excel97Producer.produceWithSheet(report, config.getOutputStream());
	}

	public void setReportRender(ReportRender reportRender) {
		this.reportRender = reportRender;
	}
}
