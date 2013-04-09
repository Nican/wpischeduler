package edu.wpi.scheduler.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

import edu.wpi.scheduler.client.controller.DBRequestCallback;
import edu.wpi.scheduler.client.controller.ScheduleDBRequest;
import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.shared.model.ScheduleDB;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Scheduler implements EntryPoint {

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	// private final GreetingServiceAsync greetingService = GWT
	// .create(G reetingService.class);

	private static ScheduleDB scheduleDB;
	private static CourseDescriptions courseDescriptions;

	public static ScheduleDB getDatabase() {
		if (scheduleDB == null)
			throw new IllegalStateException("Getting the database while it has not yet been loaded!");

		return scheduleDB;
	}
	
	public static CourseDescriptions getDescription() {
		if (courseDescriptions == null)
			throw new IllegalStateException("Getting course descriptions before it has been loaded!");

		return courseDescriptions;
	}
	
	

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		final StudentSchedule studentSchedule = new StudentSchedule();

		ScheduleDBRequest request = new ScheduleDBRequest();
		request.setCallback(new DBRequestCallback() {

			@Override
			public void OnSuccess(ScheduleDB database) {
				System.out.println("Got success: ");
				scheduleDB = database;

				RootPanel.get().add(new MainView(studentSchedule));
				studentSchedule.loadSchedule();

			}

			@Override
			public void OnFailure(Throwable failure) {
				Window.alert("Unable to receive request: " + failure.getMessage());
			}
		});
		request.send();

		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode("courses.json"));
		try {
			builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					Window.alert("Unable to get course descriptions: " + exception.getMessage());
					// Couldn't connect to server (could be timeout, SOP
					// violation, etc.)
				}

				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						// Process the response in response.getText()
						courseDescriptions = JsonUtils.safeEval( response.getText() );
						
					} else {
						// Handle the error. Can get the status text from
						// response.getStatusText()
						Window.alert("Unable to parse course descriptions: " + response.getStatusText());
					}
				}
			});
		} catch (RequestException e) {
			// Couldn't connect to server
		}

		Window.enableScrolling(false);
		Window.setMargin("0px");

	}
}
