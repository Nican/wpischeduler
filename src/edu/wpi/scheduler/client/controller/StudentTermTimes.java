package edu.wpi.scheduler.client.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.storage.client.Storage;

import edu.wpi.scheduler.shared.model.DayOfWeek;
import edu.wpi.scheduler.shared.model.Term;
import edu.wpi.scheduler.shared.model.Time;
import edu.wpi.scheduler.shared.model.TimeCell;

public class StudentTermTimes 
{
	public final StudentChosenTimes ATermTimes;
	public final StudentChosenTimes BTermTimes;
	public final StudentChosenTimes CTermTimes;
	public final StudentChosenTimes DTermTimes;

	public StudentTermTimes(StudentSchedule studentSchedule) 
	{
		ATermTimes = new StudentChosenTimes(studentSchedule);
		BTermTimes = new StudentChosenTimes(studentSchedule);
		CTermTimes = new StudentChosenTimes(studentSchedule);
		DTermTimes = new StudentChosenTimes(studentSchedule);
		this.loadStudentTermTimes();
	}

	public StudentChosenTimes getTimesForTerm(Term t)
	{
		switch(t)
		{
		case A: return ATermTimes;
		case B: return BTermTimes;
		case C: return CTermTimes;
		case D: return DTermTimes;
		default: return null;
		}
	}

	private static class StudentTermTimesData extends JavaScriptObject 
	{
		protected StudentTermTimesData() {}

		// Term = Array of Days {Monday, Tuesday, Wednesday, Thursday, Friday}
		// Day = Array of times {string, string, string...}
		public final native void setTimes(String term, String day, JsArrayString times) 
		/*-{
			if(this[term] === undefined )
				this[term] = {};

			this[term][day] = times;
		}-*/;

		public final native JsArrayString getTimes(String term, String day) 
		/*-{
			return this[term][day];
		}-*/;
	}

	public void saveStudentTermTimes() 
	{
		StudentTermTimesData termTimesData = JavaScriptObject.createObject().cast();
		// For each term
		for(Term t : Term.values()) 
		{
			String term = t.name;
			StudentChosenTimes termTimes = this.getTimesForTerm(t);
			// For each day
			for(int i=TimeCell.START_DAY; i<=TimeCell.NUM_DAYS; i++)
			{
				String day = TimeCell.week[i].getShortName();
				List<Time> dayTimes = termTimes.getTimes(TimeCell.week[i]);
				JsArrayString dayTimesData = JavaScriptObject.createArray().cast();
				dayTimesData.setLength(dayTimes.size());
				// For each time
				for(int j=0; j<dayTimes.size(); j++)
				{
					dayTimesData.set(j, dayTimes.get(j).toString());
				}
				// Set the times for this [term][day]
				termTimesData.setTimes(term, day, dayTimesData);
			}
		}
		Storage localStorage = Storage.getLocalStorageIfSupported();
		// Set local storage data
		if (localStorage != null) 
		{
			JSONObject jsonObj = new JSONObject(termTimesData);
			localStorage.setItem("chosenTimes", jsonObj.toString());
		}
	}

	public void loadStudentTermTimes()
	{
		Storage localStorage = Storage.getLocalStorageIfSupported();
		if (localStorage == null) return;

		String jsonObj = localStorage.getItem("chosenTimes");
		if (jsonObj == null) return;

		try 
		{
			StudentTermTimesData termTimesData = JsonUtils.unsafeEval(jsonObj).cast();
			for(Term t : Term.values())
			{
				HashMap<DayOfWeek, List<Time>> termTimes = new HashMap<DayOfWeek, List<Time>>();
				for(int i = TimeCell.START_DAY; i <= TimeCell.NUM_DAYS; i++)
				{
					JsArrayString timesData = termTimesData.getTimes(t.name, TimeCell.week[i].getShortName()).cast();
					List<Time> times = new ArrayList<Time>();
					for(int j=0; j < timesData.length(); j++)
					{
						times.add(new Time(timesData.get(j)));
					}
					termTimes.put(TimeCell.week[i], times);
				}
				this.getTimesForTerm(t).set(termTimes);
			}	
		} 
		catch (IllegalArgumentException e) 
		{
			e.printStackTrace();
		}
	}
}
