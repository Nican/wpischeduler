package edu.wpi.scheduler.client.storage;

import com.google.gwt.user.client.Window;

import edu.wpi.scheduler.client.Scheduler;
import edu.wpi.scheduler.client.controller.SchedulePermutation;
import edu.wpi.scheduler.shared.model.Section;

public class StorageSharing {

	public static String getShareCode(SchedulePermutation permutation) {
		assert (permutation.solutions.size() == 0);

		String output = "01"; // Version

		for (Section section : permutation.sections) {
			output += getCrnHex(section.crn);
		}

		return output;
	}

	private static String getCrnHex(int crn) {
		String hex = Integer.toHexString(crn).toUpperCase();

		while (hex.length() < 5)
			hex = "0" + hex;

		if (hex.length() > 5)
			Window.alert("Error! CRN is too large" + crn);

		return hex;

	}

	public static SchedulePermutation getPermutation(String code) {
		assert (code.substring(0, 2).equals("01"));

		SchedulePermutation permutation = new SchedulePermutation();

		for (int i = 2; i < code.length(); i += 5) {
			int crn = Integer.parseInt(code.substring(i, i + 5), 16);

			Section section = Scheduler.getDatabase().getSectionByCRN(crn);

			if (section != null)
				permutation.sections.add(section);
		}

		return permutation;
	}
}
