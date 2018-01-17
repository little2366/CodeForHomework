import java.util.Random;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ThreadControl extends Application {

	private GridPane pane;
	private Label title1;
	private Label title2;
	private Label title3;
	
	private TextField text1;
	private TextField text2;
	private TextField text3;
	
	private Button start;
	private boolean isRun = false;
	
	private PbiThread task1;
	private PbiThread task2;
	private PbiThread task3;
	
	@Override
	public void start(Stage stage) throws Exception {
		pane = new GridPane();
		pane.setAlignment(Pos.CENTER);
        pane.setHgap(15);
        pane.setVgap(12);
        pane.setPadding(new Insets(15, 25, 10, 25));
		title1 = new Label("�߳�һ");
		title1.setFont(Font.font(18));
		text1 = new TextField();
		text1.setEditable(false);
		pane.add(title1, 0, 0);
		pane.add(text1, 0, 1);
		pane.setHalignment(title1, HPos.CENTER);   //�ڱ���о���
		title2 = new Label("�̶߳�");
		title2.setFont(Font.font(18));
		text2 = new TextField();
		text2.setEditable(false);
		pane.add(title2, 1, 0);
		pane.add(text2, 1, 1);
		pane.setHalignment(title2, HPos.CENTER);
		title3 = new Label("�߳���");
		title3.setFont(Font.font(18));
		text3 = new TextField();
		text3.setEditable(false);
		pane.add(title3, 2, 0);
		pane.add(text3, 2, 1);
		pane.setHalignment(title3, HPos.CENTER);
		start = new Button("��ʼ");
		start.setMinWidth(110);
		start.setMinHeight(25);
		pane.add(start, 1, 3);
		
		
		task1 = new PbiThread();
		text1.textProperty().bind(task1.messageProperty());
		task2 = new PbiThread();
		text2.textProperty().bind(task2.messageProperty());
		task3 = new PbiThread();
		text3.textProperty().bind(task3.messageProperty());
		
		start.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				start.setText(isRun ? "��ʼ" : "ֹͣ");
				if (isRun) {
					isRun = false;
				} else {
					isRun = true;
					task1.restart();
					task2.restart();
					task3.restart();
				}
			}
		});
		
		stage.setTitle("���̿���");
		Scene scene = new Scene(pane, 400, 150);
		stage.setScene(scene);
		stage.show();
	}
	
	//��������Ĵ�д��ĸ
	private String getRandomCharacter(){
        Random r = new Random();
        char c = (char)(r.nextInt(26) + 'A');
        return String.valueOf(c);
    }
	
	public class PbiThread extends Service<Void> {
		@Override
		protected Task<Void> createTask() {
			return new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					while (isRun) { // ����ѭ������
						try {
							// ʹ�߳�����100����
							Thread.sleep(100);
						} catch (InterruptedException e) {
							System.out.println("��ǰ�̱߳��ж�");
							break;
						} 
						this.updateMessage(getRandomCharacter()); // Ϊ��ʵ��UI��������԰�
					}
					return null;
				}
			};
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}