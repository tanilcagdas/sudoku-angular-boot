
	class SimpleSudoku {


		constructor() {

			this.rowArray = [];
			this.columnArray = [];
			this.threeByThreeArray = [];
			this.solved = false;
			this.howManyCellsLeft = 81;
			this.puzzleId;
			this.puzzleLevel;
			this.sudokuHasChanged = false;
			var i;
			for (i = 0; i < 9; i++) {
				let row = new Row(this, i);
				row.sudoku = undefined;
				this.rowArray.push(row);
				
			}

			this.columnArray = [];
			var rowIndex;
			for (rowIndex = 0; rowIndex < 9; rowIndex++) {
				var columnIndex;
				for (columnIndex = 0; columnIndex < 9; columnIndex++) {

					if (this.columnArray.length < columnIndex + 1) {
						let column = new Column(this, columnIndex);
						column.sudoku = undefined;
                        this.columnArray.push(column);
//						console.log("created collumn for row : "+ row +", collumn: "+ collumn );
					}
					var cell;

					var row = this.rowArray[rowIndex];
					var group = row.getGroup();
					cell = group[columnIndex];
                    this.columnArray[columnIndex].getGroup()[rowIndex] = cell;
					cell.column = this.columnArray[columnIndex];

				}
			}
			//syncColumnsToRow();
			this.syncThreeByThreeSquaresToRow();
			

		console.log("created new sudoku");
		}
		
		  syncThreeByThreeSquaresToRow() {
	            //this.threeByThreeArray=[];


	            this.syncThreeByThreeSquaresToRowHelper(0, 3, 0, 3);
	            this.syncThreeByThreeSquaresToRowHelper(0, 3, 3, 6);
	            this.syncThreeByThreeSquaresToRowHelper(0, 3, 6, 9);
	            this.syncThreeByThreeSquaresToRowHelper(3, 6, 0, 3);
	            this.syncThreeByThreeSquaresToRowHelper(3, 6, 3, 6);
	            this.syncThreeByThreeSquaresToRowHelper(3, 6, 6, 9);
	            this.syncThreeByThreeSquaresToRowHelper(6, 9, 0, 3);
	            this.syncThreeByThreeSquaresToRowHelper(6, 9, 3, 6);
	            this.syncThreeByThreeSquaresToRowHelper(6, 9, 6, 9);


	        }

			syncColumnsToRow() {
				columnArray = [];
				var rowIndex;
				for (rowIndex = 0; rowIndex < 9; rowIndex++) {
					var columnIndex;
					for (columnIndex = 0; columnIndex < 9; columnIndex++) {

						if (columnArray.length < columnIndex + 1) {
							columnArray.push(new Column(this, columnIndex));
//							console.log("created collumn for row : "+ row +", collumn: "+ collumn );
						}
						var cell;

						var row = rowArray[rowIndex];
						var group = row.group;
						cell = group[columnIndex];
						columnArray[columnIndex].group[rowIndex] = cell;
						cell.column = columnArray[columnIndex];

					}
				}
			}
			
			syncThreeByThreeSquaresToRowHelper(rowStart, rowEnd, columnStart, columnEnd) {
				var threeByThreeIndex = 0;
				var groupCount = 0;
				var rowIndex;
				for (rowIndex = rowStart; rowIndex < rowEnd; rowIndex++) {
					var columnIndex;
					for (columnIndex = columnStart; columnIndex < columnEnd; columnIndex++, groupCount++) {
						threeByThreeIndex = this.calculateGroup(rowIndex, columnIndex);
						//alt taraf ok
						if (this.threeByThreeArray.length < threeByThreeIndex + 1) {
							let threeByThreeSquare = new ThreeByThreeSquare(this, threeByThreeIndex);
							threeByThreeSquare.sudoku = undefined;
							this.threeByThreeArray.push(threeByThreeSquare);
//							console.log("created ThreeByThreeSquare for group: "+group +", row : "+ row +", collumn: "+ collumn );
						}
						//alt taraf ok
						var cell = this.rowArray[rowIndex].getGroup()[columnIndex];
						this.threeByThreeArray[threeByThreeIndex].getGroup()[groupCount] = cell;
						cell.setThreeByThreeSquare(this.threeByThreeArray[threeByThreeIndex]);
//						Cell leftCell=threeByThreeArray.get(group).getGroup().get(groupCount);

//						console.log("For threebythree "+leftCell.equals(rightCell)+" , "+leftCell.toString()+" , "+rightCell.toString());
					}
				}

			}


		getRowArray() {
			if (this.rowArray == null) {
                this.rowArray = [];
			}
			return this.rowArray;
		}

		

		setRowArray(rowArray) {
			this.rowArray = rowArray;
		}


		isSolved() {
			return this.solved;
		}


		

		setSolved(solved) {
			this.solved = solved;
		}


		

		getHowManyCellsLeft() {
			return this.howManyCellsLeft;
		}


		

		setHowManyCellsLeft(howManyCellsLeft) {
            this.howManyCellsLeft = howManyCellsLeft;
		}


		

		getPuzzleId() {
			return this.puzzleId;
		}


		

		setPuzzleId(puzzleId) {
			this.puzzleId = puzzleId;
		}


		

		getPuzzleLevel() {
			return this.puzzleLevel;
		}


		

		setPuzzleLevel(puzzleLevel) {
			this.puzzleLevel = puzzleLevel;
		}






		

		calculateGroup(row, column) {
			var group = 0;
			if (row < 3 && column < 3)group = 0;
			else if (row < 3 && column < 6) group = 1;
			else if (row < 3 && column < 9) group = 2;
			else if (row < 6 && column < 3) group = 3;
			else if (row < 6 && column < 6) group = 4;
			else if (row < 6 && column < 9) group = 5;
			else if (row < 9 && column < 3) group = 6;
			else if (row < 9 && column < 6) group = 7;
			else if (row < 9 && column < 9) group = 8;

			return group;
		}

		/**
		 * @return the sudokuHasChanged
		 */
		

		isSudokuHasChanged() {
			return this.sudokuHasChanged;
		}


		/**
		 * @param sudokuHasChanged the sudokuHasChanged to set
		 */
		

		setSudokuHasChanged(sudokuHasChanged) {
			this.sudokuHasChanged = sudokuHasChanged;
		}


		

		copy() {
			return this;
		}


		

		compareTo(o) {
			return 0;
		}


	}
	



