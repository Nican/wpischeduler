package edu.wpi.scheduler.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A section has a list of class periods
 */
public class Section implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8236270010874393110L;
	
	/** Reference to parent course */
	public Course course;
	/** Course Registration Number */
	public int crn;
	/** Section number; e.g. A03 */
	public String number;
	/** Total seats for this section */
	public int seats;
	/** seats - numRegistered = seatsAvailable */
	public int seatsAvailable;
	/** Semester section is apart of; e.g. Fall 2012, Spring 2013 */
	public String semester;
	/** Term(s) section is offered; e.g. A Term, B Term */
	public String term;
	/** Internal note system for marking classes a special */
	/*
	 * TODO: Nican: What? Example = "Need special approval" Ryan: This is
	 * probably used for things like HUA seminars that need to be signed; do we
	 * care about this field?
	 */
	public String note;
	/** List of class periods; lecture, lab, etc */
	public List<Period> periods = new ArrayList<Period>();

	public int actualWaitlist;

	public int maxWaitlist;

	public List<Term> getTerms() {
		ArrayList<Term> terms = new ArrayList<Term>();

		for (String term : this.term.split(",")) {
			terms.add(Term.getTermByName(term.trim()));
		}
		return terms;
	}

	/**
	 * Constructs a section with a reference to the course it is apart of
	 * 
	 * @param course
	 *            course this section belongs to
	 */
	public Section(Course course) {
		this.course = course;
	}

	/**
	 * Default constructor for a section Needed for the GWT RPC to deserialize
	 * properly
	 */
	public Section() {
	}
	
	public boolean hasAvailableSats(){
		if (seatsAvailable > 0) 
			return true;
		
		if(seatsAvailable == 0 && seats == 0)
			return true;
		
		return false;		
	}	
}
