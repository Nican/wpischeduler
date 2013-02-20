package edu.wpi.scheduler.client.permutation.view;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

import edu.wpi.scheduler.client.CourseDescription;
import edu.wpi.scheduler.client.Scheduler;
import edu.wpi.scheduler.client.controller.ScheduleConflictController.ConflictedList;
import edu.wpi.scheduler.client.permutation.PermutationController;
import edu.wpi.scheduler.shared.model.Period;
import edu.wpi.scheduler.shared.model.Section;

public class PeriodDescriptionDialogBox extends DialogBox {

	public final Section section;

	public final DockPanel dockPanel = new DockPanel();
	public final DataGrid<Period> periodInfo = new DataGrid<Period>();
	public final Label title = new Label();
	public final FlowPanel conflictList = new FlowPanel();

	public PeriodDescriptionDialogBox(PermutationController controller, Section section) {
		super(true);
		this.section = section;
		setText(section.course.toString() + " - " + section.number);
		getElement().getStyle().setProperty("width", "50%");
		getElement().getStyle().setZIndex(10);

		periodInfo.addColumn(new TextColumn<Period>() {
			@Override
			public String getValue(Period period) {
				return period.professor;
			}
		}, "Professor");

		periodInfo.addColumn(new TextColumn<Period>() {
			@Override
			public String getValue(Period period) {
				return period.location;
			}
		}, "Location");

		periodInfo.addColumn(new TextColumn<Period>() {
			@Override
			public String getValue(Period period) {
				return period.type.getName();
			}
		}, "Type");

		periodInfo.addColumn(new TextColumn<Period>() {
			@Override
			public String getValue(Period period) {
				return period.startTime.toString();
			}
		}, "Start Time");

		periodInfo.addColumn(new TextColumn<Period>() {
			@Override
			public String getValue(Period period) {
				return period.endTime.toString();
			}
		}, "End Time");

		periodInfo.setRowCount(section.periods.size(), true);
		periodInfo.setRowData(0, section.periods);

		// I really hate for having to hard-code this, but debugging the
		// DataGrid is near impossible
		periodInfo.getElement().getStyle().setHeight(24.0 + section.periods.size() * 24.0 + 2.0, Unit.PX);

		ConflictedList list = controller.conflictController.getConflicts(section);

		if (list != null) {
			Label conflictTitle = new Label("Conflicts:");
			conflictTitle.getElement().getStyle().setProperty("fontSize", "large");

			conflictList.add(conflictTitle);
			
			if( list.size() > 0 ){
				conflictList.add(new PeriodConflictList(list, controller));
				conflictList.setStyleName("sectionConflictList");
			} else {
				conflictList.add( new Label("There are no sections with time conflicts with this section."));
			}
			dockPanel.add(conflictList, DockPanel.EAST);
		}

		dockPanel.add(this.periodInfo, DockPanel.SOUTH);
		dockPanel.setCellVerticalAlignment(periodInfo, DockPanel.ALIGN_BOTTOM);

		CourseDescription description = Scheduler.getDescription().getDescription(section.course);

		if (description != null)
			dockPanel.add(new Label(description.getDescription()), DockPanel.CENTER);
		else
			dockPanel.add(new Label("No description available"), DockPanel.CENTER);

		periodInfo.redraw();
		add(dockPanel);
	}

	public void setGlassEnabled(boolean enabled) {
		super.setGlassEnabled(enabled);
		getGlassElement().getStyle().setZIndex(10);
	}

}
