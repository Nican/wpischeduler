package edu.wpi.scheduler.client.timechooser;

import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.client.tabs.BaseTab;

public class TimeTab extends BaseTab
{

	TimeChooserView chooserView;
	
	public TimeTab(StudentSchedule studentSchedule) {
		super(studentSchedule, "Times");
		this.setEnabled(true);
	}

	@Override
	public Widget getBody() {
		if( chooserView == null )
			chooserView = new TimeChooserView(studentSchedule);
		
		return chooserView;
	}
}
