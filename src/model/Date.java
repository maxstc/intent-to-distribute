package model;

public class Date {

	public int hour;
	public int day;
	public int month;
	public int year;
	
	public Date(int hour, int day, int month, int year) {
		this.hour = hour;
		this.day = day;
		this.month = month;
		this.year = year;
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
	
}
