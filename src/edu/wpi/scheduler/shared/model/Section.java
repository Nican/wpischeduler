package edu.wpi.scheduler.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Section implements Serializable{
	
	public int crn;
	public String number;
	public int seats;
	public int seatsAvailable;
	public String term;
	public String partOfTerm;
	public String note; //TODO: What? Example = "Need special approval"
	
	public List<Period> periods = new ArrayList<Period>();
	
	public Section()
	{
	}
	
}
