package edu.wpi.scheduler.client.controller;

import edu.wpi.scheduler.shared.model.Term;

public class StudentTermTimes 
{
	public StudentChosenTimes ATermTimes = new StudentChosenTimes();
	public StudentChosenTimes BTermTimes = new StudentChosenTimes();
	public StudentChosenTimes CTermTimes = new StudentChosenTimes();
	public StudentChosenTimes DTermTimes = new StudentChosenTimes();

	public StudentChosenTimes getTimesForTerm(Term t)
	{
		switch(t)
		{
		case A: return ATermTimes;
		case B: return BTermTimes;
		case C: return CTermTimes;
		case D: return DTermTimes;
		default: return null;
		}
	}

}
