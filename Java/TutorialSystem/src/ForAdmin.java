import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.sql.*;
import java.util.ArrayList;


public class ForAdmin extends Application {
	private BorderPane adminPane;
	static private String userId;     //当前使用该系统的用户
	
	private TabPane tabPane;     
	private Tab stu;
	private Tab ins;
	private Tab result;
	private Tab admin;
	
	
	//编辑学生信息
	private BorderPane stuPane;
	private Button queryStu;
	private BorderPane topPane1;
	private Label title1;
	private TableView<Student> stuResult;
	private static ObservableList<Student> stuList = FXCollections.observableArrayList();
	private BorderPane stuOperation;
	private HBox stuBottom1;
	private TextField stuId;
	private TextField stuName;
	private TextField stuSex;
	private TextField stuMajor;
	private TextField stuClass;
	private TextField stuTel;
	private HBox stuBottom2;
	private Button addStu;
	private Button updStu;
	private Button delStu;
	
	
	//编辑老师信息
	private BorderPane insPane;
	private Button queryIns;
	private BorderPane topPane2;
	private Label title2;
	private TableView<Instructor> insResult;
	private static ObservableList<Instructor> insList = FXCollections.observableArrayList();
	private BorderPane insOperation;
	private HBox insBottom1;
	private TextField insId;
	private TextField insName;
	private TextField insSex;
	private TextField insTitle;
	private TextField insResearch;
	private TextField insTel;
	private TextField insPairNum;
	private HBox insBottom2;
	private Button addIns;
	private Button updIns;
	private Button delIns;
	
	
	//查看结对情况
	private BorderPane resultPane;
	private FlowPane topPane;
	private Label notice;
	private ComboBox<String> status;
	private String[] chooseType = { "未选", "待定", "选定" };
	private int mode = -1;  //0表示未选，1表示待定，2表示选定
	private TableView<StuIns> pairResult;
	private static ObservableList<StuIns> pairList = FXCollections.observableArrayList();
	private Button queryResult;
	
	//信息维护
	private BorderPane adminInfoPane;
	private GridPane changePwd;
	private PasswordField oldPwd;          //旧密码
	private PasswordField newPwd;          //新密码
	private PasswordField surePwd;         //确认密码
	private Button change;
	private GridPane addAdmin;
	private TextField adminId;          //新管理员账号
	private PasswordField adminPwd;         //新管理员密码
	private Button add;
	
	
	
	//数据库相关操作
	private Connection con;
	private Statement sta;
	private String driverName = "com.mysql.jdbc.Driver";
	private String url = "jdbc:mysql://127.0.0.1:3306/tutor";   
	private String user = "root";
	private String password = "223366";
	private ResultSet rs;   // 存放查询到的记录集
	private String sqlStr;   //根据选择的方式修改查询语句
	
	static private String userPwd;
	static private String[] insInfo = new String[2];
	private static ArrayList adminList = new ArrayList(); 
	
	protected BorderPane getPane() {
		
		adminPane = new BorderPane();
		adminPane.setPadding(new Insets(0,0,0,0));
		
		//主体的tab
		tabPane = new TabPane();
		stu = new Tab("学生信息");
		stu.setContent(showStu());
		ins = new Tab("导师信息");
		ins.setContent(showIns());
		result = new Tab("结对情况");
		result.setContent(showResult());
		admin = new Tab("系统维护");
		admin.setContent(adminInfo());
		tabPane.getTabs().addAll(stu, ins, result, admin);
		
		adminPane.setCenter(tabPane);
		
		return adminPane;
	}
	
	@Override
	public void start(Stage primaryStage) { 
		
	}
	
	public void start(Stage primaryStage, String userId) {
		this.userId = userId;
		ForAdmin queryAdmin = new ForAdmin();
		queryAdmin.connect();
		sqlStr = "select * from admin";
		queryAdmin.queryInAdmin(sqlStr);
		queryAdmin.closeConnection();
		
		Scene scene = new Scene(getPane(),700, 450);
		scene.getStylesheets().add("style.css");
		primaryStage.setTitle(""); 
		primaryStage.setScene(scene); 
		primaryStage.show(); 
	}
	
