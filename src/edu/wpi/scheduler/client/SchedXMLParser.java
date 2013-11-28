package edu.wpi.scheduler.client;

import java.util.HashSet;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;

import edu.wpi.scheduler.shared.model.Course;
import edu.wpi.scheduler.shared.model.DayOfWeek;
import edu.wpi.scheduler.shared.model.Department;
import edu.wpi.scheduler.shared.model.Period;
import edu.wpi.scheduler.shared.model.PeriodType;
import edu.wpi.scheduler.shared.model.ScheduleDB;
import edu.wpi.scheduler.shared.model.Section;
import edu.wpi.scheduler.shared.model.Time;

public class SchedXMLParser {

	public ScheduleDB parse(Document document) {
		ScheduleDB scheduleDB = new ScheduleDB();
		// TODO: Update scheduleDB fields
		
		NodeList deps = document.getElementsByTagName("dept");

		for (int i = 0; i < deps.getLength(); i++) {
			Department department = readDepartmentNode(scheduleDB, (Element) deps.item(i));

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
	private Department readDepartmentNode(ScheduleDB scheduleDB, Element node) {
		Department department = new Department(scheduleDB);

		department.abbreviation = node.getAttribute("abbrev");
		department.name = node.getAttribute("name");

		NodeList courses = node.getElementsByTagName("course");

		for (int i = 0; i < courses.getLength(); i++) {
			Course course = readCourseNode(department, (Element) courses.item(i));

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
	private Course readCourseNode(Department department, Element node) {
		Course course = new Course(department);

		course.name = node.getAttribute("name");
		course.number = node.getAttribute("number");
		course.description = node.getAttribute("course_desc");

		NodeList sections = node.getChildNodes();

		for (int i = 0; i < sections.getLength(); i++) {
			Section section = readSectionNode(course,
					(Element) sections.item(i));

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
	private Section readSectionNode(Course course, Element node) {
		Section section = new Section(course);

		section.crn = Integer.parseInt(node.getAttribute("crn"));
		section.number = node.getAttribute("number");
		section.seats = Integer.parseInt(node.getAttribute("seats"));
		section.seatsAvailable = Integer.parseInt(node.getAttribute("availableseats"));
		
		section.actualWaitlist = Integer.parseInt(node.getAttribute("actual_waitlist"));
		section.maxWaitlist = Integer.parseInt(node.getAttribute("max_waitlist"));
		
		
		section.note = node.getAttribute("note");

		// TODO (Nican): Read term information (How is this working?!)
		section.term = node.getAttribute("part-of-term");

		NodeList periods = node.getChildNodes();

		for (int i = 0; i < periods.getLength(); i++) {
			Period period = readPeriodNode(section, (Element) periods.item(i));

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
	private Period readPeriodNode(Section section, Element node) {
		Period period = new Period(section);

		period.type = getPeriodType(node.getAttribute("type"));
		period.professor = node.getAttribute("professor");
		period.startTime = new Time(node.getAttribute("starts"));
		period.endTime = new Time(node.getAttribute("ends"));
		period.location = node.getAttribute("building")
				+ node.getAttribute("room");

		String days = node.getAttribute("days");

		// TODO (Nican): What to do with this case? When the days are not
		// available
		if (!days.equals("?"))
			period.days = getDaysOfWeek(days);

		return period;
	}

	/**
	 * Get the correct period type for the given string name Check the @PeriodType
	 * for possible values
	 * 
	 * @param name
	 * @return
	 */
	private PeriodType getPeriodType(String name) {

		for (PeriodType type : PeriodType.values()) {
			if (type.getName().equals(name))
				return type;
		}

		throw new IllegalStateException("Non-existent period type: " + name);
	}

	/**
	 * Gets a list of all possible days of the week.
	 * 
	 * @param value
	 * @return
	 */
	private HashSet<DayOfWeek> getDaysOfWeek(String value) {
		HashSet<DayOfWeek> days = new HashSet<DayOfWeek>();

		for (String day : value.split(",")) {
			days.add(DayOfWeek.getByShortName(day));
		}

		return days;
	}

}
