 app.controller('gameCtrl',  ['$rootScope', '$scope','SudokuService', 'SudokuCreatorService', function($rootScope,$scope, SudokuService, SudokuCreatorService)  {
 	console.log("Game Controller");

 	$rootScope.sudoku;
 	
 	$scope.inputChange = function (cell,keyEvent) {
 		if ( isNaN(String.fromCharCode(keyEvent.keyCode) )) {
 			return false;
 		}
 		
// 		var sudokuGroup;
// 		var group = [];
// 		var rowArray = [];
// 		
// 		for (i = 0; i < 9; i++) { 
// 			sudokuGroup = $rootScope.sudoku.rowArray[i].group;
// 			var restGroup = [];
//
// 			for (j = 0; j < sudokuGroup.length; j++) { 
// 				let cell = new Cell(); 
// 				cell.value = sudokuGroup[j].value;
// 				restGroup[j] = cell;
// 				group.push(restGroup[j]);
// 			}
// 			var row ={'group' : group};
// 			rowArray.push({'row' : row});
//
// 		}
 		
 		let sudoku = $rootScope.sudoku.copy();
		let rowIndex = cell.row.index;
 		let columnIndex = cell.column.index;
 		
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
		
 		
 		var sudoku1 ={'rowArray': sudoku.rowArray}
 		var cell1 = {
 			'rowIndex':rowIndex,
 			'columnIndex':columnIndex,
 			'value':keyEvent.key,
 			'user':$rootScope.user,
 			'sudoku': sudoku1
 		}
 		
	    stompClient.send("/app/move", {},JSON.stringify({'cell':cell1}));
	}


 	function init() {
 		$rootScope.sudoku = new Sudoku() ;
 		$scope.loadDemo();
 	};

 	$scope.loadDemo = function(){
 		$rootScope.sudoku  = SudokuService.loadDemoSudoku($rootScope.sudoku )
 	};

 	init();

 	console.log($rootScope.sudoku);

 }]);
