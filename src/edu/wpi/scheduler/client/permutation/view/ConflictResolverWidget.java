package edu.wpi.scheduler.client.permutation.view;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

import edu.wpi.scheduler.client.controller.SchedulePermutation;
import edu.wpi.scheduler.client.generator.ConflictProblem;
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
	
	public class ConflictWidget extends ComplexPanel {
		
		private ConflictProblem problem;

		public ConflictWidget( ConflictProblem problem ){
			setElement(DOM.createDiv());
			this.problem = problem;
			
			
			
		}
		
		
		
	}
	
	ScheduleProducer producer;

	public ConflictResolverWidget(PermutationController controller) {
		this.controller = controller;
		ScheduleProducer producer = controller.getProducer();
		
		if( producer.getCourses().size() == 0 ){
			add(new Label("There are no courses selected. Add a course/enable a section of a course first."), getElement());
		} else {
			this.producer = new  ScheduleProducer(producer);
			this.producer.setMaxSolutions(1);
			
			for( int i = 0; i < 10000 && this.producer.canGenerate(); i++)
				this.producer.step();
			
			for( SchedulePermutation permutation : this.producer.getPermutations() ){
				add( new Label(permutation.solutions.get(0).solutionDescription()), getElement());
			}
		}

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
	
	public void update(){
		//There is nothing to search for, 0 courses have been selected! Just abort.
		if( this.producer == null )
			return;
		
		
		
		
		/*
		FlexCellFormatter cellFormatter = getFlexCellFormatter();
		ScheduleProducer producer = controller.getProducer();
		String header = "<h1>Oops! Unable to generate any schedules!</h1>";

		setSize("100%", "100%");
		getElement().getStyle().setPadding(3.0, Unit.PX);

		cellFormatter.setAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
		cellFormatter.setColSpan(0, 0, 2);
		cellFormatter.setHeight(0, 0, "128px");
		
		setWidget(1, 0, null);
		setWidget(1, 1, null);

		// First, lets check we have any material to generate schedules with!
		if (producer.producedSections.size() == 0) {
			header += "<h2>There are no sections to generate schedules from.</h2>";

		} else {
			CoursePair pair = producer.getConflictCourse();
			
			if( pair != null ){
				header += "<h2>Two courses can not exist in the same schedule.<br>All sections have conflicting times.</h2>";
				
				setWidget(1, 0, new ConflictCourse(pair.course1));
				cellFormatter.setWidth(1, 0, "50%");
				setWidget(1, 1, new ConflictCourse(pair.course2));
				cellFormatter.setWidth(1, 1, "50%");
			} else {
				header += "<h2>Unable to find solution... TODO: Brute force what course to add/remove</h2>";
			}
		}
		

		setHTML(0, 0, header);
		*/
	}

	@Override
	public void onPermutationUpdated(UpdateType type) {
		if( type == UpdateType.FINISH )
			update();
	}

}
