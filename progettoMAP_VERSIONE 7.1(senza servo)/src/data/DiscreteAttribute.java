package data;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
/**
 * Estende la classe Attribute e rappresenta un attributo discreto
 * @author Daniele Lovecchio
 *
 */
public class DiscreteAttribute extends Attribute implements Iterable<String>, Serializable{			
	//ATTRIBUTI
	/**
	 * Array di oggetti String, uno per ciascun valore discreto che l'attributo può assumere
	 */
	private Set<String> values=new TreeSet<>(); 
	
	//METODI
	/**
	 *  Invoca il costruttore della super-classe e avvalora il treeSet values con i valori
	 *  discreti in input 
	 * @param name
	 * @param index
	 * @param values
	 */
	public DiscreteAttribute(String name, int index, Set<String> values) {
		super(name,index);
		this.values=values;
	}
	
	
	/**
	 * Ritorna numero di valori discreti (cardinalità di values[])
	 * @return values.length
	 */
	public int getNumberOfDistinctValues(){		
		return values.size(); 			
	}

	@Override
	public Iterator<String> iterator() {
		return values.iterator();
	}
	
	/**
	 * Il metodo toString di Attribute (classe padre)
	 * @return String
	 */
	public String toString(){			
		return super.toString();		
		 									
	}
}//fine classe
