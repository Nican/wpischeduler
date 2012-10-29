package edu.wpi.scheduler.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.wpi.scheduler.shared.model.ScheduleDB;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;

	void getDatabase(AsyncCallback<ScheduleDB> callback);

	void updateDatabase(ScheduleDB db, AsyncCallback<Void> callback);
}
