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
import javafx.scene.control.ListView;
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
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.sql.*;


public class ForStudent extends Application {
	private BorderPane stuPane;
	private String userId;      //通过userId建立关联
	
	private TabPane tabPane;     
	private Tab chooseIns;
	private Tab stuInfo;
	
	//选择导师
	private BorderPane chooseInsPane;
	private FlowPane searchPane;           //条件搜索
	private Button query;
	private ComboBox<String> status;    //导师被选择的状态
	private String[] chooseType = {"全部导师",  "还可选", "已满额" };
	private int mode = -1;  //0表示未选，1表示待定，2表示选定
	private TableView<Instructor> insResult;
	private static ObservableList<Instructor> insList = FXCollections.observableArrayList();
	private VBox forChoose;  
	private Label name;                //当前选中导师的姓名
	private GridPane insIntro;         //导师的详细信息
	private TextField insId;           //导师工号
	private TextField insSex;          //导师性别
	private TextField insTitle;        //导师职称
	private TextField insResearch;     //导师研究方向
	private TextField insTelephone;          //导师联系电话
	private ListView<String> studentList;    //已选的学生列表
	private static ObservableList<String> stuList = FXCollections.observableArrayList();
	private Button choose;            
	private static ObservableList<Ins> ins = FXCollections.observableArrayList();
	private ModalDialog chooseInstructor;
	
	
	//信息维护
	private BorderPane stuInfoPane;
	private HBox topPane;
	private Label stuStatus;
	private GridPane stuInformation;
	private TextField stuId;           //学生学号
	private TextField stuName;         //学生姓名
	private TextField stuSex;          //学生性别
	private TextField stuMajor;        //学生专业
	private TextField stuClasses;      //学生班级
	private TextField stuTelephone;    //学生联系电话
	private TextField stuIns;          //学生的导师
	private GridPane changePwd;
	private PasswordField oldPwd;          //旧密码
	private PasswordField newPwd;          //新密码
	private PasswordField surePwd;         //确认密码
	private Button change;
	
	
	//数据库相关操作
	private Connection con;
	private Statement sta;
	private String driverName = "com.mysql.jdbc.Driver";
	private String url = "jdbc:mysql://127.0.0.1:3306/tutor";   
	private String user = "root";
	private String password = "223366";
	private ResultSet rs;   // 存放查询到的记录集
	private String sqlStr;   //根据选择的方式修改查询语句
	
	//数据库的查询
	static private String userName;
	static private String userSex;
	static private String userMajor;
	static private String userClasses;
	static private String userTelephone;
	static private String userIns;
	static private String userState;
	static private String userPwd;
	
	static private int selectedRow = -1;     //点击“选择”按钮前是否已选中一个导师
	
	protected BorderPane getPane() {
		stuPane = new BorderPane();
		stuPane.setPadding(new Insets(0,0,0,0));
		
		//主体的tab
		tabPane = new TabPane();
		//tabPane.setId("stuTab");
		chooseIns = new Tab("选择导师");
		chooseIns.setContent(chooseIns());
		stuInfo = new Tab("信息维护");
		stuInfo.setContent(stuInfo());
		stuInfo.setOnSelectionChanged(e -> {
			ForStudent queryStudent = new ForStudent();
			queryStudent.connect();
			sqlStr = "select * from student where " + "stu_id = " + this.userId;
			queryStudent.queryInStu(sqlStr);
			queryStudent.closeConnection();
			stuInfo.setContent(stuInfo());
		});
		tabPane.getTabs().addAll(chooseIns, stuInfo);
		
		stuPane.setCenter(tabPane);
		
		return stuPane;
	}
	
	@Override
	public void start(Stage primaryStage) {}
	
