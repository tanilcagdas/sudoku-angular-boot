package com.sudoku.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

public class Cell extends Observable implements Observer {
	
	Logger logger = Logger.getLogger(this.getClass().getSimpleName());

	private static final String RED = "red";
	private static final String BLUE = "blue";
	private Group row;
	private Group column;
	private Group threeByThreeSquare;
	private int value;
	private boolean found;
	private List<Integer> guesses;
	private String color = "darkgray";

	private static ArrayList<Integer> DEFAULT_GUESSES = new ArrayList<>();
	{
		if (DEFAULT_GUESSES.size() == 0) {
			for (int i = 1; i < 10; i++)
				DEFAULT_GUESSES.add(i);

		}
	}

	public Cell() {}
	//For testing
	public Cell(int value, List<Integer> guesses) {
		this.value = value;
		this.guesses = guesses;
	}
	public Cell(Group group) {
		if (group instanceof Row) {
			this.setRow(group);
		} else if (group instanceof Column) {
			this.setColumn(group);
		} else if (group instanceof ThreeByThreeSquare) {
			this.setThreeByThreeSquare(group);
		}
		// add guesses
		setGuesses((ArrayList<Integer>) DEFAULT_GUESSES.clone());
	}

	public void registerObservers() {
		for (Cell cell : getRow().getGroup()) {
			this.addObserver(cell);
		}
		for (Cell cell : getColumn().getGroup()) {
			this.addObserver(cell);
		}
		for (Cell cell : getThreeByThreeSquare().getGroup()) {
			this.addObserver(cell);
		}

	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		if (this.value == value) {
			return;
		}
//		this.value = value;
//		setGuesses(null);
//		setFound(value != 0);
		if(backupAndValidateThenApply(value)){
			setChanged();
//		getRow().getSudoku().validate();
//		getRow().getSudoku().setHowManyCellsLeft(
//				getRow().getSudoku().getHowManyCellsLeft() - 1);
			logger.info("Cell with coordinates : " + getRow().getIndex()
					+ "," + getColumn().getIndex()
//				+ " value set by "
//				+ Thread.currentThread().getStackTrace()[2].toString()
					+ " to : " + value);
			notifyObservers(this);
		}

	}

	private boolean backupAndValidateThenApply(int value) {

		Cell temp = new Cell();
		temp.value = this.value;
		temp.guesses = this.guesses;
		this.value = value;
		setGuesses(null);
		setFound(value != 0);
		if(!getRow().getSudoku().validate()){
			rollBack(temp);
			return false;
		}
		return true;
	}

	private void rollBack(Cell temp) {
		System.err.println("Rolling back !!");
		this.value = temp.value;
		this.guesses = temp.guesses;
		setFound(false);
	}

	public boolean isFound() {
		return found;
	}

	public void setFound(boolean found) {
		if (getValue() == 0 && !found) {
			this.found = false;
		} else if (getValue() != 0 && found) {
			this.found = true;
		} else {
			logger.info("trying to set cell found to " + found
					+ " but value is " + getValue());
		}
	}

	public List<Integer> getGuesses() {
		return guesses;
	}

	public void setGuesses(List<Integer> guesses) {
		this.guesses = guesses;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	public Group getRow() {
		return row;
	}

	public void setRow(Group row) {
		this.row = row;
	}

	public Group getColumn() {
		return column;
	}

	public void setColumn(Group column) {
		this.column = column;
	}

	public Group getThreeByThreeSquare() {
		return threeByThreeSquare;
	}

	public void setThreeByThreeSquare(Group threeByThreeSquare) {
		this.threeByThreeSquare = threeByThreeSquare;
	}

	public void copy(Cell cell) throws CloneNotSupportedException {
		this.clone();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Cell value = ");
		sb.append(value).append(" ");
		sb.append("Cell color = ");
		sb.append(color).append(" ");
		sb.append("Cell guesses = ");
		sb.append(guesses).append(" ");
		return sb.toString();
	}

	public void update(Observable o, Object arg) {
		if (arg instanceof Cell) {
			Cell cell = (Cell) arg;
			if (cell == this) {
				return;
			}
			if (cell.found) {
				int foundValue = cell.getValue();
				clearGuess(foundValue);
				// put all the logic here
				// method1
				if (getGuesses() != null && getGuesses().size() == 1) {
//					setValue(getGuesses().get(0));
//					setColor(RED);
				}
				// method2
//				if (getGuesses() != null) {
//					for (Integer guess : getGuesses()) {
//						if (markAsUniqueGuessAndDetermine(guess, row)
//								&& markAsUniqueGuessAndDetermine(guess, column)
//								&& markAsUniqueGuessAndDetermine(guess,	threeByThreeSquare)) {
//							setValue(guess);
//							setColor(BLUE);
//							getRow().getSudoku().setSudokuHasChanged(true);
//							break;
//						}
//
//					}
//				}

			}
		}
	}

	private boolean markAsUniqueGuessAndDetermine(int number, Group group) {
		for (int j = 0; j < 9; j++) {
			Cell compareCell = group.getGroup().get(j);
			if (compareCell == this) {
				continue;
			}
			if (compareCell.getGuesses() != null) {
				for (int compareGuess : compareCell.getGuesses()) {
					if (compareGuess == number) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private boolean clearGuess(int foundValue) {

		// logger.info(getGuesses().contains(foundValue));
		for (int gssidx = 0; gssidx < 9; gssidx++) {
			if (getGuesses() != null && getGuesses().size() > gssidx
					&& getGuesses().get(gssidx) == foundValue) {
				getGuesses().remove(gssidx);
				if (getRow().getSudoku().isSudokuHasChanged() == false) {
					getRow().getSudoku().setSudokuHasChanged(true);
				}
				return true;
			}
		}
		return false;
	}

}
