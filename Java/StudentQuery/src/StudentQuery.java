import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
//  1.导入java.sql包
import java.sql.*;


public class StudentQuery extends Application {
    
	private BorderPane pane;
	private FlowPane topPane;
	private Label notice;
	private ComboBox<String> status;
	private String[] chooseType = { "未选", "待定", "选定" };
	private int mode = -1;  //0表示未选，1表示待定，2表示选定
	private Button query;
	private TableView<Student> result;
	private static ObservableList<Student> obsList = FXCollections.observableArrayList();
	
	private Connection con;
	private Statement sta;
	private String driverName = "com.mysql.jdbc.Driver";
	private String url = "jdbc:mysql://127.0.0.1:3306/student";
	private String user = "root";
	private String password = "223366";
	
	private ResultSet rs;   // 存放查询到的记录集
	private String sqlStr;   //根据选择的方式修改查询语句
	
	private String id;
	private String name;
	private String sex;
	private String major;
	private String classes;
	private String telephone;
	private String state;
	private String instructor;
	
	protected BorderPane getPane() {
		pane = new BorderPane();
		pane.setPadding(new Insets(8, 5, 5, 5));
		
		topPane = new FlowPane();
		topPane.setPadding(new Insets(0, 5, 8, 5));
		topPane.setHgap(8);
		notice = new Label("学生状态");
		status = new ComboBox<>(); 
		status.setPrefWidth(100);   
		ObservableList<String> items = FXCollections.observableArrayList(chooseType);
		status.setItems(items);
		query = new Button("查询");
		topPane.getChildren().addAll(notice, status, query);
		
		/*
		 * TableView的使用
		 */
		
		// 1.创建表视图
		result = new TableView<Student>();
		result.setId("my-table");
		// 2.创建列对象
		TableColumn<Student, String> tColId = new TableColumn<Student, String>("学号");
		TableColumn<Student, String> tColName = new TableColumn<Student, String>("姓名");
		TableColumn<Student, String> tColSex = new TableColumn<Student, String>("性别");
		TableColumn<Student, String> tColMajor = new TableColumn<Student, String>("专业");
		TableColumn<Student, String> tColClasses = new TableColumn<Student, String>("班级");
		TableColumn<Student, String> tColTele = new TableColumn<Student, String>("电话");
		TableColumn<Student, String> tColState = new TableColumn<Student, String>("状态");
		TableColumn<Student, String> tColInstructor = new TableColumn<Student, String>("导师");
		// 3.把列对象添加到表视图
	    result.getColumns().addAll(tColId, tColName, tColSex, tColMajor, tColClasses, tColTele, tColState, tColInstructor);
	    
		
		pane.setTop(topPane);
		pane.setCenter(result);
		
		/*为选择方式的添加点击事件*/
		status.setOnAction(e->{
			mode = items.indexOf(status.getValue());
			if(mode == 0){
				sqlStr = "select * from studentdata where " + "state = '未选'";
			}
			else if(mode == 1){
				sqlStr = "select * from studentdata where " + "state = '待定'";
			}
			else if(mode == 2){
				sqlStr = "select * from studentdata where " + "state = '选定'";
			}
			System.out.println(sqlStr);
		});
		
		query.setOnAction(e -> {
			StudentQuery jdbcTest = new StudentQuery();
			jdbcTest.connect();
			obsList.clear();   //刷新数据
			jdbcTest.queryInDB(sqlStr);
			System.out.println(obsList.size());
			// 5.把数据列表和表视图关联
		    result.setItems(obsList);
		 	// 6.把数据列表和列对象建立关联
		    tColId.setCellValueFactory(new PropertyValueFactory<Student, String>("id"));
		    tColName.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
		    tColSex.setCellValueFactory(new PropertyValueFactory<Student, String>("sex"));
		    tColMajor.setCellValueFactory(new PropertyValueFactory<Student, String>("major"));
		    tColClasses.setCellValueFactory(new PropertyValueFactory<Student, String>("classes"));
		    tColTele.setCellValueFactory(new PropertyValueFactory<Student, String>("telephone"));
		    tColState.setCellValueFactory(new PropertyValueFactory<Student, String>("state"));
		    tColInstructor.setCellValueFactory(new PropertyValueFactory<Student, String>("instructor"));
			jdbcTest.closeConnection();
		});

		return pane;
	}
	@Override
	public void start(Stage primaryStage) {
		Scene scene = new Scene(getPane(), 650, 250);
		//添加自定义css样式
		scene.getStylesheets().add("tableViewStyle.css");
		primaryStage.setTitle("查询"); 
		primaryStage.setScene(scene); 
		primaryStage.show(); 
	}
	
	public void connect() {
		try {
			// 2.使用Class类的forName方法，将驱动程序类加载到JVM（Java虚拟机）中
			Class.forName(driverName);

			// 3.使用DriverManager类的静态方法getConnection来获得连接对象
			con = DriverManager.getConnection(url, user, password);

			// 显示结果
			System.out.println("数据库连接成功");

		} catch (ClassNotFoundException e) {
			System.out.println("加载驱动出错：" + e.getMessage());
		} catch (SQLException e) {
			System.out.println("连接数据库出错：" + e.getMessage());
		}
	}
	
	public void queryInDB(String sql) {
		// 创建statement对象
		try {
			sta = con.createStatement();
			// 执行查询操作
			rs = sta.executeQuery(sql);
			// 显示结果
			while(rs.next()){
				id = rs.getString("id");
				name = rs.getString("name");
				sex = rs.getString("sex");
				major = rs.getString("major");
				classes = rs.getString("class");
				telephone = rs.getString("telephone");
				state = rs.getString("state");
				instructor = rs.getString("instructor");
				System.out.println(instructor);
				if(instructor.equals(" "))
					instructor = "null";
				obsList.add((new Student(id, name, sex, major, classes, telephone, state, instructor)));
			}
			
		} catch (SQLException e) {
			System.out.println("查询出错：" + e.getMessage());
		}
	}

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
	
	public static class Student{
		private String id;
		private String name;
		private String sex;
		private String major;
		private String classes;
		private String telephone;
		private String state;
		private String instructor;
		
		private Student(String id, String name, String sex, String major, String classes, String telephone, String state, String instructor){
			this.id = id;
			this.name = name;
			this.sex = sex;
			this.major = major;
			this.classes = classes;
			this.telephone = telephone;
			this.state = state;
			this.instructor = instructor;
		}

		public String getName(){
			return name;
		}
		
		public void setName(String name){
			this.name=name;
		}
		
		public String getTelephone(){
			return telephone;
		}
		
		public void setTelephone(String telephone){
			this.telephone=telephone;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getSex() {
			return sex;
		}

		public void setSex(String sex) {
			this.sex = sex;
		}

		public String getMajor() {
			return major;
		}

		public void setMajor(String major) {
			this.major = major;
		}

		public String getClasses() {
			return classes;
		}

		public void setClasses(String classes) {
			this.classes = classes;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getInstructor() {
			return instructor;
		}

		public void setInstructor(String instructor) {
			this.instructor = instructor;
		}
		
		
	}
}