	public void start(Stage primaryStage, String userId) {
		
		//初始化信息：根据学号查询该学生信息
		this.userId = userId;
		ForStudent queryStudent = new ForStudent();
		queryStudent.connect();
		sqlStr = "select * from student where " + "stu_id = " + this.userId;
		queryStudent.queryInStu(sqlStr);
		queryStudent.closeConnection();
		
		Scene scene = new Scene(getPane(),600, 430);
		scene.getStylesheets().add("style.css");
		primaryStage.setTitle("本科生导师制管理系统"); 
		primaryStage.setScene(scene); 
		primaryStage.show(); 
		
		//选择导师按钮
	    choose.setOnAction(e -> {
	    	if(selectedRow == -1){
	    		Alert warning = new Alert(Alert.AlertType.WARNING);
	    		warning.setHeaderText("请先选择一个导师！");
	    		warning.showAndWait();
	    	}
	    	else {
	    		if(this.userState.equals("选定")){
		    		Alert warning = new Alert(Alert.AlertType.WARNING);
		    		warning.setHeaderText("不能再选了，你已经有导师了哦！");
		    		warning.showAndWait();
		    	}
		    	else if(this.userState.equals("待定")){
		    		Alert warning = new Alert(Alert.AlertType.WARNING);
		    		warning.setHeaderText("不能再选了，耐心等等哦！");
		    		warning.showAndWait();
		    	}
		    	else{
		    		chooseInstructor = new ModalDialog(primaryStage, "确定选择该导师吗？", this.userId);
		    	}
	    	}
	    	
	    });
	    
		
	}
	
