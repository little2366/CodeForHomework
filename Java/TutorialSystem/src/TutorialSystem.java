import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import javafx.stage.Stage;
import java.sql.*;

public class TutorialSystem extends Application {
	
	private BorderPane loginPane;
	private HBox titlePane;
	private HBox leftPane;
	private VBox rightPane;
	private GridPane editPane;
	
	private TextField id;
	private PasswordField password;
	
	private HBox radioPane;
	private RadioButton student;
	private RadioButton instructor;
	private RadioButton admin;
	private ToggleGroup userType;
	private int choose = 0;
	
	private HBox buttonPane;
	private Button login;
	private Button reset;
	
	//���ݿ���ز���
	private Connection con;
	private Statement sta;
	private String driverName = "com.mysql.jdbc.Driver";
	private String url = "jdbc:mysql://127.0.0.1:3306/tutor";   
	private String user = "root";
	private String pwd = "223366";
	private ResultSet rs;   // ��Ų�ѯ���ļ�¼��
	private String sqlStr;   //����ѡ��ķ�ʽ�޸Ĳ�ѯ���
	
	//���ݿ�Ĳ�ѯ
	static private String userPwd = "�����ڵ�ǰ�����û�";
	
	
	protected BorderPane getPane() {
		loginPane = new BorderPane();
		loginPane.setId("loginPane");
		loginPane.setPadding(new Insets(15,10,0,15));
		
		//��ʾ�Ĵ����
		titlePane = new HBox();
		titlePane.setAlignment(Pos.CENTER);
		Label head = new Label("��������ʦ�ƹ���ϵͳ");
		head.setFont(Font.font("����", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 26));
		titlePane.getChildren().add(head);
		
		//�������࣬������һ��ͼƬ
        leftPane = new HBox();
        leftPane.setAlignment(Pos.CENTER);
		ImageView imageView = new ImageView("image/tutor.jpg");
		imageView.setFitWidth(230);
		imageView.setFitHeight(190);
		leftPane.getChildren().add(imageView);
		
		//������Ҳ࣬����һϵ�������Ͱ�ť
		rightPane = new VBox();
		rightPane.setPadding(new Insets(50,10,0,45));
		rightPane.setSpacing(10);
		
		Label title = new Label("�û���¼");
	    title.setFont(Font.font("����", FontWeight.NORMAL, FontPosture.REGULAR, 18));
	    
	    editPane = new GridPane();
	    editPane.setHgap(13);
	    editPane.setVgap(10);
	    editPane.add(title, 0, 0);
	    editPane.add(new Label("�û���"), 0, 1);
	    id = new TextField();
	    id.setPromptText("ѧ�Ż򹤺�");  
	    editPane.add(id, 1, 1);
	    editPane.add(new Label("����"), 0, 2);
	    password = new PasswordField();
	    password.setPromptText("��ʼ����Ϊ12345");  
	    editPane.add(password, 1, 2);
	    editPane.add(new Label("�û�����"), 0, 3);
	    
	    radioPane = new HBox();
	    radioPane.setSpacing(6);
	    student = new RadioButton("ѧ��");
	    instructor = new RadioButton("��ʦ");
	    admin = new RadioButton("����Ա");
	    userType = new ToggleGroup();
	    student.setToggleGroup(userType);
	    instructor.setToggleGroup(userType);
	    admin.setToggleGroup(userType);
	    radioPane.getChildren().addAll(student, instructor, admin);
	    editPane.add(radioPane, 1, 3);
	    
	    buttonPane = new HBox();
	    buttonPane.setSpacing(15);
	    buttonPane.setAlignment(Pos.CENTER_RIGHT);
	    buttonPane.setPadding(new Insets(15,20,0,0));
	    reset = new Button("����");
	    reset.setPrefSize(50, 25);
	    login = new Button("��¼");
	    login.setPrefSize(50, 25);
	    buttonPane.getChildren().addAll(reset, login);
	    
	    rightPane.getChildren().addAll(title, editPane, buttonPane);
		
		loginPane.setTop(titlePane);
		loginPane.setLeft(leftPane);
		loginPane.setCenter(rightPane);
		
		//��ӵ�ѡ��ť��ѡ���¼�
		student.setOnAction(e -> {
			if(student.isSelected()){
				choose = 1;
			}
		});
		instructor.setOnAction(e -> {
			if(instructor.isSelected()){
				choose = 2;
			}
		});
		admin.setOnAction(e -> {
			if(admin.isSelected()){
				choose = 3;
			}
		});
		
		//����
		reset.setOnAction(e->{
		    id.setText("");
		    password.setText("");
		    student.setSelected(false);
		    instructor.setSelected(false);
		    admin.setSelected(false);
		});
			
		return loginPane;
	}
	
