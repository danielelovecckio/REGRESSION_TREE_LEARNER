package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Daniele Lovecchio
 *
 */
public class MultiServer{
	//ATTRIBUTI
	private int PORT;

	//METODI
	public MultiServer(int PORT) {
		this.PORT=PORT;
		run();
	}
    private void run()  {                                //non ci deve essere throws poi si vede
        System.out.println("Server");
        ServerSocket serverSocket=null;
        try {
            serverSocket = new ServerSocket(PORT);

        while (true) {
                Socket socket = serverSocket.accept();
                new ServerOneClient(socket);
        }

        } catch (IOException e) {
            System.out.println("Errore durante l'accettazione del client");

        }finally {
            System.out.println("Chiusura");
            try {
                serverSocket.close();
            }catch(IOException e) {
                System.out.println("Socket non chiusa");
            }
        }
    }


}

