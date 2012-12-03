package edu.wpi.scheduler.client.permutation;

import com.google.gwt.user.client.ui.Label;

public class PeriodItem extends Label{
	
	public PeriodItem( String text ){
		super(text);
		
		this.setStyleName("permutationPeriodItem");
	}
	
}
