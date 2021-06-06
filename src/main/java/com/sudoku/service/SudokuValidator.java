package com.sudoku.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sudoku.beans.Group;
import com.sudoku.beans.Sudoku;

@Service
public class SudokuValidator {
	
	boolean result;

	public boolean validate(Sudoku sudoku){
		
		result = true;
		validatorHelper(sudoku.getColumnArray());
		if(result){
			validatorHelper(sudoku.getRowArray());
		}
		if(result){
			validatorHelper(sudoku.getThreeByThreeArray());
		}
		
		
		
		return result;
		
	}

	private void validatorHelper(List<? extends Group> groupList) {
		groupList.forEach(group->{
			List<Integer> values = new ArrayList<>();
			group.getGroup().parallelStream().forEach(cell->{
				if(cell.getValue() != 0 && values.contains(cell.getValue())){
					this.result = false;
					return;
				}
				if(cell.getValue() == 0 && cell.getGuesses().size()==0){
					this.result = false;
					return;
				}
				values.add(cell.getValue());
			});
			if(!result){
				return;
			}
		});
	}
	
}
