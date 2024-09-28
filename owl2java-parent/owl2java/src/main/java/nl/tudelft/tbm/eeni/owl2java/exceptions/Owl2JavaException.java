package nl.tudelft.tbm.eeni.owl2java.exceptions;

import java.io.Serial;

public class Owl2JavaException extends Exception {

	@Serial
	private static final long serialVersionUID = 1903386932364770194L;

	public Owl2JavaException() {
		super();
	}

	public Owl2JavaException(String arg0) {
		super(arg0);
	}

	public Owl2JavaException(Throwable arg0) {
		super(arg0);
	}

	public Owl2JavaException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
