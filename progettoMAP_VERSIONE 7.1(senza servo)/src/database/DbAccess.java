package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Daniele Lovecchio
 * Realizza l'accesso alla base di dati
 */
public class DbAccess {
	//ATTRIBUTI
	/**
	 * DRIVER
	 */
	private final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    private final String DBMS = "jdbc:mysql";
	
	/**
	 * Contiene l'identificativo del server su cui risiede la base di dati
	 */
	private String SERVER = "localhost";
	
	/**
	 * Contiene il nome della base di dati
	 */
   private String DATABASE = "MapDB";
   
   /**
    * La porta su cui il DBMS MySQL accetta le connessioni
    */
   private final String PORT = "3306";
	
   /**
    * Contiene il nome dell'utente per l'accesso alla base di dati
    */
   private String USER_ID = "MapUser";
   
   /**
    * Contiene lsa password di autenticazione per l'utente identificato da USER_ID
    */
   private String PASSWORD = "map";
   
   /**
    * Gestisce una connessione
    */
   private Connection conn;
   
	//METODI
   /**
    * Impartisce al class loader l'ordine di caricare il driver mysql, inizializza la connessione
    * riferita da conn. Il metodo solleva e propaga un'eccezione di tipo DataBaseConnectionException
    * in caso di fallimento nella connessione al dataabase
    * @throws DatabaseConnectionException
    */
   
   public void initConnection()throws DataBaseConnectionException{
	   if(conn==null) {
		   try {
			   Class.forName(DRIVER_CLASS_NAME).newInstance();
			   }catch(ClassNotFoundException e) {
				   System.out.println(e.getMessage());
					throw new DataBaseConnectionException("Driver not found");
			   }catch(InstantiationException e) {
				   System.out.println(e.getMessage());
					throw new DataBaseConnectionException("Error during the instantiation");
			   }catch(IllegalAccessException e){
				   System.out.println(e.getMessage());
					throw new DataBaseConnectionException("Cannot access the driver");
			   }
		   String connectionString = DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE+ "?user=" + USER_ID + "&password=" + PASSWORD + " "+"&serverTimezone=UTC ";
	       System.out.println("Connection : "+" "+connectionString);
	       
	       try {			
				conn = DriverManager.getConnection(connectionString);
			} catch(SQLException e) {
				throw new DataBaseConnectionException();
			}
	   }
   }
   
   /**
    * @return conn
    */
    Connection getConnection() {
		return conn;
	}
    
    /**
     * Chiude la connessione con il database
     * @throws SQLException 
     */
     public void closeConnection() throws SQLException {
    	 if(conn!=null) {
    		 conn.close();
    	 }
	}
}
