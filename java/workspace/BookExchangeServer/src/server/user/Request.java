package server.user;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Request implements Serializable {
	public Type type;
	public Map<String, Object> params = new HashMap<String, Object>();
	public Object reply;
	
	public Request(Type type) {
		this.type = type; 
	}
	
	public static enum Type {
		SEARCH_BOOK,
		GET_BOOK,
		GET_PUBLIC_EXCHANGES,
		GET_PRIVATE_EXCHANGES,
		CREATE_EXCHANGE,
		UPDATE_EXCHANGE_BORROWER,
		UPDATE_EXCHANGE_LOANER,
		UPDATE_EXCHANGE_STATUS;
	}
	
	public String toString() {
		return String.format("Type: %s, Parameters: %s, Reply: %s", type, params, reply);
	}
}
