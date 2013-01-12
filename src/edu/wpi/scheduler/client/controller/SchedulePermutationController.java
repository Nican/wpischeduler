package edu.wpi.scheduler.client.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.Timer;

import edu.wpi.scheduler.shared.model.DayOfWeek;
import edu.wpi.scheduler.shared.model.Period;
import edu.wpi.scheduler.shared.model.Section;
import edu.wpi.scheduler.shared.model.Term;
import edu.wpi.scheduler.shared.model.Time;

/**
 * Finds all conflicts between periods, and courses schedules. 
 * 
 * The procedure works as follows:
 * -Place all the sections in a tree, where each layer of the tree are all sections of a given course (exclude any courses with 0 sections)
 * -Traverse the tree using DFS
 * -Before adding each item of to the DFS, compare the item to all the elements in the DFS's queue, and check for conflicts.
 * -When we reach the end of the tree. (A section of each course is in the queue), and there are no conflicts, create a new schedule. 
 * @author Nican
 *
 */
public class SchedulePermutationController implements HasHandlers {

	public static class ConflictedList extends ArrayList<Section> {
		private static final long serialVersionUID = 8823532784912354546L;
		public Section section;
	}

	private class ProducerTimer extends Timer {
		@Override
		public void run() {
			generate();

			if (!canGenerate()) {
				this.cancel();
			}
		}
	}
	
	public static class TreeStateItem {
		public int id;
		public boolean hasConflict;
		
		public TreeStateItem(int id, boolean hasConflict){
			this.id = id;
			this.hasConflict = hasConflict;
		}
	}

	public static interface PermutationUpdateEventHandler extends EventHandler {
		public void onPermutationUpdated();
	}

	private HandlerManager handlerManager = new HandlerManager(this);
	public final StudentSchedule studentSchedule;

	/**
	 * List of sections that we are going to cross match to find conflicts and
	 * schedules
	 */
	private ArrayList<List<Section>> producedSections = new ArrayList<List<Section>>();

	/**
	 * List of actual found schedule permutations
	 */
	private List<SchedulePermutation> permutations = new ArrayList<SchedulePermutation>();

	/**
	 * A mapping to what sections conflict to what sections If section A
	 * conflicts with section B, then section B conflicts with section A
	 */
	private Map<Section, ConflictedList> conflicts = new HashMap<Section, ConflictedList>();

	/**
	 * Current state of searching the producedSections tree (Using the
	 * LinkedList as a queue/FIFO)
	 */
	private ArrayList<TreeStateItem> treeSearchState = new ArrayList<TreeStateItem>();

	/**
	 * Timer that will generate more schedules as needed
	 */
	ProducerTimer timer = new ProducerTimer();

	public SchedulePermutationController(StudentSchedule studentSchedule) {
		this.studentSchedule = studentSchedule;

		for (SectionProducer producer : studentSchedule.sectionProducers) {
			List<Section> sections = producer.getSections();

			if (!sections.isEmpty())
				producedSections.add(sections);
		}

		// Start with the root node of the tree
		addState(0);

		// Start generating!
		timer.scheduleRepeating(50);

		// Make a few right off the bat so users do not have to wait
		generate();
	}

	public HandlerRegistration addUpdateHandler(PermutationUpdateEventHandler handler) {
		return handlerManager.addHandler(PermutationUpdateEvent.TYPE, handler);
	}

	public void removeUpdateHandler(PermutationUpdateEventHandler handler) {
		handlerManager.removeHandler(PermutationUpdateEvent.TYPE, handler);
	}

	public List<SchedulePermutation> getPermutations() {
		return permutations;
	}

	/**
	 * Cancels the timer from generating any more schedules
	 */
	public void cancel() {
		timer.cancel();
	}

	private void generate() {
		int i = 0;

		while (i < 10 && canGenerate()) {
			generateNext();
			i++;
		}

		if (i > 0)
			this.fireEvent(new PermutationUpdateEvent());
	}

