package edu.wpi.scheduler.client.controller;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.scheduler.shared.model.Section;

public class SchedulePermutation {
	
	public List<Section> sections = new ArrayList<Section>();
	
	@Override
	public boolean equals( Object object ){
		if(!(object instanceof SchedulePermutation))
			return false;
		
		SchedulePermutation other = (SchedulePermutation) object;
		
		if( other.sections.size() != sections.size() )
			return false;
		
		for( Section section : sections ){
			if( !other.sections.contains(section) )
				return false;
		}
		
		return true;
	}
}
