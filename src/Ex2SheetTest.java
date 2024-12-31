import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class Ex2SheetTest {
    @Test
    public void isInTest() {
        Ex2Sheet sheet = new Ex2Sheet(26, 99);
        assertTrue(sheet.isIn(1,99));
        assertTrue(sheet.isIn(25,1));
        assertTrue(sheet.isIn(0,77));
        assertTrue(sheet.isIn(1,23));
        assertFalse(sheet.isIn(12,949));
        assertFalse(sheet.isIn(27,49));
    }

    @Test
    public void depthTest() {
        Ex2Sheet sheet = new Ex2Sheet(3, 4);
        String[][] dataForSpreadSheet = {
                {
                    "1" , "=A0", "=1+2", "hey"
                },
                {
                    "=A0", "=A0+A1", "text", "111"
                },
                {
                    "=(1+2)+A0", "=B0+A0", "=B1+A0+C1","hello"
                }
        };
        int[][] wantedDepth = {
                {0,1,1,0},
                {1,2,0,0},
                {1,2,3,0}
        };
        int[][] actualDepth = new int[3][4];
        for(int i=0; i<dataForSpreadSheet.length; i++) {
            for (int j=0; j<dataForSpreadSheet[i].length; j++) {
                sheet.set(i,j,dataForSpreadSheet[i][j]);
            }
        }
        actualDepth = sheet.depth();
        for (int i = 0; i < actualDepth.length; i++) {
            System.out.println("Row " + i + ": " + Arrays.toString(actualDepth[i]));
        }
        for (int i = 0; i < wantedDepth.length; i++) {
            assertArrayEquals(wantedDepth[i], actualDepth[i], "Row " + i + " did not match");
        }
    }
}
