package edu.wpi.scheduler.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.xhr.client.ReadyStateChangeHandler;
import com.google.gwt.xhr.client.XMLHttpRequest;
import com.google.gwt.xml.client.XMLParser;

import edu.wpi.scheduler.shared.model.ScheduleDB;

public class LoadSchedule extends ComplexPanel implements ReadyStateChangeHandler{

	private XMLHttpRequest xmlHttpRequest;
	
	public final Label loadingLabel = new Label();
	public final Label loadingBytes = new Label();

	public LoadSchedule(XMLHttpRequest request) {
		setElement(Document.get().createDivElement());
		xmlHttpRequest = request;
		
		add(new Label("Loading scheduler database..."), getElement());
		add(loadingLabel, getElement());
		add(loadingBytes, getElement());
		
		setProgressUpdateHandler(request);
		request.setOnReadyStateChange(this);
	}

	private final native void setProgressUpdateHandler(XMLHttpRequest xmlHttpRequest) /*-{
		// The 'this' context is always supposed to point to the xhr object in the
		// onreadystatechange handler, but we reference it via closure to be extra sure.
		var _this = this;
		xmlHttpRequest.onprogress = $entry(function(e) {
			_this.@edu.wpi.scheduler.client.LoadSchedule::progressUpdate(Ledu/wpi/scheduler/client/ProgressEvent;)(e);
		});
	}-*/;

	private void progressUpdate(ProgressEvent event) {
		loadingBytes.setText( event.getLoaded() + "/" + event.getTotal() + " bytes loaded!");
	}

	@Override
	public void onReadyStateChange(XMLHttpRequest xhr) {
		
		switch(xhr.getReadyState()){
		case XMLHttpRequest.OPENED:
			loadingLabel.setText("Connection to server open");
			break;
		case XMLHttpRequest.HEADERS_RECEIVED:
			loadingLabel.setText("Received headers from server...");
			break;
		case XMLHttpRequest.LOADING:
			loadingLabel.setText("Loading data from server...");
			break;
		case XMLHttpRequest.DONE:
			loadingFinished();
			break;
		}
	}
	
	private void loadingFinished(){
		
		if( xmlHttpRequest == null )
			return;
		
		if( xmlHttpRequest.getStatus() != 200 ){
			//Uh oh... It failed to load
			Window.alert(xmlHttpRequest.getStatusText());
			loadingLabel.setText("Failed to load... Status: " + xmlHttpRequest.getStatus() );
			loadingBytes.setText("");
			return;
		}

		String response = xmlHttpRequest.getResponseText();
		xmlHttpRequest = null;

		//Try loading as XML
		try{ 
			Scheduler.loadScheduler(loadXML(response));
			return; 
		} catch(Exception e){
			//IE has an error that will not understand the <?xml version="1.1" encoding="UTF-8"?>
			//A possible fix is either to:
			//Change "1.1" to "1.0"
			//Or remove the line entirely.
			try{ 
				//Remove the first line
				String newResponse = response.substring(response.indexOf("\n"));
				Scheduler.loadScheduler(loadXML(newResponse));
				return; 
			} catch(Exception e2){
			}
			
			GWT.log(e.toString());
		}
		
		//Try loading as JSON
		try{ 
			Scheduler.loadScheduler(loadJSON(response));
			return; 
		} catch(Exception e){
		}
		
		Window.alert("Unable to parse database. What kind of data am I getting?");
	}
	
	private ScheduleDB loadJSON(String response)
	{
		SchedJSONParser parser = new SchedJSONParser();
		JSONObject jsonDocument = JSONParser.parseLenient(response).isObject();
		return parser.parse(jsonDocument.get("departments").isArray());
	}
	
	private ScheduleDB loadXML(String response)
	{
		SchedXMLParser parser = new SchedXMLParser();
		return parser.parse(XMLParser.parse(response));
	}


}
