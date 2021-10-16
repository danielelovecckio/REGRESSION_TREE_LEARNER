package tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import data.Attribute;
import data.ContinuousAttribute;
import data.Data;

public class ContinuousNode extends SplitNode implements Serializable{
	
	/**
	 * Istanzia un oggetto invocando il costruttore della superclasse con il parametro attribute
	 * @param trainingSet
	 * @param beginExampleIndex
	 * @param endExampleIndex
	 * @param attribute
	 */
	ContinuousNode(Data trainingSet,int beginExampleIndex, int endExampleIndex, ContinuousAttribute attribute){
		super(trainingSet,beginExampleIndex,endExampleIndex,attribute);
	}
	
	
	/**
	* istanzia oggetti SplitInfo(definita come inner class in SplitNode)
	* con ciascuno dei valori continui dell'attributo relativamente al sotto-insieme di training
	* corrente (ossia la porzione di trainingSet compresa tra beginExampleIndex e endExampleIndex)
	* quindi popola l'ArrayList mapSplit con tali oggetti
	* 
	* @param trainingSet
	* @param beginExampleIndex	
	* @param endExampleIndex	
	* @param attribute
	*/
	 void setSplitInfo(Data trainingSet,int beginExampleIndex, int endExampleIndex, Attribute attribute){
			//Update mapSplit defined in SplitNode -- contiene gli indici del partizionamento
			Double currentSplitValue= (Double)trainingSet.getExplanatoryValue(beginExampleIndex,attribute.getIndex());
			double bestInfoVariance=0;
			List <SplitInfo> bestMapSplit=null;
			
			for(int i=beginExampleIndex+1;i<=endExampleIndex;i++){
				Double value=(Double)trainingSet.getExplanatoryValue(i,attribute.getIndex());
				if(value.doubleValue()!=currentSplitValue.doubleValue()){
		//		System.out.print(currentSplitValue +" var ");
		//		System.out.print(value.doubleValue() +" variabile ");
					double localVariance=new LeafNode(trainingSet, beginExampleIndex,i-1).getVariance();
					double candidateSplitVariance=localVariance;
					localVariance=new LeafNode(trainingSet, i,endExampleIndex).getVariance();
					candidateSplitVariance+=localVariance;
			  //System.out.println(candidateSplitVariance);
					if(bestMapSplit==null){
						bestMapSplit=new ArrayList<SplitInfo>();
						bestMapSplit.add(new SplitInfo(currentSplitValue, beginExampleIndex, i-1,0,"<="));
						bestMapSplit.add(new SplitInfo(currentSplitValue, i, endExampleIndex,1,">"));
						bestInfoVariance=candidateSplitVariance;
					}
					else{							
						if(candidateSplitVariance<bestInfoVariance){
							bestInfoVariance=candidateSplitVariance;
							bestMapSplit.set(0, new SplitInfo(currentSplitValue, beginExampleIndex, i-1,0,"<="));
							bestMapSplit.set(1, new SplitInfo(currentSplitValue, i, endExampleIndex,1,">"));
						}
					}
					currentSplitValue=value;
				}
			  
			}
			mapSplit=bestMapSplit;
			
			//rimuovo split inutili (che includono tutti gli esempi nella stessa partizione)
			if((mapSplit.get(1).getBeginIndex()==mapSplit.get(1).getEndIndex())){	//debug
				mapSplit.remove(1);
			}
	 }
	 
		/**
		 * Effettua il confronto del valore in input rispetto al valore contenuto nell'attributo splitValue
		 * di ciascuno degli oggetti SplitInfo collezionati in mapSplit e restituisce l'identificativo dello split
		 * (indice della posizione nell'arrayList mapSplit) con cui il test è positivo
		 * @param value
		 * @return mapSplit.get(i).numberChild
		 */
	@Override
	 int testCondition(Object value) {
	
		return 0;
	}
	
	/**
	 * @return String
	 */
	public String toString() {
        return "CONTINUOUS " + super.toString();
    }
}