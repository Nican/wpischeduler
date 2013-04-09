package edu.wpi.scheduler.client.generator;

import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.shared.model.Section;

public class TimeConflictProblem extends AbstractProblem 
{
	Section timeConflictingSection;
	
	public TimeConflictProblem(Section section) 
	{
		timeConflictingSection = section;
	}

	@Override
	public String getTitle() {
		String deptName = timeConflictingSection.course.department.name;
		String courseNum = timeConflictingSection.course.name;
		String sectionNum = timeConflictingSection.number;
		
		return "Your chosen times conflict with " + deptName + courseNum + ": Section " + sectionNum;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void applySolution(StudentSchedule schedule) {
		// TODO Auto-generated method stub

	}

}
