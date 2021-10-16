import java.util.ArrayList;
/**
 * Modellare l'entità albero come insieeìme di sotto-albero
 * @author daniele
 *
 */
class RegressionTree {					
										
	//ATTRIBUTI
	/**
	 * Radice del sotto-albero corrente
	 */
	private Node root;					
	/**
	 * Array di sotto-alberi originanti nel nodo root:vi è un elemento dell'array per ogni figlio del nodo
	 */
	private RegressionTree childTree[];
	
	//METODI
	
	/**
	 * Costruttore : istanzia un sotto-albero dell'intero albero
	 */
	RegressionTree(){}
	
	/**
	 * Costruttore : istanzia un sotto-albero dell'intero albero 
	 * e avvia l'induzione dell'albero dagli esempi di training in input
	 * @param trainingSet
	 */
	RegressionTree(Data trainingSet){
		learnTree(trainingSet,0,trainingSet.getNumberOfExamples()-1,trainingSet.getNumberOfExamples()*10/100);
	}
	
	
	/**
	 * Verifica se il sotto-insieme corrente può essere coperto da un nodo foglia
	 * controllando che il numero di esempi del training set compresi tra begin ed end sia minore 
	 * uguale di numberOfExamplesPerLeaf
	 * @param trainingSet
	 * @param begin
	 * @param end
	 * @param numberOfExamplesPerLeaf
	 * @return boolean
	 */
	public boolean isLeaf(Data trainingSet, int begin, int end, int numberOfExamplesPerLeaf) {
		if((end-begin)<=numberOfExamplesPerLeaf) {
			return true;
		}
			return false;
    }
	
	
	/**
	 * Genera un sotto-albero con il sotto-insieme di input istanziando un nodo
	 * fogliare (isLeaf) o un nodo di split. In tal caso determina il miglior nodo
	 * rispetto al sotto-insieme di input (determineBestSplitNode()), ed a tale nodo
	 * esso associa un sotto-albero avente radice il nodo medesimo(root) e avente un numero di rami pari al numero 
	 * dei figli determinati dallo split (childTree[]).
	 * Ricorsivamente ogni oggetto DecisionTree in childTree[] sarà re-invocato al metodo learnTree() per l'apprendimento
	 * su un insieme ridotto del sotto-insieme attuale (begin..end). Nella condizione in cui il nodo
	 * di split non ordina figli, il nodo diventa fogliare
	 * @param trainingSet
	 * @param begin
	 * @param end
	 * @param numberOfExamplesPerLeaf
	 */
	private void learnTree(Data trainingSet,int begin, int end,int numberOfExamplesPerLeaf){
		if(isLeaf(trainingSet, begin, end, numberOfExamplesPerLeaf)) {
			//determina la classe che compare più frequentemente nella partizione corrente
			root=new LeafNode(trainingSet,begin,end);
		}else //split node
		{
			root=determineBestSplitNode(trainingSet, begin, end);

			if(root.getNumberOfChildren()>1) {
				childTree=new RegressionTree[root.getNumberOfChildren()];
				for(int i=0;i<root.getNumberOfChildren();i++){
					childTree[i]=new RegressionTree();
					childTree[i].learnTree(trainingSet, ((SplitNode)root).getSplitInfo(i).beginIndex, ((SplitNode)root).getSplitInfo(i).endIndex, numberOfExamplesPerLeaf);
				}
			}else {
				root=new LeafNode(trainingSet,begin,end);
			}
		}
	}

	/**
	 * Per ciascun attributo indipendente istanzia il DiscreteNode associato e seleziona
	 * il nodo di split con minore varianza tra i DiscreteNode istanziati.
	 * Ordina la porzione di trainingSet corrente (tra begin ed end) rispetto all'attributo indipendente
	 * del nodo di split selezionato.
	 * Restituisce il nodo selezionato
	 * @param trainingSet
	 * @param begin
	 * @param end
	 * @return SplitNode
	 */

