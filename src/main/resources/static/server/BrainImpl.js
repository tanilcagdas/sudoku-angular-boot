



var RED = "red";
var BLUE = "blue";
var ROW = "row";
var ALL = "all";
var COLUMN = "column";
var THREExTHREE = "3x3";
var DEFAULT_GUESSES;
var trial;
var sudokuCorrect = true;
var sudoku;

function defaultGuesses(){
	var arr= [];
	var i ;
	for ( i = 1; i < 10; i++)
		arr.push(i);
	return arr;
}



	
	function methodRange( sudoku,  methodName,  range){
		var method;
		if (range === ALL ) {
			var row;
			for ( row = 0; row < 9; row++) {
				var columnIndex;
				for ( columnIndex = 0; columnIndex < 9; columnIndex++) {
					var cell = sudoku.rowArray[row].group[columnIndex];

					this[methodName](  cell);
				}
			}
		} else {

			var group = null;
			var i;
			for ( i = 0; i < 9; i++) {
				if (range === ROW) {
					group = sudoku.getRowArray()[i];
				} else if (range === COLUMN ) {
					group = sudoku.getColumnArray()[i];
				} else if (range === "3x3") {
					group = sudoku.getThreeByThreeArray()[i];
				}
				this[methodName](  group);
			}
		}
	}

	function  evaluateGuessesForCell( cell) {
		if (cell.getValue() == undefined || cell.getValue() == null || cell.getValue() === 0 || cell.getValue() == "") {
			cell.setGuesses( defaultGuesses());
		} else {
			cell.setFound(true);
			cell.guesses = [];
			console.log(cell.getValue() + ", " + cell.isFound());
			console.log(cell.getColumn().sudoku.howManyCellsLeft);
		}

	}





	function countHowManyCellsLeft(sudoku){

		this.sudoku = sudoku;
		sudoku.howManyCellsLeft = 0 ;

		methodRange(sudoku, "countHowManyCellsLeftForCell", ALL);
		console.log(sudoku.howManyCellsLeft + " Cells is waiting to be solved");

	}

	function countHowManyCellsLeftForCell( cell)  {
		if (cell.getValue() == undefined || cell.getValue() == null || cell.getValue() === 0){
			sudoku.howManyCellsLeft = sudoku.howManyCellsLeft + 1;
		}


	}




	function  evaluateGuesses( sudoku) {
		methodRange(sudoku, "evaluateGuessesForCell", ALL);
	}

	

	function  determineCellsWhoHas1Guess( sudokuSolution) {
		methodRange(sudokuSolution, "determineCellsWhoHas1GuessForCell", ALL);
		console.log(sudokuSolution.getHowManyCellsLeft()
			+ " Cells is waiting to be solved");
	}


	function determineWhoHasUniqueGuessInGroup( sudokuSolution) {
		methodRange(sudokuSolution, "determineWhoHasUniqueGuessInGroupForGroup", ROW);
		methodRange(sudokuSolution, "determineWhoHasUniqueGuessInGroupForGroup",
			COLUMN);
		methodRange(sudokuSolution, "determineWhoHasUniqueGuessInGroupForGroup", "3x3");

	}

	

	/*function  isSudokuCorrect(Sudoku sudoku) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		setSudokuCorrect(true);
		methodRange(sudoku, "isSudokuCorrect",ROW);
		if(isSudokuCorrect())
		methodRange(sudoku, "isSudokuCorrect",COLUMN);
		if(isSudokuCorrect())
		methodRange(sudoku, "isSudokuCorrect",THREExTHREE);
		return isSudokuCorrect();
	}*/

	function isSudokuCorrect( group) {
		var i;
		for ( i = 0; i < 9; i++) {
			var uniqueValue = group.getGroup().get(i).getValue();
			if(uniqueValue == 0){
				continue;
			}
			var j;
			for ( j = 0; j < 9; j++) {
				if(i==j)
					continue;
				var compareValue = group.getGroup().get(j).getValue();
				if(compareValue == uniqueValue){
					setSudokuCorrect(false);

				}
			}
			
		}
	}

	/**
	 * @return the sudokuCorrect
	 */
	 function  isSudokuCorrect() {
	 	return sudokuCorrect;
	 }

	/**
	 * @param sudokuCorrect the sudokuCorrect to set
	 */
	 function  setSudokuCorrect( sudokuCorrect) {
	 	this.sudokuCorrect = sudokuCorrect;
	 }


