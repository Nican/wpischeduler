package edu.wpi.scheduler.client.courseselection;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Element;

import edu.wpi.scheduler.client.controller.SectionProducer;
import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.client.controller.StudentScheduleEvent;
import edu.wpi.scheduler.client.controller.StudentScheduleEventHandler;
import edu.wpi.scheduler.shared.model.Course;
import edu.wpi.scheduler.shared.model.Term;

public class TermViewSelection extends TermView implements ClickHandler, StudentScheduleEventHandler {

	private StudentSchedule schedule;

	public TermViewSelection(Course course, StudentSchedule schedule) {
		super(course);
		this.schedule = schedule;

		this.addDomHandler(this, ClickEvent.getType());

	}

	public Element addTerm(Term term) {
		Element elem = super.addTerm(term);

		if (hasTerm(term))
			elem.getStyle().setCursor(Cursor.POINTER);

		return elem;
	}

	@Override
	protected void onLoad() {
		super.onLoad();

		schedule.addStudentScheduleHandler(this);
	}
	
	@Override
	protected void onUnload(){
		schedule.removeStudentScheduleHandler(this);
	}

	@Override
	protected void update(Term term, Element label) {
		SectionProducer producer = schedule.getSectionProducer(course);

		if (producer != null && hasTerm(term) && producer.isTermDenied(term)) {
			label.getStyle().setBackgroundColor("#FFBBBB");
		} else {
			super.update(term, label);
		}
	}

	@Override
	public void onClick(ClickEvent event) {
		Term term = terms.get(event.getNativeEvent().getEventTarget().cast());

		if (term == null) {
			System.out.println("Cound not find term");
			return;
		}

		SectionProducer producer = schedule.getSectionProducer(course);

		if (producer.isTermDenied(term))
			producer.removeDenyTerm(term);
		else
			producer.denyTerm(term);
	}

	@Override
	public void onCoursesChanged(StudentScheduleEvent studentScheduleEvent) {
		if (course.equals(studentScheduleEvent.getCourse()))
			update();
	}

}
