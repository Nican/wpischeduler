package edu.wpi.scheduler.shared.model;

public enum Term {
	A("A"),
	B("B"),
	C("C"),
	D("D");

	public final String name;

	private Term(String name) {
		this.name = name;
	}

	public static Term getTermByName(String name) {
		for (Term term : Term.values()) {
			if (name.equals(term.name) || name.equals(term.name + " Term"))
				return term;
		}
		return null;
	}
}
