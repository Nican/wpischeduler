package edu.wpi.scheduler.client.courseselection;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Label;

import edu.wpi.scheduler.client.controller.SectionProducer;
import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.shared.model.Course;
import edu.wpi.scheduler.shared.model.Term;

public class TermViewSelection extends TermView implements ClickHandler {

	private StudentSchedule schedule;

	public TermViewSelection(Course course, StudentSchedule schedule) {
		super(course);
		this.schedule = schedule;
		
		addDomHandler(this, ClickEvent.getType());
	}
	
	@Override
	public Element addTerm(final Term term) {
		Element label = super.addTerm(term);
		
		if (hasTerm(term)) 
			label.getStyle().setCursor(Cursor.POINTER);
		
		/*
		label.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				SectionProducer producer = schedule.getSectionProducer(course) ;
				
				if(producer.isTermDenied(term))
					producer.removeDenyTerm(term);
				else
					producer.denyTerm(term);
				
				update();
			}
		});
		*/
		
		return label;
	}
	
	@Override
	protected void update(Term term, Label label) {		
		SectionProducer producer = schedule.getSectionProducer(course) ;
		
		if( producer.isTermDenied(term)){
			label.getElement().getParentElement().getStyle().setBackgroundColor("#FFDFDF");
		} else {
			super.update(term, label);
		}
	}

	@Override
	public void onClick(ClickEvent event) {
		System.out.println("I got clicked!" + event.getSource() );
	}

}
