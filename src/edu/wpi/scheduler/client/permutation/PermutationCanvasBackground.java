package edu.wpi.scheduler.client.permutation;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;

public class PermutationCanvasBackground {
	
	public final Canvas canvasBackground;

	public PermutationCanvasBackground( PermutationController controller ){
		canvasBackground = Canvas.createIfSupported();

		canvasBackground.getElement().setAttribute("width", "150px");
		canvasBackground.getElement().setAttribute("height", "150px");
		Context2d context = canvasBackground.getContext2d();
		double heightPerHour = 150.0 / (controller.getEndHour() - controller.getStartHour());

		context.setLineWidth(1.0);

		for (double hour = controller.getStartHour(); hour <= controller.getEndHour(); hour += 1.0) {
			//Draw in the middle: http://stackoverflow.com/questions/9311428/draw-single-pixel-line-in-html5-canvas
			double yValue = Math.floor((hour - controller.getStartHour()) * heightPerHour) + 0.5;
			
			if( hour == 12.0 ){
				context.setStrokeStyle(CssColor.make(200, 200, 200));
			} else {
				context.setStrokeStyle(CssColor.make(230, 230, 230));
			}

			context.beginPath();
			context.moveTo(0.0, yValue);
			context.lineTo(150.0, yValue);
			context.stroke();
		}

		double weekDaySize = (double) controller.getValidDaysOfWeek().size();

		for (double i = 1.0; i < weekDaySize; i += 1.0) {
			context.beginPath();
			context.moveTo(i * (150 / weekDaySize), 0.0);
			context.lineTo(i * (150 / weekDaySize), 150.0);
			context.stroke();
		}
		
	}
	
}
