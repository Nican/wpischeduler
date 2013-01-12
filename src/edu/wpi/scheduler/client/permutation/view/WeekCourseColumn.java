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

	public WeekCourseColumn(PermutationController controller, SchedulePermutation permutation, DayOfWeek day, List<Term> terms) {
		this.setElement(DOM.createTD());

		this.controller = controller;
		this.permutation = permutation;
		this.day = day;
		this.allowedTerms = terms;

		this.getElement().getStyle().setVerticalAlign(VerticalAlign.TOP);
		body.getStyle().setPosition(Position.RELATIVE);

		DOM.appendChild(getElement(), body);
		
		this.createPeriods();
	}

	@Override
	protected void onLoad() {
		this.updatePeriods();
	}

	public void createPeriods() {
		this.clear();

		for (Section section : this.permutation.sections) {
			for (Period period : section.periods) {
				if (period.days.contains(this.day)){
					for (Term term : section.getTerms()){
						if( allowTerm( term ))
							addPeriod(period, term);
					}
				}
			}
		}
		
		this.updatePeriods();
	}

	public void updatePeriods() {
		for (Widget widget : this.getChildren()) {
			PeriodItem item = (PeriodItem) widget;
			Style periodStyle = item.getElement().getStyle();
			//double termId = item.term.ordinal();
			double height = (double) this.getElement().getClientHeight();

			periodStyle.setPosition(Position.ABSOLUTE);
			periodStyle.setTop(getTimeProgress(item.period.startTime) * height, Unit.PX);
			periodStyle.setHeight((getTimeProgress(item.period.endTime) - getTimeProgress(item.period.startTime)) * height, Unit.PX);
			periodStyle.setWidth(100.0, Unit.PCT);
			periodStyle.setLeft(0, Unit.PCT);
			periodStyle.setBackgroundColor(controller.getTermColor(item.term));
			periodStyle.setZIndex(item.term.ordinal());
		}
	}
	
	public boolean allowTerm( Term term ){
		if( allowedTerms == null )
			return true;
		return allowedTerms.contains(term);
	}

	private void addPeriod(Period period, Term term) {
		PeriodItem item = new PeriodItem(period, term);

		add(item, body);
	}

	private double getTimeProgress(Time time) {
		return (time.getValue() - controller.getStartHour()) / (controller.getEndHour() - controller.getStartHour());
	}

}
