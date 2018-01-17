import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Exercise extends Application {
	private BorderPane pane;
	
	private HBox topPane;
	private Pane introductPane;
	private ComboBox<String> cbo;
	private String[] chooseType = { "单项选择", "多项选择" };
	private int mode = 0;    //表示选择的类型，0表示单选，1表示多选
	
	private HBox countryPane;
	private ListView<String> countryList; 
	private ObservableList<String> listModel = FXCollections
			.observableArrayList("Canada", "China", "Denmark", "France",
					"Germany", "India", "Norway", "United Kingdom",
					"United States of America" );
	
	private HBox selectedPane;
	private Label label;
	private TextField selectedCountry;
	private String []country = new String[listModel.size()];
	private int count;
	
	protected BorderPane getPane() {
		pane = new BorderPane();
		
		topPane = new HBox();
		introductPane = new Pane(new Label("Choose Selection Mode:"));
		introductPane.setPadding(new Insets(0,20,0,0));
		cbo = new ComboBox<>(); 
		cbo.setPrefWidth(100);   
		cbo.setValue("单项选择");
		ObservableList<String> items = FXCollections.observableArrayList(chooseType);
		cbo.setItems(items);
		topPane.getChildren().addAll(introductPane,cbo);
		
		countryPane = new HBox();
		countryPane.setAlignment(Pos.CENTER);
		countryList = new ListView<String>(listModel);
		countryList.setPrefWidth(260);
		countryList.setPrefHeight(100);
		countryPane.getChildren().add(countryList);
		
		selectedPane = new HBox();
		selectedPane.setAlignment(Pos.CENTER);
		label = new Label("You Selected: ");
		selectedCountry = new TextField();
		selectedCountry.setPrefWidth(160); 
		selectedCountry.setEditable(false);
		selectedPane.getChildren().addAll(label, selectedCountry);
		
		pane.setBottom(selectedPane);
		pane.setTop(topPane);
		pane.setCenter(countryPane);
		
		/*为选择方式的添加点击事件*/
		cbo.setOnAction(e->{
			mode = items.indexOf(cbo.getValue());
			if(mode == 0){
				countryList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			}else{
				count = 0;
				countryList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			}
			for(int i=0; i<listModel.size(); ++i)
				country[i] = "";
			selectedCountry.setText("");
		});

		countryList.getSelectionModel().selectedItemProperty().addListener(new ListViewListener());
		
		return pane;
	}
	

	private class ListViewListener implements InvalidationListener {
		@Override
		public void invalidated(Observable arg0) {
			if(mode == 0){
				int selIndex = countryList.getSelectionModel().getSelectedIndex();
				selectedCountry.setText(listModel.get(selIndex));
			}
			else{
				int flag = 0;  //如果数组中已经存在该国家，就不再往里面加
				int selIndex = countryList.getSelectionModel().getSelectedIndex();
				for(int i=0; i<count; ++i){
					if(country[i].equals(listModel.get(selIndex))){
						flag = 1;
						break;
					}
				}
				if(flag == 0){
					country[count] = listModel.get(selIndex);
					count++;
				}
				String str = country[0];
				for(int i=1; i<count; ++i){
					str += " ";
					str += country[i];
				}
				selectedCountry.setText(str);
				selectedCountry.selectPositionCaret(str.length());
			}
		}
	}
	
	@Override
	public void start(Stage primaryStage) {
		Scene scene = new Scene(getPane(), 260, 200);
		primaryStage.setTitle("Color the Panel"); 
		primaryStage.setScene(scene); 
		primaryStage.show(); 
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
