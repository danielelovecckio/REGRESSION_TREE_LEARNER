/**
 * Estende la classe Attribute e rappresenta un attributo discreto
 * @author daniele
 *
 */
class DiscreteAttribute extends Attribute{			
	//ATTRIBUTI
	/**
	 * Array di oggetti String, uno per ciascun valore discreto che l'attributo può assumere
	 */
	private String values[];
	
	//METODI
	/**
	 *  Invoca il costruttore della super-classe e avvalora l'array values[] con i valori
	 *  discreti in input 
	 * @param name
	 * @param index
	 * @param values
	 */
	public DiscreteAttribute(String name, int index, String values[]){	
		super(name,index);				//costruttore della classe Attribute			
		this.values=values;			
	}
	
	/**
	 * getter del numero di valori discreti (cardinalità di values[])
	 * @return values.length
	 */
	public int getNumberOfDistinctValues(){		
		return values.length; 			
	}
	/**
	 * getter del singolo valore discreto
	 * @param index
	 * @return values[index]
	 */
	public String getValue(int index){
		return values[index];			
	}
	
	/**
	 * Il metodo toString di Attribute (classe padre)
	 * @return String
	 */
	public String toString(){			
		return super.toString();		
		 									
	}
}//fine classe
