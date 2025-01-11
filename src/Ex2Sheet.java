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


public class Ex2Sheet implements Sheet {
    private Cell[][] table;
    // an array which displays 1 if current index in table is used for evaluation, otherwise 0
    private int[][] isEvaluating;

    public Ex2Sheet(int x, int y) {
        table = new SCell[x][y];
        isEvaluating = new int[x][y]; // Initialize the evaluation state array
        for(int i=0;i<x;i=i+1) {
            for(int j=0;j<y;j=j+1) {
                table[i][j] = new SCell("");
                isEvaluating[i][j] = 0;  // Mark all cells as not being evaluated
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

        Cell c = get(x,y);
        if(c!=null) {
            ans = c.toString();
        }

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

        CellEntry wantedCell = new CellEntry(cords);
        if(wantedCell.isValid()) {
            x = wantedCell.getX();
            y = wantedCell.getY();
            if(isIn(x,y)) {
                ans = table[x][y];
            }
        }

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
        table[x][y].updateType();
        // if it is a number, represent the data inside the cell with decimal points
        if(table[x][y].getType() == Ex2Utils.NUMBER) {
            double number = Double.parseDouble(s);
            s = String.format("%.1f", number);
        }
        table[x][y].setData(s);
        // save the formula, because the reformatting of s in line 83 should be counted as original line
        table[x][y].saveFormula();

    }
    @Override
    public void eval() {
        int[][] dd = depth();
        String res;
        for(int i=0; i<width(); i++){
            for(int j=0; j<height(); j++){
                // save the formula before any change to the cell.
                if(table[i][j].toString() == table[i][j].getData()) {
                    table[i][j].saveFormula();
                }
                // call string eval only if there is no cycle.
                if(dd[i][j] != -1 && table[i][j].getOrder() !=-1) {
                    res = eval(i,j);

                    table[i][j].setData(res);
                } else {
                    table[i][j].setType(Ex2Utils.ERR_CYCLE_FORM);
                    table[i][j].setData(Ex2Utils.ERR_CYCLE);
                }
            }
        }
    }

    @Override
    public boolean isIn(int xx, int yy) {
        boolean ans = xx>=0 && yy>=0;
        ans = ans && (xx<=width()-1 && yy<= height()-1);
        return ans;
    }
    /**
     * Returns a list of cell references found in the specified formula string.
     *
     * @param form the formula string to search for cell references.
     * @return a list of cell references.
     */
    private ArrayList<String> cellReferencesInForm(String form) {
        ArrayList<String> cellReferences = new ArrayList<>();
        try {
            // get using regex, the pattern matching parts of the formula, which are cell references
            String regex = "[A-Z]+[0-9]+";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(form);
            while (matcher.find()) {
                cellReferences.add(matcher.group()); // Add matched reference to the list
            }
        } catch (PatternSyntaxException e) {
            // log the exception
            System.err.println("PatternSyntaxException: " + e.getMessage());
        }
        return cellReferences;
    }
    /**
     * Computes the depth of each cell in the spreadsheet recursively.
     *
     * @param depth the 2D array to store the depth of each cell.
     * @param i the x-coordinate of the current cell.
     * @param j the y-coordinate of the current cell.
     * @param current_depth the current depth in the recursion.
     */
    private void cellDepth(int[][] depth, int i, int j, int current_depth) {
        Cell currentCell = table[i][j];

        int type = -1; // initialize type as cycle
        if(depth[i][j] != -1) {
            table[i][j].updateType(); // update type if there is no cycle in the current cell
            type = currentCell.getType();
        }
        String currentData = currentCell.getData();
        CellEntry cellEntry;
        CellEntry givenCellEntry = new CellEntry(i,j);
        int x,y, maxDepth = 0;

        if(type == 3) {
            current_depth++;
            ArrayList<String> cellReferences = cellReferencesInForm(currentData);
            // iterate through each cell and compute depth using the cell with the highest depth
            if(!cellReferences.isEmpty()) {
                for(String cell : cellReferences) {
                    cellEntry = new CellEntry(cell);
                    x = cellEntry.getX();
                    y = cellEntry.getY();
                    if(cell.equals(Ex2Utils.ERR_CYCLE)) {
                        depth[i][j] = Ex2Utils.ERR_CYCLE_FORM;
                    }
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
                        // update the natural order of the current cell
                        table[x][y].setOrder(depth[x][y]);
                    }
            }

            }
            depth[i][j] = current_depth + maxDepth;
        } else if(type == -1) {
            depth[i][j] = -1;
        }
        // update the natural order of the cell.
        table[i][j].setOrder(depth[i][j]);
    }

    @Override
    public int[][] depth() {
        int[][] ans = new int[width()][height()];
        // compute the depth of each cell
        for(int i = 0; i<ans.length; i++) {
            for(int j = 0; j<ans[0].length; j++) {
                if(ans[i][j] == 0) {
                    cellDepth(ans,i,j,0);
                }

            }
        }


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
                table[i][j].setType(0);
            }
        }
        //load file
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        ArrayList<String> lines = new ArrayList<String>();
        String[] splitLine;
        String form;
        int x,y;
        // read all the lines inside the file
        while(scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
        }
        scanner.close(); // close the file I after I have finished with it
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
                        table[x][y].saveFormula();
                        int type = table[x][y].updateType();
                        table[x][y].setType(type);
                    }
                } catch (NumberFormatException e) {

                }
            }
        }
        eval();

    }

    @Override
    public void save(String fileName) throws IOException {
        String currentLine;
        File file = new File(fileName); // open a file
        if(!file.exists()) {
            file.createNewFile(); // create the file if it is not found
        }
        FileWriter fileWriter = new FileWriter(file);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.println("I2CS ArielU: SpreadSheet (Ex2) assignment - this line should be ignored"); // header of the file
        for(int i=0; i< width(); i++) {
            for(int j=0; j<height(); j++) {
                if(!table[i][j].getData().isEmpty()) {
                    currentLine = i + "," + j + "," + table[i][j].getData();
                    printWriter.println(currentLine); // write each cell that contains data into the file in separated line
                }
            }
        }
        printWriter.close();

    }
    /*
        In my implementation, String eval is being called only when there is no cycle. (calling it inside void eval after checking depth)
     */
    @Override
    public String eval(int x, int y) {
        String ans = null;
        if(get(x,y)!=null) {
            ans = get(x,y).getData();
            // remove whitespaces
            ans = ans.replaceAll("\\s", "");
        }

        // If the cell is already being evaluated, return an error (cycle detected)
        if (isEvaluating[x][y] == 1) {
            table[x][y].setType(Ex2Utils.ERR_CYCLE_FORM);
            table[x][y].setOrder(-1);
            return Ex2Utils.ERR_CYCLE;
        }

        // Mark the cell as currently being evaluated
        isEvaluating[x][y] = 1;
        ArrayList<String> cellsInFormula;
        String cellData;
        CellEntry current;
        int validX = -1 , validY = -1;
        table[x][y].updateType();
        int type = table[x][y].getType();
        CellEntry wantedCellEntry = new CellEntry(x,y);

        if(ans != null && type == 3) {

            cellsInFormula = cellReferencesInForm(ans);
             if(!cellsInFormula.isEmpty()){
                 // iterate through each cell reference, compute it and replace the result inside the formula
                for(String cell : cellsInFormula) {
                    if(wantedCellEntry.toString() == cell) {
                        ans= Ex2Utils.ERR_CYCLE;
                        table[x][y].setType(Ex2Utils.ERR_CYCLE_FORM);
                        break;
                    }
                    current = new CellEntry(cell);
                    if(current.isValid()) {
                        validX = current.getX();
                        validY = current.getY();
                    } else {
                        ans = Ex2Utils.ERR_FORM; // error if there is an invalid cell Entry inside the formula
                    }
                    if(isIn(validX, validY))
                      {
                       cellData = table[validX][validY].getData();
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

            if(!ans.equals(Ex2Utils.ERR_FORM) && !ans.equals(Ex2Utils.ERR_CYCLE)) {
                ans = Double.toString(table[x][y].computeForm(ans));
                table[x][y].setData(ans);
            }
        }
        if(type == Ex2Utils.ERR_FORM_FORMAT) {
            ans = Ex2Utils.ERR_FORM;
        }
        // Mark the cell as not being evaluated after the recursion
        isEvaluating[x][y] = 0;
        return ans;
        }
}
