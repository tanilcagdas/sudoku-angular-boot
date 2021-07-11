package com.sudoku.service.algorithm;

import com.sudoku.beans.Sudoku;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlgorithmUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlgorithmUtil.class);

    public static Sudoku after(Sudoku sudokuSolution, Class<?> aClass) {
        if (sudokuSolution.getHowManyCellsLeft() == 0) {
            sudokuSolution.setSolved(true);
            sudokuSolution.setSudokuHasChanged(false);
            LOGGER.info("Sudoku is solved");
            return sudokuSolution;
        }
        LOGGER.info("Used {} {} cells left trial {} ", aClass.getSimpleName(), sudokuSolution.getHowManyCellsLeft() , sudokuSolution.getTrial()
        );
        sudokuSolution.incrementTrial();
        LOGGER.debug("Validating after using " + aClass.getSimpleName());
        if (!sudokuSolution.validate()) {
            LOGGER.error("Validation Failed");
            return sudokuSolution;
        }
        return sudokuSolution;
    }
}
