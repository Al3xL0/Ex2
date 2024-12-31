import java.io.IOException;
import java.util.ArrayList;
// Add your documentation below:

public class Ex2Sheet implements Sheet {
    private Cell[][] table;
    // Add your code here

    // ///////////////////
    public Ex2Sheet(int x, int y) {
        table = new SCell[x][y];
        for(int i=0;i<x;i=i+1) {
            for(int j=0;j<y;j=j+1) {
                table[i][j] = new SCell("");
            }
        }
        eval();
    }
    public Ex2Sheet() {
        this(Ex2Utils.WIDTH, Ex2Utils.HEIGHT);
    }

    @Override
    public String value(int x, int y) {
        String ans = Ex2Utils.EMPTY_CELL;
        // Add your code here

        Cell c = get(x,y);
        if(c!=null) {
            ans = c.toString();
        }

        /////////////////////
        return ans;
    }

    @Override
    public Cell get(int x, int y) {
        return table[x][y];
    }

    @Override
    public Cell get(String cords) {
        Cell ans = null;
        // Add your code here

        /////////////////////
        return ans;
    }

    @Override
    public int width() {
        return table.length;
    }
    @Override
    public int height() {
        return table[0].length;
    }
    @Override
    public void set(int x, int y, String s) {
        Cell c = new SCell(s);
        table[x][y] = c;
        // Add your code here

        /////////////////////
    }
    @Override
    public void eval() {
        int[][] dd = depth();
        // Add your code here

        // ///////////////////
    }

    @Override
    public boolean isIn(int xx, int yy) {
        boolean ans = xx>=0 && yy>=0;
        // Add your code here

        /////////////////////
        return ans;
    }

    @Override
    public int[][] depth() {
        int[][] ans = new int[width()][height()];
        // Add your code here

        // ///////////////////
        return ans;
    }

    @Override
    public void load(String fileName) throws IOException {
        // Add your code here

        /////////////////////
    }

    @Override
    public void save(String fileName) throws IOException {
        // Add your code here

        /////////////////////
    }

    @Override
    public String eval(int x, int y) {
        String ans = null;
        if(get(x,y)!=null) {ans = get(x,y).toString();}
        // Add your code here
        ArrayList<String> cellsInFormula = new ArrayList<String>();
        double res;
        // maybe useful
        CellEntry current;
        int validX , validY;
        // ---
        if(ans != null && table[x][y].isForm(ans)) {
            // move that to isValid
            for(String index: CellEntry.indexs) {
                if(ans.contains(index)) {
                    cellsInFormula.add(index);
                }
            }
            if(cellsInFormula.isEmpty()) {
                int type = table[x][y].getType();
                if(type==3) {
                    res = table[x][y].computeForm(ans);
                    ans = Double.toString(res);
                }
            } else {
                for(String cell : cellsInFormula) {
                    /* current = new CellEntry(cell);
                    / validX = current.getX();
                    / validY = current.getY();
                    / if(isIn(validX, validY))
                      {
                       celldata = eval(table[x][y].toString)
                       if(table[x][y].isText(celldata)) {
                         ans = Error formula
                       } else {
                         ans.replace(cell, celldata);
                       }

                      } else {
                            error formula
                      }
                    */
                }
            }
        }
        /////////////////////
        return ans;
        }
}
