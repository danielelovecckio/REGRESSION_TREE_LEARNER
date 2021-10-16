package mapClient;

import java.io.IOException; 
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import utility.Keyboard;
/**
 * Classe main che contiene l'interazione con il server.
 * @author Daniele Lovecchio, Giuseppe Alaimo, Luigi Fragale
 */
class MainClient {

	/**
	 * @param args 
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args){
		
		InetAddress addr;	
		
		try {
			addr = InetAddress.getByName(args[0]);	//indirizzo ip del server passato come parametro da args
			System.out.println(addr);
		} catch (UnknownHostException e) {
			System.out.println(e.toString());
			return;
		}
		Socket socket=null;
		ObjectOutputStream out=null;
		ObjectInputStream in=null;
		try {
			socket = new Socket(args[0], new Integer(args[1]).intValue());	//args[1] contiene la porta 8080
			System.out.println(socket);		
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());	 // stream con richieste del client
			
		}  catch (IOException e) {
			System.out.println(e.toString());
			return;
		}

		String answer="";
		
		int decision=0;
		do{
			System.out.println("Apprendimento dell'albero di regressione da database [1]");
			System.out.println("Apprendimento dell'albero di regressione da archivio [2]");
			decision=Keyboard.readInt();
		}while(!(decision==1) && !(decision ==2));
		
		String tableName="";
		System.out.println("Nome tabella:");
		tableName=Keyboard.readString();
		try{
		
		if(decision==1)
		{
			System.out.println("Inizio fase di acquisizione dati!");

			out.writeObject(0);
			out.writeObject(tableName);
			answer=in.readObject().toString();
			if(!answer.equals("OK")){
				System.out.println(answer);
				return;
			}
			System.out.println("Inizio fase di apprendimento!");
			out.writeObject(1);
		}
		else
		{
			out.writeObject(2);
			out.writeObject(tableName);
			
		}
		
		answer=in.readObject().toString();
		if(!answer.equals("OK")){
			System.out.println(answer);
			return;
		}
		
		// .........
		
		char risp='y';
		
		do{
			out.writeObject(3);
			
			System.out.println("Inizio fase di predizione!");
			answer=in.readObject().toString();
		
			
			while(answer.equals("QUERY")){
				// Formualting query, reading answer
				answer=in.readObject().toString();
				System.out.println(answer);
				int path=Keyboard.readInt();
				out.writeObject(path);
				answer=in.readObject().toString();
			}
		
			if(answer.equals("OK"))
			{ // Reading prediction
				answer=in.readObject().toString();
				System.out.println("Predicted class:"+answer);
				
			}
			else //Printing error message
				System.out.println(answer);
			
		
			System.out.println("Vuoi ripetere ? (y/n)");
			risp=Keyboard.readChar();
				
		}while (Character.toUpperCase(risp)=='Y');
		
		}
		catch(IOException | ClassNotFoundException e){
			System.out.println(e.toString());
			
		}
	}

}
