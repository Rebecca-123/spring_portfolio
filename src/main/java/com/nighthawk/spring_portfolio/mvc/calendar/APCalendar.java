package com.nighthawk.spring_portfolio.mvc.calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;

// Prototype Implementation

public class APCalendar {

    /** Returns true if year is a leap year and false otherwise.
     * isLeapYear(2019) returns False
     * isLeapYear(2016) returns True
     */          
    public static boolean isLeapYear(int year) {
        if(year % 100 == 0){ // century year
            if(year % 400 == 0){
                return true;
            }
            return false; // divisible by 100, but not 400
        }
        else if(year % 4 == 0){ // other years --> divisible by 4
            return true;
        }
        return false;
    }

    /** Returns the number of leap years between year1 and year2, inclusive.
     * Precondition: 0 <= year1 <= year2
    */ 
    public static int numberOfLeapYears(int year1, int year2) {
        int count = 0;
        for(int i = year1; i <= year2; i++){
            if(isLeapYear(i)){
                count++;
            }
        }
        return count;
    }    
        
    /** Returns the value representing the day of the week 
     * 0 denotes Sunday, 
     * 1 denotes Monday, ..., 
     * 6 denotes Saturday. 
     * firstDayOfYear(2019) returns 2 for Tuesday.
    */
    private static int firstDayOfYear(int year) {
        return dayOfWeek(1, 1, year);
    }

    /** Returns the value representing the day of the week for the given date
     * Precondition: The date represented by month, day, year is a valid date.
    */
    public static int dayOfWeek(int month, int day, int year) { 
        // https://docs.oracle.com/javase/8/docs/api/java/time/LocalDate.html
            // LocalDate.of returns local date
        LocalDate date = LocalDate.of(year, month, day);
        DayOfWeek dayCalc = date.getDayOfWeek();
        // https://docs.oracle.com/javase/8/docs/api/java/time/DayOfWeek.html
            // DayOfWeek enum: 1-7 to represent M-S
        if(dayCalc.getValue() == 7){ // Change Sunday from 7 to 0
            return 0;
        }
        return dayCalc.getValue();
    }

    /** Returns n, where month, day, and year specify the nth day of the year.
     * This method accounts for whether year is a leap year. 
     * dayOfYear(1, 1, 2019) return 1
     * dayOfYear(3, 1, 2017) returns 60, since 2017 is not a leap year
     * dayOfYear(3, 1, 2016) returns 61, since 2016 is a leap year. 
    */ 
    private static int dayOfYear(int month, int day, int year) {
        // implementation not shown

        return 1;
    }

    /** Tester method */
    public static void main(String[] args) {
        System.out.println("firstDayOfYear - January 1st, 2022: " + APCalendar.firstDayOfYear(2022));
        System.out.println("dayOfYear: " + APCalendar.dayOfYear(1, 1, 2022));
        System.out.println("dayOfWeek: " + APCalendar.dayOfWeek(1, 1, 2022));
        System.out.println("dayOfWeek: " + APCalendar.dayOfWeek(1, 2, 2022));

        // Public access modifiers
        System.out.println("isLeapYear for 2022: " + APCalendar.isLeapYear(2022));
        System.out.println("isLeapYear for 2000: " + APCalendar.isLeapYear(2000));
        System.out.println("numberOfLeapYears between 1900 and 1999: " + APCalendar.numberOfLeapYears(1900, 1999));
    }

}