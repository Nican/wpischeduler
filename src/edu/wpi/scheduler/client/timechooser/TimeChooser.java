package edu.wpi.scheduler.client.timechooser;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class TimeChooser extends AbsolutePanel{
	
	final int numDays = 5;
	final int numHours = 10;
	final int width = 700;
	final int height = 700;
	
	Control control;
	Grid grid;
	Fill fill;
	
	public TimeChooser(){
		fill = new Fill(width, height, numDays, numHours);
		grid = new Grid(width, height, numDays, numHours);
		control = new Control(this, width, height, numDays, numHours);
		this.add(control.canvas);
	}
	
	
	

}
