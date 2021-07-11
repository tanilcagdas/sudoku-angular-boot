package com.sudoku.service;

import com.sudoku.beans.Cell;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SudokuAlgorithm4Test extends TestCase {

    public void testTwoGuessesForTwoCellsInSmallGroupHelper() {
        SudokuAlgorithm4 sudokuAlgorithm4 = new SudokuAlgorithm4();
        ArrayList<Cell> cells = new ArrayList<>();
        Cell cell0 = new Cell(0, Arrays.asList(1,3));
        cells.add(cell0);
        Cell cell1 = new Cell(0, Arrays.asList(1,3));
        cells.add(cell1);
        Cell cell2 = new Cell(2, Collections.EMPTY_LIST);
        cells.add(cell2);
        Cell cell3 = new Cell(4, Collections.EMPTY_LIST);
        cells.add(cell3);
        Cell cell4 = new Cell(5, Collections.EMPTY_LIST);
        cells.add(cell4);
        Cell cell5 = new Cell(6, Collections.EMPTY_LIST);
        cells.add(cell5);
        Cell cell6 = new Cell(7, Collections.EMPTY_LIST);
        cells.add(cell6);
        Cell cell7 = new Cell(8, Collections.EMPTY_LIST);
        cells.add(cell7);
        Cell cell8 = new Cell(0, Arrays.asList(1,3,7));
        cells.add(cell8);
        ArrayList<Cell> cells1 = sudokuAlgorithm4.twoGuessesForTwoCellsInSmallGroupHelper(cells, Arrays.asList(0, 1, 2), Arrays.asList(3, 4, 5, 6, 7, 8));
        System.out.println(cells1);
        assertEquals("[7]",cells1.get(8).getGuesses().toString());
    }

    public void testTwoGuessesForTwoCellsInSmallGroupHelperVertical() {
        SudokuAlgorithm4 sudokuAlgorithm4 = new SudokuAlgorithm4();
        ArrayList<Cell> cells = new ArrayList<>();
        Cell cell0 = new Cell(0, Arrays.asList(1,3));
        cells.add(cell0);
        Cell cell1 = new Cell(4, Collections.EMPTY_LIST);
        cells.add(cell1);
        Cell cell2 = new Cell(2, Collections.EMPTY_LIST);
        cells.add(cell2);
        Cell cell3 = new Cell(0, Arrays.asList(1,3));
        cells.add(cell3);
        Cell cell4 = new Cell(5, Collections.EMPTY_LIST);
        cells.add(cell4);
        Cell cell5 = new Cell(6, Collections.EMPTY_LIST);
        cells.add(cell5);
        Cell cell6 = new Cell(7, Collections.EMPTY_LIST);
        cells.add(cell6);
        Cell cell7 = new Cell(8, Collections.EMPTY_LIST);
        cells.add(cell7);
        Cell cell8 = new Cell(0, Arrays.asList(1,3,7));
        cells.add(cell8);
        ArrayList<Cell> cells1 = sudokuAlgorithm4.twoGuessesForTwoCellsInSmallGroupHelper(cells, Arrays.asList(0, 3, 6), Arrays.asList(1, 2, 4, 5, 7, 8));
        System.out.println(cells1);
        assertEquals("[7]",cells1.get(8).getGuesses().toString());
    }
}
