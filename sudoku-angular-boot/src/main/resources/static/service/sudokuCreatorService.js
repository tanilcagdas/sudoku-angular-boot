 app.service('SudokuCreatorService', function() {
 	



 	this.create = function() {
 		var sudoku = new Sudoku();

 		try{
 			for( var number = 1; number < 10; number++){


 				for (var i = 0; i < sudoku.threeByThreeArray.length; i++) {
 					let group = sudoku.threeByThreeArray[i].group;
 					for (var trial = 0; trial < 15; trial++) {

 						let cellIndex = getRandomInt(0,9);
 						let cell = group[cellIndex];
 						if(cell.value > 0){
 							continue;
 						}
 						getAvailableNumbers(cell);
 						if(checkContains(cell.guesses, number)){
 							cell.value = number;
 							break;
 						}
 						if(trial == 14){
 							console.log(15);
 						}
 					}
 					


 				//oldImpl(group);
 			}
 		}

 	}catch(e){
 		console.log(e);
 	}




 	return sudoku;

 };

 function checkContains(guesses, number){
 	for (var i = 0; i < guesses.length; i++) {
 		if(guesses[i] === number){
 			return true;
 		}
 	}
 	return false;

 }



 function getRandomInt(min, max) {
 	min = Math.ceil(min);
 	max = Math.floor(max);
 	return Math.floor(Math.random() * (max - min)) + min;
 }

 function getAvailableNumbers(cell){

 	var group = cell.row.group;
 	getAvailableNumbersInGroup(group,cell);

 	var group = cell.column.group;
 	getAvailableNumbersInGroup(group,cell);

 	var group = cell.threeByThreeSquare.group;
 	getAvailableNumbersInGroup(group,cell);



 }

 function getAvailableNumbersInGroup(group,cell){
 	for (var i = 0; i < group.length; i++) {
 		var cell1 = group[i];
 		if(cell1.value != undefined){
 			for (var j = 0; j < cell.guesses.length; j++) {
 				if(cell.guesses[j] == cell1.value){
 					cell.guesses.splice(j, 1);
 				}
 			}
 		}
 	}
 }

 function oldImpl(group){
 	for (var j = 0; j < group.length; j++) {
 		var cell = group[j];

 		getAvailableNumbers(cell);
 		var rnd = getRandomInt(0,cell.guesses.length);
 		cell.value = cell.guesses[rnd];
 		if(!cell.value >0){
 			console.log("?");
 		}
 	}
 }

});