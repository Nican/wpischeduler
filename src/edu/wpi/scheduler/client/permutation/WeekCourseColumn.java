package edu.wpi.scheduler.client.permutation;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Label;

import edu.wpi.scheduler.client.controller.SchedulePermutation;
import edu.wpi.scheduler.shared.model.DayOfWeek;
import edu.wpi.scheduler.shared.model.Period;
import edu.wpi.scheduler.shared.model.Section;
import edu.wpi.scheduler.shared.model.Term;
import edu.wpi.scheduler.shared.model.Time;

public class WeekCourseColumn extends ComplexPanel {

	private SchedulePermutation permutation;
	private DayOfWeek day;
	private int startHour = 8;
	private int endHour = 16;

	public WeekCourseColumn(SchedulePermutation permutation, DayOfWeek day) {
		this.setElement(DOM.createDiv());

		this.permutation = permutation;
		this.day = day;

		this.getElement().getStyle().setHeight(100.0, Unit.PCT);
		this.getElement().getStyle().setPosition(Position.RELATIVE);

		Label title = new Label(day.name());
		title.getElement().getStyle().setTextAlign(TextAlign.CENTER);

		this.add(title, getElement());
	}

	@Override
	protected void onLoad() {
		this.clear();

		for (Section section : this.permutation.sections) {
			for (Period period : section.periods) {

				if (period.days.contains(this.day))
					addPeriod(period);

			}
		}

	}

	private void addPeriod(Period period) {

		PeriodItem item = new PeriodItem(period.professor);
		Term term = period.section.getTerms().get(0);
		double termId = term.ordinal();

		Style periodStyle = item.getElement().getStyle();
		double height = (double) this.getElement().getClientHeight();

		periodStyle.setPosition(Position.ABSOLUTE);
		periodStyle.setTop(getTimeProgress(period.startTime) * height + termId * 5, Unit.PX);
		periodStyle.setHeight((getTimeProgress(period.endTime) - getTimeProgress(period.startTime)) * height, Unit.PX);
		periodStyle.setWidth(100.0 - termId * 10.0, Unit.PCT);
		periodStyle.setLeft(termId * 10, Unit.PCT);
		periodStyle.setBackgroundColor(getTermColor(term));

		add(item, getElement());
	}

	private double getTimeProgress(Time time) {
		double start = (double) (time.hour - startHour);

		return (start + ((double) time.minutes) / 60.0) / (endHour - startHour);
	}

	private String getTermColor(Term term) {
		switch (term) {
		case A:
			return "#FFFFFF";
		case B:
			return "#EEFFFF";
		case C:
			return "#FFEEFF";
		case D:
			return "#FFFFEE";
		default:
			return "#EEFFEE";
		}
	}

}
