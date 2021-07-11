package com.sudoku.service;

import com.sudoku.beans.Cell;
import com.sudoku.beans.Row;
import com.sudoku.beans.Sudoku;
import com.sudoku.util.SudokuUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.logging.Level;

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

    @Autowired
    @Qualifier("SudokuAlgorithm4")
    private Algorithm sudokuAlgorithm4;

    Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    public static String RED = "red";
    public static String BLUE = "blue";
    public static ArrayList<Integer> DEFAULT_GUESSES = new ArrayList<Integer>();

    {
        if (DEFAULT_GUESSES.size() == 0) {
            for (int i = 1; i < 10; i++)
                DEFAULT_GUESSES.add(i);

        }
    }

    public Sudoku solveSudoku(Sudoku sudoku) {
        sudoku.validate();
        Sudoku sudokuSolution = sudoku.copy();
        sudokuSolution.setSudokuHasChanged(true);
        logger.info("Copied Sudoku");
        sudoku.validate();
        try {
            evaluateGuesses(sudokuSolution);
        } catch (Exception e) {
            logger.error("Error occurred while evaluating guesses", e);
        }

        // Check if the sudoku has changed
        sudoku.setTrial(1);
        while (algorithmNeeded(sudokuSolution)) {
            while (algorithmNeeded(sudokuSolution)) {
                while (algorithmNeeded(sudokuSolution)) {
                    while (algorithmNeeded(sudokuSolution)) {
                        sudokuSolution = sudokuAlgorithm1.useAlgorithm(sudokuSolution);
                    }
                    sudokuSolution = sudokuAlgorithm2.useAlgorithm(sudokuSolution);
                }
                sudokuSolution = sudokuAlgorithm3.useAlgorithm(sudokuSolution);
            }
            sudokuSolution = sudokuAlgorithm4.useAlgorithm(sudokuSolution);
        }
        if (!sudokuSolution.isSolved()) {
            try {
                notSolvedWriter.log(sudoku, sudokuSolution);
            } catch (IOException e) {
                logger.error("Error Occurred", e);
            }
        }
        sudokuSolution.validate();
        return sudokuSolution;
    }

    private boolean algorithmNeeded(Sudoku sudokuSolution) {
        return sudokuSolution.isSudokuHasChanged() && sudokuSolution.getHowManyCellsLeft() != 0 && sudokuSolution.getTrial() < 50;
    }

    public Sudoku solveSudokuStepByStep(Sudoku sudokuSolution, int algorithmNumber) {
        try {
            evaluateGuesses(sudokuSolution);
        } catch (Exception e) {
            logger.error("Error Occurred", e);
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

        // logger.info(cell.getValue() + ", " + cell.isFound());
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
