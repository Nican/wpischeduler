package edu.wpi.scheduler.client.courseselection;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.shared.model.Course;
import edu.wpi.scheduler.shared.model.Section;
import edu.wpi.scheduler.shared.model.Term;

public class TermView extends HorizontalPanel {

	private Course course;
	private StudentSchedule schedule;

	public TermView( Course course, StudentSchedule schedule ) {
		this.course = course;
		this.schedule = schedule;
		
		//this.add(new Label("A"));
		//this.add(new Label("B"));
		//this.add(new Label("C"));
		//this.add(new Label("D"));
		for( Term term : Term.values() )
			this.addTerm(term);

		DOM.setElementProperty(getTable(), "cellSpacing", "2");

		this.setStyleName("termView");
	}
	
	public Label addTerm( Term term ){
		
		boolean hasTerm = false;
		Label label = new Label( term.name );
		
		for( Section section : course.sections ){
			if( section.term.contains(term.name + " Term") ){
				hasTerm = true;
				break;
			}
		}
		
		this.add( label );
		
		if( !hasTerm ){
			label.getElement().getStyle().setOpacity(0.4);
		} else {
			label.getElement().getParentElement().getStyle().setBackgroundColor("#DFFFDF");
		}
		
		return label;
	}

	public Label getTermLabe(String title) {
		Label label = new Label(title);

		return label;
	}
	
	public void hasTerm( String term ){
		
	}
}
