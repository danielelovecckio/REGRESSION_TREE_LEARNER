package database;

/**
 * @author Daniele Lovecchio
 *
 */
@SuppressWarnings("serial")
public class EmptyTypeException extends Exception{
	public EmptyTypeException() {
		super("Empty Type Exception");
	}
	public EmptyTypeException(String e) {
		super(e);
	}
}
