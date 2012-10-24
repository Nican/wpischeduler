package edu.wpi.scheduler.client.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScheduleDB {

	public List<Department> departments = new ArrayList<Department>();
	public int SchoolYear;
	public Date generated;
	
	
	/**
	 * Finds a section in the database by the the crn
	 * @param crn The given course CRN
	 * @return the section, or null if the section is not found.
	 */
	public Section getSectionByCRN( int crn ){
		
		for( Department department : this.departments ){
			for( Course course : department.courses ){
				for( Section section : course.sections ){
					if( section.crn == crn )
						return section;
				}
			}			
		}
		
		return null;
	}
}
