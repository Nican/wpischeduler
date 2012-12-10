package edu.wpi.scheduler.client.permutation;

import com.google.gwt.user.client.ui.Label;

import edu.wpi.scheduler.shared.model.Period;

public class PeriodItem extends Label{
	
	public PeriodItem( Period period ){
		super(period.section.course.name + "\n" + period.professor );
		
		this.setStyleName("permutationPeriodItem");
	}
	
}
