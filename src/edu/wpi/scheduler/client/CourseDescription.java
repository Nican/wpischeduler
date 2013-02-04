package edu.wpi.scheduler.client;

import com.google.gwt.core.client.JavaScriptObject;

public class CourseDescription extends JavaScriptObject {

	protected CourseDescription() {
	}

	public final native String getName() /*-{
		return this[0];
	}-*/;

	public final native String getDescription() /*-{
		return this[1];
	}-*/;
}
