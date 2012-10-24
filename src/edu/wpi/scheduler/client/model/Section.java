package edu.wpi.scheduler.client.model;

import java.util.ArrayList;
import java.util.List;

public class Section {
	
	public int crn;
	public String number;
	public int seats;
	public int seatsAvailable;
	public String term;
	public String partOfTerm;
	public String note; //TODO: What? Example = "Need special approval"
	
	public List<Period> periods = new ArrayList<Period>();
	
}
