package edu.wpi.scheduler.client.permutation.view;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

import edu.wpi.scheduler.client.controller.SchedulePermutation;
import edu.wpi.scheduler.client.generator.AbstractProblem;
import edu.wpi.scheduler.client.generator.ProducerUpdateEvent.UpdateType;
import edu.wpi.scheduler.client.generator.ScheduleProducer;
import edu.wpi.scheduler.client.generator.ScheduleProducer.ProducerEventHandler;
import edu.wpi.scheduler.client.permutation.PermutationController;

public class ConflictResolverWidget extends FlowPanel implements
		ProducerEventHandler {

	public final PermutationController controller;
	public ArrayList<ConflictWidget> conflicts = new ArrayList<ConflictWidget>();
	public FlowPanel conflictList = new FlowPanel();

	public class ConflictWidget extends ComplexPanel implements ClickHandler {

		public final SchedulePermutation permutation;
		final Element titleElem = DOM.createDiv();
		final Element descriptionelem = DOM.createDiv();

		public ConflictWidget(SchedulePermutation permutation) {
			setElement(Document.get().createDivElement());
			this.permutation = permutation;

			String title = "";

			for (AbstractProblem problem : permutation.solutions) {
				title += problem.getTitle() + "<br>";

				Element problemDesc = DOM.createDiv();
				problemDesc.setInnerHTML(problem.getDescription());
				descriptionelem.appendChild(problemDesc);
			}

			titleElem.getStyle().setFontWeight(FontWeight.BOLD);
			descriptionelem.getStyle().setMarginLeft(20, Unit.PX);

			titleElem.setInnerHTML(title);

			getElement().appendChild(titleElem);
			getElement().appendChild(descriptionelem);

			Button button = new Button("APPLY!", this);
			add(button, getElement());

			setStyleName("ConflictResolverItem");
		}

		@Override
		public void onClick(ClickEvent event) {
			for (AbstractProblem problem : permutation.solutions) {
				problem.applySolution(controller.studentSchedule);
			}
		}
	}

	ScheduleProducer producer;

	Timer timer = new Timer() {
		@Override
		public void run() {
			generate();
		}
	};

	public ConflictResolverWidget(PermutationController controller) {
		this.controller = controller;

		getElement().getStyle().setOverflowY(Overflow.SCROLL);
		getElement().getStyle().setLeft(0, Unit.PX);
		getElement().getStyle().setRight(0, Unit.PX);
		getElement().getStyle().setTop(0, Unit.PX);
		getElement().getStyle().setBottom(0, Unit.PX);
		getElement().getStyle().setPosition(Position.ABSOLUTE);
		
		
		Element title = DOM.createDiv();
		title.setInnerHTML("<h2>No schedules can be generated. :(<br>We are finding a few solutions.");
		title.getStyle().setTextAlign(TextAlign.CENTER);
		
		getElement().appendChild(title);
		add(conflictList, getElement());

		updateProducer();
	}

	private void updateProducer() {
		producer = new ScheduleProducer(controller.getProducer());
		producer.maxSolutions = 1;
		update();
		timer.scheduleRepeating(10);
	}

	@Override
	protected void onLoad() {
		controller.addProduceHandler(this);
		updateProducer();
	}

	@Override
	protected void onUnload() {
		controller.removeProduceHandler(this);
		timer.cancel();
	}

	public void addPermutation(SchedulePermutation permutation) {
		// Check if we already do not have the permutation

		for (ConflictWidget widget : conflicts) {
			if (widget.permutation.equalSolution(permutation))
				return;
		}

		ConflictWidget widget = new ConflictWidget(permutation);

		conflictList.add(widget);
		conflicts.add(widget);
	}

	private void generate() {
		for (int i = 0; i < 2500 && producer.canGenerate(); i++)
			producer.step();

		if (!producer.canGenerate()) {
			if (producer.getPermutations().size() == 0 && producer.maxSolutions < 10 ) {
				ScheduleProducer oldProducer = producer;

				producer = new ScheduleProducer(oldProducer);
				producer.maxSolutions = oldProducer.maxSolutions + 1;
			} else {
				timer.cancel();
			}

			update();
		}
	}

	public void update() {
		conflictList.clear();
		conflicts.clear();

		ScheduleProducer producer = controller.getProducer();

		if (producer.getCourses().size() == 0) {
			conflictList.add(new Label(
					"There are no courses selected. Add a course/enable a section of a course first."));
			return;
		}

		List<SchedulePermutation> permutations = this.producer
				.getPermutations();

		if (permutations.size() == 0) {
			Label lbl = new Label("Attempting to find solution with "
					+ this.producer.maxSolutions + " steps.");

			conflictList.add(lbl);
			return;
		}

		for (SchedulePermutation permutation : permutations) {
			// add( new
			// Label(permutation.solutions.get(0).solutionDescription()),
			// getElement());
			addPermutation(permutation);
		}

	}

	@Override
	public void onPermutationUpdated(UpdateType type) {
		if (type == UpdateType.FINISH)
			updateProducer();
	}

}
