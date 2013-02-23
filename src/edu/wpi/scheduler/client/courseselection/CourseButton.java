package edu.wpi.scheduler.client.courseselection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Button;

import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.client.controller.StudentScheduleEvent;
import edu.wpi.scheduler.client.controller.StudentScheduleEventHandler;
import edu.wpi.scheduler.shared.model.Course;

public class CourseButton extends Button implements StudentScheduleEventHandler, ClickHandler {

	public static final SelectionResources resources = GWT
			.create(SelectionResources.class);

	private StudentSchedule schedule;
	private Course course;

	public CourseButton(StudentSchedule schedule, Course course) {
		this.schedule = schedule;
		this.course = course;

		this.addClickHandler(this);
	}

	/**
	 * Add listeners when the object is added to the document/dom tree
	 */
	@Override
	protected void onLoad() {
		schedule.addStudentScheduleHandler(this);
		this.updateIcon();
	}

	@Override
	protected void onUnload() {
		schedule.removeStudentScheduleHandler(this);
	}

	public void updateIcon() {
		//getUpFace().setHTML();
		setHTML(AbstractImagePrototype.create(getIcon()).getSafeHtml());
	}

	public ImageResource getIcon() {
		if (schedule.getSectionProducer(course) != null)
			return resources.removeIcon();
		return resources.addIcon();
	}

	@Override
	public void onCoursesChanged(StudentScheduleEvent studentScheduleEvent) {
		this.updateIcon();
	}

	@Override
	public void onClick(ClickEvent event) {
		if (schedule.getSectionProducer(course) != null)
			schedule.removeCourse(course);
		else
			schedule.addCourse(course);
	}
}
