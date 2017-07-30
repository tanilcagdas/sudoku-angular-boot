	function solveSudokuByAlgorithm3( sudokuSolution) {
		if (sudokuSolution.howManyCellsLeft != 0)
			try {
				determineWhoHasUniqueGuessInGroupHorizontal(sudokuSolution);
			    determineWhoHasUniqueGuessInGroupVertical(sudokuSolution);
			} 
			catch (e) {
				console.error(e);
				return sudokuSolution;
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


	function determineWhoHasUniqueGuessInGroupHorizontal( sudokuSolution) {

		
		for (var i = 0; i < sudokuSolution.threeByThreeArray.length ; i++) {
			console.log("determineWhoHasUniqueGuessInGroupOfGroupForGroup : " +i);
			var difs = determineWhoHasUniqueGuessInGroupOfGroupForGroup(sudokuSolution.threeByThreeArray[i]);
			clearGuesseswithdifs(sudokuSolution, difs, i);
		}


	}

	function determineWhoHasUniqueGuessInGroupVertical( sudokuSolution) {

		
		for (var i = 0; i < sudokuSolution.threeByThreeArray.length ; i++) {
			console.log("determineWhoHasUniqueGuessInGroupOfGroupVertical : " +i);
			var difs = determineWhoHasUniqueGuessInGroupOfGroupVertical(sudokuSolution.threeByThreeArray[i]);
			clearGuesseswithdifsVertical(sudokuSolution, difs, i);
		}


	}

	function clearGuesseswithdifs(sudokuSolution, difs, index){

		var div = Math.floor(index / 3);
		var start = div *3 ;
		var end = div * 3 + 3;
		for(var i = start; i < end ; i++){
			if(i==index){
				continue;
			}
			if(difs.dif1.length > 0 || difs.dif2.length > 0 || difs.dif3.length > 0){
				BLUE = "green";
				RED = "orange";
				clearGuessesFromSmallGroup(sudokuSolution.threeByThreeArray[i],difs);
				sudokuSolution.setSudokuHasChanged(true);
			}
			
		}
	}

	function clearGuesseswithdifsVertical(sudokuSolution, difs, index){

		
		var start = (index % 3) ;
		var end = start + 7;
		for(var i = start; i < end ; i+=3){
			if(i==index){
				continue;
			}
			if(difs.dif1.length > 0 || difs.dif2.length > 0 || difs.dif3.length > 0){
				BLUE = "green";
				RED = "orange";
				clearGuessesFromSmallGroupVertical(sudokuSolution.threeByThreeArray[i],difs);
				sudokuSolution.setSudokuHasChanged(true);
			}
			
		}
	}

	function clearGuessesFromSmallGroup(group,difs){

		for ( var number = 0; number < 3; number++) {
			try{
				let cell = group.getGroup()[number];
				cell.guesses = subtract(cell.guesses, difs.dif1);
			}catch(e){
				console.log(e);
			}
		}

		for ( var number = 3; number < 6; number++) {
			try{
				let cell = group.getGroup()[number];
				cell.guesses = subtract(cell.guesses, difs.dif2);
			}catch(e){
				console.log(e);
			}
		}

		for ( var number = 6; number < 9; number++) {
			try{
				let cell = group.getGroup()[number];
				cell.guesses = subtract(cell.guesses, difs.dif3);			
			}catch(e){
				console.log(e);
			}
		}

	}

	function clearGuessesFromSmallGroupVertical(group,difs){

		for ( var number = 0; number < 7; number+=3) {
			try{
				let cell = group.getGroup()[number];
				cell.guesses = subtract(cell.guesses, difs.dif1);
			}catch(e){
				console.log(e);
			}
		}

		for ( var number = 3; number < 8; number+=3) {
			try{
				let cell = group.getGroup()[number];
				cell.guesses = subtract(cell.guesses, difs.dif2);
			}catch(e){
				console.log(e);
			}
		}

		for ( var number = 6; number < 9; number+=3) {
			try{
				let cell = group.getGroup()[number];
				cell.guesses = subtract(cell.guesses, difs.dif3);			
			}catch(e){
				console.log(e);
			}
		}

	}

	function  determineWhoHasUniqueGuessInGroupOfGroupForGroup( group) {

		var grp1Gss =[]; 
		var grp2Gss =[]; 
		var grp3Gss =[]; 

		for ( var number = 0; number < 3; number++) {
			collectGuesses(grp1Gss, group, number);
		}

		for ( var number = 3; number < 6; number++) {
			collectGuesses(grp2Gss, group, number);
		}

		for ( var number = 6; number < 9; number++) {
			collectGuesses(grp3Gss, group, number);
		}

		var dif1 =  subtract(grp1Gss,grp2Gss);
		 dif1 =  subtract(dif1,grp3Gss);

		var dif2 =  subtract(grp2Gss,grp1Gss);
		 dif2 =  subtract(dif2,grp3Gss);

		var dif3 =  subtract(grp3Gss,grp1Gss);
		 dif3 =  subtract(dif3,grp2Gss);
		var difs = {'dif1':dif1,'dif2':dif2,'dif3':dif3};
		printDifs(difs);

		return difs;


	}

	
	function  determineWhoHasUniqueGuessInGroupOfGroupVertical( group) {

		var grp1Gss =[]; 
		var grp2Gss =[]; 
		var grp3Gss =[]; 

		for ( var number = 0; number < 7; number += 3) {
			collectGuesses(grp1Gss, group, number);
		}

		for ( var number = 1; number < 8; number+= 3) {
			collectGuesses(grp2Gss, group, number);
		}

		for ( var number = 2; number < 9; number += 3) {
			collectGuesses(grp3Gss, group, number);
		}

		var dif1 =  subtract(grp1Gss,grp2Gss);
		 dif1 =  subtract(dif1,grp3Gss);

		var dif2 =  subtract(grp2Gss,grp1Gss);
		 dif2 =  subtract(dif2,grp3Gss);

		var dif3 =  subtract(grp3Gss,grp1Gss);
		 dif3 =  subtract(dif3,grp2Gss);
		var difs = {'dif1':dif1,'dif2':dif2,'dif3':dif3};
		printDifs(difs);

		return difs;


	}

	function collectGuesses(grpGss ,group, number){
		try{
			let cell = group.getGroup()[number];
			insert(grpGss,cell.guesses);
		}catch(e){
			console.log(e);
		}
	}

	function printDifs(difs){
		str = "dif1 : ";
		for (var i = difs.dif1.length - 1; i >= 0; i--) {
			str += difs.dif1[i];
			str += ';'
		}
		str += " dif2 : ";
		for (var i = difs.dif2.length - 1; i >= 0; i--) {
			str += difs.dif2[i];
			str += ';'
		}
		str += " dif3 : ";
		for (var i = difs.dif3.length - 1; i >= 0; i--) {
			str += difs.dif3[i];
			str += ';'
		}
		console.log(str);

	}

	function contains(a, obj) {
		for (var i = 0; i < a.length; i++) {
			try{
				if (a[i] === obj) {
					return true;
				}

			}catch(e){
				console.log(e);
			}

		}
		return false;
	}

	function insert(a, b) {
		if(b != null){
			if(typeof b == 'number'){
				a[a.length] = b;
			}else{
				for (var i = b.length - 1; i >= 0; i--) {
					if(!contains(a,b[i])){
						a[a.length] = b[i];
					}				
				}
			}
		}
		
	}

	function subtract(a,b){
		var c = copy(a);

		for (var i = 0; i < c.length; i++) {
			for (var j = 0; j < b.length; j++) {
				if (c[i] === b[j]) {
					c.splice(i, 1);
				}
			}
		}
		return c;
	}

	function copy(a){
		if(a == null){
			return [];
		}
		var c =[];
		for(var i=0;i< a.length; i++){
			insert(c,a[i]);

		}
		return c;
	}