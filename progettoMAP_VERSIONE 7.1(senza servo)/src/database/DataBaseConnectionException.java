package database;

/**
 * @author Daniele Lovecchio
 *  Modella l'eccezione di fallimento nella connessione al database
 */
public class DataBaseConnectionException extends Exception {
	public DataBaseConnectionException() {
		super("Errore di connessione al database");
	}
	public DataBaseConnectionException(String e) {
		super(e);
	}
}
