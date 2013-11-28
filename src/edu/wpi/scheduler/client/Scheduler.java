package edu.wpi.scheduler.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.xhr.client.XMLHttpRequest;

import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.client.storage.StorageStudentSchedule;
import edu.wpi.scheduler.shared.model.ScheduleDB;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Scheduler implements EntryPoint {

	private static ScheduleDB scheduleDB;

	public static ScheduleDB getDatabase() {
		if (scheduleDB == null)
			throw new IllegalStateException(
					"Getting the database while it has not yet been loaded!");

		return scheduleDB;
	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		XMLHttpRequest xmlHttpRequest = XMLHttpRequest.create();
		xmlHttpRequest.open("GET", "new.schedb");

		LoadSchedule load = new LoadSchedule(xmlHttpRequest);

		try {
			xmlHttpRequest.send();
		} catch (JavaScriptException e) {
			Window.alert(e.getMessage());
		}

		RootPanel.get("loadingText").getElement().setInnerHTML("");
		RootPanel.get("loadingText").add(load);

		Window.enableScrolling(false);
		Window.setMargin("0px");

	}

	static void loadScheduler(ScheduleDB db) {
		scheduleDB = db;
		final StudentSchedule studentSchedule = new StudentSchedule();
		StorageStudentSchedule.loadSchedule(studentSchedule);

		RootPanel.get().clear();
		RootPanel.get().add(new MainView(studentSchedule));
	}
}
