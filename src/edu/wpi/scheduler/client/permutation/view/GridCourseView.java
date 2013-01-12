package edu.wpi.scheduler.client.permutation.view;

import java.util.Collections;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.CellPanel;

import edu.wpi.scheduler.client.controller.SchedulePermutation;
import edu.wpi.scheduler.client.permutation.PermutationController;
import edu.wpi.scheduler.shared.model.Term;

public class GridCourseView extends CellPanel {

	public GridCourseView(PermutationController controller, SchedulePermutation permutation) {
		DOM.setElementProperty(getTable(), "cellSpacing", "0");
		DOM.setElementProperty(getTable(), "cellPadding", "0");

		Element firstRow = DOM.createTR();
		Element secondRow = DOM.createTR();
		
		Element firstCell = createCell();
		Element secondCell = createCell();
		Element thirdCell = createCell();
		Element forthCell = createCell();

		secondCell.getStyle().setProperty("right", "0px");
		thirdCell.getStyle().setProperty("bottom", "0px");

		forthCell.getStyle().setProperty("right", "0px");
		forthCell.getStyle().setProperty("bottom", "0px");
	
		firstRow.appendChild(firstCell);
		firstRow.appendChild(secondCell);
		secondRow.appendChild(thirdCell);
		secondRow.appendChild(forthCell);
		
		DOM.appendChild(getBody(), firstRow);
		DOM.appendChild(getBody(), secondRow);

		add(new WeekCourseView(controller, permutation, Collections.singletonList(Term.A)), firstCell);
		add(new WeekCourseView(controller, permutation, Collections.singletonList(Term.B)), secondCell);
		add(new WeekCourseView(controller, permutation, Collections.singletonList(Term.C)), thirdCell);
		add(new WeekCourseView(controller, permutation, Collections.singletonList(Term.D)), forthCell);
	}

	private Element createCell() {
		Element cell = DOM.createTD();

		cell.getStyle().setProperty("width", "50%");
		cell.getStyle().setProperty("height", "50%");
		cell.getStyle().setProperty("position", "absolute");
		cell.getStyle().setProperty("overflow", "hidden");

		return cell;
	}

}
