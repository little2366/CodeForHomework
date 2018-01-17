package colorPanel;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class colorPanel extends Application{
	private BorderPane pane;
	
	private HBox titlePane;
	private Label title;
	
	private BorderPane blockPane;
	private HBox introductPane;
	private GridPane barPane;
	private Rectangle []rec = new Rectangle[12];
	
	private HBox showPane;
	private Pane forCenter;
	private Rectangle recShow;
	
	private HBox toolPane;	
	private Slider []sl = new Slider[3];
	private int []color = new int[3];
	
	private HBox buttonPane;
	private Button choose;
	private Button reset;
	
	private int flag = 0;   //标志位，表示当前颜色填充到了哪一块
	
	protected BorderPane getPane() {
		pane = new BorderPane();
		pane.setPadding(new Insets(20,10,10,10));
		
		titlePane = new HBox();
		titlePane.setAlignment(Pos.CENTER);
		titlePane.setPadding(new Insets(0,0,20,0));
		title = new Label("请在这里调色");
		title.setFont(Font.font(18));
		titlePane.getChildren().add(title);
		pane.setTop(titlePane);
		
		blockPane = new BorderPane();
		blockPane.setMinWidth(180);
		introductPane = new HBox(new Label("你的选择"));
		introductPane.setAlignment(Pos.CENTER);
		introductPane.setStyle("-fx-background-color: yellow");
		introductPane.setMinHeight(20);
		barPane = new GridPane();
		barPane.setHgap(7);
		barPane.setVgap(5);
		for(int i=0; i<12; ++i){
			rec[i] = new Rectangle(0,0,85,42);
			rec[i].setFill(Color.WHITE);
			rec[i].setStroke(Color.BLACK);
			barPane.add(rec[i], i%2, i/2);
		}
		blockPane.setTop(introductPane);
		blockPane.setCenter(barPane);
		pane.setLeft(blockPane);
		
		showPane = new HBox();
		showPane.setAlignment(Pos.CENTER);
		showPane.setFillHeight(false);
		forCenter = new Pane();
		recShow = new Rectangle(10,-10,300,300);
		recShow.setFill(Color.WHITE);
		recShow.setStroke(Color.BLACK);
		forCenter.getChildren().add(recShow);
		showPane.getChildren().add(forCenter);
		pane.setCenter(showPane);
		
		toolPane = new HBox();
		toolPane.setMinWidth(180);
		toolPane.setSpacing(20);
		toolPane.setPadding(new Insets(0,0,20,15));
		for(int i=0; i<3; ++i){
			sl[i] = new Slider();
			sl[i].setOrientation(Orientation.VERTICAL);
			sl[i].setShowTickLabels(true);
			sl[i].setShowTickMarks(true);
			sl[i].setMax(255);
			sl[i].setValue(255);
			sl[i].setMajorTickUnit(51);
			toolPane.getChildren().add(sl[i]);
		}
		pane.setRight(toolPane);
		
		buttonPane = new HBox();
		buttonPane.setAlignment(Pos.CENTER);
		buttonPane.setSpacing(20);      //设置子元素之间的距离
		choose = new Button("就选它了");
		reset = new Button("   重置    ");
		buttonPane.getChildren().addAll(choose, reset);
		pane.setBottom(buttonPane);
		
		//设定color的初始值
		for(int i=0; i<3; ++i){
			color[i] = 255;
		}
		/*改变滑动条位置修改颜色*/
		sl[0].valueProperty().addListener(e -> {
			color[0] = (int)sl[0].getValue();
			recShow.setFill(Color.rgb(color[0], color[1], color[2]));
		});
		sl[1].valueProperty().addListener(ov -> {
			color[1] = (int) sl[1].getValue();
			recShow.setFill(Color.rgb(color[0], color[1], color[2]));
		});
		sl[2].valueProperty().addListener(ov -> {
			color[2] = (int) sl[2].getValue();
			recShow.setFill(Color.rgb(color[0], color[1], color[2]));
		});
		
		/*将选中的颜色加入到备选框中*/
		choose.setOnAction(e -> {
			flag = flag%12;
			rec[flag].setFill(Color.rgb(color[0], color[1], color[2]));
			flag++;
		});
		
		/*添加重置按钮事件*/
		reset.setOnAction(e -> {
			flag = 0;
			recShow.setFill(Color.WHITE);
			for(int i=0; i<12; ++i){
				rec[i].setFill(Color.WHITE);
			}
			for(int i=0; i<3; ++i){
				sl[i].setValue(255);
			}
		});
		return pane;
	}
	
	@Override
	public void start(Stage primaryStage) {
		Scene scene = new Scene(getPane(), 700, 420);
		primaryStage.setTitle("Color the Panel"); 
		primaryStage.setScene(scene); 
		primaryStage.show(); 
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
