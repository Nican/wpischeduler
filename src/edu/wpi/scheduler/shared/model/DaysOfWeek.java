package edu.wpi.scheduler.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum DaysOfWeek implements IsSerializable {
	
	SUNDAY("sun","Sunday"),
	MONDAY("mon","Monday"),
	TUESDAY("tue","Tuesday"),
	WEDNESDAY("wed","Wednesday"),
	THURSDAY("thu","Thursday"),
	FRIDAY("fri", "Friday"),
	SATURDAY("sat","Saturday");
	
	private final String shortName;
	private final String name;
	
	DaysOfWeek(String shortName, String name ){
		this.shortName = shortName;
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public String getShortName(){
		return shortName;
	}
	
	public static DaysOfWeek getByShortName(String day){
		for( DaysOfWeek dayOfWeek : DaysOfWeek.values() ){
			if( dayOfWeek.getShortName().equals(day) ){
				return dayOfWeek;
			}			
		}
		
		throw new IllegalStateException("Non-existent day of the week name: " + day );
	}
	
}
