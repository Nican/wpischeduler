package edu.wpi.scheduler.client.timechooser;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.controller.StudentSchedule;

public class TimeChooserView extends Composite {

	private static TimeChooserViewUiBinder uiBinder = GWT
			.create(TimeChooserViewUiBinder.class);

	interface TimeChooserViewUiBinder extends UiBinder<Widget, TimeChooserView> {
	}
	
	@UiField
	SimplePanel canvasPanel;
	Canvas canvas;
	int dragX = -1;
	int dragY = -1;
	int dropX = -1;
	int dropY = -1;
	
	public TimeChooserView(StudentSchedule studentSchedule) {
		
		initWidget(uiBinder.createAndBindUi(this));
		
		canvas = Canvas.createIfSupported();
		if(canvas == null){
			System.err.println("null canvas");
		}
		
		canvas.addMouseDownHandler(new MouseDownHandler() {	
			@Override
			public void onMouseDown(MouseDownEvent event) {
				dragX = event.getX();
				dragY = event.getY();
			}
		});
		canvas.addMouseUpHandler(new MouseUpHandler() {	
			@Override
			public void onMouseUp(MouseUpEvent event) {
				dropX = event.getX();
				dropY = event.getY();
				if(dragX > 0 && dragY > 0 && dropX > 0 && dropY > 0){
					canvas.getContext2d().setStrokeStyle("red");
					canvas.getContext2d().fillRect(dragX, dragY, (dropX-dragX), (dropY-dragY));
				}
			}
		});
		canvas.addMouseOutHandler(new MouseOutHandler() {	
			@Override
			public void onMouseOut(MouseOutEvent event) {
				dragX = -1;
				dragY = -1;
				dropX = -1;
				dropY = -1;
			}
		});
		
		canvasPanel.add(canvas);
		canvas.getContext2d().setStrokeStyle("red");
		canvas.getContext2d().strokeRect(0, 0, canvas.getCoordinateSpaceWidth(), canvas.getCoordinateSpaceHeight());
	}

}
