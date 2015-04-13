import java.net.*;
import java.io.*;

class testobject implements Serializable {
	int value;
	String id;

	public testobject(int v, String s) {
		this.value = v;
		this.id = s;
	}
}
