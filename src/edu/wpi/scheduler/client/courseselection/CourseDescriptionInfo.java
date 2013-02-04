package edu.wpi.scheduler.client.courseselection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.CourseDescription;
import edu.wpi.scheduler.client.Scheduler;
import edu.wpi.scheduler.shared.model.Course;

/**
 * Displays description about the course, so the user knows what he is getting
 * into
 * 
 * @author Nican
 * 
 */
public class CourseDescriptionInfo extends Composite {

	private static CourseDescriptionUiBinder uiBinder = GWT
			.create(CourseDescriptionUiBinder.class);

	interface CourseDescriptionUiBinder extends
			UiBinder<Widget, CourseDescriptionInfo> {
	}
	
	@UiField
	DivElement title;
	
	@UiField
	DivElement description;

	public CourseDescriptionInfo() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setCourse(Course course) {
		CourseDescription info = Scheduler.getDescription().getDescription(course);

		if (info != null) {
			description.setInnerText(info.getDescription());
		} else {
			description.setInnerText("");
		}
		
		title.setInnerText(course.name);
	}

}
