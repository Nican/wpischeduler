package edu.wpi.scheduler.client.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.Scheduler;
import edu.wpi.scheduler.client.controller.FavoriteEvent.FavoriteEventType;
import edu.wpi.scheduler.shared.model.Course;
import edu.wpi.scheduler.shared.model.Department;
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
	
	public StudentTermTimes studentTermTimes = new StudentTermTimes();

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
		
		StudentScheduleEvent event = new StudentScheduleEvent(course, StudentScheduleEvents.ADD);
		event.setWidgetSource(source);

		this.fireEvent(event);
		saveSchedule();

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

		this.fireEvent(new StudentScheduleEvent(course, StudentScheduleEvents.REMOVE));
		saveSchedule();
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
		this.fireEvent(new StudentScheduleEvent(course, StudentScheduleEvents.UPDATE));
		saveSchedule();
	}

	private static class SectionProducerData extends JavaScriptObject {
		protected SectionProducerData() {
		}

		public final native void setDepartment(String dept) /*-{
			this.dept = dept;
		}-*/;

		public final native String getDepartment() /*-{
			return this.dept;
		}-*/;

		public final native void setName(String course) /*-{
			this.name = course;
		}-*/;

		public final native String getName() /*-{
			return this.name;
		}-*/;

		public final native void setSections(JsArrayString sections) /*-{
			this.sections = sections;
		}-*/;

		public final native JsArrayString getSections() /*-{
			return this.sections;
		}-*/;

		public final native boolean hasSections(String sectionName) /*-{
			return this.sections.indexOf(sectionName) != -1;
		}-*/;
	}

	private void saveSchedule() {
		JsArray<SectionProducerData> sections = JavaScriptObject.createArray().cast();

		for (SectionProducer producer : sectionProducers) {
			SectionProducerData data = JavaScriptObject.createObject().cast();
			JsArrayString arr = JavaScriptObject.createArray().cast();

			for (Section section : producer.deniedSections) {
				arr.push(section.number);
			}

			data.setDepartment(producer.getCourse().department.abbreviation);
			data.setName(producer.getCourse().number);
			data.setSections(arr);

			sections.push(data);
		}

		Storage localStorage = Storage.getLocalStorageIfSupported();

		if (localStorage != null) {
			JSONArray jsonArr = new JSONArray(sections);
			localStorage.setItem("savedCourse", jsonArr.toString());
		}
	}

	private Course getCourse(SectionProducerData data) {
		String department = data.getDepartment();
		String courseName = data.getName();

		for (Department dept : Scheduler.getDatabase().departments) {
			if (department.equals(dept.abbreviation)) {
				for (Course course : dept.courses) {
					if (courseName.equals(course.number))
						return course;
				}
			}
		}
		return null;

	}

	public void loadSchedule() {
		Storage localStorage = Storage.getLocalStorageIfSupported();

		if (localStorage == null)
			return;

		String depList = localStorage.getItem("savedCourse");

		if (depList == null)
			return;

		sectionProducers.clear();

		try {
			JsArray<SectionProducerData> sections = JsonUtils.unsafeEval(depList).cast();

			for (int i = 0; i < sections.length(); i++) {
				SectionProducerData data = sections.get(i);
				Course course = getCourse(data);

				if (course == null)
					continue;

				SectionProducer producer = addCourse(course, null);

				if (producer == null)
					continue;

				for (Section section : course.sections) {
					if (data.hasSections(section.number))
						producer.denySection(section);
				}
			}

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

	}
}
