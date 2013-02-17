package edu.wpi.scheduler.client.permutation.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

import edu.wpi.scheduler.client.permutation.PermutationController;
import edu.wpi.scheduler.shared.model.Period;
import edu.wpi.scheduler.shared.model.Term;

public class PeriodItem extends Label implements ClickHandler {

	public final Term term;
	public final Period period;
	public final PermutationController controller;

	public PeriodItem(PermutationController controller, Period period, Term term) {		
		super(period.section.course.department.abbreviation + " " + period.section.course.number);
		this.controller = controller;

		this.period = period;
		this.term = term;

		this.setStyleName("permutationPeriodItem");

		this.addClickHandler(this);
	}

	@Override
	public void onClick(ClickEvent event) {
		final PopupPanel imagePopup = new PopupPanel(true);
	    imagePopup.setAnimationEnabled(true);
	    imagePopup.ensureDebugId("cwBasicPopup-imagePopup");
	    imagePopup.setWidget(new PeriodDescription( controller, period.section )); //TODO: Pass the controller
	    
	    imagePopup.getElement().getStyle().setProperty("maxWidth", "50%");
	    
	    imagePopup.center();
	    
	}

}
