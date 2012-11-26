package edu.wpi.scheduler.client.permutation;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.FillStrokeStyle;

import edu.wpi.scheduler.client.controller.SchedulePermutation;
import edu.wpi.scheduler.shared.model.DayOfWeek;
import edu.wpi.scheduler.shared.model.Period;
import edu.wpi.scheduler.shared.model.Section;
import edu.wpi.scheduler.shared.model.Time;

public class PermutationCanvas {

	public final Canvas canvas;
	public final SchedulePermutation permutation;

	public PermutationCanvas(SchedulePermutation permutation) {
		this.permutation = permutation;
		this.canvas = Canvas.createIfSupported();

		this.setSize("150px", "150px");
	}

	public void setSize(String width, String height) {
		this.canvas.setSize(width, height);
		this.paint();
	}

	public void paint() {
		int total = DayOfWeek.values().length;
		Context2d context = canvas.getContext2d();

		double columnWidth = 150.0 / ((double) total);
		double hourHeight = 150.0 / 24.0;
		
		context.setFillStyle("black");

		for (Section section : this.permutation.sections) {
			for (DayOfWeek day : DayOfWeek.values()) {
				for (Period period : section.periods) {
					if (period.days.contains(day)) {
						double start = getDayProgress(period.startTime);
						double end = getDayProgress(period.endTime);

						System.out.println("Drawing: " + day.ordinal()
								* columnWidth + " " + start * hourHeight + " "
								+ columnWidth + " " + (end - start)
								* hourHeight);

						context.fillRect(day.ordinal() * columnWidth, start
								* hourHeight, columnWidth, (end - start)
								* hourHeight);

					}
				}
			}
		}

	}

	private static double getDayProgress(Time time) {
		return ((double) time.hour) + ((double) time.minutes) / 60;
	}

}
