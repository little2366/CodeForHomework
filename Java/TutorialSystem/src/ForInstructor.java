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


public class ForInstructor extends Application {
	private BorderPane insPane;
	private String userId;     //当前使用该系统的用户
	
	private TabPane tabPane;     
	private Tab chooseStu;    
	private Tab insInfo;

	//选择学生
	private BorderPane chooseStuPane;
	private FlowPane topPane;
	private Label notice;
	private ComboBox<String> status;
	private String[] chooseType = {"全部学生",  "待确认", "已选定" };
	private int mode = -1;  //0表示未选，1表示待定，2表示选定
	private Button query;
	private TableView<Student> stuResult;
	private static ObservableList<Student> stuList = FXCollections.observableArrayList();
	private HBox bottomPane;
	private TextField stuName;
	private Button sure;
	private Button out;            //淘汰
	private ModalDialog chooseStudent;
	 
	
	//信息维护
	private BorderPane insInfoPane;
	private GridPane insInformation;
	private TextField insId;           //教师工号
	private TextField insName;         //教师姓名
	private TextField insSex;          //教师性别
	private TextField insTitle;        //教师职称
	private TextField insResearch;    //教师研究方向
	private TextField insTelephone;    //教师联系电话
	private TextField insPairNum;      //教师可带学生数
	private GridPane changePwd;
	private TextField oldPwd;          //旧密码
	private TextField newPwd;          //新密码
	private TextField surePwd;         //确认密码
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
	static private String userTitle;
	static private String userResearch;
	static private String userTelephone;
	static private String userPwd;
	static private String userPair;
	static private String userSure;
	
	static private int selectedRow;   //当前选中的是哪个学生
	
	
	protected BorderPane getPane() {
		
		insPane = new BorderPane();
		insPane.setPadding(new Insets(0, 0, 0, 0));
		
		//主体的tab
		tabPane = new TabPane();
		//tabPane.setId("stuTab");
		chooseStu = new Tab("选择学生");
		chooseStu.setContent(chooseStu());
		insInfo = new Tab("信息维护");
		insInfo.setContent(insInfo(this.userId));
		tabPane.getTabs().addAll(chooseStu, insInfo);
		
		insPane.setCenter(tabPane);
	
		return insPane;
	}
	
	@Override
	public void start(Stage primaryStage) { 
		
	}
	
