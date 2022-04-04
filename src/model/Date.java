package model;

public class Date {

	public int hour;
	public int day;
	public int month;
	public int year;
	
	public int totalHour;
	
	public Date(int hour, int day, int month, int year) {
		this.hour = hour;
		this.day = day;
		this.month = month;
		this.year = year;
		
		totalHour = ((year * 12 + month) * 30 + day) * 24 + hour;
	}
	
	public void tick() {
		hour++;
		while (hour > 23) {
			hour = hour - 24;
			day++;
		}
		while (day > 29) {
			day = day - 30;
			month++;
		}
		while (month > 11) {
			month = month - 12;
			year++;
		}
	}
	
//	public static int getDifference(Date bigDate, Date smallDate) {
//		
//	}
	
}
