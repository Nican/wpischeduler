package edu.wpi.scheduler.client.controller;

import edu.wpi.scheduler.shared.model.Term;

public class StudentTermTimes 
{
	public final StudentChosenTimes ATermTimes;
	public final StudentChosenTimes BTermTimes;
	public final StudentChosenTimes CTermTimes;
	public final StudentChosenTimes DTermTimes;

	public StudentTermTimes(StudentSchedule studentSchedule) {
		ATermTimes = new StudentChosenTimes(studentSchedule);
		BTermTimes = new StudentChosenTimes(studentSchedule);
		CTermTimes = new StudentChosenTimes(studentSchedule);
		DTermTimes = new StudentChosenTimes(studentSchedule);
	}

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