	public void start(Stage primaryStage, String userId) {
		this.userId = userId;
		ForInstructor queryInstructor = new ForInstructor();
		queryInstructor.connect();
		sqlStr = "select * from instructor where " + "ins_id = " + this.userId;
		queryInstructor.queryInIns(sqlStr);
		queryInstructor.closeConnection();
		
		Scene scene = new Scene(getPane(),620, 400);
		scene.getStylesheets().add("style.css");
		primaryStage.setTitle(""); 
		primaryStage.setScene(scene); 
		primaryStage.show(); 
		
		//淘汰按钮事件
		out.setOnAction(e -> {
			String str = "确定淘汰学生" + stuName.getText() + "吗？";
			chooseStudent = new ModalDialog(primaryStage, str);			
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
				this.userName = rs.getString("name");
				this.userSex = rs.getString("sex");
				this.userTitle = rs.getString("title");
				this.userResearch = rs.getString("research");
				this.userTelephone = rs.getString("telephone");
				this.userPair = rs.getString("pair_num");
				this.userPwd = rs.getString("password");
				this.userSure = rs.getString("sure_num");
			}
			
		} catch (SQLException e) {
			System.out.println("查询出错：" + e.getMessage());
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
				String state = rs.getString("state");
				stuList.add((new Student(id, name, sex, major, classes, telephone, state)));
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
	 * 选择学生
	 */
	private Node chooseStu() {
		
		chooseStuPane = new BorderPane();
		chooseStuPane.setPadding(new Insets(16, 10, 10, 10));
		
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
		
		stuResult = new TableView<Student>();
		stuResult.setId("insStuResult");
		
		TableColumn<Student, String> tColId = new TableColumn<Student, String>("学号");
		tColId.setPrefWidth(100);
		TableColumn<Student, String> tColName = new TableColumn<Student, String>("姓名");
		tColName.setPrefWidth(70);
		TableColumn<Student, String> tColSex = new TableColumn<Student, String>("性别");
		tColSex.setPrefWidth(57);
		TableColumn<Student, String> tColMajor = new TableColumn<Student, String>("专业");
		tColMajor.setPrefWidth(90);
		TableColumn<Student, String> tColClasses = new TableColumn<Student, String>("班级");
		tColClasses.setPrefWidth(90);
		TableColumn<Student, String> tColTele = new TableColumn<Student, String>("电话");
		tColTele.setPrefWidth(120);
		TableColumn<Student, String> tColState = new TableColumn<Student, String>("状态");
		tColState.setPrefWidth(70);
	    stuResult.getColumns().addAll(tColId, tColName, tColSex, tColMajor, tColClasses, tColTele, tColState);
	    
	    //给stuResult注册鼠标事件
	    stuResult.setOnMouseClicked(new stuMouseClickedListener());
	    
		bottomPane = new HBox(8);
		bottomPane.setPadding(new Insets(15, 0, 0, 0));
		bottomPane.setSpacing(10);
		stuName = new TextField();
		stuName.setPrefWidth(100);
		stuName.setEditable(false);
		sure = new Button("选定");
		out = new Button("淘汰");
		bottomPane.getChildren().addAll(new Label("您当前选择的学生是："), stuName, sure, out);
		
		
		chooseStuPane.setTop(topPane);
		chooseStuPane.setCenter(stuResult);
		
		//一开始先不add，只有选中的那个学生状态是待定的时候才add
		//chooseStuPane.setBottom(bottomPane);
		
		/*为选择方式的添加点击事件*/
		status.setOnAction(e->{
			mode = items.indexOf(status.getValue());
			if(mode == 0){
				sqlStr = "select * from student where instructor = '" + this.userName + "'";
			}
			else if(mode == 1){
				sqlStr = "select * from student where instructor = '" + this.userName + "' and state = '待定'";
			}
			else if(mode == 2){
				sqlStr = "select * from student where instructor = '" + this.userName + "' and state = '选定'";
			}
			System.out.println(sqlStr);
		});
		
		
		//查询按钮事件
		query.setOnAction(e -> {
			ForInstructor queryStu = new ForInstructor();
			queryStu.connect();
			stuList.clear();   //刷新数据
			queryStu.queryInStu(sqlStr);
		    stuResult.setItems(stuList);
		    tColId.setCellValueFactory(new PropertyValueFactory<Student, String>("id"));
		    tColName.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
		    tColSex.setCellValueFactory(new PropertyValueFactory<Student, String>("sex"));
		    tColMajor.setCellValueFactory(new PropertyValueFactory<Student, String>("major"));
		    tColClasses.setCellValueFactory(new PropertyValueFactory<Student, String>("classes"));
		    tColTele.setCellValueFactory(new PropertyValueFactory<Student, String>("telephone"));
		    tColState.setCellValueFactory(new PropertyValueFactory<Student, String>("state"));
			queryStu.closeConnection();
		
			stuName.setText("");
			chooseStuPane.getChildren().remove(bottomPane);
		});
		
		//选定按钮事件
		sure.setOnAction(e -> {
		
			// 更新tableView中的数据
			if(selectedRow!=-1){
				Student chooseObj = stuList.get(selectedRow);
				Student stuObj = new Student(chooseObj.getId(), chooseObj.getName(),chooseObj.getSex(), chooseObj.getMajor(), chooseObj.getClasses(), chooseObj.getTelephone(), "选定");
				stuList.set(selectedRow, stuObj);
			}
			
			//更新student表中的数据
    		ForInstructor sureStu = new ForInstructor();
    		sureStu.connect();
    		sqlStr = "update student set state = '选定'" + " where name = '" + stuName.getText() + "'";
    		sureStu.updateData(sqlStr);
    		sureStu.closeConnection();
    		
    		//更新instructor表中的数据
    		ForInstructor sureIns = new ForInstructor();
    		sureIns.connect();
    		int sureNum = Integer.parseInt(this.userSure) + 1;
    		//说明该导师所带的学生数已满额
    		if(sureNum == Integer.parseInt(this.userPair)){
    			sqlStr = "update instructor set sure_num = '"  + sureNum + "', state = '已满额' " + "where ins_id = '" + this.userId + "'";
    		}
    		else{
    			sqlStr = "update instructor set sure_num = '"  + sureNum + "' where ins_id = " + this.userId;
    		}
    		sureIns.updateData(sqlStr);
    		sureIns.closeConnection();
    		
    		//弹出提示消息
    		Alert warning = new Alert(Alert.AlertType.INFORMATION);
    		String str = "您已成功选择学生" + stuName.getText() + "！";
    		warning.setHeaderText(str);
    		warning.showAndWait();
		});
		
		return chooseStuPane;
	}
	
	
	
	/*
	 * 信息维护
	 */
	private Node insInfo(String name){
		insInfoPane = new BorderPane();
		insInfoPane.setPadding(new Insets(40,30,0,30));
		
		insInformation = new GridPane();
		insInformation.setHgap(2);
		insInformation.setVgap(8);
		
		Label message = new Label("个人信息");
		message.setFont(Font.font("宋体", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 16));
		insInformation.add(message, 0, 0);
		
		//工号信息
		insInformation.add(new Label("工号"), 0, 2);
	    insId = new TextField();
	    insId.setText(this.userId);
	    insId.setEditable(false);
	    insInformation.add(insId, 1, 2);
	    
	    //姓名信息
	    insInformation.add(new Label("姓名"), 0, 3);
	    insName = new TextField();
	    insName.setText(this.userName);
	    insName.setEditable(false);
	    insInformation.add(insName, 1, 3);
	    
	    //教师性别
	    insInformation.add(new Label("性别"), 0, 4);
	    insSex = new TextField();
	    insSex.setText(this.userSex);
	    insSex.setEditable(false);
	    insInformation.add(insSex, 1, 4);
	    
	    //教师职称
	    insInformation.add(new Label("职称"), 0, 5);
	    insTitle = new TextField();
	    insTitle.setText(this.userTitle);
	    insTitle.setEditable(false);
	    insInformation.add(insTitle, 1, 5);
	    
	    //教师研究方向
	    insInformation.add(new Label("研究方向"), 0, 6);
	    insResearch = new TextField();
	    insResearch.setText(this.userResearch);
	    insResearch.setEditable(false);
	    insInformation.add(insResearch, 1, 6);   
	    
	    //教师联系电话
	    insInformation.add(new Label("手机号码"), 0, 7);
	    insTelephone = new TextField();
	    insTelephone.setText(this.userTelephone);
	    insTelephone.setEditable(false);
	    insInformation.add(insTelephone, 1, 7);
	    
	    //教师可带的学生数
	    insInformation.add(new Label("带学生数"), 0, 8);
	    insPairNum = new TextField();
	    insPairNum.setText(this.userPair);
	    insPairNum.setEditable(false);
	    insInformation.add(insPairNum, 1, 8);
	    
	    //修改密码
	    changePwd = new GridPane();
	    changePwd.setHgap(5);
	    changePwd.setVgap(8);
	    
	    Label password = new Label("修改密码");
		password.setFont(Font.font("宋体", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 16));
		changePwd.add(password, 0, 0);
		
		//原密码
		changePwd.add(new Label("输入原密码"), 0, 4);
	    oldPwd = new TextField();
	    changePwd.add(oldPwd, 1, 4);
	    
	    //新密码
	    changePwd.add(new Label("输入新密码"), 0, 5);
	    newPwd = new TextField();
	    changePwd.add(newPwd, 1, 5);
	    
	    //确认新密码
	    changePwd.add(new Label("确认新密码"), 0, 6);
	    surePwd = new TextField();
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

        insInfoPane.setLeft(insInformation);	
        insInfoPane.setRight(changePwd);  
	    
	    //确认按钮的点击事件
	    change.setOnAction(e -> {
	    	if(oldPwd.getText().equals(this.userPwd)){
	    		if(newPwd.getText().equals(surePwd.getText())){
	    			if(newPwd.getText().matches("^.*[a-zA-Z]+.*$") && newPwd.getText().matches("^.*[0-9]+.*$") && newPwd.getText().matches("^.{5,}$")){
	    				//更新数据库中的密码信息
	    	    		ForStudent changePwd = new ForStudent();
	    	    		changePwd.connect();
	    	    		sqlStr = "update instructor set password = " + newPwd.getText() + " where ins_id = " + this.userId;
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
	    
		return insInfoPane;
	}
	
	public static class Student{
		private String id;
		private String name;
		private String sex;
		private String major;
		private String classes;
		private String telephone;
		private String state;
		
		private Student(String id, String name, String sex, String major, String classes, String telephone, String state){
			this.id = id;
			this.name = name;
			this.sex = sex;
			this.major = major;
			this.classes = classes;
			this.telephone = telephone;
			this.state = state;
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
		
	}
	
	/*
	 * Student部分事件监听
	 */
	
	//tableview的点击事件
	public class stuMouseClickedListener implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent arg0) {
			// 得到用户选择的记录
			selectedRow = stuResult.getSelectionModel().getSelectedIndex();
			
			// 如果确实选取了某条记录
			if(selectedRow!=-1){
				// 获取选择的记录
				Student stuObj = stuList.get(selectedRow);
			
				// 把用户选择的记录中的内容分别添加到对应的文本框中
				stuName.setText(stuObj.getName());
				if(stuObj.getState().equals("待定")){
					chooseStuPane.setBottom(bottomPane);
				}
				else if(stuObj.getState().equals("选定")){
					stuName.setText("");
					chooseStuPane.getChildren().remove(bottomPane);
				}
			
			// if语句结束
			}
		}
	}
	
	/*
	 * 淘汰学生时弹出的模态窗口
	 */
	public class ModalDialog{
		
		private Label label; 
		private Button okBtn, cancelBtn;

		public ModalDialog(Stage stg, String contents) {
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
			
			Scene scene = new Scene(mainPane, 250, 100);
			stage.setScene(scene);
			stage.show();
			
			// 注册点击“确定”按钮事件
			okBtn.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					
					if(selectedRow!=-1){
						//从表格模型中删除
						stuList.remove(selectedRow);
						
						//更改student数据表中的信息
						ForInstructor outIns = new ForInstructor();
			    		outIns.connect();
			    		sqlStr = "update student set state = '未选'" 
			    				                 + ", instructor = null"
			    		                         + " where name = '" + stuName.getText() + "'";
			    		outIns.updateData(sqlStr);
			    		outIns.closeConnection();
			    		stage.hide();  // 关闭对话框
					}
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
