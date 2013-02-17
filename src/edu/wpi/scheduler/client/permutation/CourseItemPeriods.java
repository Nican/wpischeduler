package edu.wpi.scheduler.client.permutation;

import edu.wpi.scheduler.client.controller.SectionProducer;
import edu.wpi.scheduler.shared.model.Section;

public class CourseItemPeriods extends PeriodSelectListBase {

	public CourseItemPeriods(PermutationController permutationController, SectionProducer producer) {
		super(permutationController);

		for (final Section section : producer.getCourse().sections) {
			PeriodCheckbox checkbox = new PeriodCheckbox( section, producer );
			this.add( checkbox );
		}
		
		this.update();
	}

}
