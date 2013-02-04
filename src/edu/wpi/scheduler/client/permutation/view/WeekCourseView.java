package edu.wpi.scheduler.client.permutation.view;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.dom.client.Style;
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
import edu.wpi.scheduler.client.permutation.PermutationController;
import edu.wpi.scheduler.shared.model.DayOfWeek;
import edu.wpi.scheduler.shared.model.Term;

public class WeekCourseView extends CellPanel implements ResizeHandler {

	private Element tableRow = DOM.createTR();
	private Element timeTableRow = DOM.createTR();
	private Element timeLabelsColumn = DOM.createTD();

	private SchedulePermutation permutation;
	private PermutationController controller;
	private HandlerRegistration resizeHandle;
	private List<Term> allowedTerms;
	
	private final double timeColumnWidth = 40.0;

	public WeekCourseView(PermutationController controller,
			SchedulePermutation permutation) {
		this(controller, permutation, Arrays.asList(Term.values()));
	}

	public WeekCourseView(PermutationController controller,
			SchedulePermutation permutation, List<Term> terms) {
		getTable().setPropertyString("cellSpacing", "0");
		getTable().setPropertyString("cellPadding", "0");

		this.permutation = permutation;
		this.controller = controller;
		this.allowedTerms = terms;

		tableRow.appendChild(timeLabelsColumn);
		timeLabelsColumn.getStyle().setWidth(40, Unit.PX);
		timeTableRow.getStyle().setHeight(1.0, Unit.PX);

		List<DayOfWeek> weekDays = controller.getValidDaysOfWeek();

		for (int i = 0; i < weekDays.size(); i++) {
			WeekCourseColumn column = addColumn(weekDays.get(i));

			if (i % 2 == 0)
				column.getElement().getStyle().setBackgroundColor("#F3F7FB");
		}
		
		Style elementStyle = this.getElement().getStyle();
		elementStyle.setProperty("width", "100%");
		elementStyle.setProperty("height", "100%");
		elementStyle.setPosition(Position.ABSOLUTE);

		getBody().appendChild(timeTableRow);
		getBody().appendChild(tableRow);
	}

	private void clearTimeColumn() {
		while (timeLabelsColumn.hasChildNodes())
			timeLabelsColumn.removeChild(timeLabelsColumn.getLastChild());

		if (timeTableRow.hasChildNodes())
			timeTableRow.removeChild(timeTableRow.getLastChild());
	}

	private void createTimeColumn() {
		clearTimeColumn();

		Element timeCells = DOM.createTD();
		Element hourMarkers = DOM.createDiv();

		timeCells.setAttribute("colspan", "6");
		hourMarkers.getStyle().setPosition(Position.ABSOLUTE);
		hourMarkers.getStyle().setWidth(100, Unit.PCT);
		hourMarkers.getStyle().setTop(0.0, Unit.PX);

		double start = controller.getStartHour();
		double end = controller.getEndHour();
		double totalHeight = this.getElement().getClientHeight();
		double heightPerHour = totalHeight / (end - start);

		for (double i = start; i < end; i += 1.0) {
			Element marker = DOM.createDiv();
			Style markerStyle = marker.getStyle();
			markerStyle.setHeight(heightPerHour / 2, Unit.PX);
			markerStyle.setMarginBottom(heightPerHour / 2, Unit.PX);
			markerStyle.setPosition(Position.ABSOLUTE);
			markerStyle.setTop((i - start) * heightPerHour, Unit.PX);
			markerStyle.setLeft(timeColumnWidth, Unit.PX);
			marker.setClassName("permutationHourMarker");

			Element label = DOM.createDiv();
			Style labelStyle = label.getStyle();
			labelStyle.setHeight(heightPerHour, Unit.PX);
			labelStyle.setPosition(Position.ABSOLUTE);
			labelStyle.setTop((i - start) * heightPerHour, Unit.PX);
			labelStyle.setWidth(40.0, Unit.PX);
			label.setInnerText(Double.toString(Math.floor(i)));
			label.setClassName("permutationHourLabel");
			
			hourMarkers.appendChild(marker);
			timeLabelsColumn.appendChild(label);
		}

		createTermBackground();

		timeCells.appendChild(hourMarkers);
		timeTableRow.appendChild(timeCells);
		
		updateTimeColumns();
	}

	private void createTermBackground() {
		if (allowedTerms != null) {
			StringBuilder builder = new StringBuilder();

			for (Term term : allowedTerms)
				builder.append(term.name);

			Element termLabel = DOM.createDiv();
			termLabel.setInnerHTML(builder.toString());
			termLabel.getStyle().setProperty("opacity", "0.05");
			termLabel.getStyle().setProperty("fontSize", "325px");
			termLabel.getStyle().setProperty("position", "absolute");
			termLabel.getStyle().setProperty("right", "128px");
			termLabel.getStyle().setProperty("lineHeight", "325px");
			termLabel.getStyle().setProperty("bottom", "0px");

			timeLabelsColumn.appendChild(termLabel);
		}
	}

	@Override
	protected void onLoad() {
		this.createTimeColumn();
		resizeHandle = Window.addResizeHandler(this);
	}

	@Override
	protected void onUnload() {
		resizeHandle.removeHandler();
		resizeHandle = null;
	}

	private WeekCourseColumn addColumn(DayOfWeek day) {
		WeekCourseColumn courseColumn = new WeekCourseColumn(controller,
				permutation, day, allowedTerms);

		this.add(courseColumn, tableRow);

		return courseColumn;
	}
	
	private void updateTimeColumns(){
		double width = this.getElement().getClientWidth() - timeColumnWidth;
		double widthPerColumn = width / getChildren().size();

		for (Widget widget : this.getChildren()) {
			if (widget instanceof WeekCourseColumn) {
				widget.getElement().getStyle().setWidth(widthPerColumn, Unit.PX);
				((WeekCourseColumn) widget).updatePeriods();
			}
		}	
	}

	@Override
	public void onResize(ResizeEvent event) {
		this.createTimeColumn();
	}

}
