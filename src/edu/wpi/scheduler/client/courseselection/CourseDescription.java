package edu.wpi.scheduler.client.courseselection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.shared.model.Course;

/**
 * Displays description about the course, so the user knows what he is getting into
 * @author Nican
 *
 */
public class CourseDescription extends Composite {

	private static CourseDescriptionUiBinder uiBinder = GWT
			.create(CourseDescriptionUiBinder.class);

	interface CourseDescriptionUiBinder extends
			UiBinder<Widget, CourseDescription> {
	}

	public CourseDescription() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setCourse( Course course ){
		this.getElement().setInnerHTML("<b>Course: </b>" + course.name );
		
	}

}
