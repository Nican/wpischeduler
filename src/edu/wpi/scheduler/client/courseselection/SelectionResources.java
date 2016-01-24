package edu.wpi.scheduler.client.courseselection;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface SelectionResources extends ClientBundle {

	@Source("add.png")
	ImageResource addIcon();

	@Source("remove.png")
	ImageResource removeIcon();
}
