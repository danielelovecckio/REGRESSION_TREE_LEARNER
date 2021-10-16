/**
* La classe DiscreteNode, estende la superclasse SplitNode
* e modella l'entità nodo di split relativo ad un attributo indipendente discreto
* @author daniele
* 
*/
class DiscreteNode extends SplitNode{	
	
		//METODI
/**
 * Istanzia un oggetto invocando il costruttore della superclasse con il parametro attribute
 * @param trainingSet
 * @param beginExampleIndex
 * @param endExampleIndex
 * @param attribute
 */
	 DiscreteNode(Data trainingSet,int beginExampleIndex, int endExampleIndex, DiscreteAttribute attribute) {
		super(trainingSet,beginExampleIndex,endExampleIndex,attribute);
	}
	

	/**
	* IMPLEMENTAZIONE DA CLASSE ABSTRACT
	* istanzia oggetti SplitInfo(definita come inner class in SplitNode)
	* con ciascuno dei valori discreti dell'attributo relativamente al sotto-insieme di training
	* corrente (ossia la porzione di trainingSet compresa tra beginExampleIndex e endExampleIndex)
	* quindi popola l'ArrayList mapSplit con tali oggetti
	* 
	* @param trainingSet
	* @param beginExampleIndex	
	* @param endExampleIndex	
	* @param attribute
	*/
	public void setSplitInfo(Data trainingSet,int beginExampleIndex,int endExampleIndex,Attribute attribute) {
		Object temp=trainingSet.getExplanatoryValue(beginExampleIndex, attribute.getIndex());
	    int begin=beginExampleIndex;
	    int child=0;

	    for(int i=beginExampleIndex;i<=endExampleIndex;i++) {
	    	
	        if(temp.equals(trainingSet.getExplanatoryValue(i, attribute.getIndex()))==false) {
	            mapSplit.add(new SplitInfo(temp,begin,i-1,child));
	            temp=trainingSet.getExplanatoryValue(i, attribute.getIndex()); 
	            begin=i;                                                                            
	            child++;
	        }
	    }
	    
	    mapSplit.add(new SplitInfo(temp,begin,endExampleIndex,child));
	}
	
	
	/**
	 * IMPLEMENTAZIONE DA CLASSE ABSTRACT
	 * Effettua il confronto del valore in input rispetto al valore contenuto nell'attributo splitValue
	 * di ciascuno degli oggetti SplitInfo collezionati in mapSplit[] e restituisce l'identificativo dello split
	 * (indice della posizione nell'array mapSplit) con cui il test è positivo
	 * @param value
	 * @return int (mapSplit.get(i).numberChild)
	 */
	public int testCondition(Object value) {
		int i=0;
		for (i=0;i<mapSplit.size();i++) {
			if(mapSplit.get(i).splitValue.equals(value))
				break;
		}
		return mapSplit.get(i).numberChild;
	}
	
	/**
	 * @return String
	 */
	public String toString() {
        return "DISCRETE " + super.toString();
    }
}
