package com.xjinyao.report.core.parser.impl.value;

import com.xjinyao.report.core.definition.value.Source;
import com.xjinyao.report.core.definition.value.Value;
import com.xjinyao.report.core.definition.value.ZxingCategory;
import com.xjinyao.report.core.definition.value.ZxingValue;
import com.xjinyao.report.core.expression.ExpressionUtils;
import com.xjinyao.report.core.expression.model.Expression;
import org.dom4j.Element;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ZxingValueParser extends ValueParser {

	@Override
	public Value parse(Element element) {
		ZxingValue value = new ZxingValue();
		Source source = Source.valueOf(element.attributeValue("source"));
		value.setSource(source);
		value.setWidth(Integer.valueOf(element.attributeValue("width")));
		value.setHeight(Integer.valueOf(element.attributeValue("height")));
		value.setFormat(element.attributeValue("format"));
		value.setCategory(ZxingCategory.valueOf(element.attributeValue("category")));
		value.setCodeDisplay(Boolean.valueOf(element.attributeValue("code-display")));
		for (Object obj : element.elements()) {
			if (obj == null || !(obj instanceof Element)) {
				continue;
			}
			Element ele = (Element) obj;
			if (ele.getName().equals("text")) {
				if (source.equals(Source.text)) {
					value.setText(ele.getText());
				} else {
					value.setExpr(ele.getText());
				}
				break;
			}
		}
		if (source.equals(Source.expression)) {
			String expr = value.getExpr();
			Expression expression = ExpressionUtils.parseExpression(expr);
			value.setExpression(expression);
		}
		return value;
	}
}
