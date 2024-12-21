
public class Cell {
    private String data;
    private double value;

    public Cell(String data) {
        this.data = data;
    }

    public static boolean isNumber(String text) {
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
        System.out.println(text);
        if(isNumber(text)) {
            ans=false;
        }
        if(text.startsWith("=")) {
            ans=false;
        }
        return ans;
    }

    public boolean isForm(String form) {
        boolean ans = true;
        String current;
        if(!form.startsWith("=")) { ans=false; return ans;}
        for(int i =1 ; i<form.length(); i++) {
            current = String.valueOf(form.charAt(i));
            if(!(isNumber(current) || isOperator(current) )) {
                ans=false;
                break;
            }
        }
        return ans;
    }
    private boolean isOperator(String opr) {
        String validOperators = "%*+-";
        return validOperators.contains(opr);
    }

    private double computeForm(String form) {
        return 0;
    }
    // a protection layer to computeForm, makes sure that the value that passed into the function is a formula
    public void calcForm(String form) {
        if(isForm(form)) {
            // assign the result to value.
            this.value = computeForm(form);
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