	/*
	 * 学生信息
	 */
	private Node showStu(){
		stuPane = new BorderPane();
		stuPane.setPadding(new Insets(15, 15, 15, 15));
			
		topPane1 = new BorderPane();
		topPane1.setPadding(new Insets(0, 0, 15, 0));
		title1 = new Label("本科生导师制管理系统");
		title1.setFont(Font.font("楷体", FontWeight.NORMAL, FontPosture.REGULAR, 18));
		topPane1.setLeft(title1);
		queryStu = new Button("查询学生信息");
		topPane1.setRight(queryStu);
		
        stuResult = new TableView<Student>();
        stuResult.setId("adminStu");
		TableColumn<Student, String> tColId = new TableColumn<Student, String>("学号");
		tColId.setPrefWidth(140);
		TableColumn<Student, String> tColName = new TableColumn<Student, String>("姓名");
		tColName.setPrefWidth(100);
		TableColumn<Student, String> tColSex = new TableColumn<Student, String>("性别");
		tColSex.setPrefWidth(70);
		TableColumn<Student, String> tColMajor = new TableColumn<Student, String>("专业");
		tColMajor.setPrefWidth(100);
		TableColumn<Student, String> tColClasses = new TableColumn<Student, String>("班级");
		tColClasses.setPrefWidth(100);
		TableColumn<Student, String> tColTele = new TableColumn<Student, String>("电话");
		tColTele.setPrefWidth(158);
	    stuResult.getColumns().addAll(tColId, tColName, tColSex, tColMajor, tColClasses, tColTele);
	    
	    //给stuResult注册鼠标事件
	    stuResult.setOnMouseClicked(new stuMouseClickedListener());
	    
	    stuOperation = new BorderPane();
	    stuBottom1 = new HBox(9);
	    stuBottom1.setAlignment(Pos.CENTER);
	    stuBottom1.setPadding(new Insets(10, 0, 0, 0));
	    
	    stuBottom1.getChildren().add(new Label("学号:"));
		stuId = new TextField();
		stuId.setPrefWidth(65);
		stuBottom1.getChildren().add(stuId);
		stuBottom1.getChildren().add(new Label("姓名:"));
		stuName = new TextField();
		stuName.setPrefWidth(55);
		stuBottom1.getChildren().add(stuName);
		stuBottom1.getChildren().add(new Label("性别:"));
		stuSex = new TextField();
		stuSex.setPrefWidth(35);
		stuBottom1.getChildren().add(stuSex);
		stuBottom1.getChildren().add(new Label("专业:"));
		stuMajor = new TextField();
		stuMajor.setPrefWidth(70);
		stuBottom1.getChildren().add(stuMajor);
		stuBottom1.getChildren().add(new Label("班级:"));
		stuClass = new TextField();
		stuClass.setPrefWidth(70);
		stuBottom1.getChildren().add(stuClass);
		stuBottom1.getChildren().add(new Label("电话:"));
		stuTel = new TextField();
		stuTel.setPrefWidth(100);
		stuBottom1.getChildren().add(stuTel);
		stuOperation.setTop(stuBottom1);
		
		
		stuBottom2 = new HBox(10);
		stuBottom2.setAlignment(Pos.BASELINE_RIGHT);
		stuBottom2.setPadding(new Insets(10, 0, 0, 0));
		stuBottom2.getChildren().add(new Label("您的操作:"));
		addStu = new Button("添加");
		stuBottom2.getChildren().add(addStu);
		updStu = new Button("修改");
		stuBottom2.getChildren().add(updStu);
		delStu = new Button("删除");
		stuBottom2.getChildren().add(delStu);
		stuOperation.setBottom(stuBottom2);
	    
		stuPane.setTop(topPane1);
		stuPane.setCenter(stuResult);
		stuPane.setBottom(stuOperation);
		
		//查询学生信息按钮添加事件
		queryStu.setOnAction(e -> {
			stuId.setText("");
			stuName.setText("");
			stuSex.setText("");
			stuMajor.setText("");
			stuClass.setText("");
		    stuTel.setText("");
			ForAdmin queryStu = new ForAdmin();
			queryStu.connect();
		    stuList.clear();   //刷新数据
			sqlStr = "select * from student";
			queryStu.queryInStu(sqlStr);
		    stuResult.setItems(stuList);
		    tColId.setCellValueFactory(new PropertyValueFactory<Student, String>("id"));
		    tColName.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
		    tColSex.setCellValueFactory(new PropertyValueFactory<Student, String>("sex"));
		    tColMajor.setCellValueFactory(new PropertyValueFactory<Student, String>("major"));
		    tColClasses.setCellValueFactory(new PropertyValueFactory<Student, String>("classes"));
		    tColTele.setCellValueFactory(new PropertyValueFactory<Student, String>("telephone"));
			queryStu.closeConnection();
		});
		
		// 给按钮注册事件
		addStu.setOnAction(new stuAddListener());
		updStu.setOnAction(new stuUpdateListener());
		delStu.setOnAction(new stuDeleteListener());
		
		return stuPane;
	}
	
