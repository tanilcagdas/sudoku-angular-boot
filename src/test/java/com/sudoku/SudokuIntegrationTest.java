package com.sudoku;

import com.sudoku.beans.Sudoku;
import com.sudoku.service.BrainIF;

import com.sudoku.service.SudokuFileWriter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SudokuIntegrationTest {


    @Autowired
    BrainIF brain;

    @Test
    public void solveLevel3Test() throws IOException {
        Sudoku sudoku = SudokuFileWriter.load(4580270867l, 3);
        sudoku = brain.solveSudoku(sudoku);
        List<List<Integer>> lists = SudokuFileWriter.convertSudokuToList(sudoku);
        Assert.assertEquals("[" +
                "[9, 7, 4, 1, 3, 8, 2, 6, 5], " +
                "[8, 3, 1, 5, 2, 6, 7, 4, 9], " +
                "[5, 6, 2, 4, 9, 7, 3, 1, 8], " +
                "[6, 2, 3, 9, 1, 5, 8, 7, 4], " +
                "[4, 8, 9, 6, 7, 2, 5, 3, 1], " +
                "[7, 1, 5, 8, 4, 3, 9, 2, 6], " +
                "[1, 5, 7, 2, 8, 4, 6, 9, 3], " +
                "[3, 4, 6, 7, 5, 9, 1, 8, 2], " +
                "[2, 9, 8, 3, 6, 1, 4, 5, 7]]",lists.toString());
        System.out.println(sudoku);
    }

    @Test
    public void solveLevel4Test() throws IOException {
        Sudoku sudoku = SudokuFileWriter.load(5978709421l, 4);
        sudoku = brain.solveSudoku(sudoku);
        List<List<Integer>> lists = SudokuFileWriter.convertSudokuToList(sudoku);
        System.out.println(lists);
        System.out.println(sudoku);
        Assert.assertEquals("[" +
                "[5, 4, 6, 2, 7, 3, 9, 8, 1], " +
                "[2, 9, 8, 4, 6, 1, 7, 5, 3], " +
                "[1, 7, 3, 5, 8, 9, 6, 2, 4], " +
                "[6, 2, 1, 8, 5, 4, 3, 9, 7], " +
                "[9, 5, 4, 1, 3, 7, 2, 6, 8], " +
                "[3, 8, 7, 9, 2, 6, 1, 4, 5], " +
                "[8, 3, 2, 6, 1, 5, 4, 7, 9], " +
                "[4, 1, 5, 7, 9, 2, 8, 3, 6], " +
                "[7, 6, 9, 3, 4, 8, 5, 1, 2]]",lists.toString());

    }



    @Test
    public void testAllFromInput() {
        List<String> results = new ArrayList<>();
        List<String> failedResults = new ArrayList<>();
        List<Map<Integer, Long>> sudokuIds = SudokuFileWriter.list();
        for (Map<Integer, Long> sudokuId : sudokuIds) {
            for (Map.Entry<Integer, Long> integerLongEntry : sudokuId.entrySet()) {
                try {

                    long start = System.currentTimeMillis();
                    Sudoku sudoku = SudokuFileWriter.load(integerLongEntry.getValue(), integerLongEntry.getKey());
                    sudoku = brain.solveSudoku(sudoku);
                    if(sudoku.isSolved()){
                        results.add(String.format(" Sudoku %d %d is solved ? %b in %d ms",integerLongEntry.getValue(),integerLongEntry.getKey(),sudoku.isSolved(),
                                System.currentTimeMillis() - start));
                    } else {
                        failedResults.add(String.format(" Sudoku %d %d is solved ? %b in %d ms",integerLongEntry.getValue(),integerLongEntry.getKey(),sudoku.isSolved(),
                                System.currentTimeMillis() - start));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        results.forEach(System.out::println);
        System.out.println();
        failedResults.forEach(System.out::println);
        System.out.println();
        System.out.printf("%d success %d fail", results.size(), failedResults.size());
    }

}
