package edu.wpi.scheduler.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Enumeration of all the days of the week
 */
public enum DayOfWeek implements IsSerializable {
	
	/** Static days of the week */
	SUNDAY("sun","Sunday"),
	MONDAY("mon","Monday"),
	TUESDAY("tue","Tuesday"),
	WEDNESDAY("wed","Wednesday"),
	THURSDAY("thu","Thursday"),
	FRIDAY("fri", "Friday"),
	SATURDAY("sat","Saturday");
	
	/** Easy to type name for maintaining sanity */
	private final String shortName;
	/** Full name of day */
	private final String name;
	
	/**
	 * Constructs a new day of the week; not public since days are static
	 * @param shortName easy to type name
	 * @param name full name of day
	 */
	DayOfWeek(String shortName, String name ){
		this.shortName = shortName;
		this.name = name;
	}
	
	/**
	 * Getter
	 * @return the full name of the day
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Getter
	 * @return the abbreviated name of the day
	 */
	public String getShortName(){
		return shortName;
	}
	
	/**
	 * Get the day of the week enum by its short name
	 * @param day short name of day; e.g. mon, tue
	 * @return the enum for that day; exception if it does not exist
	 */
	public static DayOfWeek getByShortName(String day){
		for( DayOfWeek dayOfWeek : DayOfWeek.values() ){
			if( dayOfWeek.getShortName().equals(day) ){
				return dayOfWeek;
			}			
		}
		throw new IllegalStateException("Non-existent day of the week name: " + day );
	}
	
	public static DayOfWeek getByName(String day){
		for( DayOfWeek dayOfWeek : DayOfWeek.values() ){
			if( dayOfWeek.getName().toLowerCase().equals(day.toLowerCase()) ){
				return dayOfWeek;
			}			
		}
		throw new IllegalStateException("Non-existent day of the week name: " + day );
	}
}
