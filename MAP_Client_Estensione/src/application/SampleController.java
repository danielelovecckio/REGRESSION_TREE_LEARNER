package application;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/**
 * 
 * @author Daniele Lovecchio, Giuseppe Alaimo, Luigi Fragale
 */
public class SampleController implements Initializable {
	private static boolean flag,ripetuto,available;
	private static ObjectOutputStream out;
	private static ObjectInputStream in;
    private static InetAddress addr;
	private static String answer,nomeTabella;
	@FXML
	private static AnchorPane learningPane;
	@FXML
	private static AnchorPane starterPane;
	@FXML
	private Button database,archivio,si,no,avvia,ripristina,chiudi;
	@FXML
	private Label titolo,scelta,nomeTab,labelOutput,ripetere,outputFinale,labelErr,erroreImprevisto,consiglio;
	@FXML
	private TextField testo,num,risultatoFin,tipoErrore;
	@FXML
	private TextArea areaTesto;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		System.out.println("--Initialized--");
	}
	/**
	 * Una volta cliccato il bottone database, verrà eseguito questo metodo.
	 * Il metodo setta un flag a true (servirà ad indicare successivamente la scelta attuale).
	 * Inoltre viene acquisito il nome della tabella scelta e creato un nuovo schema.
	 * @param e
	 */
	@FXML
	private void databaseClicked(ActionEvent e) {
		nomeTabella = testo.getText();
		while(nomeTabella.equals("")) {
			nomeTabella = "null";
			creaFinestraIniziale("Sample.fxml",e);	
		}
		flag = true;
		creaNuovaFinestra("apprendimento.fxml",e,true);
	}
	/**
	 * Una volta cliccato il bottone archivio, verrà eseguito questo metodo.
	 * Il metodo setta un flag a false (servirà ad indicare successivamente la scelta attuale).
	 * Inoltre viene acquisito il nome del file selezionato presente nell'archivio e creato uno schema.
	 * @param e
	 */
	@FXML
	private void archivioClicked(ActionEvent e){
		nomeTabella = testo.getText();
		while(nomeTabella.equals("")) {
			nomeTabella = "null";
			creaFinestraIniziale("Sample.fxml",e);	
		}
		flag = false;
		try {
			out.writeObject(2);
			out.writeObject(nomeTabella);
			creaNuovaFinestra("apprendimento.fxml",e,true);
		} catch (IOException e1) {
			System.out.println(e1.getMessage());
		}
	}
	
	/**
	 * Una volta cliccato bottone avvia, verrà eseguito questo metodo.
	 * Il metodo avvia la fase di acquisizione dati e di apprendimento dall'albero.
	 * Grazie ad un flag riesce a capire se l'avvio del processo dev'essere fatto attraverso
	 * acquisizione da database o da file.
	 * Infine il metodo riceve i primi nodi, successivamente grazie al campo di testo e al bottone invio l'esecuzione continueà
	 * grazie alle scelte dell'utente
	 * @param e
	 */
	@FXML
	private void avviaClicked(ActionEvent e) {
		areaTesto.setText("");
	try {
		if(ripetuto==false) {
			if(flag==true) {
				areaTesto.setText("Inizio fase di acquisizione dati..."+"\n");
				out.writeObject(0);
				out.writeObject(nomeTabella);
				answer=in.readObject().toString();
				if(!answer.equals("OK")){
					areaTesto.setText(answer);
				    creaNuovaFinestra("notFound.fxml",e,true);
					return;
				}
				areaTesto.appendText("Inizio fase di apprendimento..."+"\n"+"\n");
				out.writeObject(1);		
			}
	
			answer=in.readObject().toString();
			if(!answer.equals("OK")){
				System.out.println(answer);
				creaNuovaFinestra("notFound.fxml",e,true);
				return;
			}
		}
		out.writeObject(3);
		areaTesto.appendText("Inizio fase di predizione..."+"\n"+"\n");
		answer= in.readObject().toString();
		if(answer.equalsIgnoreCase("QUERY")) {
			areaTesto.appendText(in.readObject().toString()+"\n");
		}
		
		avvia.setVisible(false);
		available = true;
		num.clear();
	}catch(IOException | ClassNotFoundException exc) {
		answer=exc.getMessage();
		creaNuovaFinestra("erroreGenerico.fxml",e,true);
		System.out.println(exc.getMessage());
	}
	
}
	
	
	
/**
 * Il metodo esegue la connessione con il server
 */
@SuppressWarnings("deprecation")
static void connection() {
	Socket socket;
		try {
			addr = InetAddress.getByName("127.0.0.1");
			System.out.println(addr);
		} catch (UnknownHostException e) {
			System.out.println(e.toString());
			return;
		}
		try {
			socket = new Socket("127.0.0.1", new Integer(8080).intValue());
			System.out.println(socket);		
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());	; // stream con richieste del client
		}  catch (IOException e) {
			System.err.println(e.getMessage());
		}

	}

