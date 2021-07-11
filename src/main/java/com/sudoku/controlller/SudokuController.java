package com.sudoku.controlller;

import java.io.IOException;
import java.util.List;

import com.sudoku.service.SudokuFileWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sudoku.beans.Cell;
import com.sudoku.beans.Row;
import com.sudoku.beans.Sudoku;
import com.sudoku.service.BrainIF;

@RestController
public class SudokuController {

	@Autowired
	private BrainIF brain;

	@RequestMapping("/solve")
	public List<Row> solve(@RequestBody RowArrayModel rowArrayModel) {
		return clearRecusionAndGetRowArray(brain.solveSudoku(rowArrayModel.getRowArray().get(0).getSudoku()));
	}

	@RequestMapping("/solveWebSudoku")
	public List<Row> solveRandom() {
		Sudoku sudoku = null;
		try {
			sudoku = SudokuFileWriter.loadRandom();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sudoku = brain.solveSudoku(sudoku);
		return clearRecusionAndGetRowArray(sudoku);
	}

	@RequestMapping("/parseWebSudoku")
	public List<Row> parseRandom() {
		Sudoku sudoku = null;
		try {
//				sudoku = SudokuFileWriter.load(5978709421l, 4);
				sudoku = SudokuFileWriter.loadRandom();
				SudokuFileWriter.write(sudoku);


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clearRecusionAndGetRowArray(sudoku);
	}

	@MessageMapping("/move")
	@SendTo("/topic/moves")
	public CellMessage greeting(CellMessage message) throws Exception {
		Thread.sleep(1000); // simulated delay
		List<Row> rows = clearRecusionAndGetRowArray(brain.solveSudoku(message.getCell().getSudoku().getRowArray().get(0).getSudoku()));
		if(rows.get(message.getCell().getRowIndex()).getGroup().get(message.getCell().getColumnIndex()).getValue() == Integer.parseInt(message.getCell().getValue())){
			message.getCell().setFound(true);
			message.getCell().setSudoku(null);
		}else{
			message.getCell().setFound(false);
			message.getCell().setSudoku(null);
		}
		return message;
	}
	
	@MessageMapping("/join")
	@SendTo("/topic/connectedusers")
	public String join(String username) throws Exception {
		Thread.sleep(1000); // simulated delay
		return username;
	}
	
//	@MessageMapping("/sudoku")
//	@SendTo("/cellMessage/{id}")
//	public CellMessage greeting(CellMessage message, @DestinationVariable int id) throws Exception {
//		logger.info(id);
//		Thread.sleep(1000); // simulated delay
//		message.getCell().setFound(true);
//		return message;
//	}

	private List<Row> clearRecusionAndGetRowArray(Sudoku sudoku) {
		List<Row> rowArray = sudoku.getRowArray();
		for (Row row : rowArray) {
			row.setSudoku(null);
			for (Cell cell : row.getGroup()) {
				cell.setRow(null);
				cell.setColumn(null);
				cell.setThreeByThreeSquare(null);
			}
		}
		return rowArray;
	}

}

class CellMessage {
	Cell cell;

	public Cell getCell() {
		return cell;
	}

	public void setCell(Cell cell) {
		this.cell = cell;
	}

	public class Cell {
		int rowIndex;
		int columnIndex;
		String value;
		Object user;
		RowArrayModel sudoku;
		public RowArrayModel getSudoku() {
			return sudoku;
		}

		public void setSudoku(RowArrayModel sudoku) {
			this.sudoku = sudoku;
		}

		boolean found;

		public boolean isFound() {
			return found;
		}

		public void setFound(boolean found) {
			this.found = found;
		}

		public int getRowIndex() {
			return rowIndex;
		}

		public void setRowIndex(int rowIndex) {
			this.rowIndex = rowIndex;
		}

		public int getColumnIndex() {
			return columnIndex;
		}

		public void setColumnIndex(int columnIndex) {
			this.columnIndex = columnIndex;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public Object getUser() {
			return user;
		}

		public void setUser(Object user) {
			this.user = user;
		}

	}
}