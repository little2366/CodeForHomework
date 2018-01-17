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
	static private String userId;     //��ǰʹ�ø�ϵͳ���û�
	
	private TabPane tabPane;     
	private Tab stu;
	private Tab ins;
	private Tab result;
	private Tab admin;
	
	
	//�༭ѧ����Ϣ
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
	
	
	//�༭��ʦ��Ϣ
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
	
	
	//�鿴������
	private BorderPane resultPane;
	private FlowPane topPane;
	private Label notice;
	private ComboBox<String> status;
	private String[] chooseType = { "δѡ", "����", "ѡ��" };
	private int mode = -1;  //0��ʾδѡ��1��ʾ������2��ʾѡ��
	private TableView<StuIns> pairResult;
	private static ObservableList<StuIns> pairList = FXCollections.observableArrayList();
	private Button queryResult;
	
	//��Ϣά��
	private BorderPane adminInfoPane;
	private GridPane changePwd;
	private PasswordField oldPwd;          //������
	private PasswordField newPwd;          //������
	private PasswordField surePwd;         //ȷ������
	private Button change;
	private GridPane addAdmin;
	private TextField adminId;          //�¹���Ա�˺�
	private PasswordField adminPwd;         //�¹���Ա����
	private Button add;
	
	
	
	//���ݿ���ز���
	private Connection con;
	private Statement sta;
	private String driverName = "com.mysql.jdbc.Driver";
	private String url = "jdbc:mysql://127.0.0.1:3306/tutor";   
	private String user = "root";
	private String password = "223366";
	private ResultSet rs;   // ��Ų�ѯ���ļ�¼��
	private String sqlStr;   //����ѡ��ķ�ʽ�޸Ĳ�ѯ���
	
	static private String userPwd;
	static private String[] insInfo = new String[2];
	private static ArrayList adminList = new ArrayList(); 
	
	protected BorderPane getPane() {
		
		adminPane = new BorderPane();
		adminPane.setPadding(new Insets(0,0,0,0));
		
		//�����tab
		tabPane = new TabPane();
		stu = new Tab("ѧ����Ϣ");
		stu.setContent(showStu());
		ins = new Tab("��ʦ��Ϣ");
		ins.setContent(showIns());
		result = new Tab("������");
		result.setContent(showResult());
		admin = new Tab("ϵͳά��");
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
	 * ѧ����Ϣ
	 */
	private Node showStu(){
		stuPane = new BorderPane();
		stuPane.setPadding(new Insets(15, 15, 15, 15));
			
		topPane1 = new BorderPane();
		topPane1.setPadding(new Insets(0, 0, 15, 0));
		title1 = new Label("��������ʦ�ƹ���ϵͳ");
		title1.setFont(Font.font("����", FontWeight.NORMAL, FontPosture.REGULAR, 18));
		topPane1.setLeft(title1);
		queryStu = new Button("��ѯѧ����Ϣ");
		topPane1.setRight(queryStu);
		
        stuResult = new TableView<Student>();
        stuResult.setId("adminStu");
		TableColumn<Student, String> tColId = new TableColumn<Student, String>("ѧ��");
		tColId.setPrefWidth(140);
		TableColumn<Student, String> tColName = new TableColumn<Student, String>("����");
		tColName.setPrefWidth(100);
		TableColumn<Student, String> tColSex = new TableColumn<Student, String>("�Ա�");
		tColSex.setPrefWidth(70);
		TableColumn<Student, String> tColMajor = new TableColumn<Student, String>("רҵ");
		tColMajor.setPrefWidth(100);
		TableColumn<Student, String> tColClasses = new TableColumn<Student, String>("�༶");
		tColClasses.setPrefWidth(100);
		TableColumn<Student, String> tColTele = new TableColumn<Student, String>("�绰");
		tColTele.setPrefWidth(158);
	    stuResult.getColumns().addAll(tColId, tColName, tColSex, tColMajor, tColClasses, tColTele);
	    
	    //��stuResultע������¼�
	    stuResult.setOnMouseClicked(new stuMouseClickedListener());
	    
	    stuOperation = new BorderPane();
	    stuBottom1 = new HBox(9);
	    stuBottom1.setAlignment(Pos.CENTER);
	    stuBottom1.setPadding(new Insets(10, 0, 0, 0));
	    
	    stuBottom1.getChildren().add(new Label("ѧ��:"));
		stuId = new TextField();
		stuId.setPrefWidth(65);
		stuBottom1.getChildren().add(stuId);
		stuBottom1.getChildren().add(new Label("����:"));
		stuName = new TextField();
		stuName.setPrefWidth(55);
		stuBottom1.getChildren().add(stuName);
		stuBottom1.getChildren().add(new Label("�Ա�:"));
		stuSex = new TextField();
		stuSex.setPrefWidth(35);
		stuBottom1.getChildren().add(stuSex);
		stuBottom1.getChildren().add(new Label("רҵ:"));
		stuMajor = new TextField();
		stuMajor.setPrefWidth(70);
		stuBottom1.getChildren().add(stuMajor);
		stuBottom1.getChildren().add(new Label("�༶:"));
		stuClass = new TextField();
		stuClass.setPrefWidth(70);
		stuBottom1.getChildren().add(stuClass);
		stuBottom1.getChildren().add(new Label("�绰:"));
		stuTel = new TextField();
		stuTel.setPrefWidth(100);
		stuBottom1.getChildren().add(stuTel);
		stuOperation.setTop(stuBottom1);
		
		
		stuBottom2 = new HBox(10);
		stuBottom2.setAlignment(Pos.BASELINE_RIGHT);
		stuBottom2.setPadding(new Insets(10, 0, 0, 0));
		stuBottom2.getChildren().add(new Label("���Ĳ���:"));
		addStu = new Button("���");
		stuBottom2.getChildren().add(addStu);
		updStu = new Button("�޸�");
		stuBottom2.getChildren().add(updStu);
		delStu = new Button("ɾ��");
		stuBottom2.getChildren().add(delStu);
		stuOperation.setBottom(stuBottom2);
	    
		stuPane.setTop(topPane1);
		stuPane.setCenter(stuResult);
		stuPane.setBottom(stuOperation);
		
		//��ѯѧ����Ϣ��ť����¼�
		queryStu.setOnAction(e -> {
			stuId.setText("");
			stuName.setText("");
			stuSex.setText("");
			stuMajor.setText("");
			stuClass.setText("");
		    stuTel.setText("");
			ForAdmin queryStu = new ForAdmin();
			queryStu.connect();
		    stuList.clear();   //ˢ������
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
		
		// ����ťע���¼�
		addStu.setOnAction(new stuAddListener());
		updStu.setOnAction(new stuUpdateListener());
		delStu.setOnAction(new stuDeleteListener());
		
		return stuPane;
	}
	
	/*
	 * �������ݿ�
	 */
	public void connect() {
		try {
			Class.forName(driverName);
			con = DriverManager.getConnection(url, user, password);
			System.out.println("���ݿ����ӳɹ�");

		} catch (ClassNotFoundException e) {
			System.out.println("������������" + e.getMessage());
		} catch (SQLException e) {
			System.out.println("�������ݿ����" + e.getMessage());
		}
	}
	/*
	 * ��ѯstudent��
	 */
	public void queryInStu(String sql) {
		// ����statement����
		try {
			sta = con.createStatement();
			// ִ�в�ѯ����
			rs = sta.executeQuery(sql);
			// ��ʾ���
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
			System.out.println("��ѯ����" + e.getMessage());
		}
	}
	
	/*
	 * �������ݱ�
	 */
	public void updateData(String sql){
		// ����statement����
		try {
			sta = con.createStatement();
			// ִ�и��²���
			sta.executeUpdate(sql);
			
		} catch (SQLException e) {
			System.out.println("���³���" + e.getMessage());
		}
	}
	
	/*
	 * ��ѯinstructor��
	 */
	public void queryInIns(String sql) {
		// ����statement����
		try {
			sta = con.createStatement();
			// ִ�в�ѯ����
			rs = sta.executeQuery(sql);
			// ��ʾ���
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
			System.out.println("��ѯ����" + e.getMessage());
		}
	}
	
	/*
	 * ��ѯadmin��
	 */
	public void queryInAdmin(String sql) {
		// ����statement����
		try {
			sta = con.createStatement();
			// ִ�в�ѯ����
			rs = sta.executeQuery(sql);
			// ��ʾ���
			while(rs.next()){
				String id = rs.getString("admin_id");
				if(id.equals(this.userId)){
					this.userPwd = rs.getString("password");
				}
				adminList.add(id);
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
		
	
	/*
	 * ��ʦ��Ϣ
	 */
	private Node showIns(){
		insPane = new BorderPane();
		insPane.setPadding(new Insets(15, 15, 15, 15));
		
		topPane2 = new BorderPane();
		topPane2.setPadding(new Insets(0, 0, 15, 0));
		title2 = new Label("��������ʦ�ƹ���ϵͳ");
		title2.setFont(Font.font("����", FontWeight.NORMAL, FontPosture.REGULAR, 18));
		topPane2.setLeft(title2);
		queryIns = new Button("��ѯ��ʦ��Ϣ");
		topPane2.setRight(queryIns);
		
		insResult = new TableView<Instructor>();
		insResult.setId("adminIns");
		TableColumn<Instructor, String> tColId = new TableColumn<Instructor, String>("����");
		tColId.setPrefWidth(100);
		TableColumn<Instructor, String> tColName = new TableColumn<Instructor, String>("����");
		tColName.setPrefWidth(90);
		TableColumn<Instructor, String> tColSex = new TableColumn<Instructor, String>("�Ա�");
		tColSex.setPrefWidth(60);
		TableColumn<Instructor, String> tColTitle = new TableColumn<Instructor, String>("ְ��");
		tColTitle.setPrefWidth(100);
		TableColumn<Instructor, String> tColResearch = new TableColumn<Instructor, String>("�о�����");
		tColResearch.setPrefWidth(100);
		TableColumn<Instructor, String> tColTel = new TableColumn<Instructor, String>("�绰");
		tColTel.setPrefWidth(130);
		TableColumn<Instructor, String> tColNum = new TableColumn<Instructor, String>("��ѧ����");
		tColNum.setPrefWidth(88);
	    insResult.getColumns().addAll(tColId, tColName, tColSex, tColTitle, tColResearch, tColTel, tColNum);
	    
	    //��insResultע������¼�
	    insResult.setOnMouseClicked(new insMouseClickedListener());
	    
	    insOperation = new BorderPane();
	    insBottom1 = new HBox(4);
	    insBottom1.setAlignment(Pos.CENTER);
	    insBottom1.setPadding(new Insets(10, 0, 0, 0));
	    
	    insBottom1.getChildren().add(new Label("����:"));
		insId = new TextField();
		insId.setPrefWidth(60);
		insBottom1.getChildren().add(insId);
		insBottom1.getChildren().add(new Label("����:"));
		insName = new TextField();
		insName.setPrefWidth(50);
		insBottom1.getChildren().add(insName);
		insBottom1.getChildren().add(new Label("�Ա�:"));
		insSex = new TextField();
		insSex.setPrefWidth(40);
		insBottom1.getChildren().add(insSex);
		insBottom1.getChildren().add(new Label("ְ��:"));
		insTitle = new TextField();
		insTitle.setPrefWidth(50);
		insBottom1.getChildren().add(insTitle);
		insBottom1.getChildren().add(new Label("�о�����:"));
		insResearch = new TextField();
		insResearch.setPrefWidth(60);
		insBottom1.getChildren().add(insResearch);
		insBottom1.getChildren().add(new Label("�绰:"));
		insTel = new TextField();
		insTel.setPrefWidth(100);
		insBottom1.getChildren().add(insTel);
		insBottom1.getChildren().add(new Label("ѧ����:"));
		insPairNum = new TextField();
		insPairNum.setPrefWidth(30);
		insBottom1.getChildren().add(insPairNum);
		insOperation.setTop(insBottom1);
		
		
		insBottom2 = new HBox(10);
		insBottom2.setAlignment(Pos.BASELINE_RIGHT);
		insBottom2.setPadding(new Insets(10, 0, 0, 0));
		insBottom2.getChildren().add(new Label("���Ĳ���:"));
		addIns = new Button("���");
		insBottom2.getChildren().add(addIns);
		updIns = new Button("�޸�");
		insBottom2.getChildren().add(updIns);
		delIns = new Button("ɾ��");
		insBottom2.getChildren().add(delIns);
		insOperation.setBottom(insBottom2);
		
	    
		insPane.setTop(topPane2);
		insPane.setCenter(insResult);
		insPane.setBottom(insOperation);
		
		//��ѯ��ʦ��Ϣ��ť����¼�
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
			insList.clear();   //ˢ������
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
		
		// ����ťע���¼�
		addIns.setOnAction(new insAddListener());
		updIns.setOnAction(new insUpdateListener());
		delIns.setOnAction(new insDeleteListener());
		
		return insPane;
	}
	
	/*
	 * ������
	 */
	private Node showResult(){
		resultPane = new BorderPane();
        resultPane.setPadding(new Insets(15, 15, 15, 15));
		
		topPane = new FlowPane();
		topPane.setPadding(new Insets(0, 5, 8, 5));
		topPane.setHgap(8);
		notice = new Label("ѧ��״̬");
		status = new ComboBox<>(); 
		status.setPrefWidth(100);   
		ObservableList<String> items = FXCollections.observableArrayList(chooseType);
		status.setItems(items);
		queryResult = new Button("��ѯ������");
		topPane.getChildren().addAll(notice, status, queryResult);
		
		pairResult = new TableView<StuIns>();
		pairResult.setId("adminPair");
		TableColumn<StuIns, String> stuName = new TableColumn<StuIns, String>("ѧ��");
		stuName.setPrefWidth(140);
		TableColumn<StuIns, String> stuState = new TableColumn<StuIns, String>("ѧ��״̬");
		stuState.setPrefWidth(130);
		TableColumn<StuIns, String> insName = new TableColumn<StuIns, String>("��ʦ");
		insName.setPrefWidth(130);
		TableColumn<StuIns, String> insSure = new TableColumn<StuIns, String>("��ѡ�õ�ʦ����");
		insSure.setPrefWidth(130);
		TableColumn<StuIns, String> insState = new TableColumn<StuIns, String>("��ʦ״̬");
		insState.setPrefWidth(140);
	    pairResult.getColumns().addAll(stuName, stuState, insName, insSure, insState);
		
	    resultPane.setTop(topPane);	
	    resultPane.setLeft(pairResult);
	    
	    /*Ϊѡ��ʽ����ӵ���¼�*/
		status.setOnAction(e->{
			mode = items.indexOf(status.getValue());
			if(mode == 0){
				sqlStr = "select * from student where " + "state = 'δѡ'";
			}
			else if(mode == 1){
				sqlStr = "select * from student where " + "state = '����'";
			}
			else if(mode == 2){
				sqlStr = "select * from student where " + "state = 'ѡ��'";
			}
			System.out.println(sqlStr);
		});
		
		//��ѯ��ť�ĵ���¼�
		queryResult.setOnAction(e -> {
			ForAdmin stuIns = new ForAdmin();
			stuIns.connect();
			pairList.clear();   //ˢ������
			stuIns.queryInStu(sqlStr);    //��ѯstudent��
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
	 * ��Ϣά��
	 */
	private Node adminInfo(){
		adminInfoPane = new BorderPane();
		adminInfoPane.setPadding(new Insets(60,50,0,50));
	    
	    //�޸�����
	    changePwd = new GridPane();
	    changePwd.setHgap(5);
	    changePwd.setVgap(8);
	    
	    Label password = new Label("�޸�����");
		password.setFont(Font.font("����", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 16));
		changePwd.add(password, 0, 0);
		
		//ԭ����
		changePwd.add(new Label("����ԭ����"), 0, 4);
	    oldPwd = new PasswordField();
	    changePwd.add(oldPwd, 1, 4);
	    
	    //������
	    changePwd.add(new Label("����������"), 0, 5);
	    newPwd = new PasswordField();
	    changePwd.add(newPwd, 1, 5);
	    
	    //ȷ��������
	    changePwd.add(new Label("ȷ��������"), 0, 6);
	    surePwd = new PasswordField();
	    changePwd.add(surePwd, 1, 6);
	    
	    //ȷ�ϰ�ť
        HBox buttonPane = new HBox();
	    buttonPane.setSpacing(15);
	    buttonPane.setAlignment(Pos.CENTER_RIGHT);
	    change = new Button("�޸�");
	    change.setPrefSize(60, 25);
	    buttonPane.getChildren().add(change);
	    changePwd.add(buttonPane, 1, 7);
	    
	    changePwd.add(new Label("1.���볤������Ϊ5λ\n2.������ĸ����������\n3.��������ĸ���ִ�Сд\n4.���벻�����˺���ͬ"), 1, 10);

	    //��������Ա
	    addAdmin = new GridPane();
	    addAdmin.setHgap(5);
	    addAdmin.setVgap(8);
	    
	    Label addadmin = new Label("��������Ա");
	    addadmin.setFont(Font.font("����", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 16));
		addAdmin.add(addadmin, 0, 4);
		
		//�¹���Ա�˺�
		addAdmin.add(new Label("�¹���Ա�˺�"), 0, 8);
	    adminId = new TextField();
	    addAdmin.add(adminId, 1, 8);
	    
	    //�¹���Ա����
	    addAdmin.add(new Label("�¹���Ա����"), 0, 9);
	    adminPwd = new PasswordField();
	    addAdmin.add(adminPwd, 1, 9);
	    
	    
	    //��Ӱ�ť
        HBox addPane = new HBox();
	    addPane.setSpacing(15);
	    addPane.setAlignment(Pos.CENTER_RIGHT);
	    add = new Button("���");
	    add.setPrefSize(60, 25);
	    addPane.getChildren().add(add);
	    addAdmin.add(addPane, 1, 11);
	    
        adminInfoPane.setLeft(changePwd);
        adminInfoPane.setRight(addAdmin);
	    
	    //ȷ�ϰ�ť�ĵ���¼�
	    change.setOnAction(e -> {
	    	if(oldPwd.getText().equals(this.userPwd)){
	    		if(newPwd.getText().equals(surePwd.getText())){
	    			if(newPwd.getText().matches("^.*[a-zA-Z]+.*$") && newPwd.getText().matches("^.*[0-9]+.*$") && newPwd.getText().matches("^.{5,}$")){
	    				//�������ݿ��е�������Ϣ
	    	    		ForStudent changePwd = new ForStudent();
	    	    		changePwd.connect();
	    	    		sqlStr = "update admin set password = '" + newPwd.getText() + "' where admin_id = '" + this.userId + "'";
	    	    		changePwd.updateInStu(sqlStr);
	    	    		changePwd.closeConnection();
			    		oldPwd.setText("");
			    		newPwd.setText("");
			    		surePwd.setText("");
			    		Alert warning = new Alert(Alert.AlertType.INFORMATION);
			    		warning.setHeaderText("�����޸ĳɹ���");
			    		warning.showAndWait();
	    			}
	    			else{
	    				newPwd.setText("");
			    		surePwd.setText("");
	    				Alert warning = new Alert(Alert.AlertType.WARNING);
			    		warning.setHeaderText("����������벻����Ҫ��Ŷ��");
			    		warning.showAndWait();
	    			}
	    		}
	    		else {
	    			newPwd.setText("");
		    		surePwd.setText("");
	    			Alert warning = new Alert(Alert.AlertType.WARNING);
		    		warning.setHeaderText("��������������벻��ͬ��");
		    		warning.showAndWait();
	    		}
	    	}
	    	else{
	    		oldPwd.setText("");
	    		Alert warning = new Alert(Alert.AlertType.WARNING);
	    		warning.setHeaderText("�������ԭ���벻��ȷ��");
	    		warning.showAndWait();
	    	}
	    });
	    
	    add.setOnAction(e -> {
	    	if(!adminList.contains(adminId.getText())){
	    		//��admin�����������
	    		ForAdmin insertAdmin = new ForAdmin();
	    		insertAdmin.connect();
	    		sqlStr = "insert into admin (admin_id, password) values('" 
	    		                              + adminId.getText() + "','" 
	    				                      + adminPwd.getText() + "')";
	    		insertAdmin.updateData(sqlStr);
	    		insertAdmin.closeConnection();
	    		Alert warning = new Alert(Alert.AlertType.INFORMATION);
	    		warning.setHeaderText("��ӹ���Ա�ɹ���");
	    		warning.showAndWait();
	    		adminId.setText("");
	    		adminPwd.setText("");
	    	}
	    	else {
	    		adminId.setText("");
	    		adminPwd.setText("");
	    		Alert warning = new Alert(Alert.AlertType.WARNING);
	    		warning.setHeaderText("���˺��Ѵ��ڣ�����������");
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
	 * Student�����¼�����
	 */
	
	//tableview�ĵ���¼�
	public class stuMouseClickedListener implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent arg0) {
			// �õ��û�ѡ��ļ�¼
			int selectedRow = stuResult.getSelectionModel().getSelectedIndex();
			
			// ���ȷʵѡȡ��ĳ����¼
			if(selectedRow!=-1){
				// ��ȡѡ��ļ�¼
				Student stuObj = stuList.get(selectedRow);
			
				// ���û�ѡ��ļ�¼�е����ݷֱ���ӵ���Ӧ���ı�����
				stuId.setText(stuObj.getId());
				stuName.setText(stuObj.getName());
				stuSex.setText(stuObj.getSex());
				stuMajor.setText(stuObj.getMajor());
				stuClass.setText(stuObj.getClasses());
				stuTel.setText(stuObj.getTelephone());
			
			// if������
			}
		}
	}
	
	//��Ӱ�ť�����¼�
	public class stuAddListener implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			Student stuObj = new Student(stuId.getText(), stuName.getText(),stuSex.getText(), stuMajor.getText(), stuClass.getText(), stuTel.getText());
			stuList.add(stuObj);
			
			//��student�����������
    		ForAdmin insertStu = new ForAdmin();
    		insertStu.connect();
    		sqlStr = "insert into student (stu_id, name, sex, major, class, telephone, state, instructor, password) values('" 
    		                              + stuId.getText() + "','" 
    				                      + stuName.getText() + "','" 
    		                              + stuSex.getText() + "','" 
    				                      + stuMajor.getText() + "','" 
    		                              + stuClass.getText() + "','" 
    				                      + stuTel.getText() + "', 'δѡ', null, '12345')";
    		insertStu.updateData(sqlStr);
    		insertStu.closeConnection();
		}
	}

	//���°�ť�����¼�
	public class stuUpdateListener implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			// ��ñ�ѡ���е�����
			int selectedRow = stuResult.getSelectionModel().getSelectedIndex();
			if (selectedRow != -1) {
				Student stuObj = new Student(stuId.getText(), stuName.getText(),stuSex.getText(), stuMajor.getText(), stuClass.getText(), stuTel.getText());
				stuList.set(selectedRow, stuObj);
				
				//��student�������ݽ����޸�
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

	//ɾ����ť�����¼�
	public class stuDeleteListener implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			// ��ñ�ѡ���е�����
			int selectedRow = stuResult.getSelectionModel().getSelectedIndex();

			// �ж��Ƿ���ڱ�ѡ����
			if (selectedRow != -1) {
				// �ӱ��ģ�͵���ɾ��ָ����
				stuList.remove(selectedRow);
				//�����ݴ�student����ɾ��
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
	 * Instructor�����¼�����
	 */
	
	//tableview�ĵ���¼�
	public class insMouseClickedListener implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent arg0) {
			// �õ��û�ѡ��ļ�¼
			int selectedRow = insResult.getSelectionModel().getSelectedIndex();
			
			// ���ȷʵѡȡ��ĳ����¼
			if(selectedRow!=-1){
				// ��ȡѡ��ļ�¼
				Instructor insObj = insList.get(selectedRow);
			
				// ���û�ѡ��ļ�¼�е����ݷֱ���ӵ���Ӧ���ı�����
				insId.setText(insObj.getId());
				insName.setText(insObj.getName());
				insSex.setText(insObj.getSex());
				insTitle.setText(insObj.getTitle());
				insResearch.setText(insObj.getResearch());
				insTel.setText(insObj.getTelephone());
				insPairNum.setText(insObj.getPairNum());
			
			// if������
			}
		}
	}
	
	//��Ӱ�ť�����¼�
	public class insAddListener implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			Instructor insObj = new Instructor(insId.getText(), insName.getText(),insSex.getText(), insTitle.getText(), insResearch.getText(), insTel.getText(), insPairNum.getText());
			insList.add(insObj);
			
			//��student�����������
    		ForAdmin insertIns = new ForAdmin();
    		insertIns.connect();
    		sqlStr = "insert into instructor (ins_id, name, sex, title, research, telephone, password, pair_num, sure_num, state) values('" 
    		                              + insId.getText() + "','" 
    				                      + insName.getText() + "','" 
    		                              + insSex.getText() + "','" 
    				                      + insTitle.getText() + "','" 
    		                              + insResearch.getText() + "','" 
    				                      + insTel.getText() + "', '12345', '5', '0', '����ѡ')";
    		insertIns.updateData(sqlStr);
    		insertIns.closeConnection();
		}
	}

	//���°�ť�����¼�
	public class insUpdateListener implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			// ��ñ�ѡ���е�����
			int selectedRow = insResult.getSelectionModel().getSelectedIndex();
			if (selectedRow != -1) {
				Instructor insObj = new Instructor(insId.getText(), insName.getText(),insSex.getText(), insTitle.getText(), insResearch.getText(), insTel.getText(), insPairNum.getText());
				insList.set(selectedRow, insObj);
				
				//��student�������ݽ����޸�
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

	//ɾ����ť�����¼�
	public class insDeleteListener implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			// ��ñ�ѡ���е�����
			int selectedRow = insResult.getSelectionModel().getSelectedIndex();

			// �ж��Ƿ���ڱ�ѡ����
			if (selectedRow != -1) {
				// �ӱ��ģ�͵���ɾ��ָ����
				insList.remove(selectedRow);
				//�����ݴ�student����ɾ��
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
