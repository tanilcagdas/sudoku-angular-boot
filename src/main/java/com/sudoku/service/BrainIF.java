package com.sudoku.service;

import com.sudoku.beans.Sudoku;

public interface BrainIF {

	public Sudoku solveSudoku(Sudoku sudoku);

	public Sudoku solveSudokuStepByStep(Sudoku sudoku,int algorithm);

	public Sudoku loadDemoSudoku(Sudoku demoSudoku);
	
	public boolean isSudokuCorrect(Sudoku sudoku) ;

}
