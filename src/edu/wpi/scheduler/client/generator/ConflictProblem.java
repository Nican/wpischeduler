package edu.wpi.scheduler.client.generator;

import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.shared.model.Section;

public class ConflictProblem extends AbstractProblem {

	/**
	 * This is the section of the course to be disabled.
	 */
	private Section conflict;
	/**
	 * This is the other section that is conflicting with
	 */
	private Section other;

	public ConflictProblem(Section conflict, Section other) {
		this.conflict = conflict;
		this.other = other;
	}

	@Override
	public String solutionDescription() {
		return conflict.course.toString() + " is conflicting with "
				+ other.course.toString();
	}

	@Override
	public void applySolution(StudentSchedule schedule) {

	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof ConflictProblem))
			return false;

		ConflictProblem conflict = (ConflictProblem) other;

		return this.conflict.equals(conflict.conflict)
				&& this.other.equals(conflict.other);
	}
}
