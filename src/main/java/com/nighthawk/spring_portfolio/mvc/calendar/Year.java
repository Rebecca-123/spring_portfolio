package com.nighthawk.spring_portfolio.mvc.calendar;

/** Simple POJO 
 * Used to Interface with APCalendar
 * The toString method(s) prepares object for JSON serialization
 * Note... this is NOT an entity, just an abstraction
 */
class Year {
   private int year;
   private boolean isLeapYear;

   private int year1;
   private int year2;
   private int leapYearCount;

   private int month;
   private int day;
   private int dayOfWeek;

   // zero argument constructor
   public Year() {} 

   /* year getter/setters */
   public int getYear() {
      return year;
   }
   public void setYear(int year) {
      this.year = year;
      this.setIsLeapYear(year);
   }

   // leap year range
   public int getYear1(){
      return year1;
   }
   public int getYear2(){
      return year2;
   }
   public void setYears(int year1, int year2){
      this.year1 = year1;
      this.year2 = year2;
      this.setLeapYearCount(year1, year2);
   }

   // day of week
   public int getMonth(){
      return month;
   }
   public int getDay(){
      return day;
   }
   public void setTheDayOfWeek(int month, int day, int year){
      this.month = month;
      this.day = day;
      this.year = year;
      this.setDayOfWeek(month, day, year);
      this.setYear(year);
   }

   /* isLeapYear getter/setters */
   public boolean getIsLeapYear(int year) {
      return APCalendar.isLeapYear(year);
   }
   private void setIsLeapYear(int year) {  // this is private to avoid tampering
      this.isLeapYear = APCalendar.isLeapYear(year);
   }

   // leap year range
   public int getLeapYearCount(int year1, int year2) {
      return APCalendar.numberOfLeapYears(year1, year2);
   }
   private void setLeapYearCount(int year1, int year2) {
      this.leapYearCount = APCalendar.numberOfLeapYears(year1, year2);
   }

   // day of week
   public int getDayOfWeek(int month, int day, int year){
      return APCalendar.dayOfWeek(month, day, year);
   }
   private void setDayOfWeek(int month, int day, int year){
      this.dayOfWeek = APCalendar.dayOfWeek(month, day, year);
   }

   /* isLeapYearToString formatted to be mapped to JSON */
   public String isLeapYearToString(){
      return ( "{ \"year\": " + this.year + ", " + "\"isLeapYear\": " + this.isLeapYear + " }" );
   }	

   // leap year range
   public String leapYearCountToString(){
      return ( "{ \"year\": "  + this.year1 + this.year2 +  ", " + "\"isLeapYear\": "  + this.leapYearCount + " }" );
   }

   // day of week
   public String dayOfWeekToString(){
      return ( "{ \"Date\": "  + this.month + this.day +  this.year + ", " + "\"dayOfWeek\": "  + this.dayOfWeek + " }" );
   }

   public String randomToString(){
      return ("{ \"year\": " + this.year + ", " + "\"isLeapYear\": " + this.isLeapYear + ", " + "\"Date\": "  + this.month + this.day +  this.year + ", " + " \"dayOfWeek\": "  + this.dayOfWeek + " }");
   }

   /* standard toString placeholder until class is extended */
   public String toString() { 
      return isLeapYearToString(); 
   }

   public static void main(String[] args) {
      Year year = new Year();
      year.setYear(2022);
      System.out.println(year);
   }
}