package edu.wpi.scheduler.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.wpi.scheduler.client.GreetingService;
import edu.wpi.scheduler.shared.FieldVerifier;
import edu.wpi.scheduler.shared.model.ScheduleDB;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	public String greetServer(String input) throws IllegalArgumentException {
		// Verify that the input is valid.
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back
			// to
			// the client.
			throw new IllegalArgumentException(
					"Name must be at least 4 characters long");
		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script
		// vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);

		return "Hello, " + input + "!<br><br>I am running " + serverInfo
				+ ".<br><br>It looks like you are using:<br>" + userAgent;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html
	 *            the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	@Override
	public void updateDatabase(ScheduleDB db) {
		try {
			File schedDbFile = new File("scheddb.ser");
			
			System.out.println(schedDbFile.getAbsolutePath());
			
			FileOutputStream fileOut = new FileOutputStream(schedDbFile);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(db);
			out.close();
			fileOut.close();
		} catch (Exception i) {
			i.printStackTrace();
		}

	}

	@Override
	public ScheduleDB getDatabase() throws Exception {
		File schedDbFile = new File("scheddb.ser");
		
		if( !schedDbFile.exists() )
			return null;
		
		ScheduleDB db;
		
		FileInputStream fileIn = new FileInputStream(schedDbFile);
		ObjectInputStream in = new ObjectInputStream(fileIn);
		db = (ScheduleDB) in.readObject();
		in.close();
		fileIn.close();
		
		return db;
	}
}
