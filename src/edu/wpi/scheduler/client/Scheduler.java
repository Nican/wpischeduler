package edu.wpi.scheduler.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

import edu.wpi.scheduler.client.controller.DBRequestCallback;
import edu.wpi.scheduler.client.controller.ScheduleDBRequest;
import edu.wpi.scheduler.client.courseselection.CourseSelectorView;
import edu.wpi.scheduler.shared.model.ScheduleDB;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Scheduler implements EntryPoint {

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);
	
	private static ScheduleDB scheduleDB;
	
	public static ScheduleDB getDatabase(){
		if( scheduleDB == null )
			throw new IllegalStateException("Getting the database while it has not yet been loaded!");
		
		return scheduleDB;
	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		ScheduleDBRequest request = new ScheduleDBRequest();
		request.setCallback(new DBRequestCallback() {

			@Override
			public void OnSuccess(ScheduleDB database) {
				// TODO Auto-generated method stub
				System.out.println("Got success: ");
				scheduleDB = database;
				
				RootPanel.get().add(new CourseSelectorView());
				
			}

			@Override
			public void OnFailure(Throwable failure) {
				// TODO Auto-generated method stub
				System.out.println("Unable to receive request: " + failure.getMessage());

			}
		});
		request.send();
		
		Window.enableScrolling(false);
		Window.setMargin("0px");
		
	}
}