	@Override
	public void start(Stage primaryStage) {
		Scene scene = new Scene(getPane(),550, 330);
		scene.getStylesheets().add("style.css");
		primaryStage.setTitle("��¼"); 
		primaryStage.setScene(scene); 
		primaryStage.show(); 
		
		/*
		 * ���Խ���ť�¼�����start�����У������Ͳ��ÿ���primaryStage��������
		 */
		login.setOnAction(e->{
			TutorialSystem queryTable = new TutorialSystem();
  		    queryTable.connect();
			switch(choose){
			  case 1:
	    		  sqlStr = "select * from student where " + "stu_id = " + id.getText();
	    	      queryTable.queryTable(sqlStr);
	    		  queryTable.closeConnection();
	    		  if((this.userPwd).equals("�����ڵ�ǰ�����û�")){
	    			  wrongType();
			      }
	    		  else {
	    			  if(password.getText().equals(this.userPwd)){
		    			  Platform.runLater(new Runnable() {
		    				    public void run() {             
		    				    	new ForStudent().start(new Stage(), id.getText());   //����ǰ�û���ID���ݵ���һ��stage
		    				    	primaryStage.close();
		    				    }
		    			   });
		    		  }
	    			  else {
		    			  wrongPwd();
	    			  }
	    		  }
	    		  break;
			  case 2:
				  sqlStr = "select * from instructor where " + "ins_id = " + id.getText();
	    	      queryTable.queryTable(sqlStr);
	    		  queryTable.closeConnection();
	    		  System.out.println(this.userPwd);
	    		  if((this.userPwd).equals("�����ڵ�ǰ�����û�")){
	    			  wrongType();
			      }
	    		  else {
	    			  if(password.getText().equals(this.userPwd)){
		    			  Platform.runLater(new Runnable() {
		    				    public void run() {             
		    				    	new ForInstructor().start(new Stage(), id.getText());   
		    				    	primaryStage.close();
		    				    }
		    			   });
		    		  }
	    			  else {
		    			  wrongPwd();
	    			  }
	    		  }
	    		  break;
	    	  case 3:
	    		  sqlStr = "select * from admin where " + "admin_id = " + id.getText();
	    	      queryTable.queryTable(sqlStr);
	    		  queryTable.closeConnection();
	    		  if((this.userPwd).equals("�����ڵ�ǰ�����û�")){
	    			  wrongType();
			      }
	    		  else {
	    			  if(password.getText().equals(this.userPwd)){
		    			  Platform.runLater(new Runnable() {
		    				    public void run() {             
		    				    	new ForAdmin().start(new Stage(), id.getText());  
		    				    	primaryStage.close();
		    				    }
		    			   });
		    		  }
	    			  else {
		    			  wrongPwd();
	    			  }
	    		  }
	    		  break;
	    	  default:
	    		  Alert warning = new Alert(Alert.AlertType.INFORMATION);
	    		  warning.setHeaderText("��ѡ���û����ͣ�");
	    		  warning.showAndWait();
	    	      break;	  
			}
		});
		
	}
	
	/*
	 * �������ݿ�
	 */
	public void connect() {
		try {
			Class.forName(driverName);
			con = DriverManager.getConnection(url, user, pwd);
			System.out.println("���ݿ����ӳɹ�");

		} catch (ClassNotFoundException e) {
			System.out.println("������������" + e.getMessage());
		} catch (SQLException e) {
			System.out.println("�������ݿ����" + e.getMessage());
		}
	}
	/*
	 * ��ѯ���ݱ��е�������Ϣ
	 */
	public void queryTable(String sql) {
		// ����statement����
		try {
			sta = con.createStatement();
			// ִ�в�ѯ����
			rs = sta.executeQuery(sql);
			this.userPwd = "�����ڵ�ǰ�����û�";
			// ��ʾ���
			while(rs.next()){
				this.userPwd = rs.getString("password");
			}
			
			
		} catch (SQLException e) {
			System.out.println("��ѯ����" + e.getMessage());
		}
	}
	
	/*
	 * �ر����ݿ�����
	 */
	public void closeConnection() {
		// ������Ӳ�Ϊ�գ��ر����Ӷ���
		if (con != null) {
			try {
				// ���������Ϊ�գ��ر�������
				if (sta != null) {
					// ����������Ϊ�գ��رս��������
					if (rs != null) {
						rs.close();
					}
					sta.close();
				}
				con.close();
				System.out.println("���ݿ����ӹر�");
			} catch (SQLException e) {
				System.out.println("���ݿ�رճ���" + e.getMessage());
			}
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	//�û��������벻һ�µ����
	private void wrongPwd() {
		password.setText("");
		Alert warning = new Alert(Alert.AlertType.WARNING);
		warning.setHeaderText("�������");
		warning.showAndWait();
	}
	
	//�������û��������ڵ����
	private void wrongType() {
		password.setText("");
	    student.setSelected(false);
	    instructor.setSelected(false);
	    admin.setSelected(false);
		Alert warning = new Alert(Alert.AlertType.WARNING);
		warning.setHeaderText("��ǰ�����û������ڣ�");
		warning.showAndWait();
	}
}