	/*
	 * 连接数据库
	 */
	public void connect() {
		try {
			Class.forName(driverName);
			con = DriverManager.getConnection(url, user, password);
			System.out.println("数据库连接成功");

		} catch (ClassNotFoundException e) {
			System.out.println("加载驱动出错：" + e.getMessage());
		} catch (SQLException e) {
			System.out.println("连接数据库出错：" + e.getMessage());
		}
	}
	/*
	 * 查询student表
	 */
	public void queryInStu(String sql) {
		// 创建statement对象
		try {
			sta = con.createStatement();
			// 执行查询操作
			rs = sta.executeQuery(sql);
			// 显示结果
			while(rs.next()){
				String id = rs.getString("stu_id");
				String name = rs.getString("name");
				String sex = rs.getString("sex");
				String major = rs.getString("major");
				String classes = rs.getString("class");
				String telephone = rs.getString("telephone");
				String instructor = rs.getString("instructor");
				String state = rs.getString("state");
				stuList.add((new Student(id, name, sex, major, classes, telephone)));
				insInfo[0] = "";
				insInfo[1] = "";
				ForAdmin queryInstructor = new ForAdmin();
				queryInstructor.connect();
				queryInstructor.queryInIns("select * from instructor where " + "name = '" + instructor + "'");
				queryInstructor.closeConnection();
				pairList.add(new StuIns(name, state, instructor, insInfo[0], insInfo[1]));
			}
			
		} catch (SQLException e) {
			System.out.println("查询出错：" + e.getMessage());
		}
	}
	
	/*
	 * 更新数据表
	 */
	public void updateData(String sql){
		// 创建statement对象
		try {
			sta = con.createStatement();
			// 执行更新操作
			sta.executeUpdate(sql);
			
		} catch (SQLException e) {
			System.out.println("更新出错：" + e.getMessage());
		}
	}
	
	/*
	 * 查询instructor表
	 */
	public void queryInIns(String sql) {
		// 创建statement对象
		try {
			sta = con.createStatement();
			// 执行查询操作
			rs = sta.executeQuery(sql);
			// 显示结果
			while(rs.next()){
				String id = rs.getString("ins_id");
				String name = rs.getString("name");
				String sex = rs.getString("sex");
				String title = rs.getString("title");
				String research = rs.getString("research");
				String telephone = rs.getString("telephone");
				String pairNum = rs.getString("pair_num");
				insInfo[0] = rs.getString("sure_num");
				insInfo[1] = rs.getString("state");
				insList.add((new Instructor(id, name, sex, title, research, telephone, pairNum)));
			}
			
		} catch (SQLException e) {
			System.out.println("查询出错：" + e.getMessage());
		}
	}
	
