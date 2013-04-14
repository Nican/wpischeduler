package edu.wpi.scheduler.client.permutation;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.TextBox;

import edu.wpi.scheduler.client.controller.SchedulePermutation;
import edu.wpi.scheduler.client.storage.StorageSharing;

public class ShareWidget extends ComplexPanel {
	
	public ShareWidget( SchedulePermutation permutation ){
		setElement(DOM.createDiv());
		getElement().getStyle().setWidth(200, Unit.PX);
		
		TextBox textbox = new TextBox();
		textbox.setValue(Document.get().getURL() + "#share/" + StorageSharing.getShareCode(permutation));
		textbox.getElement().getStyle().setWidth(95, Unit.PCT);
		
		add(textbox, getElement());
	}
	
}
