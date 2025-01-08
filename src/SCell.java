// Add your documentation below:

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
        if(isNumber(line)) {
            this.type = Ex2Utils.NUMBER;
        } else if(isForm(line)) {
            this.type = Ex2Utils.FORM;
        } else if(isText(line)) {
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
            Double.parseDouble(text);
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
     * @param arr The array of parts of the expression.
     * @param i The index of the part to process.
     * @return A string with parentheses removed.
     */
    private String removeParentheses(String[] arr , int i ) {
        boolean leftParentheses, rightParentheses, wrappedByParentheses;
        leftParentheses = arr[i].startsWith("(");
        rightParentheses = arr[i].endsWith(")");
        String res = "";
        if(i+1< arr.length ) {
            wrappedByParentheses = arr[i+1].startsWith("(") && arr[i+1].endsWith(")");
            if ( (!wrappedByParentheses) && leftParentheses && arr[i + 1].endsWith(")")) {
                res = arr[i].substring(1);
            }
        }

        if(i-1 >= 0) {
            wrappedByParentheses = arr[i-1].startsWith("(") && arr[i-1].endsWith(")");
            if ((!wrappedByParentheses) && arr[i-1].startsWith("(") && rightParentheses) {
                res = arr[i].substring(0, arr[i].length() - 1);
            }
        }
        if(!leftParentheses && !rightParentheses) {
            res = arr[i];
        }
        else if(leftParentheses && rightParentheses) {
            res = arr[i].substring(1, arr[i].length()-1);
        }
        leftParentheses = res.startsWith("(");
        rightParentheses = res.endsWith(")");
        if(leftParentheses || rightParentheses ) {
            res = removeParentheses(new String[]{res}, 0);
        }
        return res;
    }

    public boolean isForm(String form) {
        boolean ans = true;
        if(form.isEmpty()){ans=false; return ans;}
        if(!form.startsWith("=")){ans=false; return ans;}
        form = form.substring(1);
        if(form.startsWith("-")) { form = form.substring(1);}
        String[] partsOfForm = form.split("[+\\-*/]");
        String[] validPartsOfForm = new String[partsOfForm.length];

        boolean leftParentheses, rightParentheses, wrappedByParentheses;
        for(int i =0; i<partsOfForm.length; i++) {
            validPartsOfForm[i] = removeParentheses(partsOfForm, i);

            if(validPartsOfForm[i] == null || validPartsOfForm[i].isEmpty()) {
                ans=false;
                break;
            } else {
                try {
                    Double.parseDouble(validPartsOfForm[i]);
                } catch(NumberFormatException e) {
                    if(!CellEntry.indexs.contains(validPartsOfForm[i])) {
                        ans =false;
                        break;
                    }
                }
            }
        }
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
                if (form.startsWith("(")) {
                    form = form.substring(1);
                }
                if (form.endsWith(")")) {
                    form = form.substring(0, form.length() - 1);
                }
                res = Double.parseDouble(form);
            } catch (NumberFormatException e) {
                operator_index = indexOfMainOp(form);
                operator = form.charAt(operator_index);
                part1 = form.substring(0, operator_index);
                part2 = form.substring(operator_index + 1).trim();
                res = calc(computeForm(part1), operator, computeForm(part2));

            }
        } catch (StringIndexOutOfBoundsException error) {
            res = Ex2Utils.ERR;
        }
        return res;
    }

}
