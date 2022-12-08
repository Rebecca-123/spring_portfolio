package com.nighthawk.spring_portfolio.mvc.person;

public class StepTracker {
    // instance variables
    private int minSteps; // requirement to be considered active
    public int totalStepsTaken;
    public int daysTracked;
    public int activeDays;
    public int averageSteps;

    // constructor
    public StepTracker(int steps){
        this.minSteps = steps;
        this.totalStepsTaken = 0;
        this.daysTracked = 0;
        this.activeDays = 0;
    }
    public void addDailySteps(int dailySteps){
        this.totalStepsTaken += dailySteps;
        this.daysTracked += 1;
        if(dailySteps >= this.minSteps){
            this.activeDays += 1;
        }
    }
    public void averageSteps(){
        if(this.daysTracked == 0){
            this.averageSteps = 0;
        }
        else{
            this.averageSteps = this.totalStepsTaken / this.daysTracked;
        }
    }
    // additional method for PBL purposes
    public boolean isActive(int dailySteps){
        return dailySteps >= this.minSteps;
    }
}