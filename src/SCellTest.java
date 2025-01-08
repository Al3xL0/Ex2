import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SCellTest {
    @Test
    void testNumber() {
        SCell[] good = {
                new SCell("2.0"),
                new SCell("5"),
                new SCell("10"),
                new SCell("-11"),
                new SCell("-18"),
                new SCell("-1.5009"),
                new SCell("0"),
                new SCell("123456789"),
                new SCell("0.0001")
        };
        SCell[] bad = {
                new SCell("%"),
                new SCell("aa"),
                new SCell("-a"),
                new SCell(""),
                new SCell("9s"),
                new SCell("1.1.1"),
                new SCell("++1"),
                new SCell("2.3.4.5"),
                new SCell("2+3"),
                new SCell("abc"),
                new SCell("1.2.3"),
                new SCell("++5"),
                new SCell("1e1e1"),
                new SCell("2.3*4")
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
                new SCell("cd_"),
                new SCell("hello"),
                new SCell("test@123"),
                new SCell("abc_123")
        };
        SCell[] bad = {
                new SCell("="),
                new SCell("=a"),
                new SCell("=b"),
                new SCell("11"),
                new SCell("9"),
                new SCell("2.3"),
                new SCell("1234"),
                new SCell("0.5")
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
                new SCell("=(23)+2+1+(1+2)"),
                new SCell("=(0+(1+2))"),
                new SCell("=(11)"),
                new SCell("=(1)+1"),
                new SCell("=(1+2)*((3))-1"),
                new SCell("=a0+b0"),
                new SCell("=-1"),
                new SCell("=-1.1+2.5"),
                new SCell("=(a0+b0)*2"),
                new SCell("=(a0+b0)-3*(2+3)"),
                new SCell("=-(4+5)*6"),
                new SCell("=3+4+(5*6)"),
                new SCell("=3+4-(5*6)"),
                new SCell("=3/3 - 1"),
                new SCell("=-1"),
                new SCell("=3+2*5"),
                new SCell("=3+(2*5)"),
                new SCell("=5*(6-2)"),
                new SCell("=1+2"),
                new SCell("=(1+2)*3")

        };
        SCell[] bad = {
                new SCell("=123++"),
                new SCell("=a"),
                new SCell("=(a2"),
                new SCell("=((1 + 1)"),
                new SCell("=Aa"),
                new SCell("=(1)+1)"),
                new SCell("=*1"),
                new SCell("=+2+3"),
                new SCell("=(1+2"),
                new SCell("=2+3)"),
                new SCell("=a+b+"),
                new SCell("=*2+3")
        };
        for(SCell cell : good) {
            assertTrue(cell.isForm(cell.getData()));
            assertEquals(cell.getType(),Ex2Utils.FORM);
        }
        for(SCell cell : bad) {
            assertFalse(cell.isForm(cell.getData()));
            assertEquals(cell.getType(),Ex2Utils.ERR_FORM_FORMAT);
            assertEquals(cell.toString(),Ex2Utils.ERR_FORM);
        }
    }
    @Test

    void testComputeForm() {
        SCell[] formulas = {
                new SCell("=0.000001*1000000"),
                new SCell("=1.5-0.5"),
                new SCell("=-(4+5)*6"),
                new SCell("=3+4+(5*6)"),
                new SCell("=3+4-(5*6)"),
                new SCell("=3/3 - 1"),
                new SCell("=-1"),
                new SCell("=3+2*5"),
                new SCell("=3+(2*5)"),
                new SCell("=5*(6-2)"),
                new SCell("=1+2"),
                new SCell("=(1+2)*3"),
                new SCell("=1000000+2000000"),
                new SCell("=1e3+1e3"),
                new SCell("=999999/333333"),
                new SCell("=2.1+3.9"),
                new SCell("=3.5*2.4"),
                new SCell("=10/3"),
                new SCell("=1000-999"),
                new SCell("=0.0000001+0.0000002")
        };

        double[] expectedResults = {1, 1.0,-54, 37, -23, 0, -1, 13, 13, 20, 3, 9, 3000000, 2000, 3, 6.0, 8.4, 3.333333, 1, 0.0000003};
        int index = 0;
        for(SCell formula : formulas) {
            double currentResult = formula.computeForm(formula.getData());
            assertEquals(expectedResults[index], currentResult, 0.0001);  // Added tolerance for floating point comparison
            index++;
        }
    }


}
