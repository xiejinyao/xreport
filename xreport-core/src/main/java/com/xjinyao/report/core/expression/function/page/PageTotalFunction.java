package com.xjinyao.report.core.expression.function.page;

import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.model.Cell;

import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class PageTotalFunction extends PageFunction {
	@Override
	public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
		return context.getTotalPages();
	}

	@Override
	public String name() {
		return "pages";
	}
}
