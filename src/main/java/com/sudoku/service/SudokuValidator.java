package com.sudoku.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Service;

import com.sudoku.beans.Group;
import com.sudoku.beans.Sudoku;

@Service
public class SudokuValidator {
	

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
			System.out.println("sudoku is valid");
		} else {
			System.err.println("Sudoku is not valid");
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
//					cell.setGuesses(new ArrayList<>(BrainImpl.DEFAULT_GUESSES));
					return;
				}
				values.add(cell.getValue());
			});
			if(!result.get()){
				return;
			}
		});
//		if(!result.get()){
//			throw new RuntimeException("Sudoku is not valid");
//		}
		return result.get();
	}
	
}
