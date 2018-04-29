package com.sudoku.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.sudoku.beans.Cell;
import com.sudoku.beans.Group;
import com.sudoku.beans.Row;
import com.sudoku.beans.Sudoku;

@Service("SudokuAlgorithm1")
public class SudokuAlgorithm1 implements Algorithm {

	Logger logger = Logger.getLogger(this.getClass().getSimpleName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sudoku.service.Algorithm#useAlgorithm(com.sudoku.beans.Sudoku)
	 */
	@Override
	public Sudoku useAlgorithm(Sudoku sudokuSolution) {

		sudokuSolution.setSudokuHasChanged(false);
		try {
			clearGuessesInGroup(sudokuSolution);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error Ocured", e);
		}
		try {
			determineCellsWhoHas1Guess(sudokuSolution);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error Ocured", e);
		}
		if (sudokuSolution.getHowManyCellsLeft() == 0) {
			sudokuSolution.setSolved(true);
			sudokuSolution.setSudokuHasChanged(false);
			logger.info("Sudoku is solved");
			return sudokuSolution;
		}
		logger.info("This is the trial number: " + sudokuSolution.getTrial());
		sudokuSolution.incrementTrial();
		return sudokuSolution;

	}

	private void clearGuessesInGroup(Sudoku sudoku) {

		sudoku.getRowArray().forEach(group -> clearGuessesInGroup(group));
		sudoku.getColumnArray().forEach(group -> clearGuessesInGroup(group));
		sudoku.getThreeByThreeArray().forEach(group -> clearGuessesInGroup(group));
	}

	private void clearGuessesInGroup(Group group) {
		try {
			ArrayList<Integer> foundValuesInGroup = new ArrayList<Integer>();
			foundValuesInGroup.clear();
			for (int i = 0; i < 9; i++) {
				if (group.getGroup().get(i).getValue() != 0) {
					foundValuesInGroup.add(group.getGroup().get(i).getValue());
				}
			}
			for (int foundValues : foundValuesInGroup) {
				for (int Groupidx = 0; Groupidx < 9; Groupidx++) {
					for (int gssidx = 0; gssidx < 9; gssidx++) {
						ArrayList<Integer> Guesses = null;
						try {
							Guesses = group.getGroup().get(Groupidx).getGuesses();
						} catch (Exception e) {
							logger.log(Level.SEVERE, "Error Ocured", e);
						}
						if (Guesses != null && Guesses.size() > gssidx && Guesses.get(gssidx) == foundValues) {
							group.getGroup().get(Groupidx).getGuesses().remove(gssidx);
							if (group.getSudoku().isSudokuHasChanged() == false) {
								group.getSudoku().setSudokuHasChanged(true);
							}
							continue;
						}
					}
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void determineCellsWhoHas1Guess(Sudoku sudokuSolution) throws SecurityException, IllegalArgumentException,
			NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Consumer<? super Cell> action1 = cell -> determineCellsWhoHas1Guess(cell);
		Consumer<? super Row> action = group -> group.getGroup().forEach(action1);
		sudokuSolution.getRowArray().forEach(action);
		logger.info(sudokuSolution.getHowManyCellsLeft() + " Cells is waiting to be solved");
	}

	private void determineCellsWhoHas1Guess(Cell cell) {
		if (cell.getValue() == 0 && cell.getGuesses() != null && cell.getGuesses().size() == 1) {
			int value = cell.getGuesses().get(0);
			cell.setValue(value);
			cell.setColor(BrainImpl.RED);
			if (cell.getRow().getSudoku().isSudokuHasChanged() == false) {
				cell.getRow().getSudoku().setSudokuHasChanged(true);
				logger.log(Level.INFO, "sudoku has changed value has been found");
			}
		}
	}

}
