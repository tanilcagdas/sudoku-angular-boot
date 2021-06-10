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
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Algorithm3Test {


    @Autowired
    BrainIF brain;

    @Test
    public void solveLevel3Test() throws IOException {
        Sudoku sudoku = SudokuFileWriter.load(4580270867l, 3);
        sudoku = brain.solveSudoku(sudoku);
        List<List<Integer>> lists = SudokuFileWriter.convertSudokuToList(sudoku);
        Assert.assertEquals("[[9, 7, 4, 1, 3, 8, 2, 6, 5], [8, 3, 1, 5, 2, 6, 7, 4, 9], [5, 6, 2, 4, 9, 7, 3, 1, 8], [6, 2, 3, 9, 1, 5, 8, 7, 4], [4, 8, 9, 6, 7, 2, 5, 3, 1], [7, 1, 5, 8, 4, 3, 9, 2, 6], [1, 5, 7, 2, 8, 4, 6, 9, 3], [3, 4, 6, 7, 5, 9, 1, 8, 2], [2, 9, 8, 3, 6, 1, 4, 5, 7]]",lists.toString());
        System.out.println(sudoku);
    }

}
