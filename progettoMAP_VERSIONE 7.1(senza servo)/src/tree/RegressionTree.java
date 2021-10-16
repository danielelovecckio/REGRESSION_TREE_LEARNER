package tree;

import data.Data;
import data.DiscreteAttribute;
import server.UnknownValueException;
import utility.Keyboard;
import data.Attribute;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.TreeSet;
import data.ContinuousAttribute;
/**
 * Modellare l'entit� albero come insiee�me di sotto-albero
 * 
 * @author Daniele Lovecchio
 *
 */
@SuppressWarnings("serial")
public class RegressionTree implements Serializable{					
										
	//ATTRIBUTI
	/**
	 * Radice del sotto-albero corrente
	 */
	private Node root;					
	/**
	 * Array di sotto-alberi originanti nel nodo root:vi � un elemento dell'array per ogni figlio del nodo
	 */
	private RegressionTree childTree[];
	
	//METODI
	
	/**
	 * Costruttore : istanzia un sotto-albero dell'intero albero
	 */
	public RegressionTree(){} 
	
	/**
	 * Costruttore : istanzia un sotto-albero dell'intero albero 
	 * e avvia l'induzione dell'albero dagli esempi di training in input
	 * @param trainingSet
	 */
	public RegressionTree(Data trainingSet){
		learnTree(trainingSet,0,trainingSet.getNumberOfExamples()-1,trainingSet.getNumberOfExamples()*10/100);
	}
	
	
	/**
	 * Verifica se il sotto-insieme corrente pu� essere coperto da un nodo foglia
	 * controllando che il numero di esempi del training set compresi tra begin ed end sia minore 
	 * uguale di numberOfExamplesPerLeaf
	 * @param trainingSet
	 * @param begin
	 * @param end
	 * @param numberOfExamplesPerLeaf
	 * @return boolean
	 */
	private boolean isLeaf(Data trainingSet, int begin, int end, int numberOfExamplesPerLeaf) {
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
	 * Ricorsivamente ogni oggetto DecisionTree in childTree[] sar� re-invocato al metodo learnTree() per l'apprendimento
	 * su un insieme ridotto del sotto-insieme attuale (begin..end). Nella condizione in cui il nodo
	 * di split non ordina figli, il nodo diventa fogliare
	 * @param trainingSet
	 * @param begin
	 * @param end
	 * @param numberOfExamplesPerLeaf
	 */
	private void learnTree(Data trainingSet,int begin, int end,int numberOfExamplesPerLeaf){
		if(isLeaf(trainingSet, begin, end, numberOfExamplesPerLeaf)) {
			//determina la classe che compare pi� frequentemente nella partizione corrente
			root=new LeafNode(trainingSet,begin,end);
		}else //split node
		{
			root=determineBestSplitNode(trainingSet, begin, end);

			if(root.getNumberOfChildren()>1) {
				childTree=new RegressionTree[root.getNumberOfChildren()];
				for(int i=0;i<root.getNumberOfChildren();i++){
					childTree[i]=new RegressionTree();
					childTree[i].learnTree(trainingSet, ((SplitNode)root).getSplitInfo(i).getBeginIndex(), ((SplitNode)root).getSplitInfo(i).getEndIndex(), numberOfExamplesPerLeaf);
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
	 * @return v.get(pos)
	 */

 private SplitNode determineBestSplitNode(Data trainingSet, int begin,int end) {                           
	TreeSet<SplitNode> v=new TreeSet<SplitNode>();	//TreeSet di supporto, avvalorato successivamente con gli attributi discreti
	  
	  //avvaloramento del TreeSet
	  for(int i=0; i<trainingSet.getNumberOfExplanatoryAttributes();i++) {
	     SplitNode currentNode=null;
	    Attribute attribute=trainingSet.getExplanatoryAttribute(i);
	   try {
		   if(attribute instanceof DiscreteAttribute) {
	    	// System.out.println("icao");
			   currentNode=new DiscreteNode(trainingSet, begin,end,(DiscreteAttribute)attribute);
		   }else if(attribute instanceof ContinuousAttribute) {
			   currentNode = new ContinuousNode(trainingSet, begin,end,(ContinuousAttribute) attribute);
		   }
		  
		   v.add(currentNode);
	   }catch(NullPointerException e) {
		   System.err.println("Eccezione");
		   
	   }
      }
      trainingSet.sort(v.last().getAttribute(), begin, end);
      return v.last();
  }

 
	/**
	 * Concatena in una String tutte le informazioni di root-childTree[] 
	 * correnti  invocando i relativi metodo toString(): nel caso il root corrente �  
	 * di split  vengono concatenate anche le informazioni dei rami. 
	 * Fare uso di per riconoscere se  root � SplitNode o LeafNode.  
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
     * concatena le informazioni del nodo root: se � di split discende ricorsivamente
     * l'albero per ottenere le informazioni del nodo sottostante (necessario per ricostruire 
     * le condizioni in AND) di ogni ramo-regola, se � foglia (leaf) termina l'attraversamento visualizzando la regola
     */
   
 public String printRules() {
        String rules = new String();
        System.out.print("******************************************RULES*******************************************\n");
        String current = new String();
        String rule = new String();
       if (root instanceof LeafNode) {
            rules += "Class = " + ((LeafNode) root).getPredictedClassValue();
        }else if (root instanceof DiscreteNode) {
            rule += ((SplitNode) root).getAttribute().getName() + "=";
            for (int i = 0; i < ((SplitNode) root).mapSplit.size(); i++) {
                current = rule + ((SplitNode) root).mapSplit.get(i).getSplitValue();
               /* rules =*/ childTree[i].printRules(current);
            }
        }else if (root instanceof ContinuousNode) {
            rule += ((SplitNode) root).getAttribute().getName()/* + "="*/;
            for (int i = 0; i < ((SplitNode) root).mapSplit.size(); i++) {
            	String comparator = ((SplitNode) root).mapSplit.get(i).getComparator();
                current = rule +comparator+ ((SplitNode) root).mapSplit.get(i).getSplitValue();
               /* rules =*/ childTree[i].printRules(current);
            }
        }
       System.out.print( "*********************************************************************************************\n");
       return rules;
       //System.out.println(rules);
      
      // System.out.print( "*********************************************************************************************\n");
}    
    
   
    
    /** 
     * Supporta il metodo public void printRules().
     *  Concatena alle  informazioni in current del precedente nodo quelle del nodo root del corrente sotto-albero (oggetto DecisionTree): 
     *  se il nodo corrente � di split il metodo viene invocato ricorsivamente con current e le informazioni del nodo corrente, 
     *  se � di fogliare (leaf) visualizza tutte le informazioni concatenate.
     *  @param current 
     */
 private void printRules(String current) {
     if (root instanceof LeafNode) {
         //rules += current + " ==> Class = " + ((LeafNode) root).getPredictedClassValue() + "\n";
         current+=" ==> Class = " + ((LeafNode) root).getPredictedClassValue() + "\n";
         System.out.print(current);
     }
     else if (root instanceof DiscreteNode) {

         current += " AND " + ((SplitNode) root).getAttribute().getName() + "=";

         String tmp = null;
         for (int i = 0; i < ((SplitNode) root).mapSplit.size(); i++) {
             tmp = current + ((SplitNode) root).mapSplit.get(i).getSplitValue();
            // rules = childTree[i].printRules(tmp, rules);
            childTree[i].printRules(tmp);
            //System.out.println(tmp);
         }

     } else if (root instanceof ContinuousNode) {

         current += " AND " + ((SplitNode) root).getAttribute().getName() /*+ "="*/;

         String tmp = null;
         for (int i = 0; i < ((SplitNode) root).mapSplit.size(); i++) {
         	String comparator = ((SplitNode) root).mapSplit.get(i).getComparator();
             tmp = current +comparator+ ((SplitNode) root).mapSplit.get(i).getSplitValue();
            // rules = childTree[i].printRules(tmp, rules);
            childTree[i].printRules(tmp);
            //System.out.println(tmp);
         }

     } 

 }
    
 /**
  * Visualizza le informazioni di ciascuno split dell'albero (SplitNode.formulateQuery()) 
  * e per il corrispondente attributo acquisisce il  valore dell'esempio da predire da tastiera. 
  * Se il nodo root corrente � leaf  termina l'acquisizione e visualizza la predizione per l�attributo classe, 
  * altrimenti invoca ricorsivamente  sul figlio di root in childTree[] individuato dal valore acquisito da tastiera. 
  *	Il metodo sollevare l'eccezione UnknownValueException qualora la risposta dell�utente non permetta di selezionare
  * un ramo valido del nodo di split.
  * L'eccezione sar� gestita nel metodo che invoca predictClass(). 
  *
  * @return Double
  * @throws UnknownValueException
  */
public Double predictClass()throws UnknownValueException{ 
   if(root instanceof LeafNode) {
    return ((LeafNode) root).getPredictedClassValue(); 
   }
   else{ 
	   
    int risp; 
    System.out.println(((SplitNode)root).formulateQuery()); 
    risp=Keyboard.readInt(); 
    
    if(risp==-1 || risp>=root.getNumberOfChildren()) { 
      throw new UnknownValueException("The answer should be an integer between 0 and " +(root.getNumberOfChildren()-1)+"!"); 
      
    }
    else {
      return childTree[risp].predictClass();  
    }
    	     
  } 
} 

/**
 * Salva l'albero all'interno del file il cui nome � specificato come parametro
 * @param nomeFile
 * @throws FileNotFoundException
 * @throws IOException
 */
public void salva(String nomeFile) throws FileNotFoundException, IOException {
	ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(nomeFile));
    out.writeObject(this);
    out.writeObject(root);
    out.writeObject(childTree);
    out.close();
}

/**
 * Carica l'albero dal file specificato il cui nome � passato come parametro
 * @param nomeFile
 * @return
 * @throws FileNotFoundException
 * @throws IOException
 * @throws ClassNotFoundException
 */
 public static RegressionTree carica(String nomeFile)throws FileNotFoundException, IOException,ClassNotFoundException{
	 ObjectInputStream in = new ObjectInputStream(new FileInputStream(nomeFile));
     RegressionTree rTree = (RegressionTree) in.readObject();
     rTree.root = (Node) in.readObject();
     rTree.childTree = (RegressionTree[]) in.readObject();
     in.close();
     return rTree;
}
 
 /**
  * @return root of the tree
  */
 public Node getRoot() {
	 return root;
 }
 
/**
 * 
 * @param choice
 * @return sottoalbero scelto
 */
public RegressionTree getChildTree(int choice) {
	return childTree[choice];
}
}//fine classe


    
    
    
    
	


    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
