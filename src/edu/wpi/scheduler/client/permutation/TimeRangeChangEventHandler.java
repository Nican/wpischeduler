package edu.wpi.scheduler.client.permutation;

import com.google.gwt.event.shared.EventHandler;

public interface TimeRangeChangEventHandler extends EventHandler{

	void onTimeRangeChange(TimeRangeChangeEvent timeRangeChangeEvent);

}
