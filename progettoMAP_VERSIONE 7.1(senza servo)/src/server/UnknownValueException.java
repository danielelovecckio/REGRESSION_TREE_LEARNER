package server;

/**
 * Classe per gestire il caso di acquisizione di valore mancante o fuori range
 * di un attributo di un nuovo esempio da classificare. 
 * 
 * @author Daniele Lovecchio
 *
 */
@SuppressWarnings("serial")
public class UnknownValueException extends Exception {
	public  UnknownValueException(){
		 super("Missing value or out of range");
	 }
	
	/**
	 * 
	 * @param message
	 */
	public  UnknownValueException(String message){
		 super(message+"\n");
	 }
}