	public static void main(String[] args) {
		launch(args);
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
	 * 查询具体的学生信息
	 */
	public void queryInStu(String sql) {
		// 创建statement对象
		try {
			sta = con.createStatement();
			// 执行查询操作
			rs = sta.executeQuery(sql);
			// 显示结果
			while(rs.next()){
				this.userName = rs.getString("name");
				this.userSex = rs.getString("sex");
				this.userMajor = rs.getString("major");
				this.userClasses = rs.getString("class");
				this.userTelephone = rs.getString("telephone");
				this.userIns = rs.getString("instructor");  
				this.userState = rs.getString("state");
				this.userPwd = rs.getString("password");
			}
			
		} catch (SQLException e) {
			System.out.println("查询出错：" + e.getMessage());
		}
	}
	/*
	 * 查询student表
	 */
	public void queryStu(String sql) {
		// 创建statement对象
		try {
			sta = con.createStatement();
			// 执行查询操作
			rs = sta.executeQuery(sql);
			stuList.clear();
			// 显示结果
			while(rs.next()){
				if((rs.getString("state")).equals("选定")){
					stuList.add(rs.getString("name"));
					System.out.println(stuList.size());
				}
			}
			
		} catch (SQLException e) {
			System.out.println("查询出错：" + e.getMessage());
		}
	}
	/*
	 * 更新student表中的密码信息
	 */
	public void updateInStu(String sql){
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
				String name = rs.getString("name");
				int count = Integer.parseInt(rs.getString("sure_num"));
				int choose = Integer.parseInt(rs.getString("pair_num")) - count;
				insList.add((new Instructor(name, count, choose)));
			}
			
		} catch (SQLException e) {
			System.out.println("查询出错：" + e.getMessage());
		}
	}
	
	/*
	 * 查询具体的导师信息
	 */
	public void queryIns(String sql) {
		// 创建statement对象
		try {
			sta = con.createStatement();
			// 执行查询操作
			rs = sta.executeQuery(sql);
			ins.clear();
			// 显示结果
			while(rs.next()){
				String id = rs.getString("ins_id");
				String sex = rs.getString("sex");
				String title = rs.getString("title");
				String research = rs.getString("research");
				String telephone = rs.getString("telephone");
				ins.add((new Ins(id, sex, title, research, telephone)));
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
	 * 选择导师
	 */
	private Node chooseIns() {
		
		chooseInsPane = new BorderPane();
		chooseInsPane.setPadding(new Insets(15, 35, 15, 15));
		
		//条件搜索
		searchPane = new FlowPane();
		searchPane.setPadding(new Insets(0, 5, 8, 5));
		searchPane.setHgap(8);
		status = new ComboBox<>(); 
		status.setPrefWidth(100); 
		ObservableList<String> items = FXCollections.observableArrayList(chooseType);
		status.setItems(items);
		HBox queryHBox = new HBox();
		queryHBox.setPadding(new Insets(0, 0, 0, 90));
		query = new Button("查询");
		queryHBox.getChildren().add(query);
		searchPane.getChildren().addAll(new Label("导师状态"), status, queryHBox);
	    
		//搜索结果
	    insResult = new TableView<Instructor>();
	    insResult.setId("stuInsResult");
		TableColumn<Instructor, String> tColName = new TableColumn<Instructor, String>("教师姓名");
		tColName.setPrefWidth(100);
		TableColumn<Instructor, String> tColCount = new TableColumn<Instructor, String>("已定人数");
		tColCount.setPrefWidth(100);
		TableColumn<Instructor, String> tColChoose = new TableColumn<Instructor, String>("可选人数");
		tColChoose.setPrefWidth(100);
		insResult.getColumns().addAll(tColName, tColCount, tColChoose);
		
		//给insResult注册鼠标事件
	    insResult.setOnMouseClicked(new insMouseClickedListener());
		
		forChoose = new VBox();
		HBox namePane = new HBox();
		name = new Label();
		name.setText("");
		name.setFont(Font.font("宋体", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 20));
		Label instructor = new Label("老师");
		instructor.setPadding(new Insets(8, 0, 0, 8));
		namePane.getChildren().addAll(name, instructor);
		
		//导师详细信息的展示
		insIntro = new GridPane();
		insIntro.setHgap(8);
		insIntro.setVgap(8);
		
		//导师工号
		insIntro.add(new Label("工号"), 0, 2);
	    insId = new TextField();
	    insId.setEditable(false);
	    insIntro.add(insId, 1, 2);
	    
	    //导师性别
	    insIntro.add(new Label("性别"), 0, 3);
	    insSex = new TextField();
	    insSex.setEditable(false);
	    insIntro.add(insSex, 1, 3);
	    
	    //导师职称
	    insIntro.add(new Label("职称"), 0, 4);
	    insTitle = new TextField();
	    insTitle.setEditable(false);
	    insIntro.add(insTitle, 1, 4);
	    
	    //导师研究方向
	    insIntro.add(new Label("研究方向"), 0, 5);
	    insResearch = new TextField();
	    insResearch.setEditable(false);
	    insIntro.add(insResearch, 1, 5);
	    
	    //导师联系电话
	    insIntro.add(new Label("联系电话"), 0, 6);
	    insTelephone = new TextField();
	    insTelephone.setEditable(false);
	    insIntro.add(insTelephone, 1, 6);
	    
	    //具体哪些同学选了该位导师
	    insIntro.add(new Label("已选学生"), 0, 7);
	    studentList = new ListView<String>(stuList);
		studentList.setPrefWidth(100);
		studentList.setPrefHeight(120);
		insIntro.add(studentList, 1, 7);
	    
	    HBox buttonPane = new HBox();
	    buttonPane.setSpacing(15);
	    buttonPane.setAlignment(Pos.CENTER_RIGHT);
	    choose = new Button("选择");
	    choose.setPrefSize(70, 25);
	    buttonPane.getChildren().add(choose);
	    insIntro.add(buttonPane, 1, 9);
	    
	    forChoose.getChildren().addAll(namePane, insIntro);
	    
	    chooseInsPane.setTop(searchPane);
	    chooseInsPane.setLeft(insResult);
	    chooseInsPane.setRight(forChoose);
	    
	    /*为选择方式的添加点击事件*/
		status.setOnAction(e->{
			mode = items.indexOf(status.getValue());
			if(mode == 0){
				sqlStr = "select * from instructor";
			}
			else if(mode == 1){
				sqlStr = "select * from instructor where " + "state = '还可选'";
			}
			else if(mode == 2){
				sqlStr = "select * from instructor where " + "state = '已满额'";
			}
			System.out.println(sqlStr);
		});
	    
	    //查询导师按钮
	    query.setOnAction(e -> {
	    	ForStudent queryIns = new ForStudent();
			queryIns.connect();
			insList.clear();   //刷新数据
			queryIns.queryInIns(sqlStr);
		    insResult.setItems(insList);
		    tColName.setCellValueFactory(new PropertyValueFactory<Instructor, String>("name"));
		    tColCount.setCellValueFactory(new PropertyValueFactory<Instructor, String>("count"));
		    tColChoose.setCellValueFactory(new PropertyValueFactory<Instructor, String>("choose"));
			queryIns.closeConnection();
	    });
	   
	    
		return chooseInsPane;
	}
	
	
	/*
	 * 个人信息
	 */
	private Node stuInfo() {
		stuInfoPane = new BorderPane();
		stuInfoPane.setPadding(new Insets(20,30,0,30));
		
		stuInformation = new GridPane();
		stuInformation.setHgap(2);
		stuInformation.setVgap(8);
		
		topPane = new HBox();
		topPane.setPadding(new Insets(0, 0, 25, 0));
		Label title = new Label("当前的状态为：");
		title.setFont(Font.font("宋体", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 13));
	
		
		stuStatus = new Label(this.userState);
		stuStatus.setFont(Font.font("宋体", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 18));
		stuStatus.setPadding(new Insets(-5,0,0,0));
		topPane.getChildren().addAll(title, stuStatus);
		
		Label message = new Label("个人信息");
		message.setFont(Font.font("宋体", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 16));
		stuInformation.add(message, 0, 0);
		
		//学号信息
		stuInformation.add(new Label("学号"), 0, 2);
	    stuId = new TextField();
	    stuId.setText(this.userId);
	    stuId.setEditable(false);
	    stuInformation.add(stuId, 1, 2);
	    
	    //姓名信息
	    stuInformation.add(new Label("姓名"), 0, 3);
	    stuName = new TextField();
	    stuName.setText(this.userName);
	    stuName.setEditable(false);
	    stuInformation.add(stuName, 1, 3);
	    
	    //性别
	    stuInformation.add(new Label("性别"), 0, 4);
	    stuSex = new TextField();
	    stuSex.setText(this.userSex);
	    stuSex.setEditable(false);
	    stuInformation.add(stuSex, 1, 4);
	    
	    //学生专业
	    stuInformation.add(new Label("专业"), 0, 5);
	    stuMajor = new TextField();
	    stuMajor.setText(this.userMajor);
	    stuMajor.setEditable(false);
	    stuInformation.add(stuMajor, 1, 5);
	    
	    //学生班级
	    stuInformation.add(new Label("班级"), 0, 6);
	    stuClasses = new TextField();
	    stuClasses.setText(this.userClasses);
	    stuClasses.setEditable(false);
	    stuInformation.add(stuClasses, 1, 6);   
	    
	    //学生联系电话
	    stuInformation.add(new Label("手机号码"), 0, 7);
	    stuTelephone = new TextField();
	    stuTelephone.setText(this.userTelephone);
	    stuTelephone.setEditable(false);
	    stuInformation.add(stuTelephone, 1, 7);
	    
	    //学生导师
	    stuInformation.add(new Label("导师"), 0, 8);
	    stuIns = new TextField();
	    if(this.userState.equals("选定")){
	    	stuIns.setText(this.userIns);
	    }
	    stuIns.setEditable(false);
	    stuInformation.add(stuIns, 1, 8);
	    
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
		
	    stuInfoPane.setTop(topPane);
	    stuInfoPane.setLeft(stuInformation);
	    stuInfoPane.setRight(changePwd);
	    
	    //确认按钮的点击事件
	    change.setOnAction(e -> {
	    	if(oldPwd.getText().equals(this.userPwd)){
	    		if(newPwd.getText().equals(surePwd.getText())){
	    			if(newPwd.getText().matches("^.*[a-zA-Z]+.*$") && newPwd.getText().matches("^.*[0-9]+.*$") && newPwd.getText().matches("^.{5,}$")){
	    				//更新数据库中的密码信息
			    		ForStudent changePwd = new ForStudent();
			    		changePwd.connect();
			    		sqlStr = "update student set password = " + newPwd.getText() + " where stu_id = " + this.userId;
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
	    
		return stuInfoPane;
	}
	
	
	//用于tableView显示的Instructor类
	public static class Instructor{
		private String name;       //导师姓名
		private int count;         //当前已有多少学生选择该导师
		private int choose;      //当前还可以选多少个学生
		
		private Instructor(String name, int count, int choose){
			this.name = name;
			this.count = count;
			this.choose = choose;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public int getChoose() {
			return choose;
		}

		public void setChoose(int choose) {
			this.choose = choose;
		}
		
	}
	
	public static class Ins{
		private String id;       //导师姓名
		private String sex;        
		private String title;
		private String research;
		private String telephone;
		
		private Ins(String id, String sex, String title, String research, String telephone){

			this.id = id;
			this.sex = sex;
			this.title = title;
			this.research = research;
			this.telephone = telephone;
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
		
	}
	
	//tableview的点击事件
	public class insMouseClickedListener implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent arg0) {
			// 得到用户选择的记录
			selectedRow = insResult.getSelectionModel().getSelectedIndex();
			
			// 如果确实选取了某条记录
			if(selectedRow!=-1){
				// 获取选择的记录
				Instructor insObj = insList.get(selectedRow);
				ForStudent queryIns = new ForStudent();
				queryIns.connect();
				//stuList.clear();   //刷新数据
				sqlStr = "select * from instructor where name = '" + insObj.getName() + "'";			
				queryIns.queryIns(sqlStr);
				Ins iObj = ins.get(0);
				name.setText(insObj.getName());
				insId.setText(iObj.getId());
				insSex.setText(iObj.getSex());
				insTitle.setText(iObj.getTitle());
				insResearch.setText(iObj.getResearch());
				insTelephone.setText(iObj.getTelephone());
				queryIns.closeConnection();
				
				ForStudent queryStu = new ForStudent();
				queryStu.connect();
				//stuList.clear();   //刷新数据
				sqlStr = "select * from student where instructor = '" + insObj.getName() + "'";			
				queryStu.queryStu(sqlStr);
				System.out.println(stuList.size());
				studentList.setItems(stuList);
				queryStu.closeConnection();
			
			// if语句结束
			}
		}
	}
	
	/*
	 * 选择导师时弹出的模态窗口
	 */
	public class ModalDialog{
		
		private Label label; 
		private Button okBtn, cancelBtn;

		public ModalDialog(Stage stg, String contents, String userId) {
			label = new Label(contents);
			label.setFont(Font.font(15));
			label.setPadding(new Insets(30,0,0,0));
			
			HBox btnPane = new HBox(30);
			btnPane.setAlignment(Pos.CENTER);
			cancelBtn = new Button("取消");
			cancelBtn.setFont(Font.font(13));
			okBtn = new Button("确定");
			okBtn.setFont(Font.font(13));
			btnPane.getChildren().addAll(cancelBtn, okBtn);

			VBox mainPane = new VBox();
			mainPane.setAlignment(Pos.CENTER);
			VBox.setMargin(btnPane, new Insets(20,0,20,0));		
			mainPane.getChildren().addAll(label, btnPane);
			
			Stage stage = new Stage(); 
			stage.initModality(Modality.APPLICATION_MODAL); // 设定为模态窗体
			stage.initOwner(stg); // 设置主窗体
			
			Scene scene = new Scene(mainPane, 220, 100);
			stage.setScene(scene);
			stage.show();
			
			// 注册点击“确定”按钮事件
			okBtn.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					
					//更新student表中的状态信息
		    		ForStudent updateStu = new ForStudent();
		    		updateStu.connect();
		    		sqlStr = "update student set instructor = '" + name.getText() 
		    		                         + "', state = '待定'"
		    		                         + " where stu_id = " + userId;
		    		updateStu.updateInStu(sqlStr);
		    		updateStu.closeConnection();
		    		stage.hide();  // 关闭对话框
				}
			});
			
			// 注册点击“取消”按钮事件
			cancelBtn.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent arg0) {
					stage.hide();  // 关闭对话框
				}
			});
		}
	}
}
