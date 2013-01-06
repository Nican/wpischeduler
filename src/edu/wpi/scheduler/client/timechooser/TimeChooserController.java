package edu.wpi.scheduler.client.timechooser;

import edu.wpi.scheduler.client.timechooser.TimeChooser;
public class TimeChooserController {
	
	TimeChooser view;
	
	TimeChooserController(TimeChooser v){
		this.view = v;
	}

	void timeChosen(double dragX, double dragY, double dropX, double dropY){
		if(dragX > 0 && dragY > 0 && dropX > 0 && dropY > 0){
			int width = view.width;
			int height = view.height;
			int i1 = (int) Math.floor((Math.min(dragY, dropY)/view.height) * view.numHours);
			int j1 = (int) Math.floor((Math.min(dragX, dropX)/width) * view.numDays);
			int i2 = (int) Math.ceil((Math.max(dragY, dropY)/height) * view.numHours);
			int j2 = (int) Math.ceil((Math.max(dragX, dropX)/width) * view.numDays);
			view.control.canvas.getContext2d().clearRect(0, 0, view.width, view.height);
			view.fill.fillTimes(i1, j1, i2, j2);
			view.control.canvas.getContext2d().drawImage(view.fill.canvas.getCanvasElement(), 0, 0);
			view.control.canvas.getContext2d().drawImage(view.grid.canvas.getCanvasElement(), 0, 0);
		}
	}
	
	void mouseDrag(double dragX, double dragY, double dropX, double dropY){
		if(dragX > 0 && dragY > 0 && dropX > 0 && dropY > 0){
			int i1 = (int) Math.min(dragY, dropY);
			int j1 = (int) Math.min(dragX, dropX);
			int i2 = (int) Math.max(dragY, dropY);
			int j2 = (int) Math.max(dragX, dropX);
			view.control.canvas.getContext2d().clearRect(0, 0, view.width, view.height);
			view.control.canvas.getContext2d().drawImage(view.fill.canvas.getCanvasElement(), 0, 0);
			view.control.canvas.getContext2d().drawImage(view.grid.canvas.getCanvasElement(), 0, 0);
			view.control.drawDrag(i1, j1, i2, j2);
		}
	}
	
	void mouseOut(){
		view.control.canvas.getContext2d().clearRect(0, 0, view.width, view.height);
		view.control.canvas.getContext2d().drawImage(view.fill.canvas.getCanvasElement(), 0, 0);
		view.control.canvas.getContext2d().drawImage(view.grid.canvas.getCanvasElement(), 0, 0);
	}
}