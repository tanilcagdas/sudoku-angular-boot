package com.sudoku.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sudoku.beans.Cell;
import com.sudoku.beans.Group;
import com.sudoku.beans.Sudoku;

@Service("SudokuAlgorithm3")
public class SudokuAlgorithm3 implements Algorithm {

    @Autowired
    private SudokuValidator sudokuValidator;

    @Autowired
    @Qualifier("SudokuAlgorithm1")
    private Algorithm sudokuAlgorithm1;

    @Autowired
    @Qualifier("SudokuAlgorithm2")
    private Algorithm sudokuAlgorithm2;

    @Autowired
    private BrainIF brain;

    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    private void determineWhoHasUniqueGuessInGroupHorizontal(Sudoku sudokuSolution) {
        for (int i = 0; i < sudokuSolution.getThreeByThreeArray().size(); i++) {
            logger.info("determineWhoHasUniqueGuessInGroupOfGroupForGroup : " + i);
            Difs difs = determineWhoHasUniqueGuessInGroupOfGroupForGroup(sudokuSolution.getThreeByThreeArray().get(i));
            clearGuesseswithdifs(sudokuSolution, difs, i);
        }

    }

    private void determineWhoHasUniqueGuessInGroupVertical(Sudoku sudokuSolution) {
        for (int i = 0; i < sudokuSolution.getThreeByThreeArray().size(); i++) {
            logger.info("determineWhoHasUniqueGuessInGroupOfGroupVertical : " + i);
            Difs difs = determineWhoHasUniqueGuessInGroupOfGroupVertical(sudokuSolution.getThreeByThreeArray().get(i));
            clearGuesseswithdifsVertical(sudokuSolution, difs, i);
        }

    }

    private void clearGuesseswithdifs(Sudoku sudokuSolution, Difs difs, int index) {

        int div = index / 3;
        int start = div * 3;
        int end = div * 3 + 3;
        for (int i = start; i < end; i++) {
            if (i == index) {
                continue;
            }
            if (difs.dif1.size() > 0 || difs.dif2.size() > 0 || difs.dif3.size() > 0) {
//				BrainImpl.BLUE = "green";
//				BrainImpl.RED = "orange";
                System.out.println("Going to clear based on diffs " + difs);
                clearGuessesFromSmallGroup(sudokuSolution.getThreeByThreeArray().get(i), difs);
                sudokuSolution.setSudokuHasChanged(true);
                sudokuSolution = cleanUp(sudokuSolution);
            }

        }
    }

    private Sudoku cleanUp(Sudoku sudokuSolution) {
//		sudokuSolution = runAlgorithm1and2(sudokuSolution);
//		sudokuSolution = resetGuesses(sudokuSolution);
//		printCellGuesses(sudokuSolution);
//		sudokuSolution = sudokuAlgorithm1.useAlgorithm(sudokuSolution);
//		printCellGuesses(sudokuSolution);
        return sudokuSolution;
    }

    private void clearGuesseswithdifsVertical(Sudoku sudokuSolution, Difs difs, int index) {

        int start = (index % 3);
        int end = start + 7;
        for (int i = start; i < end; i += 3) {
            if (i == index) {
                continue;
            }
            if (difs.dif1.size() > 0 || difs.dif2.size() > 0 || difs.dif3.size() > 0) {
//				BrainImpl.BLUE = "green";
//				BrainImpl.RED = "orange";
                System.out.println("Going to clear based on diffs from vertical results" + difs);
                clearGuessesFromSmallGroupVertical(sudokuSolution.getThreeByThreeArray().get(i), difs);
                sudokuSolution.setSudokuHasChanged(true);
                sudokuSolution = cleanUp(sudokuSolution);
            }

        }
    }

    private void clearGuessesFromSmallGroup(Group group, Difs difs) {

        for (int number = 0; number < 3; number++) {
            clearGuessesCell(group, number, difs.dif2, difs.dif3);
        }

        for (int number = 3; number < 6; number++) {
            clearGuessesCell(group, number, difs.dif1, difs.dif3);
        }

        for (int number = 6; number < 9; number++) {
            clearGuessesCell(group, number, difs.dif1, difs.dif2);
        }

    }

