package edu.wpi.scheduler.client.courseselection;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.Duration;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetCollection;

import edu.wpi.scheduler.client.controller.HasCourse;
import edu.wpi.scheduler.client.controller.SectionProducer;
import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.client.controller.StudentScheduleEvent;
import edu.wpi.scheduler.client.controller.StudentScheduleEventHandler;
import edu.wpi.scheduler.client.controller.StudentScheduleEvents;
import edu.wpi.scheduler.shared.model.Course;

public class CourseSelection extends ComplexPanel implements StudentScheduleEventHandler {
	
	private CourseSelectionController selectionController;
	
	class UndoAnimation extends Animation {
		
		private Widget widget;
		public UndoAnimation( Widget w ){
			this.widget = w;
		}

		@Override
		protected void onUpdate(double progress) {
			widget.setHeight((32.0 * (1-progress)) + "px");
		}
		
		@Override
		protected void onComplete() {
			super.onComplete();
			widget.removeFromParent();
		}
		
	}
	
	class UndoRemoveWidget extends ComplexPanel implements HasCourse, ClickHandler{
		private final Course course;
		Element div = DOM.createDiv();

		public UndoRemoveWidget(Course course){
			setElement(DOM.createTR());
			this.course = course;
			
			Element td = DOM.createTD();
			td.setAttribute("colspan", "3");
			
			div.setInnerHTML("Course removed. <br>Click to add " + course.toAbbreviation() + " back" );
			
			td.appendChild(div);
			getElement().appendChild(td);
			getElement().getStyle().setCursor(Cursor.POINTER);
			
			setHeight("32px");	
			
			div.getStyle().setOverflow(Overflow.HIDDEN);
			div.getStyle().setPosition(Position.RELATIVE);
			div.getStyle().setTextAlign(TextAlign.CENTER);
			new UndoAnimation(this).run(500, Duration.currentTimeMillis() + 3000.0);
			
			addDomHandler(this, ClickEvent.getType());
		}
		
		@Override
		public void setHeight( String height ){
			div.getStyle().setProperty("height", height);
		}

		@Override
		public Course getCourse() {
			return course;
		}

		@Override
		public void onClick(ClickEvent event) {
			selectionController.getStudentSchedule().addCourse(course, null);
			removeFromParent();
		}
		
	}

	public CourseSelection(final CourseSelectionController selectionController) {
		this.selectionController = selectionController;

		this.setElement(DOM.createTable());

		this.setStyleName("courseList");

	}

	@Override
	public void add(Widget child) {
		this.add(child, this.getElement());
	}

	/**
	 * Add listeners when the object is added to the document/dom tree
	 */
	@Override
	protected void onLoad() {
		StudentSchedule schedule = selectionController.getStudentSchedule();
		schedule.addStudentScheduleHandler(this);
		
		this.clear();
		
		for( SectionProducer producer : schedule.sectionProducers ){
			addCourse(producer.getCourse());
		}		
	}

	@Override
	protected void onUnload() {
		selectionController.getStudentSchedule().removeStudentScheduleHandler(this);
	}

	@Override
	public void onCoursesChanged(StudentScheduleEvent event) {
		if( event.event == StudentScheduleEvents.ADD){
			CourseSelectionItem item = addCourse(event.getCourse());
			
			if( event.getWidgetSourse() != null ){
				CourseAddAnimation animation = new CourseAddAnimation(item, event.getWidgetSourse());
				animation.run(300);
			}
			
		} else if (event.event == StudentScheduleEvents.REMOVE){
			removeCourse(event.getCourse());
		}
	
	}
	
	private CourseSelectionItem addCourse( Course course ){
		CourseSelectionItem child = new CourseSelectionItem(selectionController, course);

		WidgetCollection children = getChildren();
		
		System.out.println("Starting to compare");
		
		for( int i = 0; i < children.size(); i++ ){
			int comp = CourseList.comparator.compare(child, (HasCourse) children.get(i) );
			//System.out.println("Comparing " + child.getCourse().toAbbreviation() + "  " + 
			//		((HasCourse)children.get(i)).getCourse().toAbbreviation() + " " + comp);
			if( comp <= 0){
				this.insert(child, this.getElement(), i, true);
				return child;
			}
		}
		
		//Could not find a place to insert, put it at the end!
		this.add( child, this.getElement() );
		
		return child;
	}
	
	private void removeCourse( Course course ){
		for (Widget widget : this.getChildren()) {
			HasCourse item = (HasCourse) widget;

			if (item.getCourse().equals(course)) {
				insert(new UndoRemoveWidget(course), getElement(), getWidgetIndex(widget), true);
				remove(widget);
			}
		}
	}

}
