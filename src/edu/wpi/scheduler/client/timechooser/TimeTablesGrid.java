package edu.wpi.scheduler.client.timechooser;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.client.controller.StudentTermTimes;
import edu.wpi.scheduler.shared.model.Term;

public class TimeTablesGrid extends Grid implements ResizeHandler {
	TimeTable ATermTable;
	TimeTable BTermTable;
	TimeTable CTermTable;
	TimeTable DTermTable;

	HandlerRegistration resizeHandler;

	public TimeTablesGrid(StudentSchedule studentSchedule) {
		super(2, 2);
		getElement().getStyle().setWidth(100.0, Unit.PCT);
		getElement().getStyle().setHeight(100.0, Unit.PCT);
		getElement().getStyle().setPosition(Position.ABSOLUTE);

		StudentTermTimes termTimes = studentSchedule.studentTermTimes;

		ATermTable = new TimeTable(Term.A, termTimes.getTimesForTerm(Term.A));
		BTermTable = new TimeTable(Term.B, termTimes.getTimesForTerm(Term.B));
		CTermTable = new TimeTable(Term.C, termTimes.getTimesForTerm(Term.C));
		DTermTable = new TimeTable(Term.D, termTimes.getTimesForTerm(Term.D));
		
		this.addTimeTable(0, 0, ATermTable);
		this.addTimeTable(0, 1, BTermTable);
		this.addTimeTable(1, 0, CTermTable);
		this.addTimeTable(1, 1, DTermTable);
		this.setCellPadding(10);
	}

	public void addTimeTable(int row, int column, TimeTable table) {
		setWidget(row, column, table);
		getCellFormatter().setWidth(row, column, "50%");
		getCellFormatter().setHeight(row, column, "50%");
		getCellFormatter().setAlignment(row, column,
				HasHorizontalAlignment.ALIGN_CENTER,
				HasVerticalAlignment.ALIGN_MIDDLE);
	}

	@Override
	public void onLoad() {
		resizeHandler = Window.addResizeHandler(this);
		onResize(null);
	}

	@Override
	public void onUnload() {
		resizeHandler.removeHandler();
	}

	@Override
	public void onResize(ResizeEvent event) {
		onResize();
	}

	public void onResize() {
		int width = (int) (getParentElement().getClientWidth() * 0.47);
		int height = (int) (getParentElement().getClientHeight() * 0.47);

		ATermTable.setSize(width, height);
		BTermTable.setSize(width, height);
		CTermTable.setSize(width, height);
		DTermTable.setSize(width, height);
	}

	public Element getParentElement() {
		return getElement().getParentElement();
	}
}
