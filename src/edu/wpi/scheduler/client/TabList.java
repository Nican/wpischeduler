package edu.wpi.scheduler.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class TabList extends Composite {

	private static TabListUiBinder uiBinder = GWT.create(TabListUiBinder.class);

	interface TabListUiBinder extends UiBinder<Widget, TabList> {
	}

	public TabList() {
		initWidget(uiBinder.createAndBindUi(this));
		
		
	}

}
