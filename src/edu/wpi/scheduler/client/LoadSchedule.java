package edu.wpi.scheduler.client;

import java.util.HashSet;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.xhr.client.ReadyStateChangeHandler;
import com.google.gwt.xhr.client.XMLHttpRequest;

import edu.wpi.scheduler.shared.model.Course;
import edu.wpi.scheduler.shared.model.DayOfWeek;
import edu.wpi.scheduler.shared.model.Department;
import edu.wpi.scheduler.shared.model.Period;
import edu.wpi.scheduler.shared.model.PeriodType;
import edu.wpi.scheduler.shared.model.ScheduleDB;
import edu.wpi.scheduler.shared.model.Section;
import edu.wpi.scheduler.shared.model.Time;

public class LoadSchedule extends ComplexPanel implements ReadyStateChangeHandler{

	public final XMLHttpRequest xmlHttpRequest;
	
	public final Label loadingLabel = new Label();
	public final Label loadingBytes = new Label();

	public LoadSchedule(XMLHttpRequest request) {
		setElement(DOM.createDiv());
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
		
		if( xmlHttpRequest.getStatus() != 200 ){
			//Uh oh... It failed to load
			Window.alert(xmlHttpRequest.getStatusText());
			loadingLabel.setText("Failed to load... Status: " + xmlHttpRequest.getStatus() );
			loadingBytes.setText("");
			return;
		}
		
		String response = xmlHttpRequest.getResponseText();
		JSONObject jsonDocument = JSONParser.parseLenient(response).isObject();
		ScheduleDB db = parseDB(jsonDocument.get("departments").isArray());
		Scheduler.loadScheduler(db);
		
	}

	/**
	 * <schedb xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	 * xmlns="http://www.wpischeduler.org"
	 * xsi:schemaLocation="http://www.wpischeduler.org schedb.xsd"
	 * generated="WED OCT 03 14:13:39 2012" minutes-per-block="30">
	 * 
	 * @param document
	 * @return
	 */
	private ScheduleDB parseDB(JSONArray document) {
		ScheduleDB scheduleDB = new ScheduleDB();

		int size = document.size();

		for (int i = 0; i < size; i++) {
			Department department = readDepartmentNode(scheduleDB,
					document.get(i).isObject());

			scheduleDB.departments.add(department);

		}

		return scheduleDB;
	}
	
	/**
	 * Parses a department <dept abbrev="AB" name="ARABIC">
	 * 
	 * @param scheduleDB
	 * @param node
	 * @return
	 */
	private Department readDepartmentNode(ScheduleDB scheduleDB,
			JSONObject node) {
		Department department = new Department(scheduleDB);

		department.abbreviation = node.get("abbrev").isString().stringValue();
		department.name = node.get("name").isString().stringValue();

		JSONArray courses = node.get("courses").isArray();

		for (int i = 0; i < courses.size(); i++) {
			Course course = readCourseNode(department,
					courses.get(i).isObject());

			department.courses.add(course);
		}

		return department;
	}

	/**
	 * <course number="525-101S" name="SP TOP:COMPUTR & NETWK SECURTY"
	 * min-credits="3" max-credits="4.5" grade-type="normal">
	 * 
	 * @param department
	 * @param node
	 * @return
	 */
	private Course readCourseNode(Department department, JSONObject node) {
		Course course = new Course(department);

		course.name = node.get("name").isString().stringValue();
		course.number = node.get("number").isString().stringValue();
		course.description = node.get("description").isString().stringValue();

		JSONArray sections = (JSONArray) node.get("sections");

		for (int i = 0; i < sections.size(); i++) {
			Section section = readSectionNode(course,
					sections.get(i).isObject());

			course.sections.add(section);
		}

		return course;
	}


	/**
	 * Reads a section node from the XML element. Example section: <section
	 * crn="11126" number="A01" seats="25" availableseats="0" term="201301"
	 * part-of-term="A Term">
	 * 
	 * @param course
	 *            The parent course of this section
	 * @param node
	 *            The XML node
	 * @return a new section for the corresponding XML node
	 */
	private static Section readSectionNode(Course course, JSONObject node) {
		Section section = new Section(course);

		section.crn = (int) node.get("crn").isNumber().doubleValue();
		section.number = node.get("number").isString().stringValue();
		section.seats =  (int) node.get("seats").isNumber().doubleValue();
		section.seatsAvailable = (int) node.get("availableseats").isNumber().doubleValue();
		section.note = node.get("note").isString().toString();

		// TODO (Nican): Read term information (How is this working?!)
		section.term = node.get("partOfTerm").isString().stringValue();

		JSONArray periods = node.get("periods").isArray();

		for (int i = 0; i < periods.size(); i++) {
			Period period = readPeriodNode(section, (JSONObject) periods.get(i));

			section.periods.add(period);
		}

		return section;
	}

	/**
	 * Read a period node from the XML element. Example period: <period
	 * type="Lecture" professor="Brahimi" days="mon,tue,thu,fri" starts="9:00AM"
	 * ends="9:50AM" building="SL" room="105"/>
	 * 
	 * @param section
	 * @param node
	 * @return
	 */
	private static Period readPeriodNode(Section section, JSONObject node) {
		Period period = new Period(section);

		period.type = getPeriodType(node.get("type").isString().stringValue());
		period.professor = node.get("professor").isString().stringValue();
		period.startTime = readTime(node.get("starts").isNumber());
		period.endTime = readTime(node.get("ends").isNumber());
		period.location = node.get("location").isString().stringValue();

		JSONArray days = node.get("days").isArray();

		// TODO (Nican): What to do with this case? When the days are not
		// available
		period.days = getDaysOfWeek(days);

		return period;
	}
	
	private static Time readTime(JSONNumber number){
		double value = number.doubleValue();
		int minute = (int) (value % 100.0);
		int hour = (int) (value / 100.0);
		
		return new Time(hour, minute);		
	}

	/**
	 * Get the correct period type for the given string name Check the @PeriodType
	 * for possible values
	 * 
	 * @param name
	 * @return
	 */
	private static PeriodType getPeriodType(String name) {

		for (PeriodType type : PeriodType.values()) {
			if (type.getName().equals(name))
				return type;
		}

		throw new IllegalStateException("Non-existent period type: " + name);
	}

	/**
	 * Gets a list of all possible days of the week.
	 * 
	 * @param days
	 * @return
	 */
	private static HashSet<DayOfWeek> getDaysOfWeek(JSONArray days2) {
		HashSet<DayOfWeek> days = new HashSet<DayOfWeek>();
		
		if( days2.size() == 1 && days2.get(0).isString().stringValue().equals("UNKNOWN"))
			return days;

		for(int i = 0; i < days2.size(); i++ ){
			days.add(DayOfWeek.getByName(days2.get(i).isString().stringValue()));
		}

		return days;
	}
	

}
