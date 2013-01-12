package edu.wpi.scheduler.client.permutation.view;

import com.google.gwt.user.client.ui.Label;

import edu.wpi.scheduler.shared.model.Period;
import edu.wpi.scheduler.shared.model.Term;

public class PeriodItem extends Label{
	
	public final Term term;
	public final Period period;
	
	public PeriodItem( Period period, Term term ){
		super(period.section.course.name + "\n" + period.professor );
		
		this.period = period;
		this.term = term;
		
		this.setStyleName("permutationPeriodItem");
	}
	
}
