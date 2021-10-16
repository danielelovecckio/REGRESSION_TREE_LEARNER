package data;

import java.io.Serializable;

/**
 * La classe modella un generico attributo discreto o continuo
 * @author Daniele Lovecchio
 *
 */
public abstract class Attribute implements Serializable {
	//ATTRIBUTI
	/**
	 * Nome simbolico dell'attributo
	 */
	private String name; 
	/**
	 * Identificativo numerico dell'attributo
	 */
	private int index; 
	
	//METODI
	/**
	 *   E'il costruttore di classe. 
	 *   Inizializza i valori dei membri name,index 
	 *   
	 * @param name	nome dell'attributo
	 * @param index	identificativo numerico
	 */
	Attribute(String name, int index){  //costruttore di classe
		this.name=name;
		this.index=index;
	};			
	
	/**
	 * Metodo toString 
	 * @return String
	 * 
	 */
	public String toString(){
		return name+" "+index;
	}
	/**
	 * Restituisce il nome dell'attributo; 
	 * @return name
	 */
	public String getName(){
		return name;
	}  
	/**
	 * Restituisce l'identificativo numerico swll'attributo
	 * @return index
	 */
	public int getIndex() {
	 return index;
	 }		 

}//fine classe
