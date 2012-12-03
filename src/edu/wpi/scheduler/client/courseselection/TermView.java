package edu.wpi.scheduler.client.courseselection;

import java.util.HashMap;
import java.util.Map.Entry;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.Label;

import edu.wpi.scheduler.shared.model.Course;
import edu.wpi.scheduler.shared.model.Section;
import edu.wpi.scheduler.shared.model.Term;

public class TermView extends CellPanel {

	public Course course;
	private Element tableRow;
	public HashMap<Term, Label> terms = new HashMap<Term, Label>();

	public TermView(Course course) {
		this.course = course;
		
		tableRow = DOM.createTR();
	    DOM.appendChild(getBody(), tableRow);
	
		for (Term term : Term.values())
			this.addTerm(term);

		DOM.setElementProperty(getTable(), "cellSpacing", "2");

		this.setStyleName("termView");
	}

	public boolean hasTerm(Term term) {
		for (Section section : course.sections) {
			if (section.term.contains(term.name + " Term")) {
				return true;
			}
		}

		return false;
	}

	public Element addTerm(Term term) {
		
		Element element = DOM.createTD();
		
		element.setInnerText(term.name);
		
		tableRow.appendChild(element);
		
		return element;
		
		/*Label label = new Label(term.name);

		this.add(label);
		terms.put(term, label);

		return label;
		*/
	}

	public void update() {
		for (Entry<Term, Label> entry : terms.entrySet()) {
			update(entry.getKey(), entry.getValue());
		}
	}

	protected void update(Term term, Label label) {
		if (!hasTerm(term)) {
			label.getElement().getStyle().setOpacity(0.2);
		} else {
			label.getElement().getParentElement().getStyle().setBackgroundColor("#DFFFDF");
		}
	}

	@Override
	protected void onLoad() {
		this.update();
	}

}
