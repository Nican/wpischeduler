package edu.wpi.scheduler.client;

import com.google.gwt.core.client.JavaScriptObject;

public class ProgressEvent extends JavaScriptObject {

	protected ProgressEvent() {

	}

	public final native int getTotal() /*-{
		return this.total;
	}-*/;

	public final native int getLoaded() /*-{
		return this.loaded;
	}-*/;
	
	public final native boolean lengthComputable() /*-{
		return this.lengthComputable;
	}-*/;
}
