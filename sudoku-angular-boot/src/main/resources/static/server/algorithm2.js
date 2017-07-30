	function solveSudokuByAlgorithm2( sudokuSolution) {
		if (sudokuSolution.howManyCellsLeft != 0)
			try {
				determineWhoHasUniqueGuessInGroup(sudokuSolution);
			} 
			catch (e) {

				//if(e.getCause() instanceof SudokuException){
					console.error(e);
					return sudokuSolution;
				//}else {
				//	console.log(Level.SEVERE, "Error Ocured", e);
				//}

			}
		// 
		if (sudokuSolution.getHowManyCellsLeft() == 0) {
			sudokuSolution.setSolved(true);
			sudokuSolution.setSudokuHasChanged(false);
			console.log("Sudoku is solved");
			return sudokuSolution;
		}
		return sudokuSolution;
	}

	function  markAsUniqueGuessAndDetermine( number,  group) {
		var i;
		for ( i = 0; i < 9; i++) {
			var cell = group.getGroup()[i];
			if (cell.getGuesses() != null) {
				for (let guess of cell.getGuesses()) {
					if (guess == number) {
						// TODO check others
						var j;
						for ( j = 0; j < 9; j++) {
							if (i == j)
								continue;
							var compareCell = group.getGroup()[j];
							if (compareCell.getGuesses() != null) {
								for (let compareGuess of compareCell.getGuesses()) {
									if (compareGuess == number) {
										return;
									}
								}
							};
						}
						console.log("Cell " + cell.row.index + "," + cell.column.index +" has unique guess "+ number + " in group of "+ group.constructor.name  );
						cell.setValue(number);
						cell.setColor(RED);
						group.sudoku.setSudokuHasChanged(true);
						group.sudoku.howManyCellsLeft = group.sudoku.howManyCellsLeft - 1;
						clearGuessesInGroupOfSudoku(group.sudoku);
						break;
					};
				}
			};
		}
	};


	function determineWhoHasUniqueGuessInGroup( sudokuSolution) {
		methodRange(sudokuSolution, "determineWhoHasUniqueGuessInGroupForGroup", ROW);
		methodRange(sudokuSolution, "determineWhoHasUniqueGuessInGroupForGroup",
			COLUMN);
		methodRange(sudokuSolution, "determineWhoHasUniqueGuessInGroupForGroup", "3x3");

	}

	function  determineWhoHasUniqueGuessInGroupForGroup( group) {
		var number ;

		for ( number = 1; number < 10; number++) {
			var uniqueGuessCount = 0;
			for (let cell of  group.getGroup()) {
				if (cell.guesses != null) {
					for (let guess of cell.getGuesses()) {
						if (guess == number) {
							uniqueGuessCount++;
						}
					}
				}
			}
			if (uniqueGuessCount == 1) {
				for (let cell of group.getGroup()) {
					if(cell.value==number){
						return;
					}
				}
				
				
				markAsUniqueGuessAndDetermine(number, group);
				setSudokuCorrect(true);
				isSudokuCorrect(group);
				if(!isSudokuCorrect()){
					console.error("Sudoku is not Correct after markAsUniqueGuessAndDetermine number : " +number+", group : "+group );
				}
			};
		}
	}