package com.sudoku.service.algorithm;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sudoku.service.Algorithm;
import com.sudoku.service.BrainImpl;
import com.sudoku.service.SudokuException;
import org.springframework.stereotype.Service;

import com.sudoku.beans.Cell;
import com.sudoku.beans.Group;
import com.sudoku.beans.Row;
import com.sudoku.beans.Sudoku;
import com.sudoku.util.SudokuUtil;

@Service("SudokuAlgorithm2")
public class SudokuAlgorithm2 implements Algorithm {

    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    /*
     * (non-Javadoc)
     *
     * @see com.sudoku.service.Algorithm#useAlgorithm(com.sudoku.beans.Sudoku)
     */
    @Override
    public Sudoku useAlgorithm(Sudoku sudokuSolution) {
        if (sudokuSolution.getHowManyCellsLeft() != 0)
            try {
                determineWhoHasUniqueGuessInGroup(sudokuSolution);
            } catch (Exception e) {
                if (e.getCause() instanceof SudokuException) {
                    logger.info(e.getCause().getMessage());
                    return sudokuSolution;
                } else {
                    logger.log(Level.SEVERE, "Error Occurred", e);
                }
            }
        return AlgorithmUtil.after(sudokuSolution, this.getClass());
    }

    private void determineWhoHasUniqueGuessInGroup(Sudoku sudokuSolution) throws SecurityException,
            IllegalArgumentException {

        sudokuSolution.getRowArray().forEach(group -> determineWhoHasUniqueGuessInGroup(group));
        sudokuSolution.getColumnArray().forEach(group -> determineWhoHasUniqueGuessInGroup(group));
        sudokuSolution.getThreeByThreeArray().forEach(group -> determineWhoHasUniqueGuessInGroup(group));

    }


    private void determineWhoHasUniqueGuessInGroup(Group group) {

        try {
            for (int number = 1; number < 10; number++) {
                int uniqueGuessCount = 0;
                for (Cell cell : group.getGroup()) {
                    if (cell.getGuesses() != null) {
                        for (int guess : cell.getGuesses()) {
                            if (guess == number) {
                                uniqueGuessCount++;
                            }
                        }
                    }
                }
                if (uniqueGuessCount == 1) {
                    for (Cell cell : group.getGroup()) {
                        if (cell.getValue() == number) {
                            // logger.info("The number : " + number +" is
                            // a
                            // unique guess but it exists in the group");
                            return;
                        }
                    }

                    markAsUniqueGuessAndDetermine(number, group);
                    group.getSudoku().setSudokuCorrect(true);

                    if (!SudokuUtil.isSudokuCorrect(group)) {
                        logger.log(Level.SEVERE, "Sudoku is not Correct after markAsUniqueGuessAndDetermine number : "
                                + number + ", group : " + group);
                        return;
                    }
                }
            }
        } catch (SecurityException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void markAsUniqueGuessAndDetermine(int number, Group group) {
        for (int i = 0; i < 9; i++) {
            Cell cell = group.getGroup().get(i);
            if (cell.getGuesses() != null) {
                for (int guess : cell.getGuesses()) {
                    if (guess == number) {
                        // TODO check others
                        for (int j = 0; j < 9; j++) {
                            if (i == j)
                                continue;
                            Cell compareCell = group.getGroup().get(j);
                            if (compareCell.getGuesses() != null) {
                                for (int compareGuess : compareCell.getGuesses()) {
                                    if (compareGuess == number) {
                                        return;
                                    }
                                }
                            }
                        }
                        cell.setValue(number);
                        if (cell.getRow().getSudoku().isAlgorithm3Applied()) {
                            cell.setColor("Green");
                        } else {
                            cell.setColor(BrainImpl.BLUE);
                        }
                        group.getSudoku().setSudokuHasChanged(true);
                        break;
                    }
                }
            }
        }
    }


}
