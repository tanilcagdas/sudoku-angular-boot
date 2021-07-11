package com.sudoku.service.algorithm;

import com.sudoku.beans.Cell;
import com.sudoku.beans.Sudoku;
import com.sudoku.service.Algorithm;
import com.sudoku.service.SudokuValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service("SudokuAlgorithm5")
public class SudokuAlgorithm5 implements Algorithm {
	
	Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	
	@Autowired
	private SudokuValidator sudokuValidator;

	@Autowired
	@Qualifier("SudokuAlgorithm1")
	private Algorithm sudokuAlgorithm1;

	@Autowired
	@Qualifier("SudokuAlgorithm2")
	private Algorithm sudokuAlgorithm2;


	Cell selectedCell;

	@Override
	public Sudoku useAlgorithm(Sudoku sudoku) {

		if (sudoku.getHowManyCellsLeft() != 0)
			try {
					
				Sudoku copy = null;
				for (int i = 0; i < 10; i++) {
					 copy = sudoku.copy();
					selectedCell = null;
				copy.getColumnArray().forEach(col->{
					col.getGroup().forEach(cell->{
						if(!cell.isFound() && cell.getGuesses().size()>0){
							selectedCell = cell;
							return;
						}
					});
					if(selectedCell != null){
						return;
					}
				});
				if(selectedCell == null || selectedCell.getGuesses().size()<=i){
					if(sudokuValidator.validate(copy) && sudoku.getHowManyCellsLeft() != copy.getHowManyCellsLeft()){
						sudoku = copy;
					}else{
						return sudoku;
					}
					
					break;
				}
				selectedCell.setValue(selectedCell.getGuesses().get(i));
				
				while (copy.isSudokuHasChanged()) {
					while (copy.isSudokuHasChanged()) {
						if(copy.getHowManyCellsLeft() != 0){
							sudokuAlgorithm1.useAlgorithm(copy);
							copy.incrementTrial();
							logger.info(sudokuValidator.validate(copy) + " after alg 1"+ selectedCell);
						}
						
					}
					if(copy.getHowManyCellsLeft() != 0){
						sudokuAlgorithm2.useAlgorithm(copy);
						logger.info(sudokuValidator.validate(copy) + " after alg 2" + selectedCell);
						copy.incrementTrial();
					}
				}
				logger.info(copy.getHowManyCellsLeft() + "");
			}
				if(sudokuValidator.validate(copy)){
					useAlgorithm(copy);
				}
				
	
			} catch (Exception e) {
				System.err.println(e);
				return sudoku;
			}
		
		if (sudoku.getHowManyCellsLeft() == 0) {
			sudoku.setSolved(true);
			sudoku.setSudokuHasChanged(false);
			logger.info("Sudoku is solved");
			return sudoku;
		}
		return sudoku;

	}
}

