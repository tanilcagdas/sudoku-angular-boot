package com.sudoku.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sudoku.beans.Cell;
import com.sudoku.beans.Row;
import com.sudoku.beans.Sudoku;
import com.sudoku.util.SudokuUtil;

@Service
public class BrainImpl implements BrainIF {

	@Autowired
	private NotSolvedWriter notSolvedWriter;

	@Autowired
	@Qualifier("SudokuAlgorithm1")
	private Algorithm sudokuAlgorithm1;

	@Autowired
	@Qualifier("SudokuAlgorithm2")
	private Algorithm sudokuAlgorithm2;

	@Autowired
	@Qualifier("SudokuAlgorithm3")
	private Algorithm sudokuAlgorithm3;

	Logger logger = Logger.getLogger(this.getClass().getSimpleName());

	public static String RED = "red";
	public static String BLUE = "blue";
	private static ArrayList<Integer> DEFAULT_GUESSES = new ArrayList<Integer>();
	{
		if (DEFAULT_GUESSES.size() == 0) {
			for (int i = 1; i < 10; i++)
				DEFAULT_GUESSES.add(i);

		}
	}

	public Sudoku solveSudoku(Sudoku sudoku) {
		Sudoku sudokuSolution = new Sudoku();
		sudokuSolution = sudoku.copy();
		sudokuSolution.setSudokuHasChanged(true);
		try {
			evaluateGuesses(sudokuSolution);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error Ocured", e);
		}
		// Solve the f.cking sudoku

		// Check if the sudoku has changed
		sudoku.setTrial(1);
		while (sudokuSolution.isSudokuHasChanged()) {
			while (sudokuSolution.isSudokuHasChanged()) {
				while (sudokuSolution.isSudokuHasChanged()) {
					if(sudokuSolution.getHowManyCellsLeft() != 0){
						sudokuAlgorithm1.useAlgorithm(sudokuSolution);
						sudoku.incrementTrial();
					}
					
				}
				if(sudokuSolution.getHowManyCellsLeft() != 0){
					sudokuAlgorithm2.useAlgorithm(sudokuSolution);
					sudoku.incrementTrial();
				}
			}
			if(sudokuSolution.getHowManyCellsLeft() != 0 && sudoku.getTrial() < 300){
				sudokuAlgorithm3.useAlgorithm(sudokuSolution);
				sudoku.incrementTrial();
			}
			System.out.println(sudokuSolution);
			System.out.println();;
		}
		if (!sudokuSolution.isSolved()) {
			try {
				notSolvedWriter.log(sudoku, sudokuSolution);
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Error Ocured", e);
			}
		}
		return sudokuSolution;
	}

	public Sudoku solveSudokuStepByStep(Sudoku sudokuSolution, int algorithmNumber) {
		try {
			evaluateGuesses(sudokuSolution);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error Ocured", e);
		}

		sudokuSolution.setTrial(1);
		Algorithm algorithm = null;

		switch (algorithmNumber) {
		case 1:
			algorithm = sudokuAlgorithm1;
			break;
		case 2:
			algorithm = sudokuAlgorithm2;
			break;
		default:
			break;
		}
		return algorithm.useAlgorithm(sudokuSolution);
	}

	public Sudoku loadDemoSudoku(Sudoku demoSudoku) {
		// set all zeros
		for (int row = 0; row < demoSudoku.getRowArray().size(); row++) {
			for (int column = 0; column < demoSudoku.getRowArray().get(row).getGroup().size(); column++)
				demoSudoku.getRowArray().get(row).getGroup().get(column).setValue(0);
		}
		// put known values
		CreateSudokuService createSudoku = new CreateSudokuService();
		createSudoku.loadSudoku1(demoSudoku);

		return demoSudoku;

	}

	@SuppressWarnings("unchecked")
	private void evaluateGuesses(Cell cell) {
		if (cell.getValue() == 0) {
			cell.setGuesses((ArrayList<Integer>) DEFAULT_GUESSES.clone());
		} else {
			cell.setFound(true);
		}

		// System.out.println(cell.getValue() + ", " + cell.isFound());
	}

	private void evaluateGuesses(Sudoku sudoku) throws SecurityException, IllegalArgumentException,
			NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Consumer<? super Row> action = group -> evaluateGuesses(group);
		sudoku.getRowArray().forEach(action);

	}

	private void evaluateGuesses(Row group) {
		Consumer<? super Cell> action = cell -> evaluateGuesses(cell);
		group.getGroup().forEach(action);
	}

	@Override
	public boolean isSudokuCorrect(Sudoku sudoku) {
		for (Row row : sudoku.getRowArray()) {
			if (SudokuUtil.isSudokuCorrect(row)) {
				sudoku.setSudokuCorrect(false);
				return false;
			}
		}
		return true;
	}

}
