// Add your documentation below:

public class SCell implements Cell {
    private String line;
    private int type;
    // Add your code here
    private double value;

    private int order;


    public SCell(String s) {
        // Add your code here
        setData(s);


    }

    @Override
    public int getOrder() {
        // Add your code here

        return order;
        // ///////////////////
    }

    //@Override
    @Override
    public String toString() {
        return getData();
    }

    @Override
    public void setData(String s) {
        // Add your code here
        line = s;
        /////////////////////
    }
    @Override
    public String getData() {
        return line;
    }

    @Override
    public int getType() {
        if(isNumber(line)) {
            type = Ex2Utils.NUMBER;
        } else if(isForm(line)) {
            type = Ex2Utils.FORM;
        } else if(isText(line)) {
            type = Ex2Utils.TEXT;
        }  else {
            type = Ex2Utils.ERR_FORM_FORMAT;
            this.line = Ex2Utils.ERR_FORM;
        }
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

    /*

     */
    public boolean isNumber(String text) {
        boolean ans = true;
        int i = 0;
        int charInAscii;
        if(text.isEmpty()) {ans = false;}
        // check if it has the negative sign or starts with =
        if(text.startsWith("=")) {
            i++;
            // text.length()>1 makes sure that we won't get out of bound
            if(text.length()>1&&text.charAt(i) == '-') {
                i++;
            }
        }
        if(text.startsWith("-")) {
            i++;
            if(i>=text.length()) {ans =false;}
        }
        for(i=i; i<text.length(); i++) {
            charInAscii = (int) text.charAt(i);
            if(!(charInAscii>47 && charInAscii<58)) {
                ans=false;
            }
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

    private int indexOfMainOp(String form) {
        int index = -1;
        int currentOrder = 2, previousOrder = 2;
        boolean inParentheses=false, isCurrentNotOperator;
        char current;
        for(int i=0; i<form.length(); i++) {
            current = form.charAt(i);
            isCurrentNotOperator = Character.isDigit(current) || Character.isLetter(current) ;
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
                res = 0;
                break;
        }
        return res;
    }
    public double computeForm(String form) {
        double res = 0;
        String part1 , part2;
        int operator_index;
        char operator;
        try {
            if(form.startsWith("(")) {
                form = form.substring(1);
            }
            if(form.endsWith(")")) {
                form = form.substring(0, form.length()-1);
            }
            res = Double.parseDouble(form);
        } catch(NumberFormatException e) {
            operator_index = indexOfMainOp(form);
            operator = form.charAt(operator_index);
            part1 = form.substring(0, operator_index);
            part2 = form.substring(operator_index+1).trim();

            if (part1.isEmpty() || part2.isEmpty()) {
                part1 = "0";
                part2 = "1";
            }
            res = calc(computeForm(part1), operator  ,computeForm(part2));


        }
        return res;
    }
    // a protection layer to computeForm, makes sure that the value that passed into the function is a formula
    public void calcForm() {
        if(isForm(this.line)) {
            // assign the result to value.
            this.value = computeForm(this.line.substring(1));
            this.line = Double.toString(value);
        }
    }
}
