package com.sudoku.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sudoku.beans.Group;
import com.sudoku.beans.Sudoku;

@Service
public class SudokuValidator {
	private static final Logger LOGGER = LoggerFactory.getLogger(SudokuValidator.class);

	public static boolean validate(Sudoku sudoku){
		
		boolean result = true;
		result = validatorHelper(sudoku.getColumnArray());
		if(result){
			result = validatorHelper(sudoku.getRowArray());
		}
		if(result){
			result = validatorHelper(sudoku.getThreeByThreeArray());
		}
		if(result){
			LOGGER.debug("sudoku is valid");
		} else {
			LOGGER.error("Sudoku is not valid");
		}
		return result;
		
	}

	private static boolean validatorHelper(List<? extends Group> groupList) {
		AtomicBoolean result = new AtomicBoolean(true);
		groupList.forEach(group->{
			List<Integer> values = new ArrayList<>();
			group.getGroup().parallelStream().forEach(cell->{
				if(cell.getValue() != 0 && values.contains(cell.getValue())){
					result.set(false);
					System.err.printf("Row %d Column %d value %d  failed validation\n", cell.getRow().getIndex(),cell.getColumn().getIndex(), cell.getValue());
					return;
				}
				if(cell.getValue() == 0 && cell.getGuesses().size()==0){
					result.set(false);
					System.err.printf("Row %d Column %d value %d failed validation\n", cell.getRow().getIndex(),cell.getColumn().getIndex(), cell.getValue());
					return;
				}
				values.add(cell.getValue());
			});
			if(!result.get()){
				return;
			}
		});
		return result.get();
	}
	
}
