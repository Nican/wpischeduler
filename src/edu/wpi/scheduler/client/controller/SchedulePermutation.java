package edu.wpi.scheduler.client.controller;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.scheduler.client.generator.AbstractProblem;
import edu.wpi.scheduler.client.generator.ScheduleProducer.SearchState;
import edu.wpi.scheduler.shared.model.Section;

public class SchedulePermutation {
	
	public final List<Section> sections;
	public final List<AbstractProblem> solutions;
	
	public SchedulePermutation(){
		sections = new ArrayList<Section>();
		solutions = new ArrayList<AbstractProblem>();
	}
	
	public SchedulePermutation(SearchState state){
		sections = new ArrayList<Section>(state.sections);
		solutions = new ArrayList<AbstractProblem>(state.solutions);
	}
	
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
		
		return equalSolution(other);
	}
	
	public boolean equalSolution( SchedulePermutation other ){
		if( other.solutions.size() != solutions.size() )
			return false;
		
		for( AbstractProblem problem : solutions ){
			if( !other.solutions.contains(problem) )
				return false;
		}
		
		return true;
	}
}