 SplitNode determineBestSplitNode(Data trainingSet, int begin,int end) {                           
	ArrayList <SplitNode> v=new ArrayList<SplitNode>();	//array di supporto, avvalorato successivamente con gli attributi discreti
	  
	  //avvaloramento dell'array
	  for(int i=0; i<trainingSet.getNumberOfExplanatoryAttributes();i++) {
	     SplitNode temp=null;
	     DiscreteAttribute attribute=(DiscreteAttribute)trainingSet.getExplanatoryAttribute(i);
		 temp=new DiscreteNode(trainingSet, begin,end,attribute);
		 v.add(temp);
      }
	  
	  //calcolo varianza minima
	  int pos=0;
      double min=v.get(0).getVariance();
      for(int i=0; i<v.size();i++) {
          if(v.get(i).getVariance()<min) {
              min=v.get(i).getVariance();
              pos=i;
          }
      }

      trainingSet.sort(v.get(pos).getAttribute(), begin, end);
     
      return v.get(pos);
      }

 
 
	
	/**
	 * Concatena in una String tutte le informazioni di root-childTree[] 
	 * correnti  invocando i relativi metodo toString(): nel caso il root corrente è  
	 * di split  vengono concatenate anche le informazioni dei rami. 
	 * Fare uso di per riconoscere se  root è SplitNode o LeafNode.  
	 * @return tree
	 */
	public String toString(){
		String tree=root.toString()+"\n";
		
		if(root instanceof LeafNode){
		
		}
		else //split node
		{
			for(int i=0;i<childTree.length;i++)
				tree +=childTree[i];
		}
		return tree;
}
	
	
	
    public void printTree(){
		System.out.println("********************************************************* TREE *********************************************************\n");
		System.out.println(toString());
		System.out.println("************************************************************************************************************************\n");
	}
    
    
    /**
     * Scandisce ciascun ramo dell'albero completo dalla radice alla foglia 
     * concatenando le informazioni dei nodi di split fino al nodo foglia .
     * In particolare per ogni sotto-albero (oggetto DecisionTree) in childTree[] 
     * concatena le informazioni del nodo root: se è di split discende ricorsivamente
     * l'albero per ottenere le informazioni del nodo sottostante (necessario per ricostruire 
     * le condizioni in AND) di ogni ramo-regola, se è foglia (leaf) termina l'attraversamento visualizzando la regola
     */
   
   public void printRules() {
        String rules = new String();
        System.out.print("******************************************************** RULES *********************************************************\n");
        String current = new String();
        String rule = new String();
       if (root instanceof LeafNode) {
            rules += "Class = " + ((LeafNode) root).getPredictedClassValue();
        }else if (root instanceof DiscreteNode) {
            rule += ((SplitNode) root).getAttribute().getName() + "=";
            for (int i = 0; i < ((SplitNode) root).mapSplit.size(); i++) {
                current = rule + ((SplitNode) root).mapSplit.get(i).getSplitValue();
                childTree[i].printRules(current);
            }
        } 
       System.out.print("************************************************************************************************************************\n");
        System.out.println(rules);
    }

    
   
    
    /** 
     * Supporta il metodo public void printRules().
     *  Concatena alle  informazioni in current del precedente nodo quelle del nodo root del corrente sotto-albero (oggetto DecisionTree): 
     *  se il nodo corrente è di split il metodo viene invocato ricorsivamente con current e le informazioni del nodo corrente, 
     *  se è di fogliare (leaf) visualizza tutte le informazioni concatenate.
     *  @param current 
     */

    private void printRules(String current) {
        if (root instanceof LeafNode) {
        	current+=" ==> Class = " + ((LeafNode) root).getPredictedClassValue() + "\n";
        	System.out.print(current);
        }
        else if (root instanceof DiscreteNode) {
        	
        	current += " AND " + ((SplitNode) root).getAttribute().getName() + "=";
           
            String tmp = null;
            for (int i = 0; i < ((SplitNode) root).mapSplit.size(); i++) {
                tmp = current + ((SplitNode) root).mapSplit.get(i).getSplitValue();
                childTree[i].printRules(tmp);
            }
           
        } 
        
    }
    

}//fine classe


    
    
    
    
	


    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