    private void clearGuessesCell(Group group, int number, List<Integer> difA, List<Integer> difB) {
        try {
            Cell cell = group.getGroup().get(number);
            if (cell.getGuesses() != null) {

                if (!difA.isEmpty()) {
                    System.out.printf("removing %s from row %d column %d \n", difA, cell.getRow().getIndex(), cell.getColumn().getIndex());
                    cell.getGuesses().removeAll(difA);
                }
                if (!difB.isEmpty()) {
                    System.out.printf("removing %s from row %d column %d \n", difB, cell.getRow().getIndex(), cell.getColumn().getIndex());
                    cell.getGuesses().removeAll(difB);
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
            logger.log(Level.SEVERE, "Error Occurred", e);
        }
    }

    private void clearGuessesFromSmallGroupVertical(Group group, Difs difs) {

        for (int number = 0; number < 7; number += 3) {
            clearGuessesCell(group, number, difs.dif2, difs.dif3);
        }

        for (int number = 3; number < 8; number += 3) {
            clearGuessesCell(group, number, difs.dif1, difs.dif3);
        }

        for (int number = 6; number < 9; number += 3) {
            clearGuessesCell(group, number, difs.dif1, difs.dif2);
        }

    }

    private Difs determineWhoHasUniqueGuessInGroupOfGroupForGroup(Group group) {

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
        Difs difs = new Difs(dif1, dif2, dif3);
        logger.log(Level.INFO, difs.toString());

        return difs;

    }

    private Difs determineWhoHasUniqueGuessInGroupOfGroupVertical(Group group) {

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
        Difs difs = new Difs(dif1, dif2, dif3);
        logger.info(difs.toString());

        return difs;
    }

    private void collectGuesses(Set<Integer> grpGss, Group group, int number) {
        try {
            Cell cell = group.getGroup().get(number);
            if (cell.getGuesses() != null)
                grpGss.addAll(cell.getGuesses());
//			Consumer<? super Integer> action = i-> grpGss.add(i);
//			cell.getGuesses().forEach(action);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }


    private List<Integer> subtract(Collection<Integer> a, Collection<Integer> b) {
        List<Integer> c = new ArrayList<>(a);

        c.removeAll(b);
        return c;
    }

    private List<Integer> copy(List<Integer> a) {
        if (a == null) {
            return new ArrayList<Integer>();
        }
        List<Integer> c = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            c.add(a.get(i));
        }
        return c;
    }

    @Override
    public Sudoku useAlgorithm(Sudoku sudoku) {

        printCellValues(sudoku);

        if (sudoku.getHowManyCellsLeft() != 0)
            try {
                determineWhoHasUniqueGuessInGroupHorizontal(sudoku);
                determineWhoHasUniqueGuessInGroupVertical(sudoku);
            } catch (Exception e) {
                System.err.println(e);
                return sudoku;
            }
        //
        if (sudoku.getHowManyCellsLeft() == 0) {
            sudoku.setSolved(true);
            sudoku.setSudokuHasChanged(false);
            logger.info("Sudoku is solved");
            return sudoku;
        }

        printCellValues(sudoku);
        printCellGuesses(sudoku);
        sudoku = resetGuesses(sudoku);
//        sudoku = sudokuAlgorithm1.useAlgorithm(sudoku);
        sudoku = brain.solveSudoku(sudoku);
        return sudoku;

    }

    public Sudoku runAlgorithm1and2(Sudoku sudokuSolution) {
        while (sudokuSolution.isSudokuHasChanged()) {
            while (sudokuSolution.isSudokuHasChanged()) {
                if (sudokuSolution.getHowManyCellsLeft() != 0) {
                    sudokuAlgorithm1.useAlgorithm(sudokuSolution);
//					sudoku.incrementTrial();
                    logger.info(sudokuValidator.validate(sudokuSolution) + " after alg 1");
                    if (!sudokuValidator.validate(sudokuSolution)) {
                        return sudokuSolution;
                    }
                }

            }
            if (sudokuSolution.getHowManyCellsLeft() != 0) {
                sudokuAlgorithm2.useAlgorithm(sudokuSolution);
                logger.info(sudokuValidator.validate(sudokuSolution) + " after alg 2");
                if (!sudokuValidator.validate(sudokuSolution)) {
                    return sudokuSolution;
                }
//				sudoku.incrementTrial();
            }
        }

        return sudokuSolution;
    }

    private Sudoku resetGuesses(Sudoku sudokuSolution) {
        sudokuSolution.getRowArray().forEach(row -> row.getGroup().forEach(cell -> {
            if (cell.getValue() != 0) {
                cell.setGuesses(new ArrayList<>(BrainImpl.DEFAULT_GUESSES));
            }
        }));
        return sudokuSolution;
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

class Difs {

    public List<Integer> dif1;
    public List<Integer> dif2;
    public List<Integer> dif3;


    @Override
    public String toString() {
        return "Difs [dif1=" + dif1 + ", dif2=" + dif2 + ", dif3=" + dif3 + "]";
    }


    public Difs(List<Integer> dif1, List<Integer> dif2, List<Integer> dif3) {
        super();
        this.dif1 = dif1;
        this.dif2 = dif2;
        this.dif3 = dif3;
    }
}
