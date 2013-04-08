package edu.wpi.scheduler.client.permutation.view;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

import edu.wpi.scheduler.client.controller.SchedulePermutation;
import edu.wpi.scheduler.client.generator.AbstractProblem;
import edu.wpi.scheduler.client.generator.ProducerUpdateEvent.UpdateType;
import edu.wpi.scheduler.client.generator.ScheduleProducer;
import edu.wpi.scheduler.client.generator.ScheduleProducer.ProducerEventHandler;
import edu.wpi.scheduler.client.permutation.PermutationController;

public class ConflictResolverWidget extends FlowPanel implements ProducerEventHandler {

	public final PermutationController controller;

	/*
	public class ConflictCourse extends FlexTable {

		private Course course;

		public ConflictCourse(Course course) {
			this.course = course;
			setSize("100%", "100%");
			FlexCellFormatter cellFormatter = getFlexCellFormatter();
			setHTML(0, 0, "<h3>" + course.toString() + "</h3>");
			addStyleName("ConflictResolverTable");
			
			cellFormatter.setColSpan(0, 0, 2);
			cellFormatter.setAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
			cellFormatter.setHeight(0, 0, "30px");

			PeriodSelectList selectList = new PeriodSelectList(controller);
			selectList.setSections(course.sections, true);

			setWidget(1, 0, selectList);
			setWidget(1, 1, getResolveButtons());
			
			cellFormatter.setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);
			cellFormatter.setVerticalAlignment(1, 1, HasVerticalAlignment.ALIGN_TOP);
			
			cellFormatter.setWidth(1, 0, "75%");
			cellFormatter.setWidth(1, 1, "25%");
		}

		private FlowPanel getResolveButtons() {
			FlowPanel flow = new FlowPanel();
			final SectionProducer sectionProducer = controller.getStudentSchedule().getSectionProducer(course);

			flow.add(new Label("Possible solutions:"));

			// Are there any sections we can enable?
			if (sectionProducer.hasDeniedSection()) {
				Button button = new Button("Enable all sections", new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						for (Section section : course.sections)
							sectionProducer.removeDenySection(section);
					}
				});

				flow.add(button);
			}

			// We can always disable this course
			Button button = new Button("Disable course", new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					for (Section section : course.sections)
						sectionProducer.denySection(section);
				}
			});
			flow.add(button);

			return flow;
		}

	}
	*/
	
	public ArrayList<ConflictWidget> conflicts = new ArrayList<ConflictWidget>();
	
	public class ConflictWidget extends ComplexPanel {
		
		public final SchedulePermutation permutation;
		final Element titleElem = DOM.createDiv();
		final Element descriptionelem = DOM.createDiv();

		public ConflictWidget( SchedulePermutation permutation ){
			setElement(DOM.createDiv());
			this.permutation = permutation;
			
			String title = "";
			
			for(AbstractProblem problem : permutation.solutions ){
				title += problem.getTitle() + "<br>";
				
				Element problemDesc = DOM.createDiv();
				problemDesc.setInnerText(problem.getDescription());
				descriptionelem.appendChild(problemDesc);
			}
			
			titleElem.getStyle().setFontWeight(FontWeight.BOLD);
			descriptionelem.getStyle().setMarginLeft(20, Unit.PX);
			
			titleElem.setInnerHTML(title);
			
			getElement().appendChild(titleElem);
			getElement().appendChild(descriptionelem);
			
			setStyleName("ConflictResolverItem");
		}
		
		
		
	}
	
	ScheduleProducer producer;

	public ConflictResolverWidget(PermutationController controller) {
		this.controller = controller;
		
		getElement().getStyle().setOverflowY(Overflow.SCROLL);
		getElement().getStyle().setLeft(0, Unit.PX);
		getElement().getStyle().setRight(0, Unit.PX);
		getElement().getStyle().setTop(0, Unit.PX);
		getElement().getStyle().setBottom(0, Unit.PX);
		getElement().getStyle().setPosition(Position.ABSOLUTE);
		
		update();
	}
	
	@Override
	protected void onLoad() {
		controller.addProduceHandler(this);
		update();
	}

	@Override
	protected void onUnload() {
		controller.removeProduceHandler(this);
	}
	
	public void addPermutation(SchedulePermutation permutation){
		//Check if we already do not have the permutation
		
		for( ConflictWidget widget : conflicts ){
			if( widget.permutation.equalSolution(permutation) )
				return;
		}
		
		ConflictWidget widget = new ConflictWidget(permutation);
		
		add( widget, getElement() );
		conflicts.add(widget);
	}
	
	public void update(){
		this.clear();
		conflicts.clear();
		
		ScheduleProducer producer = controller.getProducer();
		
		if( producer.getCourses().size() == 0 ){
			add(new Label("There are no courses selected. Add a course/enable a section of a course first."), getElement());
		} else {
			this.producer = new  ScheduleProducer(producer);
			this.producer.setMaxSolutions(30);
			
			for( int i = 0; i < 100000 && this.producer.canGenerate(); i++)
				this.producer.step();
			
			List<SchedulePermutation> permutations = this.producer.getPermutations();
			
			for( SchedulePermutation permutation : permutations ){
				//add( new Label(permutation.solutions.get(0).solutionDescription()), getElement());
				addPermutation(permutation);
			}
		}
		
		
	}

	@Override
	public void onPermutationUpdated(UpdateType type) {
		if( type == UpdateType.FINISH )
			update();
	}

}
