package data;



/**
 * Classe per gestire il caso di acquisizione errata del Training set 
 * (file inesistente, schema mancante, training set vuoto o training set privo di variabile target numerico)
 * @author Daniele Lovecchio
 *
 */
@SuppressWarnings("serial")
public class TrainingDataException extends Exception{
	
	public TrainingDataException(){
		super("Training Data Exception");
	}
	
	/**
	 * 
	 * @param message
	 */
	public TrainingDataException(String message){
		super(message+"\n");
	}

}
