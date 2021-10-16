//import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import data.Data;
import data.TrainingDataException;
import server.UnknownValueException;
import tree.RegressionTree;
import utility.Keyboard;

public class MainTest5 {

	/**
	 * @param args
	 * @throws SQLException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException  
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		int decision=0;
		do{
		
			System.out.println("Learn Regression Tree from data [1]");
			System.out.println("Load Regression Tree from archive [2]");
			decision=Keyboard.readInt();
		}while(!(decision==1) && !(decision ==2));
		
		String trainingfileName=""; 
		System.out.println("File name:");
		trainingfileName=Keyboard.readString();
		
		RegressionTree tree=null;
		if(decision==1)
		{
			System.out.println("Starting data acquisition phase!");
			Data trainingSet=null;
			try{
				trainingSet= new Data(trainingfileName);
			}
			catch(TrainingDataException e){System.out.println(e);return;}
		
			System.out.println("Starting learning phase!");
			tree=new RegressionTree(trainingSet);
			try {
				tree.salva(trainingfileName+".dmp");
			} catch (IOException e) {
				System.out.println(e.toString());
			}
		} else
			try {
				tree=RegressionTree.carica(trainingfileName+".dmp");
			} catch (ClassNotFoundException | IOException e) {
				System.out.print(e);
				return;
			}
			tree.printRules();
			tree.printTree();
			
			char risp='y';
			do{
				System.out.println("Starting prediction phase!");
				try {
					System.out.println(tree.predictClass());
				} catch (UnknownValueException e) {
					
					System.out.println(e);
				}
				System.out.println("Would you repeat ? (y/n)");
				risp=Keyboard.readChar();
				
			}while (Character.toUpperCase(risp)=='Y');
		
					
	}

}
