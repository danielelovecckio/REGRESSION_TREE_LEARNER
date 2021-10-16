/**
 * Classe astratta che modella l'astrazione dell'entità nodo dell'albero di decisione
 * 
 * @author daniele
 *
 */
abstract class Node {
	//ATTRIBUTI
/**
 * Contatore nodi generati dall'albero
 */
 static int idNodeCount=0;	
 /**
  * Identificativo numerico del nodo
  */
 int idNode; 	
 /**
  * Indice dell'array nel training set del primo esempio coperto dal nodo corrente
  */
 int beginExampleIndex;	
 /**
  * Indice dell'array nel training set del ultimo esempio coperto dal nodo corrente
  */
 int endExampleIndex;		//
 /**
  * Valore della varianza calcolata, rispetto all'attributo di classe, nel sotto-insieme di training del nodo
  */
 double variance; 			
 
 
 
 	//METODI
 
 /**
  * Costruttore del nodo, avvalora gli attributi primitivi di classe
  * inclusa la varianza che viene calcolata rispetto l'attributo da predire
  * nel sotto-insieme di training coperto dal nodo 
  * @param trainingSet
  * @param beginExampleIndex
  * @param endExampleIndex
  */
 Node(Data trainingSet,int beginExampleIndex,int endExampleIndex) {
	 idNode=idNodeCount++;
	 this.beginExampleIndex=beginExampleIndex;
	 this.endExampleIndex=endExampleIndex;
 
     double sum = 0.0;
     double sumOfSquare = 0.0;

     //Calcolo varianza
     for(int i = beginExampleIndex  ; i <= endExampleIndex ; i++) {
         sum += trainingSet.getClassValue(i);
         sumOfSquare += Math.pow(trainingSet.getClassValue(i) , 2);
     }
     variance=sumOfSquare-((Math.pow(sum, 2))/(endExampleIndex-beginExampleIndex+1));
 }
 
/**
 * Restituisce l'identificativo numerico del nodo
 * @return node
 */
 int getIdNode() {	
	 return this.idNode;
 }
 
 /**
  * Restituisce l'indice del primo esempio del sotto-insieme rispetto al training set complessivo
  * @return beginExampleIndex
  */
 int getBeginExampleIndex() {
	 return this.beginExampleIndex;
 }
 
/**
 * restituisce l'indice del primo esempio del sotto-insieme rispetto al training set complessivo
 * @return endExampleIndex
 */
 int getEndExampleIndex() {
	 return this.endExampleIndex;
 }
 
 /**
  * Restituisce il valore della varianza dell’attributo da predire rispetto al nodo corrente 
  * @return variance
  */
 double getVariance() {
	 return this.variance;
 }
 
 /**
  * E' un metodo astratto la cui implementazione riguarda i nodi di tipo test(split node)
  * dai quali si possono generare figli, uno per ogni split prodotto.
  * Restituisce il numero di tali figli.
  * 
  */
 abstract int getNumberOfChildren();

 /**
  * @return String
  */
 public String toString() {
	 return "Nodo : [Examples:" + beginExampleIndex + "-" + endExampleIndex + "]" + " variance:" + variance;
 }
 

 
}
















