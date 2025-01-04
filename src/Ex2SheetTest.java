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
                    "=A0" , "=A2", "=1+2", "hey"
                },
                {
                    "=A2", "=A2+A1", "=B2", "=C3"
                },
                {
                    "=(1+2)+A2", "=B0+A2", "=B1+A2+C1","=B3"
                }
        };
        // fix case C3 :=C5 C5:=C3
        int[][] wantedDepth = {
                {-1,2,1,0},
                {2,3,-1,-1},
                {2,3,4,-1}
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
    @Test
    public void testEvalString() {
        Ex2Sheet sheet = new Ex2Sheet(3, 4);
        String[][] dataForSpreadSheet = {
                {
                        "=1+2" , "=A0", "=1+2", "hey"
                },
                {
                        "=A0", "=A0+A1", "=B1+B3", "=5*8"
                },
                {
                        "=(1+2)+A0", "=B0+A0", "=B1+A0+C1","=B3"
                }
        };
        String[][] wantedDataAfterEval = {
                {
                        "3.0" , "3.0", "3.0", "hey"
                },
                {
                        "3.0", "6.0", "46.0", "40.0"
                },
                {
                        "6.0", "6.0", "15.0","40.0"
                }
        };
        String[][] actualData = new String[3][4];
        for(int i=0; i<dataForSpreadSheet.length; i++) {
            for (int j=0; j<dataForSpreadSheet[i].length; j++) {
                sheet.set(i,j,dataForSpreadSheet[i][j]);
            }
        }
        for(int i=0; i<actualData.length; i++) {
            for (int j=0; j<actualData[i].length; j++) {
                actualData[i][j] = sheet.eval(i,j);
            }
            System.out.println("Row " + i + ": " + Arrays.toString(actualData[i]));
        }
        for (int i = 0; i < wantedDataAfterEval.length; i++) {
            assertArrayEquals(wantedDataAfterEval[i], actualData[i], "Row " + i + " did not match");
        }
    }
    @Test
    void testEval() {
        Ex2Sheet sheet = new Ex2Sheet(2,3);
        String[][] dataForSpreadSheet = {
                {"=A0", "=A0",  "=B0"},
                {"3", "=3+B0", "hey"}
        };
        String[][] wantedData = {
                {Ex2Utils.ERR_CYCLE, Ex2Utils.ERR_CYCLE,  "3.0"},
                {"3", "6.0", "hey"}
        };
        String[][] res = new String[2][3];
        for(int i=0; i<dataForSpreadSheet.length; i++) {
            for (int j=0; j<dataForSpreadSheet[i].length; j++) {
                sheet.set(i,j,dataForSpreadSheet[i][j]);
            }
        }
        sheet.eval();
        for(int i=0; i<dataForSpreadSheet.length; i++) {
            for (int j=0; j<dataForSpreadSheet[i].length; j++) {
                res[i][j] = sheet.get(i,j).toString();
            }
            System.out.println("Row " + i + ": " + Arrays.toString(res[i]));
        }
        for (int i = 0; i < res.length; i++) {
            assertArrayEquals(wantedData[i], res[i], "Row " + i + " did not match");
        }
    }
}
