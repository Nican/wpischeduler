package edu.wpi.scheduler.client.permutation.view;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;

import edu.wpi.scheduler.shared.model.DayOfWeek;
import edu.wpi.scheduler.shared.model.Period;
import edu.wpi.scheduler.shared.model.Section;

public class PeriodDataGrid extends CellTable<Period> {

	public PeriodDataGrid(Section section) {
		
		getElement().getStyle().setWidth(100.0, Unit.PCT);

		addColumn(new TextColumn<Period>() {
			@Override
			public String getValue(Period period) {
				return period.professor;
			}
		}, "Professor");

		addColumn(new TextColumn<Period>() {
			@Override
			public String getValue(Period period) {
				return period.location;
			}
		}, "Location");

		addColumn(new TextColumn<Period>() {
			@Override
			public String getValue(Period period) {
				return period.type.getName();
			}
		}, "Type");

		addColumn(new TextColumn<Period>() {
			@Override
			public String getValue(Period period) {
				StringBuilder days = new StringBuilder();

				for (DayOfWeek day : period.days) {
					days.append(",");
					days.append(day.getShortName());
				}

				return days.substring(1);
			}
		}, "Weekdays");

		addColumn(new TextColumn<Period>() {
			@Override
			public String getValue(Period period) {
				return period.startTime.toString();
			}
		}, "Start");

		addColumn(new TextColumn<Period>() {
			@Override
			public String getValue(Period period) {
				return period.endTime.toString();
			}
		}, "End");

		setRowCount(section.periods.size(), true);
		setRowData(0, section.periods);

		// I really hate for having to hard-code this, but debugging the
		// DataGrid is near impossible
		//getElement().getStyle().setHeight(24.0 + section.periods.size() * 24.0 + 2.0, Unit.PX);

		redraw();
	}

}
