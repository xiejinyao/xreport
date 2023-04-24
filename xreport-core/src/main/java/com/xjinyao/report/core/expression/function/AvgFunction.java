package com.xjinyao.report.core.expression.function;

import com.xjinyao.report.core.Utils;
import com.xjinyao.report.core.build.BindData;
import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.expression.model.data.BindDataListExpressionData;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectListExpressionData;
import com.xjinyao.report.core.model.Cell;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class AvgFunction implements Function {
	@Override
	public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
		if (dataList == null || dataList.size() == 0) {
			return null;
		}
		int size = 0;
		Object singleData = null;
		BigDecimal total = new BigDecimal(0);
		for (ExpressionData<?> exprData : dataList) {
			if (exprData instanceof ObjectListExpressionData) {
				ObjectListExpressionData listExpr = (ObjectListExpressionData) exprData;
				List<?> list = listExpr.getData();
				for (Object obj : list) {
					if (obj == null || StringUtils.isBlank(obj.toString())) {
						continue;
					}
					singleData = obj;
					BigDecimal bigData = Utils.toBigDecimal(obj);
					total = total.add(bigData);
					size++;
				}
			} else if (exprData instanceof ObjectExpressionData) {
				ObjectExpressionData data = (ObjectExpressionData) exprData;
				Object obj = data.getData();
				if (obj == null || StringUtils.isBlank(obj.toString())) {
					continue;
				}
				BigDecimal bigData = Utils.toBigDecimal(data.getData());
				singleData = data.getData();
				total = total.add(bigData);
				size++;
			} else if (exprData instanceof BindDataListExpressionData) {
				BindDataListExpressionData data = (BindDataListExpressionData) exprData;
				List<BindData> bindDataList = data.getData();
				for (BindData bindData : bindDataList) {
					Object obj = bindData.getValue();
					if (obj == null || StringUtils.isBlank(obj.toString())) {
						continue;
					}
					singleData = obj;
					BigDecimal bigData = Utils.toBigDecimal(obj);
					total = total.add(bigData);
					size++;
				}
			}
		}
		if (size == 0) {
			return 0;
		} else if (size == 1) {
			if (singleData == null || singleData.equals("")) {
				return "";
			}
			return total;
		} else {
			return total.divide(new BigDecimal(size), 8, BigDecimal.ROUND_HALF_UP);
		}
	}

	@Override
	public String name() {
		return "avg";
	}
}
