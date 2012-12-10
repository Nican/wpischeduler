package edu.wpi.scheduler.client.permutation;

import com.google.gwt.event.shared.GwtEvent;


public class PermutationSelectEvent extends GwtEvent<PermutationSelectEventHandler> {
	public static final Type<PermutationSelectEventHandler> TYPE = new Type<PermutationSelectEventHandler>();


	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<PermutationSelectEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PermutationSelectEventHandler handler) {
		handler.onPermutationSelected(this);
	}
}
