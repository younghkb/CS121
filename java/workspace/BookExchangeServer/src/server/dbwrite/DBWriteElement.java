package server.dbwrite;


/**
 * Abstract class for DBWriteQueue
 */
public abstract class DBWriteElement {	
	abstract String getSQL();
}