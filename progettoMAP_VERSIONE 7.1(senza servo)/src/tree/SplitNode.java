package tree;
import java.io.Serializable;
import java.util.ArrayList;
import data.Attribute;
import data.Data;
import java.util.List;

/**
 * Classe astratta che estende la superclasse Node e
 * modella l'entità del nodo di split
 * 
 * @author Daniele Lovecchio
 *
 */
@SuppressWarnings("serial")
public abstract class SplitNode extends Node implements Comparable<SplitNode>, Serializable{
	//ATTRIBUTI
	
	/**
	 *Oggetto Attribute che modella l'attributo indipendente sul quale lo split è generato
	 */
	private Attribute attribute;		
	
	/**
	 *ArrayList per memorizzare gli split candidati in una struttura dati di dimensione pari ai possibili valori di test 
	 */
	List<SplitInfo> mapSplit=new ArrayList<SplitInfo>();		
	
	/**
	 *Attributo che contiene il valore di varianza a seguito del partizionamento indotto dallo split corrente 
	 */
	protected double splitVariance;										
	
	
	//METODI
	
	/**
	 * Costruttore di SplitNode : 
	 * Invoca il costruttore della super classe, ordina i valori dell'attributo di input per gli esempi
	 * beginExampleIndex-endExampleIndex e sfrutta questo ordinamento per popolare l'array mapSplit[]
	 * computa la varianza (splitVariance) per l'attributo usato nello split sulla base del partizionamento 
	 * indotto da uno split.
	 * @param trainingSet
	 * @param beginExampleIndex
	 * @param endExampleIndex
	 * @param attribute
	 */

	 SplitNode(Data trainingSet,int beginExampleIndex, int endExampleIndex,Attribute attribute)throws NullPointerException {           
        super(trainingSet,beginExampleIndex,endExampleIndex);
        this.attribute=attribute;
        trainingSet.sort(attribute, beginExampleIndex, endExampleIndex);
        setSplitInfo(trainingSet, beginExampleIndex,endExampleIndex,attribute);
        double localVariance=0;
        //computare la varianza
    for(int i=0; i<mapSplit.size();i++) {
        	localVariance=new LeafNode(trainingSet, mapSplit.get(i).getBeginIndex(),mapSplit.get(i).getEndIndex()).getVariance();
            splitVariance+=(localVariance);
        }
       
    }
	
	
	/**
	 * Metodo abstract per generare le informazioni necessarie per ciascuno degli split candidati(in mapSplit)
	 * @param trainingSet
	 * @param beginExampleIndex
	 * @param endExampleIndex
	 * @param attribute
	 */
	abstract void setSplitInfo(Data trainingSet,int beginExampleIndex,int endExampleIndex,Attribute attribute);
	
	/**
	 * Metodo abstract per modellare la condizione di test(ad ogni valore di test c'è un ramo dallo split)
	 * @param value
	 */
	abstract int testCondition (Object value);
	
	/** 
	 * Restituisce l'oggetto per l'attributo usato per lo split
	 * @return attribute
	 */
	Attribute getAttribute() {
		return attribute;
	}
	
	/**
	 * Restituisce l'information gain per lo split corrente 
	 * @return splitVariance
	 */
	double getVariance() {
		return splitVariance;
	}
	
	/**
	 * Restituisce il numero dei rami originanti nel nodo corrente
	 * 
	 * @return mapSplit.size()
	 */
	public int getNumberOfChildren() {
		return mapSplit.size();
	}
	
	/**
	 * Restituisce le informazioni per il ramo in mapSplit indicizzato da child 
	 * @return mapSplit.get(child) 
	 */
	SplitInfo getSplitInfo(int child){
		return mapSplit.get(child);
	}
	
	
	/**
	 * Concatena le informazioni di ciascun test (attributo, operatore e valore) 
	 * in una String finale. Necessario per la predizione di nuovi esempi.
	 * @return query
	 */
	public String formulateQuery() {
        String query="";
        for(int i=0; i<mapSplit.size(); i++) {
            query+= (i + ":" + attribute + mapSplit.get(i).getComparator() + mapSplit.get(i).getSplitValue()) +"\n";
        }
        return query;
    }

	
	/**
	 * Concatena le informazioni di ciascuno test (attributo, esempi coperti, varianza, 
	 * varianza di split) in una String finale (v).
	 * @return v
	 */
	public String toString(){
		String v= "SPLIT : attribute=" +attribute +" "+ super.toString()+  " Split Variance: " + getVariance()+ "\n" ;
		
		for(int i=0;i<mapSplit.size();i++){
			v+= "\t"+mapSplit.get(i)+"\n";
		}
		
		return v;
	}
	
	/**
	 * Confronto tra oggetti di tipo SplitNode rispetto all'information gain
	 * @param SplitNode
	 */
	@Override
	public int compareTo(SplitNode o) {
		if(o.getVariance()<this.splitVariance) {
			return -1;
		}else if(o.getVariance()>this.splitVariance){
			return 1;
		}
		return 0;
	}
	
	
	//INIZIO INNER CLASS SplitInfo
   /**
    * Classe che colleziona informazioni descrittive dello split
    */

	class SplitInfo implements Serializable{
		//ATTRIBUTI
		/**
		 * Valore di tipo Object(di un attributo indipendente) che definisce uno split
		 */
		private Object splitValue;			
		private int beginIndex;
		private int endIndex;
		
		/**
		 * numero di split(nodi figli)originati dal nodo corrente
		 */
		private int numberChild;	
		/**
		 * operatore matematico che definisce il test nel nodo corrente ("=" per valori discreti)
		 */
		private String comparator="=";		
		
		//METODI
		/**
		 * costruttore che avvalora gli attributi di classe per split a valori discreti
		 * @param splitValue
		 * @param beginIndex
		 * @param endIndex
		 * @param numberChild
		 */
		SplitInfo(Object splitValue,int beginIndex,int endIndex,int numberChild){
			this.splitValue=splitValue;
			this.beginIndex=beginIndex;
			this.endIndex=endIndex;
			this.numberChild=numberChild;
		}
		
		/**
		 * costruttore che avvalora gli attributi di classe per generici split (da usare per valori continui)
		 * @param splitValue
		 * @param beginIndex
		 * @param endIndex
		 * @param numberChild
		 * @param comparator
		 */
		SplitInfo(Object splitValue,int beginIndex,int endIndex,int numberChild, String comparator){
			this.splitValue=splitValue;
			this.beginIndex=beginIndex;
			this.endIndex=endIndex;
			this.numberChild=numberChild;
			this.comparator=comparator;
		}
		/**
		 * 
		 * @return beginIndex
		 */
		int getBeginIndex(){
			return beginIndex;			
		}
		/**
		 * 
		 * @return endIndex
		 */
		int getEndIndex(){
			return endIndex;
		}
		
		/**
		 * 
		 * @return splitValue
		 */
		Object getSplitValue(){
			return splitValue;
		}
		
		/**
		 * concatena in un oggetto String i valori di beginExampleIndex, child, splitValue, comparator
		 *	e restituisce la stringa finale
		 * @return String
		 */
		public String toString(){
			return "child " + numberChild +" split value"+comparator+splitValue + "[Examples:"+beginIndex+"-"+endIndex+"]";
		}
		
		/**
		 * 
		 * @return comparator
		 */
		String getComparator(){
			return comparator;
		}
		
		/**
		 * 
		 * @return numberChild
		 */
		int getNumberChild(){
			return numberChild;
		}
	}//FINE INNER CLASS SplitInfo

}
