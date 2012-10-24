package edu.wpi.scheduler.client.controller;

import java.util.EnumSet;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import edu.wpi.scheduler.client.model.Course;
import edu.wpi.scheduler.client.model.DaysOfWeek;
import edu.wpi.scheduler.client.model.Department;
import edu.wpi.scheduler.client.model.Period;
import edu.wpi.scheduler.client.model.PeriodType;
import edu.wpi.scheduler.client.model.ScheduleDB;
import edu.wpi.scheduler.client.model.Section;

public class ScheduleDBRequest {
	
	public RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, "/sched.xml");
	
	private RequestCallback callbackRequest = new RequestCallback() {
		
		@Override
		public void onResponseReceived(Request request, Response response) {
			parseXML(XMLParser.parse(response.getText()));
		}
		
		@Override
		public void onError(Request request, Throwable exception) {
			//TODO: Properly handle the exception
			Window.alert(exception.getMessage());
		}
	};
	
	public ScheduleDBRequest(){		
		builder.setCallback(callbackRequest);
	}
	
	public void send() throws RequestException
	{
		builder.send();	
	}
	
	/**
	 * <schedb xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://www.wpischeduler.org" xsi:schemaLocation="http://www.wpischeduler.org schedb.xsd" generated="WED OCT 03 14:13:39 2012" minutes-per-block="30">
	 * @param document
	 * @return
	 */
	public ScheduleDB parseXML(Document document)
	{
		ScheduleDB scheduleDB = new ScheduleDB();
		
		//TODO: Update scheduleDB fields
		
		NodeList deps = document.getElementsByTagName("dept");
		
		for( int i = 0; i < deps.getLength(); i++ ){
			Department department = readDepartmentNode( scheduleDB, (Element) deps.item(i) );
			
			scheduleDB.departments.add( department );
		
		}
		
		
		return scheduleDB;
	}
	
	/**
	 * <dept abbrev="AB" name="ARABIC">
	 * @param scheduleDB
	 * @param node
	 * @return
	 */
	private Department readDepartmentNode( ScheduleDB scheduleDB, Element node ){
		Department department = new Department(scheduleDB);
		
		department.abbreviation = node.getAttribute("abbrev");
		department.name = node.getAttribute("name");
		
		NodeList courses = node.getChildNodes();
		
		for( int i = 0; i < courses.getLength(); i++ ){
			Course course = readCourseNode( department, (Element) courses.item(i) );
			
			department.courses.add(course);
		}
		
		return department;
	}
	
	/**
	 * <course number="525-101S" name="SP TOP:COMPUTR & NETWK SECURTY" min-credits="3" max-credits="4.5" grade-type="normal">
	 * @param department
	 * @param node
	 * @return
	 */
	private Course readCourseNode(Department department, Element node) {
		Course course = new Course(department);
		
		course.name = node.getAttribute("name");
		course.number = node.getAttribute("number");
		
		NodeList sections = node.getChildNodes();
		
		for( int i = 0; i < sections.getLength(); i++ ){
			Section section = readSectionNode( course, (Element) sections.item(i) );
			
			course.sections.add(section);
		}
		
		return course;
	}

	/**
	 * Reads a section node from the XML element. Example section:
	 * <section crn="11126" number="A01" seats="25" availableseats="0" term="201301" part-of-term="A Term">
	 * 
	 * @param course The parent course of this section
	 * @param node The XML node
	 * @return a new section for the corresponding XML node
	 */
	private Section readSectionNode(Course course, Element node) {
		Section section = new Section();
		
		section.crn = Integer.parseInt( node.getAttribute("crn") );
		section.number = node.getAttribute("number");
		section.seats = Integer.parseInt( node.getAttribute("seats") );
		section.seatsAvailable = Integer.parseInt( node.getAttribute("availableseats") );
		section.note = node.getAttribute("note");
		
		//TODO (Nican): Read term information (How is this working?!)
		
		NodeList periods = node.getChildNodes();
		
		for( int i = 0; i < periods.getLength(); i++ ){
			Period period = readPeriodNode( section, (Element) periods.item(i) );
			
			section.periods.add(period);
		}
		
		return section;
	}

	/**
	 * Read a period node from the XML element. Example period:
	 * <period type="Lecture" professor="Brahimi" days="mon,tue,thu,fri" starts="9:00AM" ends="9:50AM" building="SL" room="105"/>
	 * @param section
	 * @param node
	 * @return
	 */
	private Period readPeriodNode(Section section, Element node) {
		Period period = new Period(section);
		
		period.type = getPeriodType(node.getAttribute("type"));
		period.professor = node.getAttribute("professor");
		period.startTime = node.getAttribute("starts");
		period.endTime = node.getAttribute("ends");
		period.location = node.getAttribute("building") + node.getAttribute("room");	
		
		
		String days = node.getAttribute("days");
		
		//TODO (Nican): What to do with this case? When the days are not available
		if (!days.equals("?"))
			period.days = getDaysOfWeek(days);
		
		return period;	
	}
	
	private PeriodType getPeriodType(String name){
		
		for( PeriodType type : PeriodType.values() ){
			if( type.getName().equals(name))
				return type;			
		}
		
		throw new IllegalStateException("Non-existent period type: " + name );
	}
	
	private EnumSet<DaysOfWeek> getDaysOfWeek(String value){
		EnumSet<DaysOfWeek> days = EnumSet.noneOf(DaysOfWeek.class);
		
		for( String day : value.split(",")){
			days.add(DaysOfWeek.getByShortName(day));			
		}
		
		return days;		
	}
	
	
}
