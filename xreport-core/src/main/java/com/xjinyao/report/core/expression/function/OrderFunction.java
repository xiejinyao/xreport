package com.xjinyao.report.core.expression.function;

import com.xjinyao.report.core.Utils;
import com.xjinyao.report.core.build.BindData;
import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.definition.Order;
import com.xjinyao.report.core.exception.ReportComputeException;
import com.xjinyao.report.core.expression.model.data.BindDataListExpressionData;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectListExpressionData;
import com.xjinyao.report.core.model.Cell;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class OrderFunction implements Function {

	@Override
	public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
		if (dataList == null || dataList.size() != 2) {
			throw new ReportComputeException("Function [order] need two parameters");
		}
		boolean result = computeOrder(dataList);
		Order orderData = Order.asc;
		if (!result) {
			orderData = Order.desc;
		}
		final Order order = orderData;
		ExpressionData<?> firstData = dataList.get(0);
		if (firstData instanceof ObjectListExpressionData) {
			ObjectListExpressionData data = (ObjectListExpressionData) firstData;
			List<?> list = data.getData();
			Collections.sort(list, new Comparator<Object>() {
				@Override
				public int compare(Object data1, Object data2) {
					return doOrder(data1, data2, order);
				}
			});
			return list;
		} else if (firstData instanceof BindDataListExpressionData) {
			BindDataListExpressionData bindDataList = (BindDataListExpressionData) firstData;
			List<BindData> list = bindDataList.getData();
			List<Object> ls = new ArrayList<>();
			for (BindData bindData : list) {
				Object obj = bindData.getValue();
				if (obj != null) {
					ls.add(obj);
				}
			}
			Collections.sort(ls, new Comparator<Object>() {
				@Override
				public int compare(Object data1, Object data2) {
					return doOrder(data1, data2, order);
				}
			});
			return ls;
		} else {
			return firstData.getData();
		}
	}

	private int doOrder(Object data1, Object data2, Order order) {
		if (data1 == null || data2 == null) {
			return 1;
		}
		if (data1 instanceof Date) {
			Date d1 = (Date) data1;
			Date d2 = (Date) data2;
			if (order.equals(Order.asc)) {
				return d1.compareTo(d2);
			} else {
				return d2.compareTo(d1);
			}
		} else if (data1 instanceof Number) {
			BigDecimal n1 = Utils.toBigDecimal(data1);
			BigDecimal n2 = Utils.toBigDecimal(data2);
			if (order.equals(Order.asc)) {
				return n1.compareTo(n2);
			} else {
				return n2.compareTo(n1);
			}
		} else {
			String str1 = data1.toString();
			String str2 = data2.toString();
			if (order.equals(Order.asc)) {
				return str1.compareTo(str2);
			} else {
				return str2.compareTo(str1);
			}
		}
	}

	private boolean computeOrder(List<ExpressionData<?>> dataList) {
		boolean order = false;
		ExpressionData<?> secondData = dataList.get(1);
		if (secondData instanceof ObjectExpressionData) {
			ObjectExpressionData data = (ObjectExpressionData) secondData;
			Object obj = data.getData();
			if (obj == null) {
				throw new ReportComputeException("Function [order] second parameter can not be null");
			}
			if (obj instanceof Boolean) {
				order = (Boolean) obj;
				return order;
			}
			if (obj instanceof String) {
				order = Boolean.valueOf(obj.toString());
			} else {
				throw new ReportComputeException("Function [order] second parameter muse be boolean type.");
			}
		}
		return order;
	}

	@Override
	public String name() {
		return "order";
	}
}
