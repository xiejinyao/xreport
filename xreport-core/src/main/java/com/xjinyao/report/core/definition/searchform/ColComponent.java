package com.xjinyao.report.core.definition.searchform;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ColComponent extends ContainerComponent {
	private int size;

	@Override
	public String toHtml(RenderContext context) {
		StringBuffer sb = new StringBuffer();
		sb.append("<div class='col-md-" + size + "' style='padding-left:2px;padding-right:2px'");
		Object gridComponent = context.getMetadata(GridComponent.KEY);
		if (gridComponent != null) {
			GridComponent gc = (GridComponent) gridComponent;
			if (gc.isShowBorder()) {
				String border = "border:solid " + gc.getBorderWidth() + "px " + gc.getBorderColor() + "";
				sb.append(" style='" + border + ";padding:10px'");
			}
		}
		sb.append(">");
		for (Component c : children) {
			sb.append(c.toHtml(context));
		}
		sb.append("</div>");
		return sb.toString();
	}

	@Override
	public String initJs(RenderContext context) {
		StringBuilder sb = new StringBuilder();
		for (Component c : children) {
			sb.append(c.initJs(context));
		}
		return sb.toString();
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public String getType() {
		return null;
	}
}
