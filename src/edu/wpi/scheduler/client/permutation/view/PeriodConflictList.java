package edu.wpi.scheduler.client.permutation.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Label;

import edu.wpi.scheduler.client.permutation.PeriodSelectListBase;
import edu.wpi.scheduler.client.permutation.PermutationController;
import edu.wpi.scheduler.shared.model.Course;
import edu.wpi.scheduler.shared.model.Section;

public class PeriodConflictList extends PeriodSelectListBase {

	public PeriodConflictList(List<Section> conflictList, PermutationController controller, boolean courseSimpleName ) {
		super(controller);
		
		HashMap<Course, List<Section>> conflictMap = new HashMap<Course, List<Section>>();
		
		for( Section section : conflictList ){
			if( !conflictMap.containsKey(section.course))
				conflictMap.put(section.course, new ArrayList<Section>());
			
			conflictMap.get(section.course).add(section);			
		}
		
		for( Entry<Course, List<Section>> entry : conflictMap.entrySet() ){
			Label label = new Label( courseSimpleName ? entry.getKey().name : entry.getKey().toString() );
			label.getElement().getStyle().setFontWeight(FontWeight.BOLD);
			label.getElement().getStyle().setMarginLeft(4.0, Unit.PX);
			this.add(label);
			
			for( Section section : entry.getValue() ){
				PeriodCheckbox checkbox = new PeriodCheckbox( section, controller.getStudentSchedule().getSectionProducer(section.course) );
				checkbox.getElement().getStyle().setPaddingLeft(8.0, Unit.PX);
				this.add( checkbox );
			}
		}
		
		this.update();
		
	}
	
	public PeriodConflictList(List<Section> conflictList, PermutationController controller ) {
		this( conflictList, controller, false );
	}

}
