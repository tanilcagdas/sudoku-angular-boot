package com.sudoku.service.algorithm;

import java.util.*;
import java.util.logging.Logger;

import com.sudoku.service.Algorithm;
import org.springframework.stereotype.Service;

import com.sudoku.beans.Cell;
import com.sudoku.beans.Sudoku;

@Service("SudokuAlgorithm4")
public class SudokuAlgorithm4 implements Algorithm {

    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    public static Map<List<Integer>, List<Integer>> miniGroupIndexes = new HashMap() {{
        put(Arrays.asList(0, 1, 2), Arrays.asList(3, 4, 5, 6, 7, 8));
        put(Arrays.asList(3, 4, 5), Arrays.asList(0, 1, 2, 6, 7, 8));
        put(Arrays.asList(6, 7, 8), Arrays.asList(0, 1, 2, 3, 4, 5));

        put(Arrays.asList(0, 3, 6), Arrays.asList(1, 2, 4, 5, 7, 8));
        put(Arrays.asList(1, 4, 7), Arrays.asList(0, 2, 3, 5, 6, 8));
        put(Arrays.asList(2, 5, 8), Arrays.asList(0, 1, 3, 4, 6, 7));
    }};
    private boolean modified;

    @Override
    public Sudoku useAlgorithm(Sudoku sudoku) {
        modified = false;
        sudoku.getThreeByThreeArray().forEach(threeByThreeSquare -> {
            miniGroupIndexes.forEach((k, v) -> {
                ArrayList<Cell> cells = twoGuessesForTwoCellsInSmallGroupHelper(threeByThreeSquare.getGroup(), k, v);
                threeByThreeSquare.setGroup(cells);
            });
        });

        sudoku.setSudokuHasChanged(modified);
        return AlgorithmUtil.after(sudoku, this.getClass());
    }

    protected ArrayList<Cell> twoGuessesForTwoCellsInSmallGroupHelper(ArrayList<Cell> group, List<Integer> check, List<Integer> modify) {
        List<Integer> uniqueDoubleGuesses = new ArrayList<>();
        int occurrence = 0;
        for (Integer i : check) {
            if (uniqueDoubleGuesses.isEmpty() && group.get(i).getGuesses() != null && group.get(i).getGuesses().size() == 2) {
                uniqueDoubleGuesses = group.get(i).getGuesses();
                occurrence++;
            } else if (uniqueDoubleGuesses.size() == 2 && group.get(i).getGuesses() != null && group.get(i).getGuesses().size() == 2) {
                int match = 0;
                for (Integer guess : group.get(i).getGuesses()) {
                    if (uniqueDoubleGuesses.contains(guess)) {
                        match++;
                    }
                }
                if (match == 2) {
                    occurrence++;
                }
            }
        }
        if (occurrence == 2) {
            for (Integer i : modify) {
                if(group.get(i).getGuesses() != null){
                    modified = true;
                    List<Integer> integers = new ArrayList<>(group.get(i).getGuesses());
                    for (Integer uniqueDoubleGuess : uniqueDoubleGuesses) {
                        integers.remove(uniqueDoubleGuess);
                    }
                    group.get(i).setGuesses(integers);
                }
            }
        }
        return group;
    }
}

