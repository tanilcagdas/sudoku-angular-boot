package com.sudoku;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Map;

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

    @Test
    public void solveLevel3TestA() throws IOException {
        Sudoku sudoku = SudokuFileWriter.load(6288359179l, 3);
        sudoku = brain.solveSudoku(sudoku);
        List<List<Integer>> lists = SudokuFileWriter.convertSudokuToList(sudoku);
        System.out.println(lists);
//        Assert.assertEquals("[[9, 7, 4, 1, 3, 8, 2, 6, 5], [8, 3, 1, 5, 2, 6, 7, 4, 9], [5, 6, 2, 4, 9, 7, 3, 1, 8], [6, 2, 3, 9, 1, 5, 8, 7, 4], [4, 8, 9, 6, 7, 2, 5, 3, 1], [7, 1, 5, 8, 4, 3, 9, 2, 6], [1, 5, 7, 2, 8, 4, 6, 9, 3], [3, 4, 6, 7, 5, 9, 1, 8, 2], [2, 9, 8, 3, 6, 1, 4, 5, 7]]",lists.toString());
        System.out.println(sudoku);
    }

    @Test
    public void solveLevel4FailingTest() throws IOException {
        Sudoku sudoku = SudokuFileWriter.load(5978709421l, 4);
        sudoku = brain.solveSudoku(sudoku);
        List<List<Integer>> lists = SudokuFileWriter.convertSudokuToList(sudoku);
//        Assert.assertEquals("[[0, 4, 0, 0, 0, 3, 9, 0, 1], [2, 0, 3, 0, 8, 0, 6, 0, 0], [1, 0, 3, 0, 8, 0, 6, 0, 0], [6, 2, 0, 0, 0, 4, 3, 9, 7], [0, 0, 4, 0, 0, 0, 2, 6, 8], [0, 8, 0, 9, 0, 0, 1, 4, 5], [0, 0, 2, 0, 1, 5, 4, 7, 9], [0, 1, 5, 7, 0, 0, 8, 0, 6], [0, 0, 9, 3, 0, 0, 5, 1, 0]]",lists.toString());
        System.out.println(lists);
        System.out.println(sudoku);

    }

    @Test
    public void solveLevel4FailingTestA() throws IOException {

        List<List<Integer>> lists  = new ObjectMapper().readValue("[[0, 4, 0, 0, 0, 3, 9, 0, 1], [2, 0, 3, 0, 8, 0, 6, 0, 0], [1, 0, 3, 0, 8, 0, 6, 0, 0], [6, 2, 0, 0, 0, 4, 3, 9, 7], [0, 0, 4, 0, 0, 0, 2, 6, 8], [0, 8, 0, 9, 0, 0, 1, 4, 5], [0, 0, 2, 0, 1, 5, 4, 7, 9], [0, 1, 5, 7, 0, 0, 8, 0, 6], [0, 0, 9, 3, 0, 0, 5, 1, 0]]", List.class);
        Sudoku sudoku = SudokuFileWriter.loadFromLists(new Sudoku(), lists);
        sudoku = brain.solveSudoku(sudoku);
        List<List<Integer>> lists1 = SudokuFileWriter.convertSudokuToList(sudoku);
//        Assert.assertEquals("[[0, 4, 0, 0, 0, 3, 9, 0, 1], [2, 0, 3, 0, 8, 0, 6, 0, 0], [1, 0, 3, 0, 8, 0, 6, 0, 0], [6, 2, 0, 0, 0, 4, 3, 9, 7], [0, 0, 4, 0, 0, 0, 2, 6, 8], [0, 8, 0, 9, 0, 0, 1, 4, 5], [0, 0, 2, 0, 1, 5, 4, 7, 9], [0, 1, 5, 7, 0, 0, 8, 0, 6], [0, 0, 9, 3, 0, 0, 5, 1, 0]]",lists.toString());
        System.out.println(lists1);
        System.out.println(sudoku);

    }

}
