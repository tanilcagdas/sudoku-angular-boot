class Row extends Group {
    constructor(sudoku, index) {

        super(sudoku, index);
        var i;
            for (i = 0; i < 9; i++) {
                var cell = new Cell(this);
                this.group.push(cell);
            }
    }
}



