package com.sudoku.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sudoku.beans.Sudoku;

@Service
public class NotSolvedWriter {

    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    public void log(Sudoku sudoku, Sudoku unfinishedSolution)
            throws IOException {
        if (unfinishedSolution.getHowManyCellsLeft() != 81) {
            StringBuffer sb = new StringBuffer();
            sb.append("Date : ");
            sb.append(new Date().toString());
            sb.append("\n");
            sb.append("Sudoku id : ");
            sb.append(sudoku.getPuzzleId());
            sb.append("\n");
            sb.append("Level : ");
            sb.append(sudoku.getPuzzleLevel());
            sb.append("\n");
            sb.append("_____________Sudoku__________________");
            sb.append("\n");
            sb.append(sudoku.toString());
            sb.append("_____________Unfinished_Solution__________________");
            sb.append("\n");
            sb.append(unfinishedSolution.toString());

            String outputFolder = "sudokusolver";
            File folder = new File(outputFolder);
            if(!folder.exists()){
                folder.mkdir();
            }

            String outputFile = "sudokusolver/unSolvedSudoku_"
                    + sudoku.getPuzzleId() + "_" + sudoku.getPuzzleLevel();
            File file = new File(outputFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outputFile), "UTF8"));
            writer.write(sb.toString());
            writer.close();
        } else {
            logger.info("This is an empty sudoku aint writing");
        }
    }

    public static List<Sudoku> readANonSolvedSudoku() throws FileNotFoundException {
        List<Sudoku> sudokuList = new ArrayList<>();
        Collection<File> all = getAllFileNames();
        Collection<File> unSolvedSudokus = all.stream().filter(file -> file.getName().contains("unSolvedSudoku")).collect(Collectors.toList());
        Object[] strArray = unSolvedSudokus.toArray();

        for (int index = 0; index < strArray.length; index++) {
            Sudoku sudoku = new Sudoku();
//        }
//        if (strArray.length != 0) {
            File file = (File) strArray[index];
            file.toString();
            String inputFile = file.toString();
            Reader reader = new InputStreamReader(
                    new FileInputStream(inputFile));
            String str = "";
            try {
                for (int i = 0; i < 1300; i++) {
                    char c = (char) reader.read();
                    str = str + c;
                }
                // logger.info(str);
                reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    int value;
                    try{
                        value = Character.getNumericValue(((str.split("" + i + j + " : "))[1].charAt(0)));
                    } catch (Exception e) {
                        value = 0;
                    }
                    // logger.info(value);
                    sudoku.getRowArray().get(i).getGroup().get(j)
                            .setValue(value);
                }
            }
            String[] split = file.getName().replace("unSolvedSudoku_", "").split("_");
            sudoku.setPuzzleId(Long.parseLong(split[0]));
            sudoku.setPuzzleLevel(Integer.parseInt(split[1]));
            sudokuList.add(sudoku);
        }
        return sudokuList;
    }


    public static Collection<File> getAllFileNames() {
        Collection<File> all = new ArrayList<>();
        addTree(new File("sudokusolver"), all);
        return all;
    }

    static void addTree(File file, Collection<File> all) {
        File[] children = file.listFiles();
        if (children != null) {
            for (File child : children) {
                all.add(child);
                addTree(child, all);
            }
        }
    }

}
