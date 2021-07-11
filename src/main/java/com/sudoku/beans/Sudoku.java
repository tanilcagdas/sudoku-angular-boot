package com.sudoku.beans;

import com.sudoku.service.SudokuValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class Sudoku implements Cloneable, Comparable<Sudoku> {

    Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    private List<Row> rowArray = new ArrayList<Row>();
    private List<Column> columnArray = new ArrayList<Column>();
    private List<ThreeByThreeSquare> threeByThreeArray = new ArrayList<ThreeByThreeSquare>();
    private boolean solved = false;
    private int HowManyCellsLeft = 81;
    private long puzzleId;
    private int puzzleLevel;
    private boolean sudokuHasChanged;
    private int trial;
    private boolean sudokuCorrect;
    private int depth;
    private boolean algorithm3Applied;

    public Sudoku() {
        for (int i = 0; i < 9; i++) {
            rowArray.add(new Row(this, i));
        }
        syncColumnsToRow();
        syncThreeByThreeSquaresToRow();
        registerAllObservers();
        logger.info("created new sudoku");
    }


    private void registerAllObservers() {
        for (Row row : rowArray) {
            for (Cell cell : row.getGroup()) {
                cell.registerObservers();
            }
        }

    }


    //GETTERS & SETTERS
    public List<Row> getRowArray() {
        if (rowArray == null) {
            rowArray = new ArrayList<Row>();
        }
        return rowArray;
    }

    public void setRowArray(List<Row> list) {
        this.rowArray = list;
    }

    public List<Column> getColumnArray() {
        return columnArray;
    }

    public void setColumnArray(List<Column> columnArray) {
        this.columnArray = columnArray;
    }

    public List<ThreeByThreeSquare> getThreeByThreeArray() {
        return threeByThreeArray;
    }

    public void setThreeByThreeArray(List<ThreeByThreeSquare> threeByThreeArray) {
        this.threeByThreeArray = threeByThreeArray;
    }

    public boolean isSolved() {
        return solved;
    }


    public void setSolved(boolean solved) {
        this.solved = solved;
    }


    public int getHowManyCellsLeft() {
        countHowManyCellsLeft();
        return HowManyCellsLeft;
    }

    private void countHowManyCellsLeft() {
        AtomicInteger count = new AtomicInteger(0);
        rowArray.stream().map(row -> row.getGroup().stream().filter(cell -> cell.getValue() == 0).count()).forEach(ct -> count.set(count.get() + ct.intValue()));
        HowManyCellsLeft = count.get();
    }


    public void setHowManyCellsLeft(int howManyCellsLeft) {
        HowManyCellsLeft = howManyCellsLeft;
    }


    public long getPuzzleId() {
        return puzzleId;
    }


    public void setPuzzleId(long puzzleId) {
        this.puzzleId = puzzleId;
    }


    public int getPuzzleLevel() {
        return puzzleLevel;
    }


    public void setPuzzleLevel(int puzzleLevel) {
        this.puzzleLevel = puzzleLevel;
    }


    private void syncColumnsToRow() {
        columnArray.clear();
        for (int rowIndex = 0; rowIndex < 9; rowIndex++) {
            for (int columnIndex = 0; columnIndex < 9; columnIndex++) {

                if (columnArray.size() < columnIndex + 1) {
                    columnArray.add(new Column(this, columnIndex));
//						logger.info("created collumn for row : "+ row +", collumn: "+ collumn );
                }
                Cell cell = rowArray.get(rowIndex).getGroup().get(columnIndex);
                columnArray.get(columnIndex).getGroup().set(rowIndex, cell);
                cell.setColumn(columnArray.get(columnIndex));
//					Cell leftCell=collumnArray.get(collumn).getGroup().get(row);

//					logger.info(leftCell.equals(rightCell)+" , "+leftCell.toString()+" , "+rightCell.toString());
            }
        }
    }


    private void syncThreeByThreeSquaresToRow() {
        threeByThreeArray.clear();


        syncThreeByThreeSquaresToRowHelper(0, 3, 0, 3);
        syncThreeByThreeSquaresToRowHelper(0, 3, 3, 6);
        syncThreeByThreeSquaresToRowHelper(0, 3, 6, 9);
        syncThreeByThreeSquaresToRowHelper(3, 6, 0, 3);
        syncThreeByThreeSquaresToRowHelper(3, 6, 3, 6);
        syncThreeByThreeSquaresToRowHelper(3, 6, 6, 9);
        syncThreeByThreeSquaresToRowHelper(6, 9, 0, 3);
        syncThreeByThreeSquaresToRowHelper(6, 9, 3, 6);
        syncThreeByThreeSquaresToRowHelper(6, 9, 6, 9);


    }

    private void syncThreeByThreeSquaresToRowHelper(int rowStart, int rowEnd, int columnStart, int columnEnd) {
        int threeByThreeIndex = 0;
        int groupCount = 0;
        for (int rowIndex = rowStart; rowIndex < rowEnd; rowIndex++) {

            for (int columnIndex = columnStart; columnIndex < columnEnd; columnIndex++, groupCount++) {
                threeByThreeIndex = calculateGroup(rowIndex, columnIndex);
                //alt taraf ok
                if (threeByThreeArray.size() < threeByThreeIndex + 1) {
                    threeByThreeArray.add(new ThreeByThreeSquare(this, threeByThreeIndex));
//						logger.info("created ThreeByThreeSquare for group: "+group +", row : "+ row +", collumn: "+ collumn );
                }
                //alt taraf ok
                Cell cell = rowArray.get(rowIndex).getGroup().get(columnIndex);
                threeByThreeArray.get(threeByThreeIndex).getGroup().set(groupCount, cell);
                cell.setThreeByThreeSquare(threeByThreeArray.get(threeByThreeIndex));
//					Cell leftCell=threeByThreeArray.get(group).getGroup().get(groupCount);

//					logger.info("For threebythree "+leftCell.equals(rightCell)+" , "+leftCell.toString()+" , "+rightCell.toString());
            }
        }

    }

    private int calculateGroup(int row, int column) {
        int group = 0;
        if (row < 3 && column < 3) group = 0;
        else if (row < 3 && column < 6) group = 1;
        else if (row < 3 && column < 9) group = 2;
        else if (row < 6 && column < 3) group = 3;
        else if (row < 6 && column < 6) group = 4;
        else if (row < 6 && column < 9) group = 5;
        else if (row < 9 && column < 3) group = 6;
        else if (row < 9 && column < 6) group = 7;
        else if (row < 9 && column < 9) group = 8;

        return group;
    }

    /**
     * @return the sudokuHasChanged
     */
    public boolean isSudokuHasChanged() {
        return sudokuHasChanged;
    }


    /**
     * @param sudokuHasChanged the sudokuHasChanged to set
     */
    public void setSudokuHasChanged(boolean sudokuHasChanged) {
        this.sudokuHasChanged = sudokuHasChanged;
    }


    public Sudoku copy() {
        Sudoku sudoku = new Sudoku();
        for (int i = 0; i < 9; i++) {
            Row row = sudoku.getRowArray().get(i);
            for (int j = 0; j < 9; j++) {
                Cell cell = row.getGroup().get(j);
                cell.setValue(this.getRowArray().get(i).getGroup().get(j).getValue());
                cell.setColor(this.getRowArray().get(i).getGroup().get(j).getColor());
            }
        }
        sudoku.setPuzzleId(puzzleId);
        sudoku.setPuzzleLevel(puzzleLevel);
        sudoku.setDepth(depth);
        sudoku.setSudokuCorrect(sudokuCorrect);
        return sudoku;
    }

    public int compareTo(Sudoku o) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Cell cell = getRowArray().get(i).getGroup().get(j);
                sb.append("Cell ").append(i).append(j).append(" : ");
                sb.append(cell.getValue()).append(" ");

                sb.append("Guesses : ");
                sb.append(cell.getGuesses()).append(" ");
                sb.append("Color :  ");
                sb.append(cell.getColor()).append(" ");

                sb.append("\n");
            }
        }
        return sb.toString();
    }


    public void setTrial(int trial) {
        this.trial = trial;
    }


    public int getTrial() {
        return trial;
    }


    public void incrementTrial() {
        trial++;
    }


    public void setSudokuCorrect(boolean sudokuCorrect) {
        this.sudokuCorrect = sudokuCorrect;

    }

    public boolean isSudokuCorrect() {
        return sudokuCorrect;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public boolean validate() {
        return SudokuValidator.validate(this);
    }

    public boolean isAlgorithm3Applied() {
        return algorithm3Applied;
    }

    public void setAlgorithm3Applied(boolean algorithm3Applied) {
        this.algorithm3Applied = algorithm3Applied;
    }
}
