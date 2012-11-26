package edu.wpi.scheduler.client.permutation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.controller.StudentSchedule;

public class PermutationChooserView extends Composite {

	private static PermutationChooserViewUiBinder uiBinder = GWT
			.create(PermutationChooserViewUiBinder.class);

	interface PermutationChooserViewUiBinder extends
			UiBinder<Widget, PermutationChooserView> {
	}



	private final StudentSchedule studentSchedule;

	public PermutationChooserView(StudentSchedule studentSchedule) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.studentSchedule = studentSchedule;
	}

	
	
	public void updatePermutations(){
		
		System.out.println("Found " + studentSchedule.getSchedulePermutations().size() + " permutations.");
		
	}
}
