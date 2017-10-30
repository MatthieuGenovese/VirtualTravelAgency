package travelcosts.date;


public class Date {
    private String day;
    private String month;
    private String year;

    public Date(){}

    public Date(String day, String month, String year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }


    public String getDay() {
        return day;
    }


    public String getMonth() {
        return month;
    }


    public String getYear() {
        return year;
    }

    public Date(String date){
        String[] parts  = date.split("/");
        this.day = parts[0];
        this.month = parts[1];
        this.year = parts[2];
    }


}


