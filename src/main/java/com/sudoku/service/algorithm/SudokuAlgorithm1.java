package com.sudoku.service.algorithm;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sudoku.service.Algorithm;
import com.sudoku.service.BrainImpl;
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
            logger.log(Level.SEVERE, "Error Occurred", e);
        }
        try {
            determineCellsWhoHas1Guess(sudokuSolution);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error Occurred", e);
        }
        return AlgorithmUtil.after(sudokuSolution, this.getClass());

    }

    private void clearGuessesInGroup(Sudoku sudoku) {

        sudoku.getRowArray().forEach(this::clearGuessesInGroup);
        sudoku.getColumnArray().forEach(this::clearGuessesInGroup);
        sudoku.getThreeByThreeArray().forEach(this::clearGuessesInGroup);
    }

    private void clearGuessesInGroup(Group group) {
        try {
            ArrayList<Integer> foundValuesInGroup = new ArrayList<>();
            for (int i = 0; i < 9; i++) {
                if (group.getGroup().get(i).getValue() != 0) {
                    foundValuesInGroup.add(group.getGroup().get(i).getValue());
                }
            }
            for (int foundValues : foundValuesInGroup) {
                for (int Groupidx = 0; Groupidx < 9; Groupidx++) {
                    for (int gssidx = 0; gssidx < 9; gssidx++) {
                        List<Integer> Guesses = null;
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

    private void determineCellsWhoHas1Guess(Sudoku sudokuSolution) throws SecurityException, IllegalArgumentException {
        Consumer<? super Cell> action1 = cell -> determineCellsWhoHas1Guess(cell);
        Consumer<? super Row> action = group -> group.getGroup().forEach(action1);
        sudokuSolution.getRowArray().forEach(action);
    }

    private void determineCellsWhoHas1Guess(Cell cell) {
        if (cell.getValue() == 0 && cell.getGuesses() != null && cell.getGuesses().size() == 1) {
            int value = cell.getGuesses().get(0);
            cell.setValue(value);
            if (cell.getRow().getSudoku().isAlgorithm3Applied()) {
                cell.setColor("Orange");
            } else {
                cell.setColor(BrainImpl.RED);
            }
            if (!cell.getRow().getSudoku().isSudokuHasChanged()) {
                cell.getRow().getSudoku().setSudokuHasChanged(true);
                logger.log(Level.INFO, "sudoku has changed value has been found");
            }
        }
    }

}
