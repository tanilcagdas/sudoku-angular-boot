package com.sudoku.beans;

public class Row extends Group {
	
	public Row(){
		
		super();
	}

	public Row(Sudoku sudoku,int index) {
		super(sudoku, index);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

}
