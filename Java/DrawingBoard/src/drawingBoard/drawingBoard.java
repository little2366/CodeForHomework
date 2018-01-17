package drawingBoard;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class drawingBoard extends Application {
	
	final int WINDOW_WIDTH = 500;
	final int WINDOW_HEIGHT = 500;

	private BorderPane mainPane;
	private MenuBar menuBar;
	private Menu fileMenu, shapesMenu, modeMenu;
	private MenuItem exitItem;
	private RadioMenuItem fillItem, unfillItem;
	private RadioMenuItem rectangleItem, polygonItem;
	
	private double startX = 0; // Mouse cursor's X position
	private double startY = 0; // Mouse cursor's Y position
	private double width = 0; // The rectangle's width
	private double height = 0; // The rectangle's height
	
	//private Rectangle rec;
	private Polygon rec;
	private Polygon pol;
	
	private int isFill = 1;
	
	private Scene scene;
	
	@Override
	public void start(Stage stage) throws Exception {
		mainPane = new BorderPane();
		menuBar = new MenuBar();

		buildFileMenu();

		buildShapesMenu();

		menuBar.getMenus().add(fileMenu);
		menuBar.getMenus().add(shapesMenu);

		mainPane.setTop(menuBar);

		stage.setTitle("Watch Me");

		scene = new Scene(mainPane, this.WINDOW_WIDTH, this.WINDOW_HEIGHT);
		stage.setScene(scene);
		stage.show();
		
	}
	
	public void buildFileMenu() {
		fileMenu = new Menu("File");

		exitItem = new MenuItem("exit");
		exitItem.setOnAction(e->System.exit(0));
		
		fileMenu.getItems().add(exitItem);
	}

	public void buildShapesMenu() {
		shapesMenu = new Menu("Shapes");

		//创建mode子菜单
		modeMenu = new Menu("Drawing mode");

		fillItem = new RadioMenuItem("fill");
		fillItem.setSelected(true);
		fillItem.setOnAction(e -> {
			isFill = 1;
		});

		unfillItem = new RadioMenuItem("unfill");
		unfillItem.setOnAction(e -> {
			isFill = 0;
		});

		
		ToggleGroup mode = new ToggleGroup();
		mode.getToggles().addAll(fillItem, unfillItem);
		modeMenu.getItems().addAll(fillItem, unfillItem);

		//创建rectangle菜单项
		rectangleItem = new RadioMenuItem("Rectangle");
		polygonItem = new RadioMenuItem("Polygon");
		ToggleGroup shape = new ToggleGroup();
		shape.getToggles().addAll(rectangleItem, polygonItem);
		
		rectangleItem.setOnAction(e -> {
			drawRectangle();
		});
	    polygonItem.setOnAction(e -> {
	    	drawPolygon();
	    });
		
		shapesMenu.getItems().addAll(rectangleItem, polygonItem, modeMenu);
	}
	
	public void drawRectangle(){
		mainPane.setOnMousePressed(e->{
			startX = e.getX();
			startY = e.getY();
		});
		mainPane.setOnMouseDragged(e -> {
			width = e.getX() - startX;
			height = e.getY() - startY;

			//rec = new Rectangle(startX, startY, width, height);
			//改进绘制长方形
			rec = new Polygon(startX, startY, startX+width, startY, startX+width, startY+height, startX, startY+height);
			if(isFill == 0)
				rec.setFill(Color.WHITE);
			else
				rec.setFill(Color.BLACK);
			rec.setStroke(Color.BLACK);
			mainPane.getChildren().clear();
			mainPane.setTop(menuBar);
			mainPane.getChildren().addAll(rec);
		});
	}
	
	public void drawPolygon(){
		mainPane.setOnMousePressed(e->{
			startX = e.getX();
			startY = e.getY();
		});
		mainPane.setOnMouseDragged(e -> {
			width = e.getX() - startX;
			height = e.getY() - startY;
			pol = new Polygon(startX, startY, startX+width,
					startY, startX+3*width/2, startY+height/2, startX+width, startY+height,
					startX, startY+height, startX-width/2, startY+height/2);
			
			if(isFill == 0)
				pol.setFill(Color.WHITE);
			else
				pol.setFill(Color.BLACK);
			pol.setStroke(Color.BLACK);
			mainPane.getChildren().clear();
			mainPane.setTop(menuBar);
			mainPane.getChildren().addAll(pol);
		});
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
