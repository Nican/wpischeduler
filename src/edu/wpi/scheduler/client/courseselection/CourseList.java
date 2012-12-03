package edu.wpi.scheduler.client.courseselection;

import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.shared.model.Course;
import edu.wpi.scheduler.shared.model.Department;

public class CourseList extends ComplexPanel {

	public CourseList(final CourseSelectionController selectionController, Department department) {

		this.setElement(DOM.createTable());

		for( Course course : department.courses ){
			CourseListItemBase item = new CourseListItemBase(selectionController, course);
			
			item.add("128px", new TermView(course));
			item.add(null, new Label(fixCase(course.name)));
			item.add(null, new Label(course.sections.get(0).term));

			this.add( item );
		}

		this.setStyleName("courseList");

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
