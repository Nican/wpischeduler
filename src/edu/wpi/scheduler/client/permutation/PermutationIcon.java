package edu.wpi.scheduler.client.permutation;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.user.client.ui.SimplePanel;

import edu.wpi.scheduler.client.controller.SchedulePermutation;

public class PermutationIcon extends SimplePanel {

	protected final Canvas canvas = Canvas.createIfSupported();
	protected final SchedulePermutation permutation;

	public PermutationIcon(SchedulePermutation permutation) {		
		this.permutation = permutation;
		setWidget(canvas);
		
		canvas.setSize("150px", "100px");
	}


}
