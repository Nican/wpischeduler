package edu.wpi.scheduler.client.permutation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

import edu.wpi.scheduler.client.controller.SchedulePermutation;
import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.shared.model.DayOfWeek;
import edu.wpi.scheduler.shared.model.Period;
import edu.wpi.scheduler.shared.model.Section;
import edu.wpi.scheduler.shared.model.Term;

public class PermutationController implements HasHandlers {

	private HandlerManager handlerManager = new HandlerManager(this);

	private final StudentSchedule studentSchedule;

	protected double startTime = 8.0;
	protected double endTime = 16.0;

	protected List<DayOfWeek> validDayOfWeek =
			Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY);

	protected SchedulePermutation selectedPermutation;

	public final Map<Term, String> termColor = new HashMap<Term, String>();

	public PermutationController(StudentSchedule studentSchedule) {
		this.studentSchedule = studentSchedule;

		termColor.put(Term.A, "rgb(154, 156, 255)");
		termColor.put(Term.B, "rgb(179, 220, 108)");
		termColor.put(Term.C, "rgb(255, 173, 70)");
		termColor.put(Term.D, "rgb(202, 189, 191)");
	}
	
	public String getTermColor(Term term){
		return termColor.get(term);
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	public StudentSchedule getStudentSchedule() {
		return studentSchedule;
	}

	public HandlerRegistration addTimeChangeListner(TimeRangeChangEventHandler handler) {
		return handlerManager.addHandler(TimeRangeChangeEvent.TYPE, handler);
	}

	public void removeTimeChangeListner(TimeRangeChangEventHandler handler) {
		handlerManager.removeHandler(TimeRangeChangeEvent.TYPE, handler);
	}

	public HandlerRegistration addSelectListner(PermutationSelectEventHandler handler) {
		return handlerManager.addHandler(PermutationSelectEvent.TYPE, handler);
	}

	public void removeSelectListner(PermutationSelectEventHandler handler) {
		handlerManager.removeHandler(PermutationSelectEvent.TYPE, handler);
	}

	public double getStartHour() {
		return startTime;
	}

	public double getEndHour() {
		return endTime;
	}

	public List<DayOfWeek> getValidDaysOfWeek() {
		return validDayOfWeek;
	}

	public void setTimeRange(double startTime, double endTime) {
		
		startTime = Math.floor(startTime);
		endTime = Math.ceil(endTime);

		if (this.startTime == startTime && this.endTime == endTime)
			return;

		this.startTime = startTime;
		this.endTime = endTime;

		this.fireEvent(new TimeRangeChangeEvent());
	}

	public void updateTimeRange(List<SchedulePermutation> permutations) {
		double startTime = 10.0;
		double endTime = 16.0;

		for (SchedulePermutation permutation : permutations) {
			for (Section section : permutation.sections) {
				for (Period period : section.periods) {

					startTime = Math.min(period.startTime.getValue(), startTime);
					endTime = Math.max(period.endTime.getValue(), endTime);

				}

			}

		}

		setTimeRange(startTime, endTime);
	}

	public void selectPermutation(SchedulePermutation permutation) {

		if (this.selectedPermutation == permutation)
			return;

		this.selectedPermutation = permutation;

		this.fireEvent(new PermutationSelectEvent());
	}
}
