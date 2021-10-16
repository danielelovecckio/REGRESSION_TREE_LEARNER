/**
*Classe che estende la superclasse SplitNode e modella l'entità del nodo foglia
*@author daniele
*/ 
class LeafNode extends Node {			
	//ATTRIBUTI
	/**
	 * Valore dell'attributo di classe espresso nella foglia corrente
	 */
	private Double predictedClassValue;	

	//METODI
	/**
	 * Istanzia un oggetto invocando il costruttore della superclasse
	 * e l'attributo predictedClassValue (come media dei valori dell'attributo 
	 * che ricadono nella partizione ossia la porzione di trainingSet compresa
	 * tra beginExampleIndex e endExampleIndex
	 * @param trainingSet
	 * @param beginExampleIndex
	 * @param endExampleIndex
	 */
	LeafNode(Data trainingSet,int beginExampleIndex, int endExampleIndex){
		super(trainingSet,beginExampleIndex,endExampleIndex);
		double x=0;
		for(int i=beginExampleIndex;i<endExampleIndex;i++) {
			x+=trainingSet.getClassValue(i);
		}
		x=x/(endExampleIndex-beginExampleIndex);
		predictedClassValue=new Double(x);
	}
	
	/**
	 * restituisce il valore del membro predictedClassValue 
	 * @return predictedClassValue
	 */
	public Double getPredictedClassValue() {
		return  predictedClassValue;
	}
	
	/**
	 *  restituisce il numero di split originanti dal nodo foglia, ovvero 0. 
	 *  @return 0 (numero di figli di un nodo foglia)
	 */
	public int getNumberOfChildren() {
		return 0;
	}
	
	/**
	 * Invoca il metodo della superclasse e assegnando anche il valore della classe foglia
	 * @return String
	 */
	public String toString() {
	        return "LEAF class=" + predictedClassValue + " " + super.toString();
	    }
	}


