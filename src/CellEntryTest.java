import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CellEntryTest {

    @Test
    void testAll() {
        CellEntry[] validEntries = {
                new CellEntry("A0"),
                new CellEntry("B0"),
                new CellEntry("D9"),
                new CellEntry("Z99"),
                new CellEntry("A29"),
                new CellEntry("c0")
        };
        CellEntry[] invalidEntries = {
                new CellEntry("AB0"),
                new CellEntry("BB0"),
                new CellEntry("_D9"),
                new CellEntry("Za99"),
                new CellEntry("0A29"),
                new CellEntry("0C")
        };
        validate(validEntries, invalidEntries);
        getAxis(validEntries);
    }

    void validate(CellEntry[] validEntries, CellEntry[] invalidEntries) {
        for(CellEntry entry : validEntries) {
            assertTrue(entry.isValid());
        }
        for(CellEntry entry: invalidEntries) {
            assertFalse(entry.isValid());
        }
    }
    void getAxis(CellEntry[] validEntries) {
        int[] goodX = {0,1,3,25,0,2};
        int[] goodY = {0,0,9,99,29,0};

        for(int i = 0; i < validEntries.length; i++) {
            assertEquals(validEntries[i].getX(),goodX[i]);
            assertEquals(validEntries[i].getY(),goodY[i]);
        }
    }
}
