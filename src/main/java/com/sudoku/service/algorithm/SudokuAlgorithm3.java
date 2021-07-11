package com.sudoku.service.algorithm;

import com.sudoku.beans.Cell;
import com.sudoku.beans.Group;
import com.sudoku.beans.Sudoku;
import com.sudoku.service.Algorithm;
import com.sudoku.service.BrainImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("SudokuAlgorithm3")
public class SudokuAlgorithm3 implements Algorithm {

    private int maxDepth = 10; //TODO probably we dont need it

    Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private int previousRemaining;

    @Override
    public Sudoku useAlgorithm(Sudoku sudoku) {
        if (previousRemaining == sudoku.getHowManyCellsLeft()) {
            return sudoku;
        }
        if (sudoku.getDepth() > maxDepth) {
            System.err.println("Max depth have been reached");
            return sudoku;
        }

        sudoku.setAlgorithm3Applied(true);
        sudoku.setDepth(sudoku.getDepth() + 1);

        if (logger.isDebugEnabled()) {
            printCellValues(sudoku);
        }

        try {
            sudoku.validate();
            determineWhoHasUniqueGuessInGroupHorizontal(sudoku);
            sudoku.validate();
            determineWhoHasUniqueGuessInGroupVertical(sudoku);
            sudoku.validate();
        } catch (Exception e) {
            System.err.println(e);
            return sudoku;
        }

        if (logger.isDebugEnabled()) {
            printCellValues(sudoku);
            printCellGuesses(sudoku);
        }

        previousRemaining = sudoku.getHowManyCellsLeft();
        return AlgorithmUtil.after(sudoku, this.getClass());
    }

    private void determineWhoHasUniqueGuessInGroupHorizontal(Sudoku sudokuSolution) {
        for (int i = 0; i < sudokuSolution.getThreeByThreeArray().size(); i++) {
            logger.debug("determineWhoHasUniqueGuessInGroupOfGroupForGroup : " + i);
            Diffs diffs = determineWhoHasUniqueGuessInGroupOfGroupForGroup(sudokuSolution.getThreeByThreeArray().get(i));
            clearGuesseswithdifs(sudokuSolution, diffs, i);
        }

    }

    private void determineWhoHasUniqueGuessInGroupVertical(Sudoku sudokuSolution) {
        for (int i = 0; i < sudokuSolution.getThreeByThreeArray().size(); i++) {
            logger.debug("determineWhoHasUniqueGuessInGroupOfGroupVertical : " + i);
            Diffs diffs = determineWhoHasUniqueGuessInGroupOfGroupVertical(sudokuSolution.getThreeByThreeArray().get(i));
            clearGuesseswithdifsVertical(sudokuSolution, diffs, i);
        }

    }

    private void clearGuesseswithdifs(Sudoku sudokuSolution, Diffs diffs, int index) {

        int div = index / 3;
        int start = div * 3;
        int end = div * 3 + 3;
        for (int i = start; i < end; i++) {
            if (i == index) {
                continue;
            }
            if (diffs.dif1.size() > 0 || diffs.dif2.size() > 0 || diffs.dif3.size() > 0) {
                BrainImpl.BLUE = "green";
                BrainImpl.RED = "orange";
                logger.debug("Going to clear based on diffs " + diffs);
                clearGuessesFromSmallGroup(sudokuSolution.getThreeByThreeArray().get(i), diffs);
                sudokuSolution.setSudokuHasChanged(true);
            }

        }
    }


    private void clearGuesseswithdifsVertical(Sudoku sudokuSolution, Diffs diffs, int index) {

        int start = (index % 3);
        int end = start + 7;
        for (int i = start; i < end; i += 3) {
            if (i == index) {
                continue;
            }
            if (diffs.dif1.size() > 0 || diffs.dif2.size() > 0 || diffs.dif3.size() > 0) {
                BrainImpl.BLUE = "green";
                BrainImpl.RED = "orange";
                logger.debug("Going to clear based on diffs from vertical results " + diffs);
                sudokuSolution.validate();
                clearGuessesFromSmallGroupVertical(sudokuSolution.getThreeByThreeArray().get(i), diffs);
                sudokuSolution.validate();
                sudokuSolution.setSudokuHasChanged(true);
            }

        }
    }

    private void clearGuessesFromSmallGroup(Group group, Diffs diffs) {

        for (int number = 0; number < 3; number++) {
            clearGuessesCell(group, number, diffs.dif1);
        }

        for (int number = 3; number < 6; number++) {
            clearGuessesCell(group, number, diffs.dif2);
        }

        for (int number = 6; number < 9; number++) {
            clearGuessesCell(group, number, diffs.dif3);
        }

    }

