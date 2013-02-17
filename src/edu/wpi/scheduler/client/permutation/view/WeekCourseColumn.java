package edu.wpi.scheduler.client.permutation.view;

import java.util.List;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.controller.SchedulePermutation;
import edu.wpi.scheduler.client.permutation.PermutationController;
import edu.wpi.scheduler.shared.model.DayOfWeek;
import edu.wpi.scheduler.shared.model.Period;
import edu.wpi.scheduler.shared.model.Section;
import edu.wpi.scheduler.shared.model.Term;
import edu.wpi.scheduler.shared.model.Time;

public class WeekCourseColumn extends ComplexPanel {

	private SchedulePermutation permutation;
	private DayOfWeek day;
	private PermutationController controller;
	private Element body = DOM.createDiv();
	private List<Term> allowedTerms = null;

	public WeekCourseColumn(PermutationController controller, DayOfWeek day, List<Term> terms) {
		this.setElement(DOM.createTD());

		this.controller = controller;
		this.day = day;
		this.allowedTerms = terms;

		this.getElement().getStyle().setVerticalAlign(VerticalAlign.TOP);
		body.getStyle().setPosition(Position.RELATIVE);

		getElement().appendChild(body);
	}

	@Override
	protected void onLoad() {
		this.updatePeriods();
	}
	
	public void setPermutation(SchedulePermutation permutation){
		this.permutation = permutation;
		createPeriods();
	}

	public void createPeriods() {
		this.clear();
		
		if( controller.getSelectedSection() != null )
			addSection(controller.getSelectedSection());
		
		if( this.permutation != null ){
			for (Section section : this.permutation.sections) {
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
			periodStyle.setTop(getTimeProgress(item.period.startTime) * height, Unit.PX);
			periodStyle.setHeight((getTimeProgress(item.period.endTime) - getTimeProgress(item.period.startTime)) * height, Unit.PX);
			periodStyle.setBackgroundColor(controller.getTermColor(item.term));
			//periodStyle.setZIndex(item.term.ordinal());
			
			if( selectedSection == null || item.period.section.equals(selectedSection) ){
				periodStyle.setOpacity(1.0f);				
			} else {
				periodStyle.setOpacity(0.5f);
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

	private double getTimeProgress(Time time) {
		return (time.getValue() - controller.getStartHour()) / (controller.getEndHour() - controller.getStartHour());
	}

}
