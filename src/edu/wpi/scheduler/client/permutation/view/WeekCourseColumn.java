package edu.wpi.scheduler.client.permutation.view;

import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.controller.SchedulePermutation;
import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.client.permutation.PermutationController;
import edu.wpi.scheduler.client.permutation.PermutationSelectEvent;
import edu.wpi.scheduler.client.permutation.PermutationSelectEventHandler;
import edu.wpi.scheduler.shared.model.DayOfWeek;
import edu.wpi.scheduler.shared.model.Period;
import edu.wpi.scheduler.shared.model.Section;
import edu.wpi.scheduler.shared.model.Term;
import edu.wpi.scheduler.shared.model.Time;

public class WeekCourseColumn extends ComplexPanel implements PermutationSelectEventHandler {
	private DayOfWeek day;
	private PermutationController controller;
	private Element body = DOM.createDiv();
	private List<Term> allowedTerms = null;

	public WeekCourseColumn(PermutationController controller, DayOfWeek day, List<Term> terms) {
		this.setElement(Document.get().createTDElement());

		this.controller = controller;
		this.day = day;
		this.allowedTerms = terms;

		this.getElement().getStyle().setVerticalAlign(VerticalAlign.TOP);
		body.getStyle().setPosition(Position.RELATIVE);

		getElement().appendChild(body);
	}

	@Override
	protected void onLoad() {
		createPeriods();
		controller.addSelectListner(this);
	}
	
	@Override
	protected void onUnload() {
		controller.removeSelectListner(this);
	}

	public void createPeriods() {
		this.clear();
		
		if( controller.getSelectedSection() != null )
			addSection(controller.getSelectedSection());
		
		SchedulePermutation permutation = controller.getSelectedPermutation();
		
		if( permutation != null ){
			for (Section section : permutation.sections) {
				if( controller.getSelectedSection() != section )
					addSection(section);
			}
		}
		
		this.updatePeriods();
	}
	
	private void addSection( Section section ){
		for (Period period : section.periods) {
			if (period.days.contains(this.day)){
				for (Term term : section.getTerms()){
					if( allowTerm( term ))
						addPeriod(period, term);
				}
			}
		}
	}

	public void updatePeriods() {
		if(!isAttached())
			return;
		
		double height = (double) getElement().getClientHeight();
		Section selectedSection = controller.getSelectedSection();
		
		for (Widget widget : this.getChildren()) {
			PeriodItem item = (PeriodItem) widget;
			Style periodStyle = item.getElement().getStyle();

			periodStyle.setPosition(Position.ABSOLUTE);
			periodStyle.setTop(timeProgress(item.period.startTime) * height, Unit.PX);
			item.setHeight((timeProgress(item.period.endTime) - timeProgress(item.period.startTime)) * height);
			periodStyle.setBackgroundColor(controller.getCourseColor(item.period.section.course));
			
			if( selectedSection == null || item.period.section.equals(selectedSection) ){
				periodStyle.setOpacity(1.0f);
				periodStyle.setZIndex(2);
			} else {
				periodStyle.setOpacity(0.5f);
				periodStyle.setZIndex(0);
			}
		}
	}
	
	public boolean allowTerm( Term term ){
		return allowedTerms.contains(term);
	}

	private PeriodItem addPeriod(Period period, Term term) {
		PeriodItem item = new PeriodItem(controller, period, term);

		add(item, body);
		
		return item;
	}

	private double timeProgress(Time time) {
		StudentSchedule schedule = controller.getStudentSchedule();
		return (time.getValue() - schedule.getStartHour()) / (schedule.getEndHour() - schedule.getStartHour());
	}

	@Override
	public void onPermutationSelected(PermutationSelectEvent permutation) {
		createPeriods();
	}

}
