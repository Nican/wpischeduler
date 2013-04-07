package edu.wpi.scheduler.client.generator;

import edu.wpi.scheduler.client.controller.StudentSchedule;

public abstract class AbstractProblem {
	
	
	public abstract String solutionDescription();
	
	public abstract void applySolution(StudentSchedule schedule);
}
