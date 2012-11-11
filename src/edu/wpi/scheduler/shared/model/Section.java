package edu.wpi.scheduler.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A section has a list of class periods
 */
@SuppressWarnings("serial")
public class Section implements Serializable{
	
	/** Reference to parent course */
	public Course course;
	/** Course Registration Number */
	public int crn;
	/** Section number; e.g. A03 */
	public String number;
	/** Total seats for this section */
	public int seats;
	/** seats - numRegistered = seatsAvailable*/
	public int seatsAvailable;
	/** Semester section is apart of; e.g. Fall 2012, Spring 2013 */ 
	public String semester;
	/** Term(s) section is offered; e.g. A Term, B Term */
	public String term;
	/** Internal note system for marking classes a special */
	/* TODO:	Nican: What? Example = "Need special approval"
	 *  		Ryan: This is probably used for things like HUA seminars that need to be signed; do we care about this field?
	 */
	public String note; 
	/** List of class periods; lecture, lab, etc */
	public List<Period> periods = new ArrayList<Period>();
	
	/**
	 * Constructs a section with a reference to the course it is apart of
	 * @param course course this section belongs to
	 */
	public Section(Course course) {
		this.course = course;
	}
	
	/**
	 * Default constructor for a section
	 * Needed for the GWT RPC to deserialize properly
	 */
	public Section(){}
	
}
