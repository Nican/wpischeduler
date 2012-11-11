package edu.wpi.scheduler.shared.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Primary Schedule Class Database
 */
@SuppressWarnings("serial")
public class ScheduleDB implements Serializable {
	
	/** A Schedule is a list of departments */
	public ArrayList<Department> departments = new ArrayList<Department>();
	/** School Year this schedule is for; e.g academic year 2012-2013 = 2012 */
	public int SchoolYear;
	/** Data this DB was generated */
	//public Date generated; // TODO live async schedDB updates
	
	/**
	 * Finds a section in the database by the the crn
	 * @param crn The given course CRN
	 * @return the section, or null if the section is not found.
	 */
	public Section getSectionByCRN( int crn ){
		// If the section exists, return it
		for( Department department : this.departments ){
			for( Course course : department.courses ){
				for( Section section : course.sections ){
					if( section.crn == crn )
						return section;
				}
			}
		}
		// Could not find section, return null
		return null;
	}
}
