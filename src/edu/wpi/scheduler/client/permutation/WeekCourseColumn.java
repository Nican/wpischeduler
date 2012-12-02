package edu.wpi.scheduler.client.permutation;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;

import edu.wpi.scheduler.client.controller.SchedulePermutation;
import edu.wpi.scheduler.shared.model.DayOfWeek;
import edu.wpi.scheduler.shared.model.Period;
import edu.wpi.scheduler.shared.model.Section;

public class WeekCourseColumn extends ComplexPanel {

	private SchedulePermutation permutation;
	private DayOfWeek day;
	private int startHour = 8;
	private int endHour = 16;

	public WeekCourseColumn(SchedulePermutation permutation, DayOfWeek day) {
		this.setElement(DOM.createDiv());
		
		this.permutation = permutation;
		this.day = day;
		
		for( Section section : this.permutation.sections ){
			for( Period period : section.periods ){
				
				if( period.days.contains(this.day))
					addPeriod( period );
				
			}
		}
	}

	private void addPeriod(Period period) {
		
		PeriodItem item = new PeriodItem( period.professor );
		
		Style periodStyle = item.getElement().getStyle();
		
		periodStyle.setPosition(Position.ABSOLUTE);
		periodStyle.setTop( ((double)(period.startTime.hour - startHour))/((double)(endHour-startHour)) * 100 , Unit.PCT);
		
		add( item, getElement() );
	}
	
	
	
	

}
