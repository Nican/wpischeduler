package edu.wpi.scheduler.client.permutation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.controller.SchedulePermutation;
import edu.wpi.scheduler.client.controller.StudentSchedule;

public class PermutationChooserView extends Composite {

	private static PermutationChooserViewUiBinder uiBinder = GWT
			.create(PermutationChooserViewUiBinder.class);

	interface PermutationChooserViewUiBinder extends
			UiBinder<Widget, PermutationChooserView> {
	}

	
	@UiField
	public VerticalPanel thumbList;
	
	boolean initalized = false;

	private final StudentSchedule studentSchedule;

	public PermutationChooserView(StudentSchedule studentSchedule) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.studentSchedule = studentSchedule;
		
		getElement().getStyle().setLeft(0, Unit.PX);
		getElement().getStyle().setRight(0, Unit.PX);
		getElement().getStyle().setTop(0, Unit.PX);
		getElement().getStyle().setBottom(0, Unit.PX);
		getElement().getStyle().setPosition(Position.ABSOLUTE);
	}

	
	
	public void updatePermutations(){
		
		if( initalized == true ){
			return;
		}
		
		initalized = true;
		
		for( SchedulePermutation permutation : studentSchedule.getSchedulePermutations() ){
			PermutationCanvas canvas = new PermutationCanvas(permutation);
			canvas.setSize("150px", "150px");
			canvas.paint();
			
			thumbList.add(canvas.canvas);
		}
		
		
	}
}
