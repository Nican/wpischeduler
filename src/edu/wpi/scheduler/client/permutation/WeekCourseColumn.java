package edu.wpi.scheduler.client.permutation;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;

import edu.wpi.scheduler.client.controller.SchedulePermutation;
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

	public WeekCourseColumn(PermutationController controller, SchedulePermutation permutation, DayOfWeek day) {
		this.setElement(DOM.createTD());

		this.controller = controller;
		this.permutation = permutation;
		this.day = day;

		this.getElement().getStyle().setVerticalAlign(VerticalAlign.TOP);
		body.getStyle().setPosition(Position.RELATIVE);

		DOM.appendChild(getElement(), body);
	}

	@Override
	protected void onLoad() {
		this.updatePeriods();
	}

	public void updatePeriods() {
		// TODO: Do not delete everything! Just update their position...
		this.clear();

		for (Section section : this.permutation.sections) {
			for (Period period : section.periods) {

				if (period.days.contains(this.day))
					for (Term term : section.getTerms())
						addPeriod(period, term);

			}
		}
	}

	private void addPeriod(Period period, Term term) {

		PeriodItem item = new PeriodItem(period);
		double termId = term.ordinal();

		Style periodStyle = item.getElement().getStyle();
		double height = (double) this.getElement().getClientHeight();

		periodStyle.setPosition(Position.ABSOLUTE);
		periodStyle.setTop(getTimeProgress(period.startTime) * height + termId * 5.0, Unit.PX);
		periodStyle.setHeight((getTimeProgress(period.endTime) - getTimeProgress(period.startTime)) * height, Unit.PX);
		periodStyle.setWidth(100.0 - termId * 10.0, Unit.PCT);
		periodStyle.setLeft(termId * 10, Unit.PCT);
		periodStyle.setBackgroundColor(controller.getTermColor(term));
		periodStyle.setZIndex(term.ordinal());

		add(item, body);
	}

	private double getTimeProgress(Time time) {
		return (time.getValue() - controller.getStartHour()) / (controller.getEndHour() - controller.getStartHour());
	}

}
