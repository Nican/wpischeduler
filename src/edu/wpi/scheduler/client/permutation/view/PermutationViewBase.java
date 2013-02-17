package edu.wpi.scheduler.client.permutation.view;

import com.google.gwt.user.client.ui.CellPanel;

import edu.wpi.scheduler.client.controller.SchedulePermutation;
import edu.wpi.scheduler.client.permutation.PermutationController;
import edu.wpi.scheduler.client.permutation.PermutationSelectEvent;
import edu.wpi.scheduler.client.permutation.PermutationSelectEventHandler;

public abstract class PermutationViewBase extends CellPanel implements PermutationSelectEventHandler {
	final PermutationController controller;
	
	public PermutationViewBase(PermutationController controller) {
		this.controller = controller;
	}
	
	@Override
	protected void onLoad() {
		controller.addSelectListner(this);
		this.setPermutation(controller.getSelectedPermutation());
	}

	@Override
	protected void onUnload() {
		controller.removeSelectListner(this);
	}
	
	@Override
	public void onPermutationSelected(PermutationSelectEvent permutation){
		this.setPermutation(controller.getSelectedPermutation());
	}
	
	public abstract void setPermutation( SchedulePermutation permutation );
}
