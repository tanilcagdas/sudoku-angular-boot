package com.sudoku.util;

import com.sudoku.beans.Group;

public class SudokuUtil {

	public static boolean isSudokuCorrect(Group group) {
		for (int i = 0; i < 9; i++) {
			int uniqueValue = group.getGroup().get(i).getValue();
			if (uniqueValue == 0) {
				continue;
			}
			for (int j = 0; j < 9; j++) {
				if (i == j)
					continue;
				int compareValue = group.getGroup().get(j).getValue();
				if (compareValue == uniqueValue) {
					group.getSudoku().setSudokuCorrect(false);
					return false;
				}
			}
		}
		return true;
	}
	

}
