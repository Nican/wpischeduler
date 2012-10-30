package edu.wpi.scheduler.client.courseselection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class CourseItem extends Composite {

	private static CourseItemUiBinder uiBinder = GWT
			.create(CourseItemUiBinder.class);

	interface CourseItemUiBinder extends UiBinder<Widget, CourseItem> {
	}

	public CourseItem() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
