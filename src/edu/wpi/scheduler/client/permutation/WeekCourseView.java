package edu.wpi.scheduler.client.permutation;

import java.util.List;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.controller.SchedulePermutation;
import edu.wpi.scheduler.shared.model.DayOfWeek;

public class WeekCourseView extends CellPanel implements ResizeHandler {

	private Element tableRow = DOM.createTR();
	private Element timeTableRow = DOM.createTR();
	private Element timeLabelsColumn = DOM.createTD();

	private SchedulePermutation permutation;
	private PermutationController controller;
	private HandlerRegistration resizeHandle;

	public WeekCourseView(PermutationController controller, SchedulePermutation permutation) {
		DOM.setElementProperty(getTable(), "cellSpacing", "0");
		DOM.setElementProperty(getTable(), "cellPadding", "0");

		this.permutation = permutation;
		this.controller = controller;

		updateTimeColumn();
		
		tableRow.appendChild(timeLabelsColumn);
		timeLabelsColumn.getStyle().setWidth(40, Unit.PX);

		List<DayOfWeek> weekDays = controller.getValidDaysOfWeek();

		for (int i = 0; i < weekDays.size(); i++) {
			WeekCourseColumn column = addColumn(weekDays.get(i));

			if (i % 2 == 0)
				column.getElement().getStyle().setBackgroundColor("#F3F7FB");
		}

		this.getElement().getStyle().setProperty("width", "100%");
		this.getElement().getStyle().setProperty("height", "100%");
		this.getElement().getStyle().setPosition(Position.ABSOLUTE);

		timeTableRow.getStyle().setHeight(0.0, Unit.PX);

		DOM.appendChild(getBody(), timeTableRow);		
		DOM.appendChild(getBody(), tableRow);
	}

	private void updateTimeColumn() {
		while(timeLabelsColumn.hasChildNodes())
			timeLabelsColumn.removeChild(timeLabelsColumn.getLastChild());
		
		Element timeCells = DOM.createTD();
		Element hourMarkers = DOM.createDiv();

		timeCells.setAttribute("colspan", "5");
		hourMarkers.getStyle().setPosition(Position.ABSOLUTE);
		hourMarkers.getStyle().setWidth(100, Unit.PCT);

		double start = controller.getStartHour();
		double end = controller.getEndHour();
		double totalHeight = this.getElement().getClientHeight();
		double heightPerHour = totalHeight / (end - start);

		for (double i = start; i < end; i += 1.0) {

			Element marker = DOM.createDiv();
			marker.getStyle().setHeight(heightPerHour / 2, Unit.PX);
			marker.getStyle().setMarginBottom(heightPerHour / 2, Unit.PX);
			marker.getStyle().setPosition(Position.ABSOLUTE);
			marker.getStyle().setTop((i - start) * heightPerHour, Unit.PX);
			marker.getStyle().setLeft(40.0, Unit.PX);
			marker.setClassName("permutationHourMarker");

			DOM.appendChild(hourMarkers, marker);
			
			Element label = DOM.createDiv();
			label.getStyle().setHeight(heightPerHour, Unit.PX);
			label.getStyle().setPosition(Position.ABSOLUTE);
			label.getStyle().setTop((i - start) * heightPerHour, Unit.PX);
			label.getStyle().setWidth(40.0, Unit.PX);
			label.setInnerText(Double.toString(Math.floor(i)));
			label.setClassName("permutationHourLabel");
			
			DOM.appendChild(timeLabelsColumn, label);
		}

		DOM.appendChild(timeCells, hourMarkers);

		if (timeTableRow.hasChildNodes())
			timeTableRow.removeChild(timeTableRow.getLastChild());

		timeTableRow.appendChild(timeCells);
	}

	@Override
	protected void onLoad() {
		this.updateTimeColumn();
		resizeHandle = Window.addResizeHandler(this);
	}

	@Override
	protected void onUnload() {
		resizeHandle.removeHandler();
		resizeHandle = null;
	}

	private WeekCourseColumn addColumn(DayOfWeek day) {
		WeekCourseColumn courseColumn = new WeekCourseColumn(controller, permutation, day);

		this.add(courseColumn, tableRow);

		return courseColumn;
	}

	@Override
	public void onResize(ResizeEvent event) {
		this.updateTimeColumn();

		for (Widget widget : this.getChildren()) {
			if (widget instanceof WeekCourseColumn) {
				((WeekCourseColumn) widget).updatePeriods();
			}
		}
	}

}
