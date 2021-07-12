 app.controller('sudokuCtrl',  ['$rootScope', '$scope','SudokuService', 'SudokuCreatorService', function($rootScope,$scope, SudokuService, SudokuCreatorService)  {
 	console.log("Sudoku Controller");

 	$rootScope.sudoku;
 		
// 	$rootScope.connectedUsers = [];
// 	
// 	$rootScope.disconnect = function () {
// 		if (stompClient != null) {
// 			stompClient.disconnect();
// 		}
// 		setConnected(false);
// 		console.log("Disconnected");
// 	}
// 	
// 	setConnected(false)
// 	
// 	function setConnected(connected) {
// 		$("#connect").prop("disabled", connected);
// 		$("#disconnect").prop("disabled", !connected);
// 		$("#start").prop("disabled", !connected);
// 		if (connected) {
// 			$("#conversation").show();
// 		} else {
// 			$("#conversation").hide();
// 		}
// 		$("#greetings").html("");
// 	}
// 	
// 	
// 	$rootScope.connect = function() {
//	    var socket = new SockJS(baseurl+'websocket');
//	    stompClient = Stomp.over(socket);
//	    stompClient.connect({}, function (frame) {
//	        setConnected(true);
//	        console.log('Connected: ' + frame);
//	        stompClient.subscribe('/topic/moves', function (cellBody) {
//	            parseCell(cellBody.body);
//	        });
//	        stompClient.subscribe('/topic/connectedusers', function (cellBody) {
//	        	parseConnectedUsers(cellBody.body);
//	        });
//	        console.log($rootScope.user.name)
//	        var username = $("#name").val();
//	        stompClient.send("/app/join", {}, JSON.stringify({'username':username}));
//	    });
//	}
 	
 	
// 	function parseConnectedUsers(cellBody) {
// 		var body = JSON.parse(cellBody)
// 		$rootScope.connectedUsers.push(body.username);
// 		$rootScope.$apply()
// 	}
 	
 	// connect();
 	
 	$scope.inputChange = function (cell,keyEvent) {
 		if ( isNaN(cell.value + String.fromCharCode(keyEvent.keyCode) )) {
 			return false;
 		}
 		var cell1 = {
 			'rowIndex':cell.row.index,
 			'columnIndex':cell.column.index,
 			'value':keyEvent.key,
 			'user':$("#name").val(),
 			
 		}
	    stompClient.send("/app/move", {}, JSON.stringify({'cell':cell1}));
	}


 	function init() {
 		$rootScope.sudoku = new Sudoku() ;
 		<!--printSudoku($rootScope.sudoku);-->
 	};

 	$scope.loadDemo = function(){
        res = SudokuService.parse();
            res.then(function(response) {
                var finalData = response.data;

                for (i = 0; i < finalData.length; i++) {

                    sudokuGroup = $rootScope.sudoku.rowArray[i].group;
                    restGroup = finalData[i].group;
                    for (j = 0; j < sudokuGroup.length; j++) {
                        sudokuGroup[j].value = restGroup[j].value != 0 ? restGroup[j].value : '';
                        sudokuGroup[j].found = restGroup[j].found;
                    }
                }
            }, function errorCallback(response) {
                console.log(response.status + " : " + response.statusText);
            });
 	};

 	$scope.loadFromJava = function(){
 		$rootScope.sudoku  = parseFromJava($rootScope.sudoku )
 	};

 	init();


 	$scope.loadDemo();

 	$rootScope.solve = function(){
 	res2 = SudokuService.solveSudoku($rootScope.sudoku);
 	res2.then(function(response) {
 		var finalData = response.data;

 		for (i = 0; i < finalData.length; i++) { 

 			sudokuGroup = $rootScope.sudoku.rowArray[i].group;
 			restGroup = finalData[i].group;
 			for (j = 0; j < sudokuGroup.length; j++) { 
 				sudokuGroup[j].value = restGroup[j].value;
 				sudokuGroup[j].color = restGroup[j].color;
 			}

 		}

 		$rootScope.sudoku.syncThreeByThreeSquaresToRow();

 	}, function errorCallback(response) {
 		console.log(response.status + " : " + response.statusText);
 	});
 };


 	$scope.solveJS = function(){
 		$rootScope.sudoku  = SudokuService.solveSudokuJS($rootScope.sudoku );
 	};

 	$scope.solveSudokuStepByStep = function(i){
 		$rootScope.sudoku  = SudokuService.solveSudokuStepByStep($rootScope.sudoku, i );
 	};

 	

 	$scope.clear = function(){
 		init();
 	};

 	
 	// $rootScope.sudoku = SudokuCreatorService.create()

 	console.log($rootScope.sudoku);

 }]);


	
	// var sudokuSolution;

	
	
	

	
	// reset();


	// function action(){
	// sudokuSolution=solveSudoku(sudoku);
	// return "succes";
	// }

	// function solveStepByStep1(){
	// solveStepByStep( 1);
	// return "succes";
	// }
	// function solveStepByStep2(){
	// solveStepByStep( 2);
	// return "succes";
	// }
	// function solveStepByStep( algorithm){
	// if(!startedSolving()){
	// sudokuSolution=solveSudokuStepByStep(sudoku, algorithm);
	// }else{
	// sudokuSolution=solveSudokuStepByStep(sudokuSolution, algorithm);
	// }
	// return "succes";
	// }

	// function startedSolving() {
	// for ( i = 0; i < 9; i++) {
	// for ( j = 0; j < 9; j++) {
	// cell = sudokuSolution.getRowArray().get(i).getGroup().get(j);
	// if(cell.getValue() != 0){
	// return true;
	// }
	// }
	// };
	// return false;
 // };
 // function reset(){
	// sudoku= new Sudoku();
	// sudokuSolution = new Sudoku();
	// return null;
 // }
 // function loadDemoSudoku(){
	// reset();
	// sudoku = brain.loadDemoSudoku(sudoku);
	// return null;
 // }
 // function loadWebSudoku() {
	// reset();
	// sudoku = Parser.parseWebSudoku(0,selectedSudokuLevel);
	// return null;
 // }
 // function loadCustomWebSudoku() {
	// reset();
	// sudoku =
	// Parser.parseWebSudoku(getSelectedSudokuId(),getSelectedSudokuLevel());
	// return null;
 // }
 // function loadUnSolvedSudoku() {
	// reset();
	// sudoku = NotSolvedWriter.readANonSolvedSudoku();
	// return null;
	// }

	// function isSudokuCorrect() {
	// if(startedSolving()){
	// return brain.isSudokuCorrect(sudokuSolution);
	// }else {
	// return false;
	// }
 // }
    
	// function setSudokuCorrect(sudokuCorrect) {
	// this.sudokuCorrect = sudokuCorrect;
	// }

	// function getDemoSudoku() {
	// return sudoku;
	// }

	// function setDemoSudoku( demoSudoku) {
	// SudokuController.sudoku = demoSudoku;
	// }
	// function getSudokuSolution() {
	// return sudokuSolution;
	// }
	// function setSudokuSolution( sudokuSolution) {
	// SudokuController.sudokuSolution = sudokuSolution;
	// }

	// function getSelectedSudokuId() {
	// return selectedSudokuId;
	// }

	// function setSelectedSudokuId( selectedSudokuId) {
	// this.selectedSudokuId = selectedSudokuId;
	// }

	// function getSelectedSudokuLevel() {
	// return selectedSudokuLevel;
	// }

	// function setSelectedSudokuLevel( selectedSudokuLevel) {
	// this.selectedSudokuLevel = selectedSudokuLevel;
	// }

