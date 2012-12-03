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

		for (Section section : this.permutation.sections) {
			for (Period period : section.periods) {

				if (period.days.contains(this.day))
					addPeriod(period);

			}
		}

		Label title = new Label(day.name());
		title.getElement().getStyle().setTextAlign(TextAlign.CENTER);

		this.add(title, getElement());
	}

	private void addPeriod(Period period) {

		PeriodItem item = new PeriodItem(period.professor);
		Term term = period.section.getTerms().get(0);

		Style periodStyle = item.getElement().getStyle();

		periodStyle.setPosition(Position.ABSOLUTE);
		periodStyle.setTop(((double) (period.startTime.hour - startHour)) / ((double) (endHour - startHour)) * 100.0, Unit.PCT);
		periodStyle.setHeight((getTimeProgress(period.endTime) - getTimeProgress(period.startTime)) * 20.0, Unit.PX);

		//periodStyle.setLeft(term.ordinal() * 10, Unit.PX);

		add(item, getElement());
	}

	private double getTimeProgress(Time time) {
		return ((double) time.hour) + ((double) time.minutes) / 60.0;
	}

}
