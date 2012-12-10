package edu.wpi.scheduler.client.permutation;

import com.google.gwt.event.shared.GwtEvent;

public class TimeRangeChangeEvent extends GwtEvent<TimeRangeChangEventHandler> {
	
	public static final Type<TimeRangeChangEventHandler> TYPE = new Type<TimeRangeChangEventHandler>();


	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<TimeRangeChangEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(TimeRangeChangEventHandler handler) {
		handler.onTimeRangeChange(this);
	}

}
