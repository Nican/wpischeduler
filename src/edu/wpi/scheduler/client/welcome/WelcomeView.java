package edu.wpi.scheduler.client.welcome;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class WelcomeView extends Composite
{
	private static WelcomeViewUiBinder uiBinder = GWT.create(WelcomeViewUiBinder.class);
	interface WelcomeViewUiBinder extends UiBinder<Widget, WelcomeView> {}

	public WelcomeView() 
	{	
		initWidget(uiBinder.createAndBindUi(this));
		getElement().getStyle().setLeft(0, Unit.PX);
		getElement().getStyle().setRight(0, Unit.PX);
		getElement().getStyle().setTop(0, Unit.PX);
		getElement().getStyle().setBottom(0, Unit.PX);
		getElement().getStyle().setPosition(Position.ABSOLUTE);
	}
}