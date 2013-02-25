package edu.wpi.scheduler.client.permutation.view;

import java.util.List;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;

import edu.wpi.scheduler.client.permutation.PermutationController;
import edu.wpi.scheduler.client.permutation.PermutationSelectEvent;
import edu.wpi.scheduler.client.permutation.PermutationSelectEventHandler;
import edu.wpi.scheduler.shared.model.Section;

public class DetailedView extends ScrollPanel implements PermutationSelectEventHandler {

	public final PermutationController controller;
	public final FlowPanel body = new FlowPanel();

	public DetailedView(PermutationController controller) {
		this.controller = controller;
		add(body);
		
		getElement().getStyle().setPosition(Position.ABSOLUTE);
		getElement().getStyle().setLeft(0.0, Unit.PX);
		getElement().getStyle().setRight(0.0, Unit.PX);
		getElement().getStyle().setTop(0.0, Unit.PX);
		getElement().getStyle().setBottom(0.0, Unit.PX);
	}

	@Override
	protected void onLoad() {
		controller.addSelectListner(this);
		update();
	}

	@Override
	protected void onUnload() {
		controller.removeSelectListner(this);
	}

	public void update() {
		List<Section> sections = controller.getSelectedPermutation().sections;
		body.clear();

		for (Section section : sections) {
			add(section);
		}
	}

	private void add(Section section) {

		FlexTable table = new FlexTable();
		PeriodDataGrid dataGrid = new PeriodDataGrid(section);
		FlexCellFormatter cellFormatter = table.getFlexCellFormatter();

		table.addStyleName("DetailedSection");
		cellFormatter.setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);
		cellFormatter.setVerticalAlignment(1, 1, HasVerticalAlignment.ALIGN_TOP);

		cellFormatter.setWidth(1, 0, "50%");
		cellFormatter.setWidth(1, 1, "50%");
		
		String html = "<h3>" + section.course.toString() + "</h3>CRN: " + section.crn;
		html += "<br>Seats available: " + section.seatsAvailable + "/" + section.seats;

		table.setHTML(0, 0, html);
		table.setWidget(0, 1, dataGrid);

		body.add(table);
	}

	@Override
	public void onPermutationSelected(PermutationSelectEvent permutation) {
		update();
	}

}
