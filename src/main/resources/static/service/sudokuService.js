 app.service('SudokuService', function($http,$rootScope) {


 	this.parse = function() {
 		url = baseurl+"parseWebSudoku";
 		var req = {
 			method : 'GET',
 			url : url,
 			headers : headers
 		}
 		return $http(req);

 	};

 	this.solveSudoku = function(sudoku) {
 		var url = baseurl+"solve";
 		let rowArray = sudoku.rowArray;
 		for (var i = rowArray.length - 1; i >= 0; i--) {
 			let row =rowArray[i]
 		
			row.sudoku =null;
			for(let j=0 ; j< row.group.length; j++){
				let cell = row.group[j];
				cell.setRow(null);
				cell.setColumn(null);
				cell.setThreeByThreeSquare(null);
			}
		}
		 rowArray = sudoku.columnArray;
 		for (var i = rowArray.length - 1; i >= 0; i--) {
 			let row =rowArray[i]
 		
			row.sudoku =null;
			for(let j=0 ; j< row.group.length; j++){
				let cell = row.group[j];
				cell.setRow(null);
				cell.setColumn(null);
				cell.setThreeByThreeSquare(null);
			}
		}
		 rowArray = sudoku.threeByThreeArray;
 		for (var i = rowArray.length - 1; i >= 0; i--) {
 			let row =rowArray[i]
 		
			row.sudoku =null;
			for(let j=0 ; j< row.group.length; j++){
				let cell = row.group[j];
				cell.setRow(null);
				cell.setColumn(null);
				cell.setThreeByThreeSquare(null);
			}
		}
		
 		var req = {
 			method : 'POST',
 			url : url,
 			headers : headers,
			data : 
				{'rowArray': sudoku.rowArray}
			
 		}
 		return $http(req);

 	};

 	this.solveSudokuJS = function(sudoku) {
	var startTime = new Date().getTime();
	var sudokuSolution ;
	/* = new Sudoku(); */

	RED = "red";
	BLUE = "blue";
	sudokuSolution = sudoku.copy();
	sudokuSolution.sudokuHasChanged = true;
	this.sudoku = sudokuSolution;
	try {
		evaluateGuesses(sudokuSolution);
	} catch (e) {
		console.error("Error Ocured", e);
	}

	try {
		countHowManyCellsLeft(sudokuSolution);
	} catch ( e) {
		console.error(e);
	}
		// Solve the f.cking sudoku

		// Check if the sudoku has changed
		trial = 1;
		while (sudokuSolution.sudokuHasChanged) {
			while (sudokuSolution.sudokuHasChanged) {
				while (sudokuSolution.sudokuHasChanged) {
					sudokuSolution.sudokuHasChanged = false;
					if(sudokuSolution.howManyCellsLeft != 0){
					solveSudokuByAlgorithm1(sudokuSolution);
					countHowManyCellsLeft(sudoku);
					}
				}
				sudokuSolution.sudokuHasChanged = false;
				if(sudokuSolution.howManyCellsLeft != 0){
				solveSudokuByAlgorithm2(sudokuSolution);
				countHowManyCellsLeft(sudoku)
				}
			}
			if(sudokuSolution.howManyCellsLeft != 0 && trial < 20){

			solveSudokuByAlgorithm3(sudokuSolution);

		}
		}
		if (!sudokuSolution.solved) {
			try {
				// TODO NotSolvedWriter.log(sudoku, sudokuSolution);
			} catch (e) {
				console.error( "Error Ocured", e);
			}
		}
		var endTime = new Date().getTime();
		console.log('time :'+ (endTime - startTime) );
		this.logSudoku(sudokuSolution);
		return sudokuSolution;
	}

	this.logSudoku = function(sudokuSolution){
		for (var i = 0; i < sudokuSolution.rowArray.length; i++) {
			let cellArr = sudokuSolution.rowArray[i].getGroup();
			for (var j = 0; j < cellArr.length; j++) {
				let cell = cellArr[j];
				console.log( cell.row.index  + "," +  cell.column.index + ":" +cell.value+ "[" + cell.guesses+"]");
			}
		}
	}

	// function countHowManyCellsLeft(sudoku){

	// sudoku.howManyCellsLeft = 0 ;

	// methodRange(sudoku, "countHowManyCellsLeftForCell", ALL);
	// console.log(sudoku.howManyCellsLeft + " Cells is waiting to be solved");

	// }

	// function countHowManyCellsLeftForCell( cell) {
	// if (cell.getValue() == undefined || cell.getValue() == null ||
	// cell.getValue() === 0){
	// sudoku.howManyCellsLeft = sudoku.howManyCellsLeft + 1;
	// }


	// }

	 this.solveSudokuStepByStep = function(sudokuSolution, algorithm) {
		try {
			evaluateGuesses(sudokuSolution);
		} catch ( e) {
			console.error( "Error Ocured", e);
		}

		// Check if the sudoku has changed
		trial = 1;

		// reflection
		var methodName = "solveSudokuByAlgorithm" + algorithm;
		try {


			// this[methodName]( sudokuSolution);
			if(algorithm == 1){
				solveSudokuByAlgorithm1(sudokuSolution);
			}else if(algorithm == 2){
				solveSudokuByAlgorithm2(sudokuSolution);
			}


		} catch (e) {
			console.error("Error Ocured", e);
		} 
		return sudokuSolution;
	}





	 this.loadDemoSudoku = function(demoSudoku) {
		// set all zeros
		var row;
		for ( row = 0; row < demoSudoku.getRowArray().length; row++) {
			var column;
			for ( column = 0; column < demoSudoku.getRowArray()[row]
				.getGroup().length; column++)
				var cell = demoSudoku.getRowArray()[row].getGroup()[column];
			cell.setValue(0);
		}
		// put known values
		
		loadSudoku1(demoSudoku);

		return demoSudoku;

	}
	 
	 this.parseCell= function (cellBody) {
	 		var body = JSON.parse(cellBody)
	 		if(body.cell.found == true){
	 			$rootScope.sudoku.rowArray[body.cell.rowIndex].group[body.cell.columnIndex].value = Number(body.cell.value)
	 			$rootScope.sudoku.rowArray[body.cell.rowIndex].group[body.cell.columnIndex].user = body.cell.user
	 			$rootScope.sudoku.rowArray[body.cell.rowIndex].group[body.cell.columnIndex].found = body.cell.found
	 		}else{
	 			$rootScope.sudoku.rowArray[body.cell.rowIndex].group[body.cell.columnIndex].value = undefined
	 			$rootScope.sudoku.rowArray[body.cell.rowIndex].group[body.cell.columnIndex].user = body.cell.user
	 			$rootScope.sudoku.rowArray[body.cell.rowIndex].group[body.cell.columnIndex].found = body.cell.found
	 			
	 		}
	 		
		    $("#greetings").append("<tr><td>" + cellBody + "</td></tr>");
	 		$rootScope.$apply()
		}



 });