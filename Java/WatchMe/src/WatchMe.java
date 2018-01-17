import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;

public class WatchMe extends Application{
	
	final int WINDOW_WIDTH = 300;
	final int WINDOW_HEIGHT = 300;
	private Circle ciLeft;
	private Circle ciRight;

	protected BorderPane getPane() {
		
		//总体布局采用边界布局
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(50,50,50,50));
		
		//眼睛部分采用HBox布局
		HBox eyePane = new HBox();
		eyePane.setAlignment(Pos.CENTER);
		eyePane.setFillHeight(false);
		Pane cPane = new Pane();
		eyePane.getChildren().add(cPane);
		
		//两个椭圆
		Ellipse elLeft = new Ellipse(40, 80, 40, 80);
		elLeft.setStroke(Color.BLACK);
		elLeft.setFill(Color.WHITE);
		cPane.getChildren().add(elLeft);
		Ellipse elRight = new Ellipse(150, 80, 40, 80);
		elRight.setStroke(Color.BLACK);
		elRight.setFill(Color.WHITE);
		cPane.getChildren().add(elRight);
		
		//两个实心圆
		
		ciLeft = new Circle(40, 80, 10);
		ciRight = new Circle(150, 80, 10);
		ciLeft.setFill(Color.BLACK);
		cPane.getChildren().add(ciLeft);
		ciRight.setFill(Color.BLACK);
		cPane.getChildren().add(ciRight);
		
		
		pane.setOnMouseExited(e -> {
			cPane.getChildren().removeAll(ciLeft, ciRight);
			ciLeft = new Circle(40, 80, 10);
			ciRight = new Circle(150, 80, 10);
			ciLeft.setFill(Color.BLACK);
			cPane.getChildren().add(ciLeft);
			ciRight.setFill(Color.BLACK);
			cPane.getChildren().add(ciRight);
		});
		
		pane.setCenter(eyePane);
		pane.setOnMouseMoved(e -> {
			int x = (int) e.getX();
			int y = (int) e.getY();
			cPane.getChildren().removeAll(ciLeft, ciRight);
			//正上方
			if(x>WINDOW_WIDTH/2-95 && x<WINDOW_WIDTH/2+95 && y>=0 && y<WINDOW_HEIGHT/2-80){
				ciLeft = new Circle(40, 10, 10);
				ciRight = new Circle(150, 10, 10);
			}
			//正右方
			else if(x>WINDOW_WIDTH/2+95 && x<=WINDOW_WIDTH && y>WINDOW_HEIGHT/2-80 && y<WINDOW_HEIGHT/2+80){
				ciLeft = new Circle(70, 80, 10);
				ciRight = new Circle(180, 80, 10);
			}
			else if(x>WINDOW_WIDTH/2-95 && x<=WINDOW_WIDTH/2+95 && y>=WINDOW_HEIGHT/2+80 && y<WINDOW_HEIGHT){
				ciLeft = new Circle(40, 150, 10);
				ciRight = new Circle(150, 150, 10);
			}
			else if(x>=0 && x<WINDOW_WIDTH/2-95 && y>WINDOW_HEIGHT/2-80 && y<WINDOW_HEIGHT/2+80){
				ciLeft = new Circle(10, 80, 10);
				ciRight = new Circle(120, 80, 10);
			}
			else{
				ciLeft = new Circle(40, 80, 10);
				ciRight = new Circle(150, 80, 10);
			}
			ciLeft.setFill(Color.BLACK);
			cPane.getChildren().add(ciLeft);
			ciRight.setFill(Color.BLACK);
			cPane.getChildren().add(ciRight);
		});
		return pane;
		
	}
	
	@Override
	public void start(Stage primaryStage) {
		Scene scene = new Scene(getPane(),300,300);
		primaryStage.setTitle("Watch Me"); 
		primaryStage.setScene(scene); 
		primaryStage.show(); 
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
