package com.xjinyao.report.core.expression.function.date;

import com.xjinyao.report.core.Utils;
import com.xjinyao.report.core.exception.ReportComputeException;
import com.xjinyao.report.core.expression.function.Function;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectListExpressionData;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public abstract class CalendarFunction implements Function {
	protected Calendar buildCalendar(List<ExpressionData<?>> dataList) {
		Date date = new Date();
		if (dataList != null && dataList.size() > 0) {
			ExpressionData<?> data = dataList.get(0);
			if (data instanceof ObjectListExpressionData) {
				ObjectListExpressionData listData = (ObjectListExpressionData) data;
				List<?> list = listData.getData();
				if (list == null || list.size() != 1) {
					throw new ReportComputeException("Function [day] first parameter need a data of Date.");
				}
				Object obj = list.get(0);
				if (obj == null) {
					throw new ReportComputeException("Function [day] first parameter can not be null.");
				}
				date = Utils.toDate(obj);
			} else if (data instanceof ObjectExpressionData) {
				ObjectExpressionData objData = (ObjectExpressionData) data;
				Object obj = objData.getData();
				if (obj == null) {
					throw new ReportComputeException("Function [day] first parameter can not be null.");
				}
				date = Utils.toDate(obj);
			} else {
				throw new ReportComputeException("Function [day] first parameter need a data of Date.");
			}
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c;
	}
}