	protected boolean canGenerate() {
		return !treeSearchState.isEmpty();
	}

	private void generateNext() {
		int treeSize = treeSearchState.size();

		// Can we move down the tree?
		if (treeSize < producedSections.size()) {
			// Move a step down and compare the current state
			addState(0);

			return;
		}

		// We are at the leaf of the tree, go one back and look for the next
		// section
		while (!treeSearchState.isEmpty()) {
			int lastId = treeSearchState.remove(treeSearchState.size() - 1).id;
			int newId = lastId + 1;
			int sectionsSize = producedSections.get(treeSearchState.size()).size();

			if (sectionsSize > newId) {
				addState(newId);
				return;
			}
		}
	}

	private void addState(int newId) {
		Section newSection = producedSections.get(treeSearchState.size()).get(newId);
		boolean hasConflicts = false;

		// Look at our current state, and see if we have any conflicts
		for (int i = 0; i < treeSearchState.size(); i++) {
			Section section = producedSections.get(i).get(treeSearchState.get(i).id);

			if (hasConflicts(newSection, section)) {
				hasConflicts = true;
				addConflicts(newSection, section);
				addConflicts(section, newSection);
			}
		}

		treeSearchState.add(new TreeStateItem( newId, hasConflicts ));


		// If we are at the leaf of the tree, and we do not have a conflict
		// We have a winner!
		if (!hasStateConflicts() && treeSearchState.size() == producedSections.size()) {
			addStateToSchedules();
		}
	}
	
	public boolean hasStateConflicts(){
		for( TreeStateItem item : treeSearchState )
			if( item.hasConflict == true )
				return true;
		
		return false;
	}

	private void addStateToSchedules() {
		SchedulePermutation schedule = new SchedulePermutation();

		for (int i = 0; i < treeSearchState.size(); i++) {
			Section section = producedSections.get(i).get(treeSearchState.get(i).id);

			schedule.sections.add(section);
		}

		permutations.add(schedule);
	}

	private void addConflicts(Section newSection, Section section) {
		ConflictedList list;

		if (!conflicts.containsKey(newSection)) {
			list = new ConflictedList();
			conflicts.put(newSection, list);
		} else {
			list = conflicts.get(newSection);
		}

		list.add(section);
	}

	private boolean hasConflicts(Section newSection, Section section) {
		List<Term> newTerms = newSection.getTerms();
		List<Term> terms = section.getTerms();
		boolean conflictingTerms = false;

		// Checks if any terms exists in both sections
		for (Term term : terms) {
			if (newTerms.contains(term)) {
				conflictingTerms = true;
			}
		}

		// The classes are not even in the same days of the year
		// There are no conflict here
		if (conflictingTerms == false)
			return false;

		for (Period period : section.periods) {
			for (Period newPeriod : newSection.periods) {
				if (hasConflitcs(period, newPeriod))
					return true;
			}
		}

		return false;

	}
	
	private static boolean containsAny( HashSet<DayOfWeek> days1, HashSet<DayOfWeek> days2 ){
		for( DayOfWeek day : days1 ){
			if( days2.contains(day)){
				return true;
			}
		}
		return false;
	}

	private boolean hasConflitcs(Period period, Period newPeriod) {

		//No days in common, no conflicts		
		if(!containsAny(period.days, newPeriod.days))
			return false;

		Time periodStart = period.startTime;
		Time periodEnd = period.endTime;

		Time otherStart = newPeriod.startTime;
		Time otherEnd = newPeriod.endTime;

		return (otherStart.compareTo(periodStart) >= 0 && otherStart.compareTo(periodEnd) <= 0)
				|| (otherEnd.compareTo(periodStart) >= 0 && otherEnd.compareTo(periodEnd) <= 0)
				|| (otherStart.compareTo(periodStart) <= 0 && otherEnd.compareTo(periodEnd) >= 0);
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

}
