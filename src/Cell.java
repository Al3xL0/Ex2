import java.util.ArrayList;
import java.util.Arrays;

public class Cell {
    private String data;
    private double value;

    public Cell(String data) {
        this.data = data;
        this.value = 0;
    }

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
    /*
        The following function checks if given string is a formula using regex
     */
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
                    ans =false;
                    break;
                }
            }
        }
        return ans;
    }

    private int indexOfMainOp(String form) {
        int index = 0;
        int currentOrder = 2, previousOrder = 2;
        boolean inParentheses=false, isCurrentNumber;
        char current;
        for(int i=0; i<form.length(); i++) {
            current = form.charAt(i);
            isCurrentNumber = isNumber(Character.toString(form.charAt(i)));
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
            if(!isCurrentNumber&& (currentOrder <= previousOrder)) {
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
    private double computeForm(String form) {
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
        if(isForm(this.data)) {
            // assign the result to value.
            this.value = computeForm(this.data.substring(1));
            this.data = Double.toString(value);
        }
    }
    public String getData() {
        return data;
    }

    public double getValue() {
        return this.value;
    }
}
