/**
 * ArielU. Intro2CS, Ex2: https://docs.google.com/document/d/1-18T-dj00apE4k1qmpXGOaqttxLn-Kwi/edit?usp=sharing&ouid=113711744349547563645&rtpof=true&sd=true
 * DO NOT CHANGE THIS INTERFACE!!
 * This interface represents a spreadsheet entry for Ex2:
 * Each spreadsheet entry (aka a Cell) which can be:
 * a number (Double), a String (Text), or a form, the data of each cell is represented as a String (e.g., "abc", "4.2", "=2+3*2", "=A1*(3-A2)".
 */
public interface Cell {
    /**
     * Return the input text (aka String) this cell was init by (without any computation).
     * @return
     */
    String getData();

/** Changes the underline string of this cell
 *  */
    void setData(String s);


    /**
     * Returns the type of this cell {TEXT,NUMBER, FORM, ERR_CYCLE_FORM, ERR_WRONG_FORM}
     * @return an int value (as defined in Ex2Utils)
     */
    public int getType();

    /**
     * Changes the type of this Cell {TEXT,NUMBER, FORM, ERR_CYCLE_FORM, ERR_WRONG_FORM}
     * @param t an int type value as defines in Ex2Utils.
     */
    public void setType(int t);
    /**
     * Computes the natural order of this entry (cell) in case of a number or a String =0, else 1+ the max of all dependent cells.
     * @return an integer representing the "number of rounds" needed to compute this cell (using an iterative approach)..
     */
    public int getOrder();
    /**
     * Changes the order of this Cell
     * @param t
     */
    public void setOrder(int t);



    /**
     *  Checks if the given string is a valid formula.
     *  a vaild formula conations only cells and number, and start with "=".
     *  example of valid formula : "=A0+1*(5/A1)"
     * @param form the current cell data
     * @return if is formula true, else false.
     */
    public boolean isForm(String form);

    /**
     * computes the given formula
     * @param form the current cell data
     * @return the formula after computation
     */
    public double computeForm(String form);

    /**
     * Checks if the given string is a number or formula, if it is neither assumes that it is text
     * @param text the current cell data
     * @return true if text, false if formula or number.
     */
    public boolean isText(String text);

    /**
     * saves the original line before any computation
     */
    public void saveFormula();
    /**
     * computes the type of this Cell
     * @return the type of the cell after updating it
     **/
    public int updateType();
}
