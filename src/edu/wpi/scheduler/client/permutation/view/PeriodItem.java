package edu.wpi.scheduler.client.permutation.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;

import edu.wpi.scheduler.client.permutation.PermutationController;
import edu.wpi.scheduler.shared.model.Period;
import edu.wpi.scheduler.shared.model.Term;

public class PeriodItem extends FlowPanel implements ClickHandler {

	public static final PermutationViewResources resources = GWT
			.create(PermutationViewResources.class);
	
	public static AbstractImagePrototype personIcon = AbstractImagePrototype.create(resources.personIcon());
	public static AbstractImagePrototype clockIcon = AbstractImagePrototype.create(resources.clockIcon());
	
	public final Term term;
	public final Period period;
	public final PermutationController controller;
	
	public final Label title = new Label();
	public final InlineHTML seats = new InlineHTML();

	public PeriodItem(PermutationController controller, Period period, Term term) {	
		this.controller = controller;
		this.period = period;
		this.term = term;
		
		String personHTML = personIcon.getHTML() + " " + period.section.seats + "/" + period.section.seatsAvailable;
		String waitlistHTML = clockIcon.getHTML() + " " + period.section.actualWaitlist + "/" + period.section.maxWaitlist;
		
		title.setText(period.section.course.department.abbreviation + " " + period.section.course.number);
		seats.setHTML(personHTML + " " + waitlistHTML);
		
		seats.getElement().getStyle().setFontSize(9.0, Unit.PX);
		seats.getElement().getStyle().setLeft(5.0, Unit.PX);
		seats.getElement().getStyle().setBottom(0.0, Unit.PX);
		seats.getElement().getStyle().setPosition(Position.ABSOLUTE);
		
		
		add(title);
		add(seats);

		this.setStyleName("permutationPeriodItem");
	}
	
	@Override
	public void onLoad(){
		addDomHandler(this, ClickEvent.getType());
	}
	
	@Override
	public void onUnload(){
		
	}

	@Override
	public void onClick(ClickEvent event) {
		controller.displayDescription(period.section);
	}

	public void setHeight(double height) {
		getElement().getStyle().setHeight(height, Unit.PX);
		seats.setVisible(height >= 24.0);
	}

}
