package logging;

import java.text.SimpleDateFormat;
import java.util.Date;

// SimpleLog
public abstract class Log {
	
	private final static Mode MODE = Mode.STDOUT;
	private final static String LOGNAME = "out.log";
	//private final static PrintWriter pw;

	//TODO look into java.util.logging?
	//TODO pipe logging
	//TODO ignore list
	
	private final static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	// pad left  	String.format("%10s", "Hello"
	// pad right 	String.format("%-10s", "Hello"
	
	public static void main(String[] args) {
		log("SIMPLELOG", "TESTING", "SIMPLE LOG TEST");
		log("SIMPLELOG", "TESTING", "SIMPLE LOG TEST");
		log("SIMPLELOG", "TESTING", "SIMPLE LOG TEST");
		log("SIMPLELOG", "TESTING", "SIMPLE LOG TEST");
	}
	
//	public static void log(String calling, String entry) {
//		String msg = df.format(new Date()) + "  | " + String.format("%-12s | ", calling) + String.format("%-60s |", entry);
//		switch (MODE) {
//		case STDOUT:
//			System.out.println(msg);
//		case FILE:
//			//TODO implement
//		case BOTH:
//			//TODO implement
//		}
//	}

	public static void event(String event) {
		System.out.println(event); // TODO make more standard
	}
	
	public static void log(String calling, String entry, String details) {
		//String msg = "| " + df.format(new Date()) + " | " + String.format("%-16s | ", calling) + String.format("%-16s | ", entry) + String.format("%-64s |", details);
		
		if (calling.length() > 16) { calling = calling.substring(0, 16); }; // TODO make 16 a final variable
		if (entry.length() > 16) { entry = entry.substring(0, 16); };
		
		String msg = df.format(new Date()) + " | " + String.format("%-16s | ", calling) + String.format("%-16s | ", entry) + details;
		switch (MODE) {
		case STDOUT:
			stdout(msg);
		case FILE:
			//TODO implement
		case BOTH:
			//TODO implement
		}
	}
	
	private static void stdout(String msg) {
		System.out.println(msg);
	}
	
	private static void file(String msg) {
		// TODO implement
	}
	
	private static void stream(String msg) {
		// TODO implement
	}
	
	private static enum Mode {
		STDOUT,
		FILE,
		BOTH, // TODO better name
		STREAM;
	}
}
