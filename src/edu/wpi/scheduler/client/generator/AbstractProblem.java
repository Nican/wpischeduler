package edu.wpi.scheduler.client.generator;

import edu.wpi.scheduler.client.controller.StudentSchedule;

/**
 * Do not ask me what I was thinking for the name of this class. This class will
 * represent a possible solution to be applied on the StudentSchedule for the
 * student to have a working schedule.
 * 
 * @author nican
 * 
 */
public abstract class AbstractProblem {

	public abstract String getTitle();

	public abstract String getDescription();

	public abstract void applySolution(StudentSchedule schedule);
}
