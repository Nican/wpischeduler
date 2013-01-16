package edu.wpi.scheduler.client.permutation.view;

import java.util.Collections;
import java.util.List;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.CellPanel;

import edu.wpi.scheduler.client.controller.SchedulePermutation;
import edu.wpi.scheduler.client.permutation.PermutationController;
import edu.wpi.scheduler.shared.model.Term;

public class GridCourseView extends CellPanel {

	final PermutationController controller;
	final SchedulePermutation permutation;

	public GridCourseView(PermutationController controller,
			SchedulePermutation permutation) {
		this.controller = controller;
		this.permutation = permutation;

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

		addView(Collections.singletonList(Term.A), firstCell);
		addView(Collections.singletonList(Term.B), secondCell);
		addView(Collections.singletonList(Term.C), thirdCell);
		addView(Collections.singletonList(Term.D), forthCell);
	}

	private Element createCell() {
		Element cell = DOM.createTD();

		cell.getStyle().setProperty("width", "50%");
		cell.getStyle().setProperty("height", "50%");
		cell.getStyle().setProperty("position", "absolute");

		return cell;
	}

	private void addView(List<Term> terms, Element cell) {
		Element div = DOM.createDiv();
		div.getStyle().setProperty("border", "black 1px solid");
		div.getStyle().setProperty("position", "absolute");
		div.getStyle().setProperty("top", "1px");
		div.getStyle().setProperty("bottom", "1px");
		div.getStyle().setProperty("right", "1px");
		div.getStyle().setProperty("left", "1px");
		div.getStyle().setProperty("borderRadius", "15px");
		div.getStyle().setProperty("overflow", "hidden");

		cell.appendChild(div);
		
		//Element termLabel = DOM.createDiv();
		//termLabel.setInnerHTML(terms.get(0).name);
		
		//div.appendChild(termLabel);

		add(new WeekCourseView(controller, permutation, terms), div);
	}

}
