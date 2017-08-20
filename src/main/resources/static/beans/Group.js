
class Group {

    constructor (sudoku, index) {
        this.index = index;
        this.sudoku = sudoku;
        if (this.group == null) {
            //group=new ArrayList<Cell>();
            this.group = [];

            
        }
    }

    getGroup(){
        return this.group;
    }


    toString() {
        var sb = "";
        var cell;
        for (var i = 0; i < this.group.length; i++) {
        var cell = group[i];
            sb = sb + cell.toString();
        }
        return sb;
    }

}