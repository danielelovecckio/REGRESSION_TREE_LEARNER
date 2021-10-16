package data;

import java.io.Serializable;

/**
 * Estende la classe Attribute e rappresenta un attributo continuo
 * 
 * @author Daniele Lovecchio
 */
public class ContinuousAttribute extends Attribute implements Serializable{		        
		//METODI
		/**
		 * Invoca il costruttore della super-classe 
		 * @param name
		 * @param index
		 */
		public ContinuousAttribute(String name, int index){  
			super(name,index);						        
		}
		
		/**
		 * Il metodo toString di Attribute (classe padre)
		 * @return String
		 */
		public String toString() {
			return super.toString();		               
		}
}
