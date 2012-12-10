package edu.wpi.scheduler.client.permutation;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;

import edu.wpi.scheduler.client.controller.SchedulePermutation;
import edu.wpi.scheduler.shared.model.Period;
import edu.wpi.scheduler.shared.model.Section;
import edu.wpi.scheduler.shared.model.Term;
import edu.wpi.scheduler.shared.model.Time;

public class PermutationCanvas {

	public final Canvas canvas;
	public final SchedulePermutation permutation;
	public final PermutationController controller;

	public PermutationCanvas(PermutationController permutationController, SchedulePermutation permutation) {
		this.controller = permutationController;
		this.permutation = permutation;
		this.canvas = Canvas.createIfSupported();

		this.setSize("150px", "150px");
		this.canvas.setStyleName("permutationCanvas");
	}

	public void setSize(String width, String height) {
		// this.canvas.setSize(width, height);
		setCanvasSize(this.canvas, width, height);
	}

	public void paint(Canvas background) {
		Context2d context = canvas.getContext2d();

		double columnWidth = ((double) canvas.getCoordinateSpaceWidth()) / controller.getValidDaysOfWeek().size();
		double hourHeight = ((double) canvas.getCoordinateSpaceHeight()) / (controller.getEndHour() - controller.getStartHour());

		// context.setFillStyle("black");
		context.drawImage(background.getCanvasElement(), 0.0, 0.0);

		for (Section section : this.permutation.sections) {
			for (Term term : section.getTerms()) {
				context.setFillStyle(controller.getTermColor(term));

				for (int i = 0; i < controller.getValidDaysOfWeek().size(); i++) {
					for (Period period : section.periods) {
						if (period.days.contains(controller.getValidDaysOfWeek().get(i))) {

							double start = getDayProgress(period.startTime);
							double end = getDayProgress(period.endTime);

							context.fillRect(
									i * columnWidth + columnWidth * term.ordinal() * 0.25,
									start * hourHeight,
									columnWidth * 0.25,
									(end - start) * hourHeight);
						}
					}
				}
			}
		}
	}

	private double getDayProgress(Time time) {
		return ((double) time.hour) - controller.getStartHour() + ((double) time.minutes) / 60;
	}

	public static void setCanvasSize(Canvas canvas, String width, String height) {
		canvas.getElement().setAttribute("width", width);
		canvas.getElement().setAttribute("height", height);
	}
}
