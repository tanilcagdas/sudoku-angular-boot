package com.sudoku.controlller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sudoku.beans.Cell;
import com.sudoku.beans.Row;
import com.sudoku.beans.Sudoku;

public class RowArrayModel {
	
	List<Row> rowArray;

	public List<Row> getRowArray() {
		return rowArray;
	}

	public void setRowArray(List<LinkedHashMap<?, ?>> rowArray) {
		try {
			this.rowArray = convert(rowArray);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private List<Row> convert(List<LinkedHashMap<?, ?>> rowArray2) throws JsonParseException, JsonMappingException, IOException {
		Sudoku sudoku = new Sudoku();
		int index = 0;
		rowArray = new ArrayList<>();
		sudoku.setRowArray(rowArray);
		for (LinkedHashMap<?, ?> rowMap : rowArray2) {
			List<LinkedHashMap<?, ?>> groupMap = (List<LinkedHashMap<?, ?>>) rowMap.get("group");
			Row row = new Row(sudoku, index);
			for (int j = 0; j < groupMap.size(); j++) {
				LinkedHashMap<?, ?> cellMap = groupMap.get(j) ;
				Cell cell = row.getGroup().get(j);
				try {
					cell.setValue(Integer.parseInt(cellMap.get("value").toString()));
					
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			index ++;
			rowArray.add(row);
		}
		return rowArray;
	}

}

