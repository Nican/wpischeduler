package edu.wpi.scheduler.client.generator;

import java.util.HashMap;
import java.util.List;

import edu.wpi.scheduler.client.controller.StudentChosenTimes;
import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.shared.model.Section;
import edu.wpi.scheduler.shared.model.Term;
import edu.wpi.scheduler.shared.model.TimeCell;

public class TimeConflictProblem extends AbstractProblem 
{
	ScheduleProducer parent;
	Section section;

	public TimeConflictProblem(ScheduleProducer parent, Section section) 
	{
		this.section = section;
		this.parent = parent;
	}

	@Override
	public String getTitle() {
		String deptName = section.course.department.abbreviation;
		String courseNum = section.course.number;
		String sectionNum = section.number;

		return "Your chosen times conflict with " + deptName + courseNum + ": Section " + sectionNum;
	}

	@Override
	public String getDescription() 
	{
		HashMap<Term, List<TimeCell>> conflicts = parent.getTimeConflicts(section);

		String desc = "Re-enable the following times to allow this section: ";

		for(Term term : section.getTerms())
		{
			desc += "<br>" + term.name() + "-Term: ";
			for(TimeCell conflict : conflicts.get(term))
			{
				desc += conflict.day.getName() + "@" + conflict.time.toString() + " ";
			}
		}
		return desc;
	}

	@Override
	public void applySolution(StudentSchedule schedule) 
	{
		HashMap<Term, List<TimeCell>> conflicts = parent.getTimeConflicts(section);
		for(Term term : section.getTerms())
		{
			StudentChosenTimes chosenTimes = schedule.studentTermTimes.getTimesForTerm(term);
			for(TimeCell conflict : conflicts.get(term))
			{
				chosenTimes.selectTime(conflict.timeToGrid(), conflict.dayToGrid());
			}
		}
		schedule.courseUpdated(section.course);
	}
}
