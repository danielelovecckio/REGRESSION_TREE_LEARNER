package application;
	
import javafx.application.Application; 
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;
  
/** 
 *  
 * @author Daniele Lovecchio, Giuseppe Alaimo, Luigi Fragale  
 */
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root =(BorderPane) FXMLLoader.load(getClass().getResource("Sample.fxml"));
			Scene scene = new Scene(root,800,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		SampleController.connection();
		launch(args);
	}
}
