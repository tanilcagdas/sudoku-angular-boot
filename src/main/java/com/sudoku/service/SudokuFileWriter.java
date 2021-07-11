package com.sudoku.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sudoku.beans.Sudoku;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SudokuFileWriter {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void write(Sudoku sudoku) throws IOException {

        createFolder();
        Map<String, Object> sudokuMap = new HashMap<>();
        List<List<Integer>> rowList = convertSudokuToList(sudoku);
        sudokuMap.put("sudoku", rowList);
        sudokuMap.put("puzzleId", sudoku.getPuzzleId());
        sudokuMap.put("puzzleLevel", sudoku.getPuzzleLevel());

        OBJECT_MAPPER.writeValue(getFileName(sudoku), sudokuMap);

    }

    public static List<List<Integer>> convertSudokuToList(Sudoku sudoku) {
        List<List<Integer>> rowList = new ArrayList();
        sudoku.getRowArray().forEach(row -> {
            List<Integer> cellList = new ArrayList<>();
            row.getGroup().forEach(cell -> cellList.add(cell.getValue()));
            rowList.add(cellList);
        });
        return rowList;
    }

    private static File getFileName(Sudoku sudoku) {
        return new File("sudokusolver/sudoku_" + sudoku.getPuzzleLevel() + "_" + sudoku.getPuzzleId() + ".json");
    }

    public static void convertUnsolvedToJson() throws IOException {
        List<Sudoku> sudokus = NotSolvedWriter.readANonSolvedSudoku();
        for (Sudoku sudoku : sudokus) {
            write(sudoku);
        }

    }

    public static void update(Sudoku sudoku) throws IOException {
        Map sudokuMap = OBJECT_MAPPER.readValue(getFileName(sudoku), Map.class);
        List<List<Integer>> solution = convertSudokuToList(sudoku);
        sudokuMap.put("solution", solution);
        OBJECT_MAPPER.writeValue(getFileName(sudoku), sudokuMap);
    }

    private static void createFolder() {
        String outputFolder = "sudokusolver";
        File folder = new File(outputFolder);
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    public static Sudoku load(long puzzleId, int puzzleLevel) throws IOException {

        Sudoku sudoku = new Sudoku();
        sudoku.setPuzzleId(puzzleId);
        sudoku.setPuzzleLevel(puzzleLevel);
        Map sudokuMap = OBJECT_MAPPER.readValue(getFileName(sudoku), Map.class);
        List<List<Integer>> sudoku1 = (List<List<Integer>>) sudokuMap.get("sudoku");
        return loadFromLists(sudoku, sudoku1);

    }

    public static Sudoku loadFromLists(Sudoku sudoku, List<List<Integer>> sudoku1) {
        for (int i = 0; i < sudoku1.size(); i++) {
            List<Integer> row = sudoku1.get(i);
            for (int j = 0; j < row.size(); j++) {
                Integer cell = row.get(j);
                sudoku.getRowArray().get(i).getGroup().get(j).setValue(cell);
            }
        }
        return sudoku;
    }
}
