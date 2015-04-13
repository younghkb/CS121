package logging;

public class Duration {
	
	long start;
	
	public Duration() {
		start = System.currentTimeMillis();
	}
	
	public long lap() {
		return System.currentTimeMillis() - start;
	}
}
