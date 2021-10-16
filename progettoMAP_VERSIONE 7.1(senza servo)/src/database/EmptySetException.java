package database;

/**
 * @author Daniele Lovecchio
 *	Modella l'eccezione di resultSet vuoto
 */
@SuppressWarnings("serial")
public class EmptySetException extends Exception {
	public  EmptySetException() {
		super("ResultSet vuoto");
	}
	public  EmptySetException(String e) {
		super(e);
	}
}
