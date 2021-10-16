package data;


import java.sql.SQLException;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import database.*;

/**
 * In apprendimento automatico un training set (o insieme di addestramento) è un insieme di 
 * dati che vengono utilizzati per addestrare un sistema supervisionato 
 * (come una rete neurale o un classificatore probabilistico).
 *
 *
 * Modella l'insieme di esempi di training
 * @author Daniele Lovecchio
 */
public class Data{

	//ATTRIBUTI
		/**
		 * Matrice di tipo object che contiene il training set, 
		 * organizzato come (numero di esempi) * (numero di attributi)
		 */
		private List<Example> data = new ArrayList<Example>();	
		
		/**
		 * cardinalità del training set
		 */
		private int numberOfExamples;	
		
		/**
		 * LinkedList di oggetti di tipo Attribute per rappresentare gli attributi indipendenti
		 */
		private List<Attribute> explanatorySet=new LinkedList<Attribute>();
		
		/**
		 * oggetto per modellare l'attributo di classe (target attribute), l'attributo di classe è un valore numerico
		 */
		private ContinuousAttribute classAttribute;	
		
	//METODI
		/**
		 *  Avvalora l'array explanatorySet[]. Per il dataset servo.dat, istanzia 4 oggetti di tipo DiscreteAttribute,
		 *  uno per ogni attributo indipendente(motor, screw,pain,vgain). 
		 *  Ogni attributo indipendente è creato associando ad esso l'array dei valori discreti che esso può assumere.
		 *  Ad esempio, all'attributo motor saranno associati i valori A,B,C,D,E 
		 * @param tableName
		 * @throws TrainingDataException
		 * @throws ClassNotFoundException
		 * @throws InstantiationException
		 * @throws IllegalAccessException
		 * @throws SQLException
		 */
public Data(String tableName) throws TrainingDataException, ClassNotFoundException, InstantiationException,IllegalAccessException, SQLException {
	DbAccess db = new DbAccess();				//oggetto che serve per instaurare l'accesso alla base di dati
	try {
		db.initConnection();					//instauro la connessione
	} catch (DataBaseConnectionException e) {
		throw new TrainingDataException(e.getMessage());			//connessione fallita
	}
	TableSchema ts = new TableSchema(db, tableName);			//acquisisco lo schema della tabella
	if (ts.getNumberOfAttributes() < 2) {
		throw new TrainingDataException("La tabella ha meno di due colonne.");	//numero di attributi minore di 2
	}
	TableData td = new TableData(db);  //modello la tabella
	Iterator<Column> it = ts.iterator();		//iterator per iterare lo schema (le colonne)
	int i = 0;
	while (it.hasNext()) {
		Column column = (Column) it.next();	//acquisico la colonna corrente
		if (!column.isNumber() && it.hasNext()) { 
			//valore non numerico -> lavoro su attributi discreti
		    Set<Object> distinctValues = (TreeSet<Object>) td.getDistinctColumnValues(tableName, column);
			Set<String> discreteValues = new TreeSet<String>();
		    for (Object o : distinctValues) {
			discreteValues.add((String) o); 
		    }
			explanatorySet.add(new DiscreteAttribute(column.getColumnName(), i, discreteValues));
			i++;
		} else if (it.hasNext() && column.isNumber()) {
			//valore numerico -> lavoro su attributi continui
			explanatorySet.add(new ContinuousAttribute(column.getColumnName(), i));
			i++;
		}
		else if (!it.hasNext() && column.isNumber()) {
			//siamo nell'utilma colonna -> lavoro sull'attributo di classe (target) 
			classAttribute = new ContinuousAttribute(column.getColumnName(), i);
		}
		else {
			throw new TrainingDataException("L'ultima colonna non è numerica.");
		}
	}
	try {
		data = td.getTransazioni(tableName);		//In data inseriamo gli esempi
	} catch (EmptySetException e) {
		throw new TrainingDataException(e.getMessage()); //eccezione per l'empty set -> esempi non trovati
	}
	numberOfExamples = data.size();		
	db.closeConnection();
	}

	
	/**
	* Legge i valori di tutti gli attributi per ogni esempio in data[][] 
	* e li concatena in un oggetto String (value) 
	* @return value
	*/
	public String toString() {
		String value="";
		for(int i=0; i<numberOfExamples;i++) {
			for(int j=0;j<explanatorySet.size();j++) {
				value+=data.get(i).get(j);
			}
			value+=data.get(i).get(explanatorySet.size())+"\n";
		}
		return value;
	}
	
