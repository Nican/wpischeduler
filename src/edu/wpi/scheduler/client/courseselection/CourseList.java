package edu.wpi.scheduler.client.courseselection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.shared.model.Course;
import edu.wpi.scheduler.shared.model.Department;

public class CourseList extends ComplexPanel {
	
	private final CourseSelectionController selectionController;
	
	private static class CourseComparator implements Comparator<CourseListItemBase>{

		@Override
		public int compare(CourseListItemBase o1, CourseListItemBase o2) {
			Course course1 = o1.getCourse();
			Course course2 = o2.getCourse();
			
			return course1.number.compareToIgnoreCase(course2.number);			
		}
		
	}

	public CourseList(CourseSelectionController selectionController) {
		this.setElement(DOM.createTable());
		this.selectionController = selectionController;

		this.setStyleName("courseList");
	}
	
	public void addDeparment( Department department ){
		for( Course course : department.courses ){
			CourseListItemBase item = new CourseListItemBase(selectionController, course);
			
			item.add("128px", new TermView(course));
			item.add(null, fixCase(course.name));
			item.add(null, course.sections.get(0).term);

			this.add( item );
		}
	}
	
	public void sort(){
		ArrayList<CourseListItemBase> widgets = new ArrayList<CourseListItemBase>();
		
		for( Widget widget : this.getChildren() ){
			widgets.add( (CourseListItemBase) widget );
		}
		
		Collections.sort(widgets, new CourseComparator() );
		
		this.clear();
		
		for( Widget widget : widgets ){
			this.add(widget);
		}		
	}

	@Override
	public void add( Widget child ){
		this.add( child, this.getElement() );
	}
	
	public static String fixCase(String input){
		RegExp reg = RegExp.compile("^I+V*$");
		// Break it into words
		String strings[] = input.split("\\s+");
		for (int i = 0; i < strings.length; i++){
			// If its I, II, etc, just leave it uppercase
			if(!reg.test(strings[i])){
				// Make it all lowercase
				strings[i] = strings[i].toLowerCase();
				// If it should be capitalized, capitalize it
				if(shouldBeCapitalized(strings[i])){
					if(strings[i].length() > 1)
						strings[i] = Character.toUpperCase(strings[i].charAt(0)) + strings[i].substring(1);
					else
						strings[i] = Character.toUpperCase(strings[i].charAt(0)) + "";
				}
			}


		}
		String returnstring = strings[0];
		for(int i = 1; i < strings.length; i++){
			returnstring = returnstring + " " + strings[i];
		}
		return returnstring;
	}


	public static boolean shouldBeCapitalized(String s){
		if(s.compareTo("a") == 0 || s.compareTo("and") == 0 || s.compareTo("the") == 0 || s.compareTo("in") == 0 || s.compareTo("an") == 0 || s.compareTo("or") == 0 || s.compareTo("at") == 0 || s.compareTo("of") == 0)
			return false;
		return true;
	}

}
