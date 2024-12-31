import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SCellTest {
    @Test
    void testNumber() {
        SCell[] good = {
                new SCell("2"),
                new SCell("5"),
                new SCell("10"),
                new SCell("=-11"),
                new SCell("-18")
        };
        SCell[] bad = {
                new SCell("%"),
                new SCell("aa"),
                new SCell("-a"),
                new SCell(""),
                new SCell("9d")
        };
        for(SCell cell : good) {
            assertTrue(cell.isNumber(cell.getData()));
        }
        for(SCell cell : bad) {
            assertFalse(cell.isNumber(cell.getData()));
        }
    }
    @Test
    void testIsText() {
        SCell[] good = {
                new SCell("%"),
                new SCell("aa"),
                new SCell("a0"),
                new SCell("ab"),
                new SCell("cd_")
        };
        SCell[] bad = {
                new SCell("="),
                new SCell("=a"),
                new SCell("=b"),
                new SCell("11"),
                new SCell("9")
        };
        for(SCell cell : good) {
            assertTrue(cell.isText(cell.getData()));
        }
        for(SCell cell : bad) {
            assertFalse(cell.isText(cell.getData()));
        }
    }
    @Test
    void testIsForm() {
        SCell[] good = {
                new SCell("=((0))"),
                new SCell("=(23)+2+1+(1+2)"),
                new SCell("=(11)"),
                new SCell("=(1)+1"),
                new SCell("=(1+2)*((3))-1"),
                new SCell("=a0+b0")
        };
        SCell[] bad = {
                new SCell("a"),
                new SCell("=(a2"),
                new SCell("=((1 + 1)"),
                new SCell("=Aa"),
                new SCell("=(1)+1)"),
                new SCell("=*1")
        };
        for(SCell cell : good) {
            assertTrue(cell.isForm(cell.getData()));
        }
        for(SCell cell : bad) {
            assertFalse(cell.isForm(cell.getData()));
        }
    }
    @Test
    void testComputeForm() {
        SCell[] formulas = {
                new SCell("=3+4+(5*6)"),
                new SCell("=3+4-(5*6)"),
                new SCell("=5*8+(1-1)")
        };
        int index = 0;
        double[] wantedResults ={37, -23, 40  };
        for(SCell formula : formulas) {
            formula.calcForm();
            assertEquals(formula.getValue(), wantedResults[index]);
            index++;
        }
    }
}