	/**
	 * Ordina il sottoinsieme di esempi compresi nell'intervallo [inf,sup] in data[][] rispetto allo specifico attributo attribute.
	 *  Usa l'Algoritmo quicksort per l'ordinamento di un array di interi usando come relazione d'ordine totale "<=". L'array, 
	 *  in questo caso, è dato dai valori assunti dall'attributo passato in input. Vengono richiamati i metodi:
	 *  
	 *  @param attribute
	 *  @param beginExampleIndex	
	 *  @param endExampleIndex
	 */
	public void sort(Attribute attribute, int beginExampleIndex, int endExampleIndex){
			quicksort(attribute, beginExampleIndex, endExampleIndex);
	}
	
	/**
	 * Scambio di elementi 
	 * @param i
	 * @param j
     */
	private void swap(int i,int j) {
		for(int k=0; k<getNumberOfExplanatoryAttributes()+1;k++) {
			Collections.swap(data, i, j);
		}
	}
	
	/**
	 * Partiziona il vettore rispetto all'elemento x e restiutisce il punto di separazione
	 * @param attribute
	 * @param inf
	 * @param sup
	 * @return int
	 */
	private int partition(DiscreteAttribute attribute,int inf, int sup) {		
		int i,j;
		i=inf;
		j=sup;
		int med=(inf+sup)/2;
		String x=(String)getExplanatoryValue(med, attribute.getIndex()); 
		swap(inf,med);
		
		while(true) {
			while(i<=sup && ((String)getExplanatoryValue(i,attribute.getIndex())).compareTo(x)<=0) {
				i++;
			}
			while(((String)getExplanatoryValue(j,attribute.getIndex())).compareTo(x)>0) {
				j--;
			}
			if(i<j) {
				swap(i,j);
			}else break;
			
		}
		swap(inf,j);
		return j;
		
	}

private  int partition(ContinuousAttribute attribute, int inf, int sup){
		int i,j;
	
		i=inf; 
		j=sup; 
		int	med=(inf+sup)/2;
		Double x=(Double)getExplanatoryValue(med, attribute.getIndex());
		swap(inf,med);
	
		while (true) 
		{
			
			while(i<=sup && ((Double)getExplanatoryValue(i, attribute.getIndex())).compareTo(x)<=0){ 
				i++; 
				
			}
		
			while(((Double)getExplanatoryValue(j, attribute.getIndex())).compareTo(x)>0) {
				j--;
			
			}
			
			if(i<j) { 
				swap(i,j);
			}
			else break;
		}
		swap(inf,j);
		return j;

}
	/**
	 * Algoritmo quicksort per l'ordinamento di un array di interi A
	 * usando come relazione d'ordine totale "<="
	 * @param attribute
	 * @param inf
	 * @param sup
	 */
private void quicksort(Attribute attribute, int inf, int sup){
		
		if(sup>=inf){
			int pos;
			if(attribute instanceof DiscreteAttribute)
				pos=partition((DiscreteAttribute)attribute, inf, sup);
			else
				pos=partition((ContinuousAttribute)attribute, inf, sup);
					
			if ((pos-inf) < (sup-pos+1)) {
				quicksort(attribute, inf, pos-1); 
				quicksort(attribute, pos+1,sup);
			}
			else
			{
				quicksort(attribute, pos+1, sup); 
				quicksort(attribute, inf, pos-1);
			}
	   }
}

	/**
	 * Ritorna il numero di esempi
	 * @return numberOfExamples
	 */
	public int getNumberOfExamples(){
		return numberOfExamples;
	}	
	
	/**
	 *  Restituisce la lunghezza dell'array explanatorySet[] 
	 * @return explanatorySet.length
	 */
    public int getNumberOfExplanatoryAttributes(){
    	return explanatorySet.size();
    }  
    
    /**
     * Restituisce il valore dell'attributo di classe per l'esempio exampleIndex 
     * @param exampleIndex
     * @return data[exampleIndex][classAttribute.getIndex()]
     */
	public Double getClassValue(int exampleIndex) {
		return (Double) data.get(exampleIndex).get(classAttribute.getIndex());
	}	
	
	/**
	 *  Restituisce il valore dell'attributo indicizzato 
	 *  da attributeIndex  per l'esempio exampleIndex  
	 * @param exampleIndex
	 * @param attributeIndex
	 * @return data[exampleIndex][attributeIndex]
	 */
	public Object  getExplanatoryValue(int exampleIndex, int attributeIndex){
		return data.get(exampleIndex).get(explanatorySet.get(attributeIndex).getIndex());
	}
	/**
	 *  Restituisce l'attributo indicizzato da index in explanatorySet[] 
	 * @param index
	 * @return explanatorySet[index]
	 */
	public Attribute getExplanatoryAttribute(int index){
		return explanatorySet.get(index);
	}
	/**
	 *  Restituisce l'oggetto corrispondente all'attributo di classe 
	 * @return classAttribute
	 */
	ContinuousAttribute getClassAttribute(){
		return classAttribute;
	}	


}//fine classe

