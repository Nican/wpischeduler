package edu.wpi.scheduler.client.timechooser;

import edu.wpi.scheduler.client.timechooser.TimeChooser;
public class TimeChooserController {
	
	TimeChooser view;
	
	TimeChooserController(TimeChooser v){
		this.view = v;
	}

	void timeChosen(double dragX, double dragY, double dropX, double dropY){
		if(dragX > 0 && dragY > 0 && dropX > 0 && dropY > 0){
			int width = view.canvas.getCoordinateSpaceWidth();
			int height = view.canvas.getCoordinateSpaceHeight();
			int i1 = (int) Math.floor((Math.min(dragY, dropY)/height) * view.numHours);
			int j1 = (int) Math.floor((Math.min(dragX, dropX)/width) * view.numDays);
			int i2 = (int) Math.ceil((Math.max(dragY, dropY)/height) * view.numHours);
			int j2 = (int) Math.ceil((Math.max(dragX, dropX)/width) * view.numDays);
			view.fillTimes(i1, j1, i2, j2);
			view.drawGrid(i1, j1, i2, j2);
		}
	}
}
