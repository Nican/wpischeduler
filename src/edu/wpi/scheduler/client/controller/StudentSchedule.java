package edu.wpi.scheduler.client.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

import edu.wpi.scheduler.shared.model.Course;
import edu.wpi.scheduler.shared.model.DayOfWeek;
import edu.wpi.scheduler.shared.model.Time;

// FIXME should we put this in the model somewhere? It's confusing me that it's in the controller folder
public class StudentSchedule implements HasHandlers 
{
	public final List<SectionProducer> sectionProducers = new ArrayList<SectionProducer>();
	private HandlerManager handlerManager = new HandlerManager(this);

	// Time constants
	final static DayOfWeek[] week = {DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY};
	final static int START_DAY = 1; // Monday
	final static int START_HOUR = 8;
	final static int START_MIN = 0;
	public final static int GRIDS_PER_HOUR = 2;
	public final static int NUM_DAYS = 5;
	public final static int NUM_HOURS = 10;
	// List of student's chosen times from TimeTab
	private final HashMap<DayOfWeek, List<Time>> termTimes = new HashMap<DayOfWeek, List<Time>>();

	public StudentSchedule()
	{
		for(int i = START_DAY; i <= NUM_DAYS; i++)
		{
			termTimes.put(week[i], new ArrayList<Time>());
		}
	}

	// Chosen Times stuff
	private DayOfWeek gridToDayOfWeek(int j)
	{
		int index = START_DAY + j;
		// j = 9
		while(index >= week.length)
		{
			index -= week.length;
			j -= week.length;
		}
		return week[START_DAY + j];
	}
	private Time gridToTime(int i)
	{
		Time calculatedTime = new Time(START_HOUR, START_MIN);
		calculatedTime.increment(0, (60/GRIDS_PER_HOUR) * i);
		return calculatedTime;
	}
	public void selectTime(int i, int j)
	{
		Time selectedTime = gridToTime(i);
		DayOfWeek selectedDay = gridToDayOfWeek(j);
		List<Time> dayTimes = termTimes.get(selectedDay);
		//boolean isSelected = dayTimes.contains(selectedTime);
		//System.out.println(">>>select: " + selectedDay + " @ " + selectedTime.toString());
		//System.out.println(">>>isSelected: " + isSelected);
		if(!dayTimes.contains(selectedTime)){
			dayTimes.add(selectedTime);
		}
	}
	public void deselectTime(int i, int j)
	{
		Time deselectedTime = gridToTime(i);
		DayOfWeek deselectedDay = gridToDayOfWeek(j);
		List<Time> dayTimes = termTimes.get(deselectedDay);
		//boolean isSelected = dayTimes.contains(deselectedTime);
		//System.out.println(">>>select: " + deselectedDay + " @ " + deselectedTime.toString());
		//System.out.println(">>>isSelected: " + isSelected);
		if(dayTimes.contains(deselectedTime)){
			dayTimes.remove(deselectedTime);
		}
	}
	public boolean isTimeSelected(int i, int j)
	{
		Time queriedTime = gridToTime(i);
		DayOfWeek queriedDay = gridToDayOfWeek(j);
		List<Time> dayTimes = termTimes.get(queriedDay);
		return dayTimes.contains(queriedTime);
	}
	public void printTimes()
	{
		for(int i=START_DAY; i<=NUM_DAYS; i++){
			DayOfWeek day = week[i];
			System.out.print(day.toString());
			System.out.print(": ");
			List<Time> dayTimes = termTimes.get(day);
			for(int j=0; j<dayTimes.size(); j++){
				System.out.print(dayTimes.get(j).toString());
				System.out.print(", ");
			}
			System.out.print("\n");
		}
	}

	// Chosen classes / section stuff
	public SectionProducer addCourse( Course course ){
		for( SectionProducer producer : sectionProducers ){
			if( producer.getCourse().equals(course)){
				throw new UnsupportedOperationException("The course it already in the list of producers!");
			}
		}

		SectionProducer producer = new SectionProducer(this, course);

		sectionProducers.add(producer);

		this.fireEvent(new StudentScheduleEvent(course, StudentScheduleEvents.ADD));

		return producer;
	}

	public SectionProducer getSectionProducer(Course course){

		for( SectionProducer producer : sectionProducers ){
			if( producer.getCourse().equals(course))
				return producer;
		}

		return null;
	}

	public void removeCourse( Course course ){

		SectionProducer producer = getSectionProducer(course);

		if( course != null )
			sectionProducers.remove( producer );

		this.fireEvent(new StudentScheduleEvent(course, StudentScheduleEvents.REMOVE));
	}
	/*
	public List<SchedulePermutation> getSchedulePermutations(){

		SchedulePermutationController producer = new SchedulePermutationController(this);
		return producer.getPermutations();
	}
	 */

	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	public HandlerRegistration addStudentScheduleHandler( StudentScheduleEventHandler handler ){
		return handlerManager.addHandler(StudentScheduleEvent.TYPE, handler);
	}

	public void removeStudentScheduleHandler( StudentScheduleEventHandler handler ){
		handlerManager.removeHandler(StudentScheduleEvent.TYPE, handler);
	}

	public void courseUpdated(Course course) {
		this.fireEvent(new StudentScheduleEvent(course, StudentScheduleEvents.UPDATE));
	}

}
