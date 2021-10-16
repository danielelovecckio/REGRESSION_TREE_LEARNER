import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * In apprendimento automatico un training set (o insieme di addestramento) è un insieme di 
 * dati che vengono utilizzati per addestrare un sistema supervisionato 
 * (come una rete neurale o un classificatore probabilistico).
 *
 *
 * Modella l'insieme di esempi di training
 * @author daniele
 */
class Data {

	//ATTRIBUTI
		/**
		 * Matrice di tipo object che contiene il training set, 
		 * organizzato come (numero di esempi) * (numero di attributi)
		 */
		private Object data[][];	
		
		/**
		 * cardinalità del training set
		 */
		private int numberOfExamples;	
		
		/**
		 * Array di oggetti di tipo Attribute per rappresentare gli attributi indipendenti
		 */
		private Attribute explanatorySet[];	
		
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
		 * @param fileName
		 * @throws FileNotFoundException
		 */
		Data(String fileName)throws FileNotFoundException{
			
			File inFile = new File(fileName);
			
			Scanner sc=new Scanner(inFile);			//Legge il file
			String line = sc.nextLine();			//Legge una riga intera
			if(!line.contains("@schema")) {
				throw new RuntimeException("Errore nello schema");
			}
				
			String s[]=line.split(" ");
				
				  //popolare explanatory Set 
			      //@schema 4
				
			explanatorySet = new Attribute[new Integer(s[1])]; //??
			short iAttribute=0;
			line=sc.nextLine();
			while(!line.contains("@data")) {
				s=line.split(" ");
				if(s[0].equals("@desc")) {
					//aggiungo l'attributo allo spazio descrittivo
					///@desc motor discrete A,B,C,D,E
					String discreteValues[]=s[2].split(",");
					explanatorySet[iAttribute] = new DiscreteAttribute(s[1],iAttribute, discreteValues);
				}
				else if(s[0].equals("@target")) {
					classAttribute=new ContinuousAttribute(s[1], iAttribute);
				}
				  iAttribute++;
		    	  line = sc.nextLine();
			}
			
			//avvalorare numero di esempi
			//@data 167
			
			 numberOfExamples=new Integer(line.split(" ")[1]);
			
			 //popolare data
			 data=new Object[numberOfExamples][explanatorySet.length+1];
			 short iRow=0;
			while(sc.hasNextLine()) {
				line=sc.nextLine();
				 // assumo che attributi siano tutti discreti
				 s=line.split(","); 		//E,E,5,4, 0.28125095
				 
				 for(short jColumn=0;jColumn<s.length-1;jColumn++) {
		    		  data[iRow][jColumn]=s[jColumn];
				 }
		    	 
				 data[iRow][s.length-1]=new Double(s[s.length-1]);
		    	 iRow++;
			}
			sc.close();	
		}
	
	/**
	* Legge i valori di tutti gli attributi per ogni esempio in data[][] 
	* e li concatena in un oggetto String (value) 
	* @return value
	*/
	public String toString() {
		String value="";
		for(int i=0; i<numberOfExamples;i++) {
			for(int j=0;j<explanatorySet.length;j++) {
				value+=data[i][j]+",";
			}
			value+=data[i][explanatorySet.length]+"\n";
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
	void sort(Attribute attribute, int beginExampleIndex, int endExampleIndex) {
		quicksort(attribute,beginExampleIndex,endExampleIndex);
	}
	
	/**
	 * scambio i con j
	 * @param i
	 * @param j
     */
	private void swap(int i,int j) {
		Object temp;
		for(int k=0; k<getNumberOfExplanatoryAttributes()+1;k++) {
			temp=data[i][k];
			data[i][k]=data[j][k];
			data[j][k]=temp;
		}
	}
	
	/**
	 * Partiziona il vettore rispetto all'elemento x e restiutisce il punto di separazione
	 * @param attribute
	 * @param inf
	 * @param sup
	 * @return j
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
	
	/**
	 * Algoritmo quicksort per l'ordinamento di un array di interi A
	 * usando come relazione d'ordine totale "<="
	 * @param attribuute
	 * @param inf
	 * @param sup
	 */
	private void quicksort(Attribute attribute, int inf, int sup){
		
		if(sup>=inf){
			
			int pos;
			
			pos=partition((DiscreteAttribute)attribute, inf, sup);
					
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
	
	public static void main(String args[])throws FileNotFoundException{
	/*Data trainingSet= new Data("servo.dat");
	System.out.println(trainingSet);
	for(int jColumn=0;jColumn<trainingSet.getNumberOfExplanatoryAttributes();jColumn++) {
		System.out.println("ORDER BY"+trainingSet.getExplanatoryAttribute(jColumn));
		trainingSet.quicksort(trainingSet.getExplanatoryAttribute(jColumn),0 , trainingSet.getNumberOfExamples()-1);
		System.out.println(trainingSet);
	}
	System.out.println(trainingSet.getClassValue(0));*/
		
		
		/**
		 * @param args
		 */
		
			Data trainingSet= new Data("prova.dat");
			
			RegressionTree tree=new RegressionTree(trainingSet);
			
			tree.printRules();
			
			tree.printTree();
	}
	
	
	/**
	 * Ritorna il numero di esempi
	 * @return numberOfExamples
	 */
	int getNumberOfExamples(){
		return numberOfExamples;
	}	
	
	/**
	 *  Restituisce la lunghezza dell'array explanatorySet[] 
	 * @return explanatorySet.length
	 */
    int getNumberOfExplanatoryAttributes(){
    	return explanatorySet.length;
    }  
    
    /**
     * Restituisce il valore dell'attributo di classe per l'esempio exampleIndex 
     * @param exampleIndex
     * @return data[exampleIndex][classAttribute.getIndex()]
     */
	public Double getClassValue(int exampleIndex) {
		return (Double) data[exampleIndex][classAttribute.getIndex()];
	}	
	
	/**
	 *  Restituisce il valore dell'attributo indicizzato 
	 *  da attributeIndex  per l'esempio exampleIndex  
	 * @param exampleIndex
	 * @param attributeIndex
	 * @return data[exampleIndex][attributeIndex]
	 */
	Object getExplanatoryValue(int exampleIndex, int attributeIndex){
		return data[exampleIndex][attributeIndex];
	}
	/**
	 *  Restituisce l'attributo indicizzato da index in explanatorySet[] 
	 * @param index
	 * @return explanatorySet[index]
	 */
	Attribute getExplanatoryAttribute(int index){
		return explanatorySet[index];
	}
	/**
	 *  Restituisce l'oggetto corrispondente all'attributo di classe 
	 * @return classAttribute
	 */
	ContinuousAttribute getClassAttribute(){
		return classAttribute;
	}	


}//fine classe

