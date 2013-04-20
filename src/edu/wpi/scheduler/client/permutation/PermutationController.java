package edu.wpi.scheduler.client.permutation;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.Timer;

import edu.wpi.scheduler.client.controller.ConflictController;
import edu.wpi.scheduler.client.controller.SchedulePermutation;
import edu.wpi.scheduler.client.controller.SectionProducer;
import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.client.controller.StudentScheduleEvent;
import edu.wpi.scheduler.client.controller.StudentScheduleEventHandler;
import edu.wpi.scheduler.client.generator.ProducerUpdateEvent;
import edu.wpi.scheduler.client.generator.ProducerUpdateEvent.UpdateType;
import edu.wpi.scheduler.client.generator.ScheduleProducer;
import edu.wpi.scheduler.client.generator.ScheduleProducer.ProducerEventHandler;
import edu.wpi.scheduler.client.permutation.view.PeriodDescriptionDialogBox;
import edu.wpi.scheduler.shared.model.Course;
import edu.wpi.scheduler.shared.model.DayOfWeek;
import edu.wpi.scheduler.shared.model.Section;

public class PermutationController implements HasHandlers, StudentScheduleEventHandler {

	private HandlerManager handlerManager = new HandlerManager(this);

	public final StudentSchedule studentSchedule;

	protected List<DayOfWeek> validDayOfWeek =
			Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY);

	/**
	 * Current selected schedule to be displayed
	 */
	protected SchedulePermutation selectedPermutation;

	/**
	 * To quickly display the section times on the course view
	 */
	protected Section selectedSection = null;

	/**
	 * Generate schedules from the conflicts
	 */
	private ScheduleProducer producer;
	
	/**
	 * Timer that will generate more schedules as needed
	 */
	Timer timer = new Timer() {
		@Override
		public void run() {
			generateSchedules();
		}
	};

	public final String[] colors = new String[] {
			"rgb(172, 114, 94)", "rgb(250, 87, 60)", "rgb(255, 173, 70)",
			"rgb(66, 214, 146)", "rgb(123, 209, 72)", "rgb(154, 156, 255)",
			"rgb(179, 220, 108)", "rgb(202, 189, 191)",
			"rgb(251, 233, 131)", "rgb(205, 116, 230)", "rgb(194, 194, 194)",
			"rgb(159, 225, 231)", "rgb(246, 145, 178)", "#92E1C0",
			"rgb(251, 233, 131)", "#7BD148", "rgb(159, 198, 231)" };

	public PermutationController(StudentSchedule studentSchedule) {
		this.studentSchedule = studentSchedule;
		updateProducer();

		studentSchedule.addStudentScheduleHandler(this);
	}

	public String getCourseColor(Course course) {

		for (int i = 0; i < studentSchedule.sectionProducers.size() && i < colors.length; i++) {
			SectionProducer producer = studentSchedule.sectionProducers.get(i);
			if (producer.getCourse().equals(course)) {
				return colors[i];
			}
		}

		return "rgb(255,255,255)";
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	public StudentSchedule getStudentSchedule() {
		return studentSchedule;
	}

	public HandlerRegistration addSelectListner(PermutationSelectEventHandler handler) {
		return handlerManager.addHandler(PermutationSelectEvent.TYPE, handler);
	}

	public void removeSelectListner(PermutationSelectEventHandler handler) {
		handlerManager.removeHandler(PermutationSelectEvent.TYPE, handler);
	}
	
	public HandlerRegistration addProduceHandler(ProducerEventHandler handler) {
		return handlerManager.addHandler(ProducerUpdateEvent.TYPE, handler);
	}

	public void removeProduceHandler(ProducerEventHandler handler) {
		handlerManager.removeHandler(ProducerUpdateEvent.TYPE, handler);
	}




	public List<DayOfWeek> getValidDaysOfWeek() {
		return validDayOfWeek;
	}

	

	public void selectPermutation(SchedulePermutation permutation) {

		if (selectedPermutation != null && selectedPermutation.equals(permutation))
			return;

		this.selectedPermutation = permutation;

		this.fireEvent(new PermutationSelectEvent());
	}

	public SchedulePermutation getSelectedPermutation() {
		return selectedPermutation;
	}

	public Section getSelectedSection() {
		return this.selectedSection;
	}

	public void setSelectedSection(Section section) {
		if (this.selectedSection != null && this.selectedSection.equals(section))
			return;
		
		this.selectedSection = section;
		
		this.fireEvent(new PermutationSelectEvent());
	}

	@Override
	public void onCoursesChanged(StudentScheduleEvent studentScheduleEvent) {
		updateProducer();
	}
	
	public ConflictController getConflictController(){
		return getStudentSchedule().conflicts;
	}
	
	public void generateSchedules(){
		int count = producer.getPermutations().size();
		
		for(int i = 0; i < 30 && producer.canGenerate(); i++){
			producer.step();
		}
		
		int newCount = producer.getPermutations().size();
		
		if( newCount != count ){
			fireEvent(new ProducerUpdateEvent(UpdateType.UPDATE));
		}
		
		if(getSelectedPermutation() == null && newCount > 0){
			selectPermutation(producer.getPermutations().get(0));
		}
		
		if(!producer.canGenerate() || newCount > 300 ){
			fireEvent(new ProducerUpdateEvent(UpdateType.FINISH));
			timer.cancel();
		}
	}

	private void updateProducer() {
		producer = new ScheduleProducer(this);
		fireEvent(new ProducerUpdateEvent(UpdateType.NEW));
		
		selectedPermutation = null;
		
		generateSchedules();
		timer.scheduleRepeating(10);
		
		if( producer.getPermutations().size() == 0 )
			selectPermutation(null);
	}

	public ScheduleProducer getProducer() {
		return producer;
	}

	public void displayDescription(Section section) {
		PeriodDescriptionDialogBox dialog = new PeriodDescriptionDialogBox(this, section);
		dialog.setGlassEnabled(true);
		dialog.center();
		dialog.show();
	}
}
