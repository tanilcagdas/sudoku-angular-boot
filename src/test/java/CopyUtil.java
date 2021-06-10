import com.sudoku.beans.Sudoku;
import com.sudoku.service.ParserService;
import com.sudoku.service.SudokuFileWriter;
import org.junit.Test;

import java.io.IOException;


public class CopyUtil {

    @Test
    public void copyFromWeb() throws IOException {
        Sudoku sudoku = new ParserService().parseWebSudoku(4580270867l, 3);
        SudokuFileWriter.write(sudoku);
    }

}
