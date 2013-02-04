package edu.wpi.scheduler.client.permutation.view;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Label;

import edu.wpi.scheduler.client.Scheduler;
import edu.wpi.scheduler.shared.model.Period;
import edu.wpi.scheduler.shared.model.Section;

public class PeriodDescription extends DockPanel {

	public final Section section;
	
	public final DataGrid<Period> periodInfo = new DataGrid<Period>();
	public final Label title = new Label();
	
	public PeriodDescription(Section section) {
		this.section = section;
		
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
		
		title.setText(section.course.name);
		title.getElement().getStyle().setTextAlign(TextAlign.CENTER);
		title.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		
		periodInfo.setRowCount(section.periods.size(), true);
		periodInfo.setRowData(0, section.periods);
		
		//I really hate for having to hard-code this, but debugging the DataGrid is near impossible
		periodInfo.getElement().getStyle().setHeight(24.0 + section.periods.size() * 24.0 + 2.0, Unit.PX);
		
		
		this.add(title, NORTH );
		this.add(this.periodInfo, SOUTH );
		this.add(new Label(Scheduler.getDescription().getDescription(section.course).getDescription()), CENTER );
		
		periodInfo.redraw();
	}

}
