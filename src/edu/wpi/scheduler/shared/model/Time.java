package edu.wpi.scheduler.shared.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Time implements Comparable<Time>, Serializable {

	public int hour;
	public int minutes;

	// @SuppressWarnings("unused")
	public Time() {
	}

	public Time(int hour, int minutes) {
		this.hour = hour;
		this.minutes = minutes;

		assertValidTime();
	}

	/**
	 * Constructs a time with input values such as "1:00PM", "8:00AM", "11:59AM"
	 * 
	 * @param time
	 */
	public Time(String time) {
		int length = time.length();
		boolean meridian = time.substring(length - 2).equals("AM");

		this.hour = Integer.valueOf(time.substring(0, time.indexOf(":")));
		this.minutes = Integer.valueOf(time.substring(length - 4, length - 2));

		// If we are dealing with PM, we have to transform 1PM into 13, but
		// leave 12PM into 12.
		if (!meridian && this.hour != 12) {
			this.hour += 12;
		}

		assertValidTime();
	}

	private void assertValidTime() {
		if (hour < 0 || hour >= 24 || minutes < 0 || minutes >= 60) {
			throw new IllegalArgumentException("Input times is not within bounds.");
		}
	}

	@Override
	public String toString() {
		String min = Integer.toString(minutes);

		if (min.length() == 1)
			min = "0" + min;

		// Remember: ... 10AM, 11AM, 12PM, 1PM, 2PM ...
		if (hour <= 12)
			return hour + ":" + min + (hour == 12 ? "PM" : "AM");

		return (hour - 12) + ":" + min + "PM";
	}

	public double getValue() {
		return ((double) this.hour) + ((double) this.minutes) / 60.0;
	}

	/**
	 * the value 0 if x == y; a value less than 0 if x < y (Earlier on the day);
	 * and a value greater than 0 if x > y (Later on the day)
	 */
	@Override
	public int compareTo(Time o) {
		if (this.hour == o.hour) {
			if (this.minutes == o.minutes)
				return 0;

			if (this.minutes < o.minutes)
				return -1;
		}

		return this.hour < o.hour ? -1 : 1;
	}

}
