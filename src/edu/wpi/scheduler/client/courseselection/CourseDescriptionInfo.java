package edu.wpi.scheduler.client.courseselection;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.CourseDescription;
import edu.wpi.scheduler.client.Scheduler;
import edu.wpi.scheduler.shared.model.Course;
import edu.wpi.scheduler.shared.model.Period;
import edu.wpi.scheduler.shared.model.Section;

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
	
	@UiField
	SpanElement professorList;

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
		
		List<String> professors = new ArrayList<String>();
		
		for( Section section : course.sections ){
			for( Period period : section.periods ){
				if(!professors.contains(period.professor))
					professors.add(period.professor);
			}
		}
		
		if( professors.size() > 0 ){
			String names = professors.get(0);
			
			for( int i = 1; i < professors.size(); i++ ){
				names += ", " + professors.get(i);
			}
			
			professorList.setInnerHTML(names);
		} else {
			professorList.setInnerHTML("<i>N/A</i>");
		}
		
		title.setInnerText(course.name);
	}

}
