package com.xjinyao.report.core.expression.model.condition;

import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.expression.model.Expression;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.model.Cell;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class CurrentValueExpressionCondition extends BaseCondition {
	private ConditionType type = ConditionType.current;
	@JsonIgnore
	private Expression rightExpression;

	@Override
	Object computeLeft(Cell cell, Cell currentCell, Object obj, Context context) {
		return obj;
	}

	@Override
	Object computeRight(Cell cell, Cell currentCell, Object obj, Context context) {
		ExpressionData<?> exprData = rightExpression.execute(cell, currentCell, context);
		return extractExpressionData(exprData);
	}

	@Override
	public ConditionType getType() {
		return type;
	}

	public Expression getRightExpression() {
		return rightExpression;
	}

	public void setRightExpression(Expression rightExpression) {
		this.rightExpression = rightExpression;
	}
}
