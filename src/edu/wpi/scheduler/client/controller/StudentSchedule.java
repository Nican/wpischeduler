package edu.wpi.scheduler.client.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.controller.FavoriteEvent.FavoriteEventType;
import edu.wpi.scheduler.client.permutation.TimeRangeChangEventHandler;
import edu.wpi.scheduler.client.permutation.TimeRangeChangeEvent;
import edu.wpi.scheduler.client.storage.StorageStudentSchedule;
import edu.wpi.scheduler.shared.model.Course;
import edu.wpi.scheduler.shared.model.Period;
import edu.wpi.scheduler.shared.model.Section;

// FIXME should we put this in the model somewhere? It's confusing me that it's in the controller folder
public class StudentSchedule implements HasHandlers
{
	public final List<SectionProducer> sectionProducers = new ArrayList<SectionProducer>();

	/**
	 * Find out conflicts between sections
	 */
	public final ConflictController conflicts = new ConflictController();

	public final ArrayList<SchedulePermutation> favoritePermutations = new ArrayList<SchedulePermutation>();

	private HandlerManager handlerManager = new HandlerManager(this);
	
	public StudentTermTimes studentTermTimes = new StudentTermTimes(this);
	
	protected double startTime = 8.0;
	protected double endTime = 16.0;

	public StudentSchedule() {

	}

	public SectionProducer addCourse(Course course, Widget source) {
		for (SectionProducer producer : sectionProducers) {
			if (producer.getCourse().equals(course)) {
				throw new UnsupportedOperationException("The course it already in the list of producers!");
			}
		}

		if (sectionProducers.size() >= 18) {
			Window.alert("There is a hard-coded limit of 18 courses.");
			return null;
		}

		SectionProducer producer = new SectionProducer(this, course);

		conflicts.addCourse(course);
		sectionProducers.add(producer);
		
		courseUpdated(course, StudentScheduleEvents.ADD, source);

		return producer;
	}

	public SectionProducer getSectionProducer(Course course) {

		for (SectionProducer producer : sectionProducers) {
			if (producer.getCourse().equals(course))
				return producer;
		}

		return null;
	}

	public void removeCourse(Course course) {

		SectionProducer producer = getSectionProducer(course);

		if (course != null)
			sectionProducers.remove(producer);
		
		courseUpdated(course, StudentScheduleEvents.REMOVE, null);
	}

	/*
	 * public List<SchedulePermutation> getSchedulePermutations(){
	 * 
	 * SchedulePermutationController producer = new
	 * SchedulePermutationController(this); return producer.getPermutations(); }
	 */

	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	public HandlerRegistration addStudentScheduleHandler(StudentScheduleEventHandler handler) {
		return handlerManager.addHandler(StudentScheduleEvent.TYPE, handler);
	}

	public void removeStudentScheduleHandler(StudentScheduleEventHandler handler) {
		handlerManager.removeHandler(StudentScheduleEvent.TYPE, handler);
	}

	public HandlerRegistration addFavoriteHandler(FavoriteEventHandler handler) {
		return handlerManager.addHandler(FavoriteEvent.TYPE, handler);
	}

	public void removeFavoriteHandler(FavoriteEventHandler handler) {
		handlerManager.removeHandler(FavoriteEvent.TYPE, handler);
	}

	public boolean containsFavorite(SchedulePermutation permutation) {
		for (SchedulePermutation perm : favoritePermutations) {
			if (perm.equals(permutation))
				return true;
		}

		return false;
	}

	public void addFavorite(SchedulePermutation permutation) {
		if (containsFavorite(permutation))
			return;

		this.favoritePermutations.add(permutation);

		fireEvent(new FavoriteEvent(FavoriteEventType.ADD));
	}

	public void removeFavorite(SchedulePermutation permutation) {
		for (Iterator<SchedulePermutation> iter = favoritePermutations.iterator(); iter.hasNext();) {
			SchedulePermutation perm = iter.next();
			if (perm.equals(permutation)) {
				iter.remove();
				fireEvent(new FavoriteEvent(FavoriteEventType.REMOVE));
				return;
			}
		}
	}

	public void courseUpdated(Course course) {
		courseUpdated(course, StudentScheduleEvents.UPDATE, null);
	}
	
	public void courseUpdated(Course course, StudentScheduleEvents eventType, Widget source) {
		updateTimeRange();
		StudentScheduleEvent event = new StudentScheduleEvent(course, eventType);
		event.setWidgetSource(source);
		
		this.fireEvent(event);
		
		StorageStudentSchedule.saveSchedule(this);
	}
	

	public double getStartHour() {
		return startTime;
	}

	public double getEndHour() {
		return endTime;
	}
	
	public void setTimeRange(double startTime, double endTime) {

		startTime = Math.floor(startTime);
		endTime = Math.ceil(endTime);

		if (this.startTime == startTime && this.endTime == endTime)
			return;

		this.startTime = startTime;
		this.endTime = endTime;

		this.fireEvent(new TimeRangeChangeEvent());
	}

	private void updateTimeRange() {
		double startTime = 10.0;
		double endTime = 16.0;

		for (SectionProducer producer : sectionProducers) {
			for (Section section : producer.getCourse().sections) {
				for (Period period : section.periods) {
					startTime = Math.min(period.startTime.getValue(), startTime);
					endTime = Math.max(period.endTime.getValue(), endTime);
				}
			}
		}

		setTimeRange(startTime, endTime);
	}
	
	public HandlerRegistration addTimeChangeListner(TimeRangeChangEventHandler handler) {
		return handlerManager.addHandler(TimeRangeChangeEvent.TYPE, handler);
	}

	public void removeTimeChangeListner(TimeRangeChangEventHandler handler) {
		handlerManager.removeHandler(TimeRangeChangeEvent.TYPE, handler);
	}

}
