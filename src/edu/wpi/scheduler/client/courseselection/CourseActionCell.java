package edu.wpi.scheduler.client.courseselection;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;
import static com.google.gwt.dom.client.BrowserEvents.KEYDOWN;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.shared.model.Course;

public class CourseActionCell extends AbstractCell<Course> {

	public static final SelectionResources resources = GWT
			.create(SelectionResources.class);

	final CourseSelectionController selectionController;

	public CourseActionCell(CourseSelectionController selectionController) {
		super(CLICK, KEYDOWN);

		this.selectionController = selectionController;
	}

	@Override
	public void render(Context context, Course course, SafeHtmlBuilder sb) {
		
		//sb.appendHtmlConstant("<button type=\"button\" tabindex=\"-1\">");
		sb.append(AbstractImagePrototype.create(getIcon(course)).getSafeHtml());
		//sb.appendHtmlConstant("</button>");
	}

	public ImageResource getIcon(Course course) {
		if (selectionController.studentHasCourse(course))
			return resources.removeIcon();
		return resources.addIcon();
	}

	@Override
	public void onBrowserEvent(Context context, Element parent, Course value,
			NativeEvent event, ValueUpdater<Course> valueUpdater) {
		super.onBrowserEvent(context, parent, value, event, valueUpdater);
		if (CLICK.equals(event.getType())) {
			EventTarget eventTarget = event.getEventTarget();
			if (!Element.is(eventTarget)) {
				return;
			}
			if (parent.getFirstChildElement().isOrHasChild(
					Element.as(eventTarget))) {
				// Ignore clicks that occur outside of the main element.
				onEnterKeyDown(context, parent, value, event, valueUpdater);
			}
		}
	}

	@Override
	protected void onEnterKeyDown(Context context, Element parent,
			Course course, NativeEvent event, ValueUpdater<Course> valueUpdater) {
		
		System.out.println("Button clicked!");

		StudentSchedule studentSchedule = selectionController
				.getStudentSchedule();
		if (selectionController.studentHasCourse(course))
			studentSchedule.removeCourse(course);
		else
			studentSchedule.addCourse(course);
			
	}

}
