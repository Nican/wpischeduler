package edu.wpi.scheduler.client.permutation;

import com.google.gwt.event.shared.EventHandler;

public interface PermutationSelectEventHandler extends EventHandler {
	public void onPermutationSelected(PermutationSelectEvent permutation);
}
