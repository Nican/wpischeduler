package edu.wpi.scheduler.client.permutation.view;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

import edu.wpi.scheduler.client.CourseDescription;
import edu.wpi.scheduler.client.Scheduler;
import edu.wpi.scheduler.client.controller.ConflictController.ConflictedList;
import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.client.permutation.PeriodSelectList;
import edu.wpi.scheduler.client.permutation.PermutationController;
import edu.wpi.scheduler.client.permutation.SectionCheckbox;
import edu.wpi.scheduler.shared.model.Section;

public class PeriodDescriptionDialogBox extends DialogBox {

	public static class TitleCheckbox extends SectionCheckbox implements Caption {
		public TitleCheckbox(StudentSchedule schedule, Section section ){
			super(schedule, section);
			setStyleName("Caption");
			getElement().getStyle().setDisplay(Display.BLOCK);
			setText(section.course.toString() + " - " + section.number);
		}
	}
	
	
	public final Section section;

	public final DockPanel dockPanel = new DockPanel();
	public final PeriodDataGrid periodInfo;
	public final Label title = new Label();
	public final FlowPanel conflictList = new FlowPanel();
	
	public static final String dialogWidth = "800px";

	public PeriodDescriptionDialogBox(PermutationController controller, Section section) {
		super(true, true, new TitleCheckbox(controller.getStudentSchedule(), section));
		this.section = section;
		periodInfo = new PeriodDataGrid(section);
		
		getElement().getStyle().setProperty("width", dialogWidth);
		getElement().getStyle().setZIndex(10);

		ConflictedList list = controller.getConflictController().getConflicts(section);

		if (list != null) {
			Label conflictTitle = new Label("Conflicts:");
			conflictTitle.getElement().getStyle().setProperty("fontSize", "large");

			conflictList.add(conflictTitle);
			
			if( list.size() > 0 ){
				PeriodSelectList periodList = new PeriodSelectList(controller);
				periodList.setSections(list, false);
				
				conflictList.add(periodList);
			} else {
				conflictList.add( new Label("There are no sections with time conflicts with this section."));
			}
			dockPanel.add(conflictList, DockPanel.EAST);
			dockPanel.setCellWidth(conflictList, "200px");
		}

		dockPanel.add(this.periodInfo, DockPanel.SOUTH);
		dockPanel.add(new Label(getDescription()), DockPanel.CENTER);
		
		dockPanel.setCellVerticalAlignment(periodInfo, DockPanel.ALIGN_BOTTOM);
		dockPanel.setWidth(dialogWidth);

		add(dockPanel);
	}
	
	private String getDescription(){
		CourseDescription description = Scheduler.getDescription().getDescription(section.course);

		return description != null ? description.getDescription() : "No description available";
	}

	public void setGlassEnabled(boolean enabled) {
		super.setGlassEnabled(enabled);
		getGlassElement().getStyle().setZIndex(10);
	}

}
