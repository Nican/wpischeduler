package edu.wpi.scheduler.client.courseselection;

import java.util.HashMap;
import java.util.Map.Entry;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.shared.model.Course;
import edu.wpi.scheduler.shared.model.Section;
import edu.wpi.scheduler.shared.model.Term;

public class TermView extends Widget {

	public Course course;
	public HashMap<Element, Term> terms = new HashMap<Element, Term>();

	public TermView(Course course) {
		this.course = course;
		setElement(DOM.createDiv());
	
		for (Term term : Term.values())
			this.addTerm(term);

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
		Element elem = DOM.createDiv();
		elem.setInnerText(term.name);
		
		getElement().appendChild(elem);
		terms.put(elem, term);

		return elem;
	}

	public void update() {
		for (Entry<Element, Term> entry : terms.entrySet()) {
			update(entry.getValue(), entry.getKey());
		}
	}

	protected void update(Term term, Element label) {
		if (!hasTerm(term)) {
			label.getStyle().setOpacity(0.2);
		} else {
			label.getStyle().setBackgroundColor("#DFFFDF");
		}
	}

	@Override
	protected void onLoad() {
		this.update();
	}



}
