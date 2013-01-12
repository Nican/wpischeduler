package edu.wpi.scheduler.client.controller;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;

import edu.wpi.scheduler.client.controller.SchedulePermutationController.PermutationUpdateEventHandler;
import edu.wpi.scheduler.client.permutation.PermutationSelectEventHandler;

public class PermutationUpdateEvent extends GwtEvent<PermutationUpdateEventHandler> {
	public static final Type<PermutationUpdateEventHandler> TYPE = new Type<PermutationUpdateEventHandler>();


	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<PermutationUpdateEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PermutationUpdateEventHandler handler) {
		handler.onPermutationUpdated();
	}
}