    private void clearGuessesCell(Group group, int number, List<Integer> difA) {
        try {
            Cell cell = group.getGroup().get(number);
            if (cell.getGuesses() != null) {

                if (!difA.isEmpty()) {
                    logger.debug("removing {} from row {} column {} ", difA, cell.getRow().getIndex(), cell.getColumn().getIndex());
                    cell.getGuesses().removeAll(difA);
                }
                if (cell.getGuesses().isEmpty()) {
                    System.out.println("WTF");
                }
                if (cell.getGuesses().size() == 1) {
                    cell.setValue(cell.getGuesses().get(0));
                    System.out.println("found one");
                }
            }
        } catch (Exception e) {
            logger.error("Error Occurred", e);
        }
    }

    private void clearGuessesFromSmallGroupVertical(Group group, Diffs diffs) {

        for (int number = 0; number < 7; number += 3) {
            clearGuessesCell(group, number, diffs.dif1);
        }

        for (int number = 1; number < 8; number += 3) {
            clearGuessesCell(group, number, diffs.dif2);
        }

        for (int number = 2; number < 9; number += 3) {
            clearGuessesCell(group, number, diffs.dif3);
        }

    }

    private Diffs determineWhoHasUniqueGuessInGroupOfGroupForGroup(Group group) {

        Set<Integer> grp1Gss = new HashSet<>();
        Set<Integer> grp2Gss = new HashSet<>();
        Set<Integer> grp3Gss = new HashSet<>();

        for (int number = 0; number < 3; number++) {
            collectGuesses(grp1Gss, group, number);
        }

        for (int number = 3; number < 6; number++) {
            collectGuesses(grp2Gss, group, number);
        }

        for (int number = 6; number < 9; number++) {
            collectGuesses(grp3Gss, group, number);
        }

        List<Integer> dif1 = subtract(grp1Gss, grp2Gss);
        dif1 = subtract(dif1, grp3Gss);

        List<Integer> dif2 = subtract(grp2Gss, grp1Gss);
        dif2 = subtract(dif2, grp3Gss);

        List<Integer> dif3 = subtract(grp3Gss, grp1Gss);
        dif3 = subtract(dif3, grp2Gss);
        Diffs diffs = new Diffs(dif1, dif2, dif3);
        logger.debug(diffs.toString());

        return diffs;

    }

    private Diffs determineWhoHasUniqueGuessInGroupOfGroupVertical(Group group) {

        Set<Integer> grp1Gss = new HashSet<>();
        Set<Integer> grp2Gss = new HashSet<>();
        Set<Integer> grp3Gss = new HashSet<>();

        for (int number = 0; number < 7; number += 3) {
            collectGuesses(grp1Gss, group, number);
        }

        for (int number = 1; number < 8; number += 3) {
            collectGuesses(grp2Gss, group, number);
        }

        for (int number = 2; number < 9; number += 3) {
            collectGuesses(grp3Gss, group, number);
        }

        List<Integer> dif1 = subtract(grp1Gss, grp2Gss);
        dif1 = subtract(dif1, grp3Gss);

        List<Integer> dif2 = subtract(grp2Gss, grp1Gss);
        dif2 = subtract(dif2, grp3Gss);

        List<Integer> dif3 = subtract(grp3Gss, grp1Gss);
        dif3 = subtract(dif3, grp2Gss);
        Diffs diffs = new Diffs(dif1, dif2, dif3);
        logger.debug(diffs.toString());

        return diffs;
    }

    private void collectGuesses(Set<Integer> grpGss, Group group, int number) {
        try {
            Cell cell = group.getGroup().get(number);
            if (cell.getGuesses() != null)
                grpGss.addAll(cell.getGuesses());
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }


    private List<Integer> subtract(Collection<Integer> a, Collection<Integer> b) {
        List<Integer> c = new ArrayList<>(a);

        c.removeAll(b);
        return c;
    }


    private void printCellGuesses(Sudoku sudoku) {
        sudoku.getRowArray().forEach(row -> {
            row.getGroup().forEach(cell -> {
                System.out.print(cell.getGuesses() + " : ");
            });
            System.out.println();
        });
    }

    private void printCellValues(Sudoku sudoku) {
        sudoku.getRowArray().forEach(row -> {
            row.getGroup().forEach(cell -> {
                System.out.print(cell.getValue() + " : ");
            });
            System.out.println();
        });
    }
}

class Diffs {

    public List<Integer> dif1;
    public List<Integer> dif2;
    public List<Integer> dif3;


    @Override
    public String toString() {
        return "Difs [dif1=" + dif1 + ", dif2=" + dif2 + ", dif3=" + dif3 + "]";
    }


    public Diffs(List<Integer> dif1, List<Integer> dif2, List<Integer> dif3) {
        super();
        this.dif1 = dif1;
        this.dif2 = dif2;
        this.dif3 = dif3;
    }
}