	/*
	 * 查询admin表
	 */
	public void queryInAdmin(String sql) {
		// 创建statement对象
		try {
			sta = con.createStatement();
			// 执行查询操作
			rs = sta.executeQuery(sql);
			// 显示结果
			while(rs.next()){
				String id = rs.getString("admin_id");
				if(id.equals(this.userId)){
					this.userPwd = rs.getString("password");
				}
				adminList.add(id);
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
		
	
	/*
	 * 导师信息
	 */
	private Node showIns(){
		insPane = new BorderPane();
		insPane.setPadding(new Insets(15, 15, 15, 15));
		
		topPane2 = new BorderPane();
		topPane2.setPadding(new Insets(0, 0, 15, 0));
		title2 = new Label("本科生导师制管理系统");
		title2.setFont(Font.font("楷体", FontWeight.NORMAL, FontPosture.REGULAR, 18));
		topPane2.setLeft(title2);
		queryIns = new Button("查询导师信息");
		topPane2.setRight(queryIns);
		
		insResult = new TableView<Instructor>();
		insResult.setId("adminIns");
		TableColumn<Instructor, String> tColId = new TableColumn<Instructor, String>("工号");
		tColId.setPrefWidth(100);
		TableColumn<Instructor, String> tColName = new TableColumn<Instructor, String>("姓名");
		tColName.setPrefWidth(90);
		TableColumn<Instructor, String> tColSex = new TableColumn<Instructor, String>("性别");
		tColSex.setPrefWidth(60);
		TableColumn<Instructor, String> tColTitle = new TableColumn<Instructor, String>("职称");
		tColTitle.setPrefWidth(100);
		TableColumn<Instructor, String> tColResearch = new TableColumn<Instructor, String>("研究方向");
		tColResearch.setPrefWidth(100);
		TableColumn<Instructor, String> tColTel = new TableColumn<Instructor, String>("电话");
		tColTel.setPrefWidth(130);
		TableColumn<Instructor, String> tColNum = new TableColumn<Instructor, String>("带学生数");
		tColNum.setPrefWidth(88);
	    insResult.getColumns().addAll(tColId, tColName, tColSex, tColTitle, tColResearch, tColTel, tColNum);
	    
	    //给insResult注册鼠标事件
	    insResult.setOnMouseClicked(new insMouseClickedListener());
	    
	    insOperation = new BorderPane();
	    insBottom1 = new HBox(4);
	    insBottom1.setAlignment(Pos.CENTER);
	    insBottom1.setPadding(new Insets(10, 0, 0, 0));
	    
	    insBottom1.getChildren().add(new Label("工号:"));
		insId = new TextField();
		insId.setPrefWidth(60);
		insBottom1.getChildren().add(insId);
		insBottom1.getChildren().add(new Label("姓名:"));
		insName = new TextField();
		insName.setPrefWidth(50);
		insBottom1.getChildren().add(insName);
		insBottom1.getChildren().add(new Label("性别:"));
		insSex = new TextField();
		insSex.setPrefWidth(40);
		insBottom1.getChildren().add(insSex);
		insBottom1.getChildren().add(new Label("职称:"));
		insTitle = new TextField();
		insTitle.setPrefWidth(50);
		insBottom1.getChildren().add(insTitle);
		insBottom1.getChildren().add(new Label("研究方向:"));
		insResearch = new TextField();
		insResearch.setPrefWidth(60);
		insBottom1.getChildren().add(insResearch);
		insBottom1.getChildren().add(new Label("电话:"));
		insTel = new TextField();
		insTel.setPrefWidth(100);
		insBottom1.getChildren().add(insTel);
		insBottom1.getChildren().add(new Label("学生数:"));
		insPairNum = new TextField();
		insPairNum.setPrefWidth(30);
		insBottom1.getChildren().add(insPairNum);
		insOperation.setTop(insBottom1);
		
		
		insBottom2 = new HBox(10);
		insBottom2.setAlignment(Pos.BASELINE_RIGHT);
		insBottom2.setPadding(new Insets(10, 0, 0, 0));
		insBottom2.getChildren().add(new Label("您的操作:"));
		addIns = new Button("添加");
		insBottom2.getChildren().add(addIns);
		updIns = new Button("修改");
		insBottom2.getChildren().add(updIns);
		delIns = new Button("删除");
		insBottom2.getChildren().add(delIns);
		insOperation.setBottom(insBottom2);
		
	    
		insPane.setTop(topPane2);
		insPane.setCenter(insResult);
		insPane.setBottom(insOperation);
		
		//查询导师信息按钮添加事件
		queryIns.setOnAction(e -> {
			insId.setText("");
			insName.setText("");
			insSex.setText("");
			insTitle.setText("");
			insResearch.setText("");
			insTel.setText("");
			insPairNum.setText("");
			ForAdmin queryIns = new ForAdmin();
			queryIns.connect();
			insList.clear();   //刷新数据
			sqlStr = "select * from instructor";
			queryIns.queryInIns(sqlStr);
		    insResult.setItems(insList);
		    tColId.setCellValueFactory(new PropertyValueFactory<Instructor, String>("id"));
		    tColName.setCellValueFactory(new PropertyValueFactory<Instructor, String>("name"));
		    tColSex.setCellValueFactory(new PropertyValueFactory<Instructor, String>("sex"));
		    tColTitle.setCellValueFactory(new PropertyValueFactory<Instructor, String>("title"));
		    tColResearch.setCellValueFactory(new PropertyValueFactory<Instructor, String>("research"));
		    tColTel.setCellValueFactory(new PropertyValueFactory<Instructor, String>("telephone"));
		    tColNum.setCellValueFactory(new PropertyValueFactory<Instructor, String>("pairNum"));
			queryIns.closeConnection();
		});
		
		// 给按钮注册事件
		addIns.setOnAction(new insAddListener());
		updIns.setOnAction(new insUpdateListener());
		delIns.setOnAction(new insDeleteListener());
		
		return insPane;
	}
	
	/*
	 * 结对情况
	 */
	private Node showResult(){
		resultPane = new BorderPane();
        resultPane.setPadding(new Insets(15, 15, 15, 15));
		
		topPane = new FlowPane();
		topPane.setPadding(new Insets(0, 5, 8, 5));
		topPane.setHgap(8);
		notice = new Label("学生状态");
		status = new ComboBox<>(); 
		status.setPrefWidth(100);   
		ObservableList<String> items = FXCollections.observableArrayList(chooseType);
		status.setItems(items);
		queryResult = new Button("查询结对情况");
		topPane.getChildren().addAll(notice, status, queryResult);
		
		pairResult = new TableView<StuIns>();
		pairResult.setId("adminPair");
		TableColumn<StuIns, String> stuName = new TableColumn<StuIns, String>("学生");
		stuName.setPrefWidth(140);
		TableColumn<StuIns, String> stuState = new TableColumn<StuIns, String>("学生状态");
		stuState.setPrefWidth(130);
		TableColumn<StuIns, String> insName = new TableColumn<StuIns, String>("导师");
		insName.setPrefWidth(130);
		TableColumn<StuIns, String> insSure = new TableColumn<StuIns, String>("已选该导师人数");
		insSure.setPrefWidth(130);
		TableColumn<StuIns, String> insState = new TableColumn<StuIns, String>("导师状态");
		insState.setPrefWidth(140);
	    pairResult.getColumns().addAll(stuName, stuState, insName, insSure, insState);
		
	    resultPane.setTop(topPane);	
	    resultPane.setLeft(pairResult);
	    
	    /*为选择方式的添加点击事件*/
		status.setOnAction(e->{
			mode = items.indexOf(status.getValue());
			if(mode == 0){
				sqlStr = "select * from student where " + "state = '未选'";
			}
			else if(mode == 1){
				sqlStr = "select * from student where " + "state = '待定'";
			}
			else if(mode == 2){
				sqlStr = "select * from student where " + "state = '选定'";
			}
			System.out.println(sqlStr);
		});
		
		//查询按钮的点击事件
		queryResult.setOnAction(e -> {
			ForAdmin stuIns = new ForAdmin();
			stuIns.connect();
			pairList.clear();   //刷新数据
			stuIns.queryInStu(sqlStr);    //查询student表
		    pairResult.setItems(pairList);
		    stuName.setCellValueFactory(new PropertyValueFactory<StuIns, String>("stuName"));
		    stuState.setCellValueFactory(new PropertyValueFactory<StuIns, String>("stuState"));
		    insName.setCellValueFactory(new PropertyValueFactory<StuIns, String>("insName"));
		    insSure.setCellValueFactory(new PropertyValueFactory<StuIns, String>("insSure"));
		    insState.setCellValueFactory(new PropertyValueFactory<StuIns, String>("insState"));
		    
			stuIns.closeConnection();
		});
		
		return resultPane;
	}
	
	/*
	 * 信息维护
	 */
	private Node adminInfo(){
		adminInfoPane = new BorderPane();
		adminInfoPane.setPadding(new Insets(60,50,0,50));
	    
	    //修改密码
	    changePwd = new GridPane();
	    changePwd.setHgap(5);
	    changePwd.setVgap(8);
	    
	    Label password = new Label("修改密码");
		password.setFont(Font.font("宋体", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 16));
		changePwd.add(password, 0, 0);
		
		//原密码
		changePwd.add(new Label("输入原密码"), 0, 4);
	    oldPwd = new PasswordField();
	    changePwd.add(oldPwd, 1, 4);
	    
	    //新密码
	    changePwd.add(new Label("输入新密码"), 0, 5);
	    newPwd = new PasswordField();
	    changePwd.add(newPwd, 1, 5);
	    
	    //确认新密码
	    changePwd.add(new Label("确认新密码"), 0, 6);
	    surePwd = new PasswordField();
	    changePwd.add(surePwd, 1, 6);
	    
	    //确认按钮
        HBox buttonPane = new HBox();
	    buttonPane.setSpacing(15);
	    buttonPane.setAlignment(Pos.CENTER_RIGHT);
	    change = new Button("修改");
	    change.setPrefSize(60, 25);
	    buttonPane.getChildren().add(change);
	    changePwd.add(buttonPane, 1, 7);
	    
	    changePwd.add(new Label("1.密码长度至少为5位\n2.包含字母、数字两种\n3.密码中字母区分大小写\n4.密码不可与账号相同"), 1, 10);

	    //新增管理员
	    addAdmin = new GridPane();
	    addAdmin.setHgap(5);
	    addAdmin.setVgap(8);
	    
	    Label addadmin = new Label("新增管理员");
	    addadmin.setFont(Font.font("宋体", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 16));
		addAdmin.add(addadmin, 0, 4);
		
		//新管理员账号
		addAdmin.add(new Label("新管理员账号"), 0, 8);
	    adminId = new TextField();
	    addAdmin.add(adminId, 1, 8);
	    
	    //新管理员密码
	    addAdmin.add(new Label("新管理员密码"), 0, 9);
	    adminPwd = new PasswordField();
	    addAdmin.add(adminPwd, 1, 9);
	    
	    
	    //添加按钮
        HBox addPane = new HBox();
	    addPane.setSpacing(15);
	    addPane.setAlignment(Pos.CENTER_RIGHT);
	    add = new Button("添加");
	    add.setPrefSize(60, 25);
	    addPane.getChildren().add(add);
	    addAdmin.add(addPane, 1, 11);
	    
        adminInfoPane.setLeft(changePwd);
        adminInfoPane.setRight(addAdmin);
	    
	    //确认按钮的点击事件
	    change.setOnAction(e -> {
	    	if(oldPwd.getText().equals(this.userPwd)){
	    		if(newPwd.getText().equals(surePwd.getText())){
	    			if(newPwd.getText().matches("^.*[a-zA-Z]+.*$") && newPwd.getText().matches("^.*[0-9]+.*$") && newPwd.getText().matches("^.{5,}$")){
	    				//更新数据库中的密码信息
	    	    		ForStudent changePwd = new ForStudent();
	    	    		changePwd.connect();
	    	    		sqlStr = "update admin set password = '" + newPwd.getText() + "' where admin_id = '" + this.userId + "'";
	    	    		changePwd.updateInStu(sqlStr);
	    	    		changePwd.closeConnection();
			    		oldPwd.setText("");
			    		newPwd.setText("");
			    		surePwd.setText("");
			    		Alert warning = new Alert(Alert.AlertType.INFORMATION);
			    		warning.setHeaderText("密码修改成功！");
			    		warning.showAndWait();
	    			}
	    			else{
	    				newPwd.setText("");
			    		surePwd.setText("");
	    				Alert warning = new Alert(Alert.AlertType.WARNING);
			    		warning.setHeaderText("输入的新密码不符合要求哦！");
			    		warning.showAndWait();
	    			}
	    		}
	    		else {
	    			newPwd.setText("");
		    		surePwd.setText("");
	    			Alert warning = new Alert(Alert.AlertType.WARNING);
		    		warning.setHeaderText("两次输入的新密码不相同！");
		    		warning.showAndWait();
	    		}
	    	}
	    	else{
	    		oldPwd.setText("");
	    		Alert warning = new Alert(Alert.AlertType.WARNING);
	    		warning.setHeaderText("你输入的原密码不正确！");
	    		warning.showAndWait();
	    	}
	    });
	    
	    add.setOnAction(e -> {
	    	if(!adminList.contains(adminId.getText())){
	    		//向admin表中添加数据
	    		ForAdmin insertAdmin = new ForAdmin();
	    		insertAdmin.connect();
	    		sqlStr = "insert into admin (admin_id, password) values('" 
	    		                              + adminId.getText() + "','" 
	    				                      + adminPwd.getText() + "')";
	    		insertAdmin.updateData(sqlStr);
	    		insertAdmin.closeConnection();
	    		Alert warning = new Alert(Alert.AlertType.INFORMATION);
	    		warning.setHeaderText("添加管理员成功！");
	    		warning.showAndWait();
	    		adminId.setText("");
	    		adminPwd.setText("");
	    	}
	    	else {
	    		adminId.setText("");
	    		adminPwd.setText("");
	    		Alert warning = new Alert(Alert.AlertType.WARNING);
	    		warning.setHeaderText("该账号已存在，请重新输入");
	    		warning.showAndWait();
	    	}
	    });
	    
	    
		return adminInfoPane;
	}
	
	public static class Student{
		private String id;
		private String name;
		private String sex;
		private String major;
		private String classes;
		private String telephone;
		
		private Student(String id, String name, String sex, String major, String classes, String telephone){
			this.id = id;
			this.name = name;
			this.sex = sex;
			this.major = major;
			this.classes = classes;
			this.telephone = telephone;
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
		
	}
	
	/*
	 * Student部分事件监听
	 */
	
	//tableview的点击事件
	public class stuMouseClickedListener implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent arg0) {
			// 得到用户选择的记录
			int selectedRow = stuResult.getSelectionModel().getSelectedIndex();
			
			// 如果确实选取了某条记录
			if(selectedRow!=-1){
				// 获取选择的记录
				Student stuObj = stuList.get(selectedRow);
			
				// 把用户选择的记录中的内容分别添加到对应的文本框中
				stuId.setText(stuObj.getId());
				stuName.setText(stuObj.getName());
				stuSex.setText(stuObj.getSex());
				stuMajor.setText(stuObj.getMajor());
				stuClass.setText(stuObj.getClasses());
				stuTel.setText(stuObj.getTelephone());
			
			// if语句结束
			}
		}
	}
	
	//添加按钮监听事件
	public class stuAddListener implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			Student stuObj = new Student(stuId.getText(), stuName.getText(),stuSex.getText(), stuMajor.getText(), stuClass.getText(), stuTel.getText());
			stuList.add(stuObj);
			
			//向student表中添加数据
    		ForAdmin insertStu = new ForAdmin();
    		insertStu.connect();
    		sqlStr = "insert into student (stu_id, name, sex, major, class, telephone, state, instructor, password) values('" 
    		                              + stuId.getText() + "','" 
    				                      + stuName.getText() + "','" 
    		                              + stuSex.getText() + "','" 
    				                      + stuMajor.getText() + "','" 
    		                              + stuClass.getText() + "','" 
    				                      + stuTel.getText() + "', '未选', null, '12345')";
    		insertStu.updateData(sqlStr);
    		insertStu.closeConnection();
		}
	}

