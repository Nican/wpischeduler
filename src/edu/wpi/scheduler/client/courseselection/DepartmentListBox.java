package edu.wpi.scheduler.client.courseselection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.OptGroupElement;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.ui.ListBox;

import edu.wpi.scheduler.client.Scheduler;
import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.shared.model.Department;

public class DepartmentListBox extends ListBox {

	public static class AcademicGroup {
		public final String name;
		public final List<String> departments;

		public AcademicGroup(String name, String... depts) {
			this.name = name;
			this.departments = Arrays.asList(depts);
		}
	}

	private Map<Department, OptionElement> options = new HashMap<Department, OptionElement>();

	private static final List<AcademicGroup> groups = Arrays.asList(
			new AcademicGroup("Science", "MA", "PH", "AS", "BB", "BCB", "CH", "CS", "ES", "GE"),
			new AcademicGroup("Engineering", "ECE", "RBE", "AREN", "BME", "CE", "CHE", "ME", "MFE", "MTE", "NSE", "OIE", "FP", "SYS"),
			new AcademicGroup("Language", "GN", "AB", "CN", "EN", "ESL", "SP"),
			new AcademicGroup("Humanities", "PY", "SOC", "AR", "HI", "HU", "MU", "RE"),
			new AcademicGroup("Social Science", "ECON", "SS", "PSY")
			);

	public DepartmentListBox(StudentSchedule studentSchedule) {
		super(true); // Create is a multi-select
		
		this.getElement().getStyle().setHeight(100.0, Unit.PCT);
	}

	private AcademicGroup findAcademicGroup(Department department) {
		for (AcademicGroup group : groups) {
			if (group.departments.contains(department.abbreviation))
				return group;
		}
		return null;
	}

	public void update() {
		clear();

		Map<AcademicGroup, OptGroupElement> groupElements = new HashMap<AcademicGroup, OptGroupElement>();
		List<String> savedDeps = savedSelectedDepartments();
		SelectElement select = getElement().cast();

		for (AcademicGroup group : groups) {
			OptGroupElement groupElem = Document.get().createOptGroupElement();
			groupElem.setLabel(group.name);

			groupElements.put(group, groupElem);
			select.appendChild(groupElem);
		}

		OptGroupElement otherGroup = Document.get().createOptGroupElement();
		select.appendChild(otherGroup);
		otherGroup.setLabel("Other");
		groupElements.put(null, otherGroup);

		for (Department dept : Scheduler.getDatabase().departments) {
			AcademicGroup deptGroup = findAcademicGroup(dept);
			OptionElement option = Document.get().createOptionElement();

			setOptionText(option, dept.name, null);
			option.setValue(dept.name);

			groupElements.get(deptGroup).appendChild(option);

			if (savedDeps == null ){
				if( dept.abbreviation.equals("MA"))
					option.setSelected(true);
				
			} else if( savedDeps.contains(dept.abbreviation))
				option.setSelected(true);

			options.put(dept, option);
		}

	}

	public List<Department> getSelectedDepartments() {
		List<Department> depts = new ArrayList<Department>();
		JsArrayString arr = JavaScriptObject.createArray().cast();

		for (Entry<Department, OptionElement> entry : options.entrySet()) {
			if (entry.getValue().isSelected()) {
				depts.add(entry.getKey());
				arr.push(entry.getKey().abbreviation);
			}
		}

		Storage localStorage = Storage.getLocalStorageIfSupported();

		if (localStorage != null) {
			JSONArray jsonArr = new JSONArray(arr);
			localStorage.setItem("selectedDepts", jsonArr.toString());
		}

		return depts;
	}

	private List<String> savedSelectedDepartments() {
		Storage localStorage = Storage.getLocalStorageIfSupported();
		List<String> deparments = new ArrayList<String>();

		if (localStorage == null)
			return null;

		String depList = localStorage.getItem("selectedDepts");

		if (depList == null)
			return null;

		try {
			JsArrayString selectedDepsJs = JsonUtils.unsafeEval(depList);

			for (int i = 0; i < selectedDepsJs.length(); i++) {
				deparments.add(selectedDepsJs.get(i));
			}

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		}

		return deparments;
	}

}
