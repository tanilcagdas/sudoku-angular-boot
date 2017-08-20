
class SimpleCell{
	constructor(group){

		// add guesses

		this.guesses = [];

		this.createGuesses();

		this.row;
		this.rowindex = group.index;
		this.columnindex = group.group.length;
		this.column;
		this.threeByThreeSquare;
		this.value;
		this.found;
		this.color = "darkgray";
	//?
	var observers = [];
}

createGuesses(){
	this.guesses = [];
	for (var i = 0; i < 9; i++) {
		this.guesses[i] = i+1;
	}
}







registerObservers  (){

	for (let cell of getRow().getGroup()) {
		this.addObserver(cell);
	}
	for (let cell of getColumn().getGroup()) {
		this.addObserver(cell);
	}
	for (let cell of getThreeByThreeSquare().getGroup()) {
		this.addObserver(cell);
	}

}




addObserver(obj){
	observers.push(obj);
		//TODO
	}

	getValue() {
		return this.value;
	}





	isFound() {
		return this.found;
	}

	setFound( found) {
		if (this.value === 0 && found == false) {
			this.found = found;
		} else if (this.value != 0 && found == true) {
			this.found = found;
		} else {
			console.log("trying to set cell found to " + found
				+ " but value is " + getValue());
		}
	}

	getGuesses() {
		return this.guesses;
	}

	setGuesses(guesses) {
		this.guesses = guesses;
	}


	getColor() {
		return this.color;
	}

	setColor( color) {
		this.color = color;
	}

	getRow() {
		return this.row;
	}

	setRow( row) {
		this.row = row;
	}

	getColumn() {
		return this.column;
	}

	setColumn( column) {
		this.column = column;
	}

	getThreeByThreeSquare() {
		return this.threeByThreeSquare;
	}

	setThreeByThreeSquare( threeByThreeSquare) {
		this.threeByThreeSquare = threeByThreeSquare;
	}

	copy( cell)  {
		this.clone();
	}


	toString() {
	 //StringBuffer sb = new StringBuffer();
	 //sb.append("Cell value = ");
	 //sb.append(value).append(" ");
	 //sb.append("Cell color = ");
	 //sb.append(color).append(" ");
	 //sb.append("Cell guesses = ");
	 //sb.append(guesses).append(" ");
	 //return sb.toString();
	 return this.value;
	}

	update( o,  arg) {
		if (arg instanceof Cell) {
			var cell = arg;
			if (cell == this) {
				return;
			}
			if (cell.found) {
				var foundValue = cell.getValue();
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

markAsUniqueGuessAndDetermine( number,  group) {
	var j;
	for ( j = 0; j < 9; j++) {
		var compareCell = group.getGroup().get(j);
		if (compareCell == this) {
			continue;
		}
		if (compareCell.getGuesses() != null) {
			for (let compareGuess of compareCell.getGuesses()) {
				if (compareGuess == number) {
					return false;
				}
			}
		}
	}
	return true;
}

clearGuess( foundValue) {

		// console.log(getGuesses().contains(foundValue));
		var gssidx;
		for ( gssidx = 0; gssidx < 9; gssidx++) {
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

setValue( value) {
	if (this.value == value) {
		return;
	}
	this.value = value;
	this.guesses = null ;
	if(value == 0){
		this.found = false;
	}else{
		this.found =  true;
	}
		try {
					console.log("Cell with coordinates : " + this.row.index
			+ "," + this.column.index + " value set by "
			+ "\n to : " + value);
		} catch (e) {
			// TODO: handle exception
		}
	}



}

