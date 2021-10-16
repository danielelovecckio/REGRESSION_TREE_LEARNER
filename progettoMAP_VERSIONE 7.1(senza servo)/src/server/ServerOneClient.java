package server;

import java.io.*;
import java.net.*;

import java.sql.SQLException;

import data.Data;
import data.TrainingDataException;
import tree.*;
import tree.RegressionTree;


/**
 * @author Daniele Lovecchio
 */
public class ServerOneClient extends Thread {
	//ATTRIBUTI
	private Socket socket; 
	private ObjectInputStream in=null; 
	private ObjectOutputStream out=null;

	//METODI
	public ServerOneClient(Socket socket) throws IOException{
		
		System.out.println("sono nel costruttore di serverOne cliente");
		this.socket=socket;
		String socketName=socket.getInetAddress().getHostName();
		System.out.println("Connessione stabilita "+socketName+" con porta"+socket.getPort());
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
		System.out.println("Client: "+" "+socketName);
		start();
	
	}
	
	public void run(){
		RegressionTree tree=new RegressionTree();
		String str=null;
		Integer x=null;
		Data trainingSet=null;
		String nomeTabella=null;
		try {
			str=in.readObject().toString();
			x=Integer.valueOf(str);
			if(x==0) {
				nomeTabella=in.readObject().toString();
				out.writeObject("OK");
				str=in.readObject().toString();
				x=Integer.valueOf(str);
				trainingSet= new Data(nomeTabella);
				tree=new RegressionTree(trainingSet);
				if(x==1) {
					tree.salva(nomeTabella+".dmp"); 
				}
				
			}else if( x==2) {
					nomeTabella=in.readObject().toString();
					tree=RegressionTree.carica(nomeTabella+".dmp");
			}
			out.writeObject("OK");
			str=in.readObject().toString();
			x=Integer.valueOf(str);
			System.out.println(" x e "+x);
			if(x==3) {
				
				
				String risp="y";
				do{
					risp="y";
					try {
						out.writeObject(predictClass(tree));
					} catch (UnknownValueException e) {
						out.writeObject(e.getMessage());	//invio messaggio di errore al client fuori range
					}
					
					try {
					risp=in.readObject().toString();
					}catch(EOFException | SocketException e) {
						risp="4";
						socket.close();	
						in.close();
						out.close();
					}
					
				}while (risp.equalsIgnoreCase("3"));
		
			}				
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (TrainingDataException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

  }
	
	/**
	  * Visualizza le informazioni di ciascuno split dell'albero (SplitNode.formulateQuery()) 
	  * e per il corrispondente attributo acquisisce il  valore dell'esempio da predire da tastiera. 
	  * Se il nodo root corrente è leaf  termina l'acquisizione e visualizza la predizione per l’attributo classe, 
	  * altrimenti invoca ricorsivamente  sul figlio di root in childTree[] individuato dal valore acquisito da tastiera. 
	  *	Il metodo sollevare l'eccezione UnknownValueException qualora la risposta dell’utente non permetta di selezionare
	  * un ramo valido del nodo di split.
	  * L'eccezione sarà gestita nel metodo che invoca predictClass(). 
	  *
	  * @return Double
	  * @throws UnknownValueException
	  * @throws ClassNotFoundException
	  * @throws IOException
	  */
	private Double predictClass(RegressionTree tree) throws UnknownValueException, ClassNotFoundException, IOException{
	      if(tree.getRoot() instanceof LeafNode) {
	        	out.writeObject("OK");
	            return ( (LeafNode)tree.getRoot() ).getPredictedClassValue();
	        
	        }else
	        {
	        	out.writeObject("QUERY");
	            int choice;
	            out.writeObject( ((SplitNode)tree.getRoot()).formulateQuery() );
	            choice = ((Integer)in.readObject()).intValue();
	        
	             int max = ((SplitNode) tree.getRoot()).getNumberOfChildren();
	            if( choice < 0 || choice >= max ) {
	                throw new UnknownValueException("La risposta è fuori range");
	            }else {
	                return predictClass( tree.getChildTree(choice) );
	            }   
	        }
	    }


	
}

