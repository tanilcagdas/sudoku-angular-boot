

class Column extends Group {
	constructor( sudoku, index) {
		super(sudoku, index);

/*		var i;
            for (i = 0; i < 9; i++) {
                var cell = Sudoku.getRowArray()[i].[index];
                this.group.push(cell);
            }*/





	}



	 toString() {

		return this.group.toString();
	}
}

