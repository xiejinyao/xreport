package com.xjinyao.report.core.build.cell.right;

import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.model.Cell;
import com.xjinyao.report.core.model.Column;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class CellRightDuplicateUnit {
	private Cell mainCell;
	private int mainCellColNumber;
	private Context context;
	private RightDuplicate rightDuplicate;
	private RightDuplocatorWrapper rightDuplocatorWrapper;

	public CellRightDuplicateUnit(Context context, RightDuplocatorWrapper rightDuplocatorWrapper, Cell mainCell, int mainCellColNumber, int colSize) {
		this.context = context;
		this.rightDuplocatorWrapper = rightDuplocatorWrapper;
		this.mainCell = mainCell;
		this.mainCellColNumber = mainCellColNumber;
		this.rightDuplicate = new RightDuplicate(mainCell, colSize, context);
	}

	public void duplicate(Cell cell, int index) {
		Map<Cell, Cell> newCellMap = new HashMap<>();
		newCellMap.put(mainCell, cell);
		rightDuplicate.setIndex(index);
		for (CellRightDuplicator childDuplicator : rightDuplocatorWrapper.getMainCellChildren()) {
			Cell newCell = childDuplicator.duplicateChildrenCell(rightDuplicate, cell, mainCell, false);
			newCellMap.put(childDuplicator.getCell(), newCell);
			processChildrenCells(newCell, childDuplicator.getCell(), newCellMap, rightDuplicate, childDuplicator.isNonChild());
			childDuplicator.setNonChild(false);
		}
		for (CellRightDuplicator cellRightDuplicator : rightDuplocatorWrapper.getCellDuplicators()) {
			cellRightDuplicator.duplicate(rightDuplicate, cell);
		}
		Column newCol = rightDuplicate.newColumn(cell.getColumn(), mainCellColNumber);
		cell.setColumn(newCol);
		newCol.getCells().add(cell);
		cell.getRow().getCells().add(cell);
		context.addReportCell(cell);
		rightDuplicate.reset();
		for (Cell newCell : newCellMap.values()) {
			Cell originLeftCell = newCell.getLeftParentCell();
			if (originLeftCell != null && newCellMap.containsKey(originLeftCell)) {
				newCell.setLeftParentCell(newCellMap.get(originLeftCell));
			}
		}
	}

	public void complete() {
		rightDuplicate.complete();
	}

	private void processChildrenCells(Cell cell, Cell originalCell, Map<Cell, Cell> newCellMap, RightDuplicate rightDuplicate, boolean parentNonChild) {
		List<CellRightDuplicator> childCellRightDuplicators = rightDuplocatorWrapper.fetchChildrenDuplicator(originalCell);
		if (childCellRightDuplicators == null) {
			return;
		}
		for (CellRightDuplicator duplicator : childCellRightDuplicators) {
			Cell newCell = duplicator.duplicateChildrenCell(rightDuplicate, cell, originalCell, parentNonChild);
			newCellMap.put(duplicator.getCell(), newCell);
			processChildrenCells(newCell, duplicator.getCell(), newCellMap, rightDuplicate, duplicator.isNonChild());
			duplicator.setNonChild(false);
		}
	}
}
