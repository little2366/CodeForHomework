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
	
	//数据库相关操作
	private Connection con;
	private Statement sta;
	private String driverName = "com.mysql.jdbc.Driver";
	private String url = "jdbc:mysql://127.0.0.1:3306/tutor";   
	private String user = "root";
	private String pwd = "223366";
	private ResultSet rs;   // 存放查询到的记录集
	private String sqlStr;   //根据选择的方式修改查询语句
	
	//数据库的查询
	static private String userPwd = "不存在当前类型用户";
	
	
	protected BorderPane getPane() {
		loginPane = new BorderPane();
		loginPane.setId("loginPane");
		loginPane.setPadding(new Insets(15,10,0,15));
		
		//显示的大标题
		titlePane = new HBox();
		titlePane.setAlignment(Pos.CENTER);
		Label head = new Label("本科生导师制管理系统");
		head.setFont(Font.font("宋体", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 26));
		titlePane.getChildren().add(head);
		
		//主体的左侧，包括了一张图片
        leftPane = new HBox();
        leftPane.setAlignment(Pos.CENTER);
		ImageView imageView = new ImageView("image/tutor.jpg");
		imageView.setFitWidth(230);
		imageView.setFitHeight(190);
		leftPane.getChildren().add(imageView);
		
		//主体的右侧，包括一系列输入框和按钮
		rightPane = new VBox();
		rightPane.setPadding(new Insets(50,10,0,45));
		rightPane.setSpacing(10);
		
		Label title = new Label("用户登录");
	    title.setFont(Font.font("楷体", FontWeight.NORMAL, FontPosture.REGULAR, 18));
	    
	    editPane = new GridPane();
	    editPane.setHgap(13);
	    editPane.setVgap(10);
	    editPane.add(title, 0, 0);
	    editPane.add(new Label("用户名"), 0, 1);
	    id = new TextField();
	    id.setPromptText("学号或工号");  
	    editPane.add(id, 1, 1);
	    editPane.add(new Label("密码"), 0, 2);
	    password = new PasswordField();
	    password.setPromptText("初始密码为12345");  
	    editPane.add(password, 1, 2);
	    editPane.add(new Label("用户类型"), 0, 3);
	    
	    radioPane = new HBox();
	    radioPane.setSpacing(6);
	    student = new RadioButton("学生");
	    instructor = new RadioButton("教师");
	    admin = new RadioButton("管理员");
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
	    reset = new Button("重置");
	    reset.setPrefSize(50, 25);
	    login = new Button("登录");
	    login.setPrefSize(50, 25);
	    buttonPane.getChildren().addAll(reset, login);
	    
	    rightPane.getChildren().addAll(title, editPane, buttonPane);
		
		loginPane.setTop(titlePane);
		loginPane.setLeft(leftPane);
		loginPane.setCenter(rightPane);
		
		//添加单选按钮的选择事件
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
		
		//重置
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
		primaryStage.setTitle("登录"); 
		primaryStage.setScene(scene); 
		primaryStage.show(); 
		
		/*
		 * 可以将按钮事件放在start函数中，这样就不用考虑primaryStage的问题了
		 */
		login.setOnAction(e->{
			TutorialSystem queryTable = new TutorialSystem();
  		    queryTable.connect();
			switch(choose){
			  case 1:
	    		  sqlStr = "select * from student where " + "stu_id = " + id.getText();
	    	      queryTable.queryTable(sqlStr);
	    		  queryTable.closeConnection();
	    		  if((this.userPwd).equals("不存在当前类型用户")){
	    			  wrongType();
			      }
	    		  else {
	    			  if(password.getText().equals(this.userPwd)){
		    			  Platform.runLater(new Runnable() {
		    				    public void run() {             
		    				    	new ForStudent().start(new Stage(), id.getText());   //将当前用户的ID传递到下一个stage
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
	    		  if((this.userPwd).equals("不存在当前类型用户")){
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
	    		  if((this.userPwd).equals("不存在当前类型用户")){
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
	    		  warning.setHeaderText("请选择用户类型！");
	    		  warning.showAndWait();
	    	      break;	  
			}
		});
		
	}
	
	/*
	 * 连接数据库
	 */
	public void connect() {
		try {
			Class.forName(driverName);
			con = DriverManager.getConnection(url, user, pwd);
			System.out.println("数据库连接成功");

		} catch (ClassNotFoundException e) {
			System.out.println("加载驱动出错：" + e.getMessage());
		} catch (SQLException e) {
			System.out.println("连接数据库出错：" + e.getMessage());
		}
	}
	/*
	 * 查询数据表中的密码信息
	 */
	public void queryTable(String sql) {
		// 创建statement对象
		try {
			sta = con.createStatement();
			// 执行查询操作
			rs = sta.executeQuery(sql);
			this.userPwd = "不存在当前类型用户";
			// 显示结果
			while(rs.next()){
				this.userPwd = rs.getString("password");
			}
			
			
		} catch (SQLException e) {
			System.out.println("查询出错：" + e.getMessage());
		}
	}
	
	/*
	 * 关闭数据库连接
	 */
	public void closeConnection() {
		// 如果连接不为空，关闭连接对象
		if (con != null) {
			try {
				// 如果语句对象不为空，关闭语句对象
				if (sta != null) {
					// 如果结果集不为空，关闭结果集对象
					if (rs != null) {
						rs.close();
					}
					sta.close();
				}
				con.close();
				System.out.println("数据库连接关闭");
			} catch (SQLException e) {
				System.out.println("数据库关闭出错：" + e.getMessage());
			}
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	//用户名与密码不一致的情况
	private void wrongPwd() {
		password.setText("");
		Alert warning = new Alert(Alert.AlertType.WARNING);
		warning.setHeaderText("密码错误！");
		warning.showAndWait();
	}
	
	//该类型用户名不存在的情况
	private void wrongType() {
		password.setText("");
	    student.setSelected(false);
	    instructor.setSelected(false);
	    admin.setSelected(false);
		Alert warning = new Alert(Alert.AlertType.WARNING);
		warning.setHeaderText("当前类型用户不存在！");
		warning.showAndWait();
	}
}
