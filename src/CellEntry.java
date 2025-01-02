// Add your documentation below:

import java.util.ArrayList;

public class CellEntry  implements Index2D {
    public static ArrayList<String> indexs = create_indexs();
    private String indexOfCell;
    public CellEntry(String entry){
        this.indexOfCell = entry.toUpperCase();
    }
    public CellEntry(int x, int y) {
        this.indexOfCell = convertX(x) + convertY(y);
    }
    @Override
    public String toString() {
        return  this.indexOfCell;
    }
    @Override
    public boolean isValid() {
        boolean ans = false;
        if(indexs.contains(this.indexOfCell)) {
            ans =true;
        }
        return ans;
    }

    @Override
    public int getX() {
        int ans;
        if(!isValid()) {
            ans = Ex2Utils.ERR;
        } else {

            ans = indexOfCell.charAt(0) - 'A';
        }
        return ans;
    }

    @Override
    public int getY() {
        int ans;
        if(!isValid()) {
            ans = Ex2Utils.ERR;
        } else {
            ans = Integer.parseInt(this.indexOfCell.substring(1));
        }
        return ans;
    }

    private static ArrayList<String> create_indexs() {
        ArrayList<String> indexList = new ArrayList<String>();
        String current;
        for(String x : Ex2Utils.ABC) {
            for(int i=0; i<=99; i++) {
                current = x + i;
                indexList.add(current);
                current = x.toLowerCase() + i;
                indexList.add(current);
            }
        }

        return indexList;
    }

    private String convertX(int num) {
        if (num >= 0 && num < 26) {
            return Character.toString((char) ('A' + num)); // Convert 0-25 to 'A'-'Z'
        }
        return null;
    }
    private String convertY(int num) {
        return String.valueOf(num);
    }
}
