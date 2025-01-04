import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
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
        int x,y;
        // Add your code here
        CellEntry wantedCell = new CellEntry(cords);
        if(wantedCell.isValid()) {
            x = wantedCell.getX();
            y = wantedCell.getY();
            if(isIn(x,y)) {
                ans = table[x][y];
            }
        }
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
        c.setData(s);
        /////////////////////
    }
    @Override
    public void eval() {
        int[][] dd = depth();
        // Add your code here
        String res;
        for(int i=0; i<width(); i++){
            for(int j=0; j<height(); j++){
                table[i][j].saveFormula();
                if(dd[i][j] != -1) {
                    res = eval(i,j);
                    table[i][j].setData(res);
                    table[i][j].getType();
                } else {
                    table[i][j].setType(Ex2Utils.ERR_CYCLE_FORM);
                    table[i][j].setData(Ex2Utils.ERR_CYCLE);
                }
            }
        }
        // ///////////////////
    }

    @Override
    public boolean isIn(int xx, int yy) {
        boolean ans = xx>=0 && yy>=0;
        // Add your code here
        ans = ans && (xx<=width() && yy<= height());
        /////////////////////
        return ans;
    }
    private ArrayList<String> cellReferencesInForm(String form) {
        ArrayList<String> cellReferences = new ArrayList<>();
        try {
            String regex = "[A-Z]+[0-9]+";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(form);
            while (matcher.find()) {
                cellReferences.add(matcher.group()); // Add matched reference to the list
            }
        } catch (PatternSyntaxException e) {
            // Handle the exception gracefully, maybe log it
            System.err.println("PatternSyntaxException: " + e.getMessage());
        }
        return cellReferences;
    }
    private void cellDepth(int[][] depth, int i, int j, int current_depth) {
        Cell currentCell = table[i][j];

        int type = -1;
        if(depth[i][j] != -1) {type = currentCell.getType();}
        String currentData = currentCell.toString();
        CellEntry cellEntry;
        CellEntry givenCellEntry = new CellEntry(i,j);
        int x,y, maxDepth = 0;

        if(type == 3) {
            current_depth++;

            // find cells using regex

            ArrayList<String> cellReferences = cellReferencesInForm(currentData);

            if(!cellReferences.isEmpty()) {
                for(String cell : cellReferences) {
                    cellEntry = new CellEntry(cell);
                    x = cellEntry.getX();
                    y = cellEntry.getY();
                    if(isIn(x,y)) {
                        if(Objects.equals(givenCellEntry.toString(), cell)) {
                            current_depth=-1;
                            break;
                        }
                        try {
                            cellDepth(depth, x, y, 0);
                        } catch (StackOverflowError infiniteCall) {
                            depth[x][y] = -1;
                            table[x][y].setType(Ex2Utils.ERR_CYCLE_FORM);
                            current_depth = -1;
                            maxDepth = 0;
                            break;
                        }
                        if(depth[x][y] == -1) {
                            current_depth = -1;
                            maxDepth = 0;
                            break;
                        }
                        if(depth[x][y] > maxDepth) {
                            maxDepth = depth[x][y];
                        }
                    }
            }

            }
            depth[i][j] = current_depth + maxDepth;
        } else if(type == -1) {
            depth[i][j] = -1;
        }

    }

    @Override
    public int[][] depth() {
        int[][] ans = new int[width()][height()];

        // Add your code here
        for(int i = 0; i<ans.length; i++) {
            for(int j = 0; j<ans[0].length; j++) {
                if(ans[i][j] == 0) {
                    cellDepth(ans,i,j,0);
                }

            }
        }
        // ///////////////////

        return ans;
    }

    @Override
    public void load(String fileName) throws IOException {

        // clean table
        for(int i=0; i<width(); i++) {
            for(int j=0; j<height(); j++) {
                // reset the original formula + the current data
                table[i][j].setData("");
                table[i][j].saveFormula();
            }
        }
        //load file
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        ArrayList<String> lines = new ArrayList<String>();
        String[] splitLine;
        String form;
        int x,y;
        while(scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
        }
        scanner.close();
        for(int i = 1; i<lines.size(); i++) {
            splitLine = lines.get(i).split(",");
            if(splitLine.length>=3) {
                try {
                    x = Integer.parseInt(splitLine[0]);
                    y = Integer.parseInt(splitLine[1]);
                    form = splitLine[2];
                    CellEntry cellEntry = new CellEntry(x,y);
                    if(cellEntry.isValid() && isIn(x,y)) {
                        table[x][y].setData(form);
                    }
                } catch (NumberFormatException e) {

                }
            }
        }
        eval();

        /////////////////////
    }

    @Override
    public void save(String fileName) throws IOException {
        // Add your code here
        String currentLine;
        File file = new File(fileName);
        if(!file.exists()) {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        for(int i=0; i< width(); i++) {
            for(int j=0; j<height(); j++) {
                if(!table[i][j].getData().isEmpty()) {
                    currentLine = i + "," + j + "," + table[i][j].getData();
                    printWriter.println(currentLine);
                }
            }
        }
        printWriter.close();
        /////////////////////
    }
    /*
        In my implemention String eval is being called only when there is no cycle. (calling it inside void eval after checking depth)
     */
    @Override
    public String eval(int x, int y) {
        String ans = null;
        if(get(x,y)!=null) {ans = get(x,y).toString();}
        // Add your code here
        ArrayList<String> cellsInFormula = new ArrayList<String>();
        double res;
        // maybe useful
        String cellData;
        CellEntry current;
        int validX , validY;
        int type = table[x][y].getType();
        // ---
        if(ans != null && type == 3) {
            // move that to isValid
            cellsInFormula = cellReferencesInForm(ans);
             if(!cellsInFormula.isEmpty()){
                for(String cell : cellsInFormula) {
                    current = new CellEntry(cell);
                    validX = current.getX();
                    validY = current.getY();
                    if(isIn(validX, validY))
                      {
                       cellData = table[validX][validY].toString();
                       switch(table[validX][validY].getType()) {

                           case Ex2Utils.FORM:
                               cellData = eval(validX,validY);
                               // update the specific cell after eval
                               table[validX][validY].setData(cellData);
                               ans = ans.replace(cell, cellData);
                               break;
                           case Ex2Utils.NUMBER:
                               ans = ans.replace(cell, cellData);
                               break;
                           default:
                               ans = Ex2Utils.ERR_FORM;
                               break;
                       }
                    } else {
                        ans = Ex2Utils.ERR_FORM;
                    }

                }
            }

            if(!ans.equals(Ex2Utils.ERR_FORM)) {
                ans = Double.toString(table[x][y].computeForm(ans));
                table[x][y].setData(ans);
            }
        }
        if(type == Ex2Utils.ERR_FORM_FORMAT) {
            ans = Ex2Utils.ERR_FORM;
        }
        /////////////////////
        return ans;
        }
}
