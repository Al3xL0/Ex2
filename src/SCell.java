// Add your documentation below:

import java.util.ArrayList;
import java.util.Arrays;

public class SCell implements Cell {
    private String line;
    private int type;
    // Add your code here
    private double value;
    private String originalLine;
    private int order;


    public SCell(String s) {
        originalLine=s;
        setData(s);
        updateType();
        this.order = 0;
    }

    @Override
    public int getOrder() {

        return order;

    }

    //@Override
    @Override
    public String toString() {
        return line;
    }

    @Override
    public void setData(String s) {
        line = s;
    }
    public void saveFormula() {
        this.originalLine = line;
    }
    @Override
    public String getData() {
        return originalLine;
    }
    public int updateType() {
        if(type == Ex2Utils.ERR_CYCLE_FORM) {
            return type;
        }
        if(isNumber(originalLine)) {
            this.type = Ex2Utils.NUMBER;
            double d = Double.parseDouble(line);


        } else if(isForm(originalLine)) {
            this.type = Ex2Utils.FORM;
        } else if(isText(originalLine)) {
            this.type = Ex2Utils.TEXT;
        }  else {
            this.type = Ex2Utils.ERR_FORM_FORMAT;
            this.line = Ex2Utils.ERR_FORM;
        }
        return type;
    }
    @Override
    public int getType() {

        return type;
    }

    public double getValue() {
        return value;
    }

    @Override
    public void setType(int t) {
        type = t;
    }

    @Override
    public void setOrder(int t) {
        // Add your code here
        order = t;
    }

    /**
     * Checks if the given string represents a valid number.
     *
     * @param text The string to check.
     * @return true if the string can be parsed as a double, false otherwise.
     */
    public boolean isNumber(String text) {
        boolean ans = true;
        try {
            double d = Double.parseDouble(text);

        } catch (NumberFormatException e) {
            ans = false;
        }
        return ans;
    }

    public boolean isText(String text) {
        boolean ans = true;

        if(isNumber(text)) {
            ans=false;
        }
        if(text.startsWith("=")) {
            ans=false;
        }
        return ans;
    }

    /**
     * Removes parentheses around a part of a mathematical expression.
     * The method recursively strips parentheses only if they are not needed for
     * defining the order of operations.
     *
     * @param str The array of parts of the expression.
     * @return An array of strings splitted by parentheses
     */
    private String[] removeParentheses(String str) {
        int openParentheses = 0, closeParentheses = 0;
        String[] res = new String[str.length()];
        for(int i=0; i<str.length(); i++) {
            if(str.charAt(i)=='('){
                openParentheses++;
            } else if(str.charAt(i)==')'){
                closeParentheses++;
            }
        }
        if(openParentheses == closeParentheses) {
            res = str.split("[()]");
        } else {
            res[0] = "err";
        }
        return res;
    }

    public boolean isForm(String form) {
        boolean ans = true;
        if(form.isEmpty()){ans=false; return ans;}
        if(!form.startsWith("=")){ans=false; return ans;}
        form = form.substring(1);
        if(form.startsWith("-")) { form = form.substring(1);}

        // Check for consecutive operators like ++, --, **, etc.
        if (form.matches(".*[+\\-*/]{2,}.*")) {
            ans = false;
        }

        String[] partsWitoutParentheses = removeParentheses(form);
        partsWitoutParentheses = Arrays.stream(partsWitoutParentheses)
                .filter(part -> part != null &&  !part.isEmpty())  // Remove empty strings
               .toArray(String[]::new);
        ArrayList<String> partsOfForm = new ArrayList<>();
        for(int i = 0; i < partsWitoutParentheses.length; i++) {
            String[] separatedByOps = partsWitoutParentheses[i].split("[+\\-*/]");
            if(separatedByOps.length>0) {
                if( i!=0 && separatedByOps[0].isEmpty()) {
                    separatedByOps = Arrays.copyOfRange(separatedByOps, 1, separatedByOps.length);

                }
                for(String base : separatedByOps) {
                    partsOfForm.add(base);
                }
            }

        }
        for (String part : partsOfForm) {
            if(part == null || part.isEmpty()) {
                ans=false;
                break;
            }
            try {
                Double.parseDouble(part);
            }catch(NumberFormatException e) {
                if (!CellEntry.indexs.contains(part)) {
                    ans = false;
                    break;
                }
            }
        }
        //for(int i =0; i<partsOfForm.length; i++) {

           // if(validPartsOfForm[i] == null || validPartsOfForm[i].isEmpty()) {
              //  ans=false;
               // break;
           // } else {
                //try {
                   // Double.parseDouble(validPartsOfForm[i]);
              //  } catch(NumberFormatException e) {
                   // if(!CellEntry.indexs.contains(validPartsOfForm[i])) {
                      //  ans =false;
                    //    break;
                 //   }
              //  }
            //}
        //}
        return ans;
    }
    /**
     * Finds the index of the main operator in a mathematical expression.
     * The main operator is the one that is the last to be applied according to
     * operator precedence and parentheses.
     *
     * @param form The mathematical expression as a string.
     * @return The index of the main operator in the string. If no operator is found, returns -1.
     */
    private int indexOfMainOp(String form) {
        int index = -1;
        int currentOrder = 2, previousOrder = 2;
        boolean inParentheses=false, isCurrentNotOperator;
        char current;
        String operators = "+-*/";
        for(int i=0; i<form.length(); i++) {
            current = form.charAt(i);
            isCurrentNotOperator = operators.indexOf(current) == -1;
            if(current == '(') {
                inParentheses = true;
                currentOrder = 2;
            } else if(current == ')') {
                inParentheses = false;
                currentOrder = 2;
            }
            if(!inParentheses) {
                if(current == '*' || current == '/') {
                    currentOrder = 1;
                } else if(current == '-' || current == '+') {
                    currentOrder = 0;
                }
            }
            if(!isCurrentNotOperator&& (currentOrder <= previousOrder)) {
                previousOrder = currentOrder;
                index = i;
            }
        }
        return index;
    }
    private double calc(double a, char operator, double b) {
        double res;
        switch (operator) {
            case '*':
                res = a * b;
                break;
            case '/':
                res = a/b;
                break;
            case '+':
                res = a + b;
                break;
            case '-':
                res = a - b;
                break;
            default:
                res =0;
                break;
        }
        return res;
    }
    public double computeForm(String form) {
        double res;
        if(form.startsWith("=")){
            form = form.substring(1);
        }

        String part1 , part2;
        int operator_index;
        char operator;
        try {
            try {
                if (form.startsWith("(") && form.endsWith(")")) {
                    form = form.substring(1, form.length() - 1);
                }
                res = Double.parseDouble(form);
            } catch (NumberFormatException e) {
                operator_index = indexOfMainOp(form);
                operator = form.charAt(operator_index);
                part1 = form.substring(0, operator_index);
                if(part1.isEmpty()) {part1 = "0";}
                part2 = form.substring(operator_index + 1).trim();
                res = calc(computeForm(part1), operator, computeForm(part2));

            }
        } catch (StringIndexOutOfBoundsException error) {
            res = Ex2Utils.ERR;
        }
        return res;
    }

}