	//更新按钮监听事件
	public class stuUpdateListener implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			// 获得被选中行的索引
			int selectedRow = stuResult.getSelectionModel().getSelectedIndex();
			if (selectedRow != -1) {
				Student stuObj = new Student(stuId.getText(), stuName.getText(),stuSex.getText(), stuMajor.getText(), stuClass.getText(), stuTel.getText());
				stuList.set(selectedRow, stuObj);
				
				//对student表中数据进行修改
	    		ForAdmin updateStu = new ForAdmin();
	    		updateStu.connect();
	    		sqlStr = "update student set name = '"  + stuName.getText() + 
	    				                 "', sex = '" + stuSex.getText() + 
	    				                 "', major = '" + stuMajor.getText() + 
	    				                 "', class = '" + stuClass.getText() + 
	    				                 "', telephone = '" + stuTel.getText() + 
	    				                 "' where stu_id = '" + stuId.getText() + "'";
	    		updateStu.updateData(sqlStr);
	    		updateStu.closeConnection();
			}

		}
	}

	//删除按钮监听事件
	public class stuDeleteListener implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			// 获得被选中行的索引
			int selectedRow = stuResult.getSelectionModel().getSelectedIndex();

			// 判断是否存在被选中行
			if (selectedRow != -1) {
				// 从表格模型当中删除指定行
				stuList.remove(selectedRow);
				//将数据从student表中删除
	    		ForAdmin deleteStu = new ForAdmin();
	    		deleteStu.connect();
	    		sqlStr = "delete from student where stu_id = '" + stuId.getText() + "'";
	    		deleteStu.updateData(sqlStr);
	    		deleteStu.closeConnection();
			}
		}
	}
	
	public static class Instructor{
		private String id;
		private String name;
		private String sex;
		private String title;
		private String research;
		private String telephone;
		private String pairNum;
		
		private Instructor(String id, String name, String sex, String title, String research, String telephone, String pairNum){
			this.id = id;
			this.name = name;
			this.sex = sex;
			this.title = title;
			this.research = research;
			this.telephone = telephone;
			this.pairNum = pairNum;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getSex() {
			return sex;
		}

		public void setSex(String sex) {
			this.sex = sex;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getResearch() {
			return research;
		}

		public void setResearch(String research) {
			this.research = research;
		}

		public String getTelephone() {
			return telephone;
		}

		public void setTelephone(String telephone) {
			this.telephone = telephone;
		}

		public String getPairNum() {
			return pairNum;
		}

		public void setPairNum(String pairNum) {
			this.pairNum = pairNum;
		}
	}
	
	/*
	 * Instructor部分事件监听
	 */
	
	//tableview的点击事件
	public class insMouseClickedListener implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent arg0) {
			// 得到用户选择的记录
			int selectedRow = insResult.getSelectionModel().getSelectedIndex();
			
			// 如果确实选取了某条记录
			if(selectedRow!=-1){
				// 获取选择的记录
				Instructor insObj = insList.get(selectedRow);
			
				// 把用户选择的记录中的内容分别添加到对应的文本框中
				insId.setText(insObj.getId());
				insName.setText(insObj.getName());
				insSex.setText(insObj.getSex());
				insTitle.setText(insObj.getTitle());
				insResearch.setText(insObj.getResearch());
				insTel.setText(insObj.getTelephone());
				insPairNum.setText(insObj.getPairNum());
			
			// if语句结束
			}
		}
	}
	
	//添加按钮监听事件
	public class insAddListener implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			Instructor insObj = new Instructor(insId.getText(), insName.getText(),insSex.getText(), insTitle.getText(), insResearch.getText(), insTel.getText(), insPairNum.getText());
			insList.add(insObj);
			
			//向student表中添加数据
    		ForAdmin insertIns = new ForAdmin();
    		insertIns.connect();
    		sqlStr = "insert into instructor (ins_id, name, sex, title, research, telephone, password, pair_num, sure_num, state) values('" 
    		                              + insId.getText() + "','" 
    				                      + insName.getText() + "','" 
    		                              + insSex.getText() + "','" 
    				                      + insTitle.getText() + "','" 
    		                              + insResearch.getText() + "','" 
    				                      + insTel.getText() + "', '12345', '5', '0', '还可选')";
    		insertIns.updateData(sqlStr);
    		insertIns.closeConnection();
		}
	}

	//更新按钮监听事件
	public class insUpdateListener implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			// 获得被选中行的索引
			int selectedRow = insResult.getSelectionModel().getSelectedIndex();
			if (selectedRow != -1) {
				Instructor insObj = new Instructor(insId.getText(), insName.getText(),insSex.getText(), insTitle.getText(), insResearch.getText(), insTel.getText(), insPairNum.getText());
				insList.set(selectedRow, insObj);
				
				//对student表中数据进行修改
	    		ForAdmin updateIns = new ForAdmin();
	    		updateIns.connect();
	    		sqlStr = "update instructor set name = '"  + insName.getText() + 
		    				                 "', sex = '" + insSex.getText() + 
		    				                 "', title = '" + insTitle.getText() + 
		    				                 "', research = '" + insResearch.getText() + 
		    				                 "', telephone = '" + insTel.getText() + 
		    				                 "', pair_num = '" + insPairNum.getText() +
		    				                 "' where ins_id = '" + insId.getText() + "'";
	    		updateIns.updateData(sqlStr);
	    		updateIns.closeConnection();
			}

		}
	}

	//删除按钮监听事件
	public class insDeleteListener implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			// 获得被选中行的索引
			int selectedRow = insResult.getSelectionModel().getSelectedIndex();

			// 判断是否存在被选中行
			if (selectedRow != -1) {
				// 从表格模型当中删除指定行
				insList.remove(selectedRow);
				//将数据从student表中删除
	    		ForAdmin deleteIns = new ForAdmin();
	    		deleteIns.connect();
	    		sqlStr = "delete from instructor where ins_id = '" + insId.getText() + "'";
	    		deleteIns.updateData(sqlStr);
	    		deleteIns.closeConnection();
			}
		}
	}
	
	
	public static class StuIns {
		private String stuName;
		private String stuState;
		private String insName;
		private String insSure;
		private String insState;
		
		public StuIns(String stuName, String stuState, String insName, String insSure, String insState){
			this.stuName = stuName;
			this.stuState = stuState;
			this.insName = insName;
			this.insSure = insSure;
			this.insState = insState;
		}
		public String getStuName() {
			return stuName;
		}
		public void setStuName(String stuName) {
			this.stuName = stuName;
		}
		public String getInsName() {
			return insName;
		}
		public void setInsName(String insName) {
			this.insName = insName;
		}
		public String getStuState() {
			return stuState;
		}
		public void setStuState(String stuState) {
			this.stuState = stuState;
		}
		public String getInsSure() {
			return insSure;
		}
		public void setInsSure(String insSure) {
			this.insSure = insSure;
		}
		public String getInsState() {
			return insState;
		}
		public void setInsState(String insState) {
			this.insState = insState;
		}
		
	}
}
