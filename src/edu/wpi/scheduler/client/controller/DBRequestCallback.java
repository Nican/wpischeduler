package edu.wpi.scheduler.client.controller;

import edu.wpi.scheduler.client.model.ScheduleDB;

public interface DBRequestCallback {
	
	/**
	 * Called when the request was successful
	 * @param database
	 */
	void OnSuccess( ScheduleDB database );
	
	/**
	 * If the client is unable to request, or parse, the database file.
	 * @param failure
	 */
	void OnFailure( Throwable failure );
	
}
