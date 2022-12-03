package com.nighthawk.spring_portfolio.mvc.calculator;

import java.util.ArrayList;

public class Delimiters {
    private String openDel;
    private String closeDel;

    public Delimiters(String openDel, String closeDel){
        this.openDel = openDel;
        this.closeDel = closeDel;
    }

    public ArrayList<String> getDelimitersList(String[] tokens){
        ArrayList<String> delims = new ArrayList<String>(); // store delimiters in tokens
        for(String token: tokens){
            if(token.equals(this.openDel) || token.equals(this.closeDel)){ // check if delimeter
                delims.add(token);
            }
        }
        return delims;
    }

    public boolean isBalanced(ArrayList<String> delimiters){
        int openCount = 0;
        int closeCount = 0;
        for(String delim: delimiters){
            if(delim.equals(this.openDel)){
                openCount++;
            }
            else{
                closeCount++;
                if(openCount >= closeCount){
                    continue;
                }
                else{
                    return false;
                }
            }
        }
        if(openCount == closeCount){
            return true;
        }
        return false;
    }
}
