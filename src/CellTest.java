import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CellTest {
    @Test
    void testNumber() {
        Cell[] good = {
                new Cell("2"),
                new Cell("5"),
                new Cell("10"),
                new Cell("=-11"),
                new Cell("-18")
        };
        Cell[] bad = {
                new Cell("%"),
                new Cell("aa"),
                new Cell("-a"),
                new Cell(""),
                new Cell("9d")
        };
        for(Cell cell : good) {
            assertTrue(cell.isNumber(cell.getData()));
        }
        for(Cell cell : bad) {
            assertFalse(cell.isNumber(cell.getData()));
        }
    }
    @Test
    void testIsText() {
        Cell[] good = {
                new Cell("%"),
                new Cell("aa"),
                new Cell("a0"),
                new Cell("ab"),
                new Cell("cd_")
        };
        Cell[] bad = {
                new Cell("="),
                new Cell("=a"),
                new Cell("=b"),
                new Cell("11"),
                new Cell("9")
        };
        for(Cell cell : good) {
            assertTrue(cell.isText(cell.getData()));
        }
        for(Cell cell : bad) {
            assertFalse(cell.isText(cell.getData()));
        }
    }
    @Test
    void testIsForm() {
        Cell[] good = {
                new Cell("=((0))"),
                new Cell("=(23)+2+1+(1+2)"),
                new Cell("=(11)"),
                new Cell("=(1)+1"),
                new Cell("=5")
        };
        Cell[] bad = {
                new Cell("a"),
                new Cell("=(a2"),
                new Cell("="),
                new Cell("=Aa"),
                new Cell("=(1)+1)")
        };
        for(Cell cell : good) {
            assertTrue(cell.isForm(cell.getData()));
        }
        for(Cell cell : bad) {
            assertFalse(cell.isForm(cell.getData()));
        }
    }
}