/**
 * Il metodo crea una nuova scena a seconda del nomeFile che viene passato, dall'evento e dalla modalità (definita dal booleano)
 * @param nomeFile : nome del file .fxml
 * @param e : evento 
 * @param boole : booleano, se true crea un nuovvo pannello sostiuendo il precedente, 
 * 							se false quest' ultimo viene lasciato in secondo piano durante la creazione del nuovo pannello
 * @throws IOException
 */
private void creaNuovaFinestra(String nomeFile, ActionEvent e, boolean boole) {
	try {
		if(nomeFile.equals("erroreGenerico.fxml")) {
			FXMLLoader loader=new FXMLLoader();
	        loader.setLocation(getClass().getResource(nomeFile));
	        loader.load();
	        SampleController contrll=loader.getController();
	        contrll.tipoErrore.setText(answer);

	        Parent p=loader.getRoot();
	        Stage stage=(Stage)((Node)e.getSource()).getScene().getWindow();
	        stage.setScene(new Scene(p));
	        stage.show();
	        return;
		}
		Parent root = FXMLLoader.load(getClass().getResource(nomeFile));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
		if(boole == true) 
			stage=(Stage)((Node)e.getSource()).getScene().getWindow();
		else 
            stage=new Stage(StageStyle.DECORATED);
          stage.setTitle(nomeFile);
          stage.setScene(scene);
          stage.show();
	}catch(IOException exc) {
		System.out.println(exc.getMessage());
	}
	
}
	/**
	 * Una volta cliccato il tasto 'enter' da tastiera, verrà eseguito questo metodo.
	 * Dove, una volta scelto il numero corrispondente al figlio scelto dell'albero,
	 * si clicca invio per apponto inviare al server la scelta fatta.
	 * Elaborata la scelta, viene restituito a video il risultato, che può essere un insieme di nodi o un nodo foglia.
	 * Nel caso di nodo foglia, non sarà più possibile ricliccare invio dato che a quel punto, verrà interrotta l'esecuzione. 
	 * @param e
	 */
	@FXML
	private void invioClicked(ActionEvent e) {
	if(available == true) {
		try {
			out.writeObject(Integer.valueOf(num.getText()));
			answer=in.readObject().toString();
			if(answer.equals("QUERY")){
				answer=in.readObject().toString();
				System.out.println(answer);
				areaTesto.appendText(answer+"\n");
			}
			else if(answer.equals("OK"))
			{ 
				answer=in.readObject().toString();
				System.out.println("Predicted class:"+answer);
				areaTesto.appendText("Predict class:"+ answer+"\n");
				available = false;
				creaFinestraFinale(e);
			}
			else { 
				System.out.println(answer);
				creaNuovaFinestra("erroreGenerico.fxml",e,true);
			}
			num.clear();
			
		} catch (IOException e1) {
			System.out.println(e1.toString());
		} catch (ClassNotFoundException e1) {
			System.out.println(e1.toString());
		} catch (NumberFormatException e1) {	//non è stato inviato un numero
		//	answer = "Non hai inserito un numero";
		//	creaNuovaFinestra("erroreGenerico.fxml",e,true);
			System.out.println(e1.toString());
			areaTesto.appendText("Non hai inserito un numero\n\n");		
			num.clear();
		}
	}else {
		num.setText("Premi il tasto 'avvia'");
	}
   }

	/**
	 * Una volta cliccato il bottone no, verrà eseguito questo metodo.
	 * Il metodo terminerà l'esecuzione del programma.
	 * @param e
	 */
	@FXML
	private void noClicked(ActionEvent e) {
		System.exit(0);
	}
	/**
	 * Una volta cliccato il bottone si, verrà eseguito questo metodo.
	 * Il metodoo farà ritornare alla scena precedente
	 * @param e
	 */
	@FXML
	private void siClicked(ActionEvent e) {
			creaNuovaFinestra("apprendimento.fxml",e,true);
			ripetuto = true;
	}
	
	private void creaFinestraFinale(ActionEvent e) {
		FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("Scelta.fxml"));

        try {
            loader.load();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        SampleController contrll=loader.getController();
        contrll.risultatoFin.setText("Predict class :"+answer);

        Parent p=loader.getRoot();
        Stage stage=(Stage)((Node)e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(p));
        stage.show();
	}
	@FXML
   private void ripristinaClicked(ActionEvent e) {
		ripetuto = true;
		creaNuovaFinestra("apprendimento.fxml",e,true);
	}
	@FXML
	private void chiudiClicked(ActionEvent e) {
		System.exit(0);
	}
	private void creaFinestraIniziale(String nomeFile, ActionEvent e) {
		FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource(nomeFile));
        try {
			loader.load();
		}catch (IOException e1) {
			System.out.println(e1.getMessage());
		}
        SampleController contrll=loader.getController();
        contrll.testo.setText("Inserisci il nome della tabella");

        Parent p=loader.getRoot();
        Stage stage=(Stage)((Node)e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(p));
        stage.show();
	}
}
