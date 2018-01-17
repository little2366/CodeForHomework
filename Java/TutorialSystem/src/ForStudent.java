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
	private String userId;      //ͨ��userId��������
	
	private TabPane tabPane;     
	private Tab chooseIns;
	private Tab stuInfo;
	
	//ѡ��ʦ
	private BorderPane chooseInsPane;
	private FlowPane searchPane;           //��������
	private Button query;
	private ComboBox<String> status;    //��ʦ��ѡ���״̬
	private String[] chooseType = {"ȫ����ʦ",  "����ѡ", "������" };
	private int mode = -1;  //0��ʾδѡ��1��ʾ������2��ʾѡ��
	private TableView<Instructor> insResult;
	private static ObservableList<Instructor> insList = FXCollections.observableArrayList();
	private VBox forChoose;  
	private Label name;                //��ǰѡ�е�ʦ������
	private GridPane insIntro;         //��ʦ����ϸ��Ϣ
	private TextField insId;           //��ʦ����
	private TextField insSex;          //��ʦ�Ա�
	private TextField insTitle;        //��ʦְ��
	private TextField insResearch;     //��ʦ�о�����
	private TextField insTelephone;          //��ʦ��ϵ�绰
	private ListView<String> studentList;    //��ѡ��ѧ���б�
	private static ObservableList<String> stuList = FXCollections.observableArrayList();
	private Button choose;            
	private static ObservableList<Ins> ins = FXCollections.observableArrayList();
	private ModalDialog chooseInstructor;
	
	
	//��Ϣά��
	private BorderPane stuInfoPane;
	private HBox topPane;
	private Label stuStatus;
	private GridPane stuInformation;
	private TextField stuId;           //ѧ��ѧ��
	private TextField stuName;         //ѧ������
	private TextField stuSex;          //ѧ���Ա�
	private TextField stuMajor;        //ѧ��רҵ
	private TextField stuClasses;      //ѧ���༶
	private TextField stuTelephone;    //ѧ����ϵ�绰
	private TextField stuIns;          //ѧ���ĵ�ʦ
	private GridPane changePwd;
	private PasswordField oldPwd;          //������
	private PasswordField newPwd;          //������
	private PasswordField surePwd;         //ȷ������
	private Button change;
	
	
	//���ݿ���ز���
	private Connection con;
	private Statement sta;
	private String driverName = "com.mysql.jdbc.Driver";
	private String url = "jdbc:mysql://127.0.0.1:3306/tutor";   
	private String user = "root";
	private String password = "223366";
	private ResultSet rs;   // ��Ų�ѯ���ļ�¼��
	private String sqlStr;   //����ѡ��ķ�ʽ�޸Ĳ�ѯ���
	
	//���ݿ�Ĳ�ѯ
	static private String userName;
	static private String userSex;
	static private String userMajor;
	static private String userClasses;
	static private String userTelephone;
	static private String userIns;
	static private String userState;
	static private String userPwd;
	
	static private int selectedRow = -1;     //�����ѡ�񡱰�ťǰ�Ƿ���ѡ��һ����ʦ
	
	protected BorderPane getPane() {
		stuPane = new BorderPane();
		stuPane.setPadding(new Insets(0,0,0,0));
		
		//�����tab
		tabPane = new TabPane();
		//tabPane.setId("stuTab");
		chooseIns = new Tab("ѡ��ʦ");
		chooseIns.setContent(chooseIns());
		stuInfo = new Tab("��Ϣά��");
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
		
		//��ʼ����Ϣ������ѧ�Ų�ѯ��ѧ����Ϣ
		this.userId = userId;
		ForStudent queryStudent = new ForStudent();
		queryStudent.connect();
		sqlStr = "select * from student where " + "stu_id = " + this.userId;
		queryStudent.queryInStu(sqlStr);
		queryStudent.closeConnection();
		
		Scene scene = new Scene(getPane(),600, 430);
		scene.getStylesheets().add("style.css");
		primaryStage.setTitle("��������ʦ�ƹ���ϵͳ"); 
		primaryStage.setScene(scene); 
		primaryStage.show(); 
		
		//ѡ��ʦ��ť
	    choose.setOnAction(e -> {
	    	if(selectedRow == -1){
	    		Alert warning = new Alert(Alert.AlertType.WARNING);
	    		warning.setHeaderText("����ѡ��һ����ʦ��");
	    		warning.showAndWait();
	    	}
	    	else {
	    		if(this.userState.equals("ѡ��")){
		    		Alert warning = new Alert(Alert.AlertType.WARNING);
		    		warning.setHeaderText("������ѡ�ˣ����Ѿ��е�ʦ��Ŷ��");
		    		warning.showAndWait();
		    	}
		    	else if(this.userState.equals("����")){
		    		Alert warning = new Alert(Alert.AlertType.WARNING);
		    		warning.setHeaderText("������ѡ�ˣ����ĵȵ�Ŷ��");
		    		warning.showAndWait();
		    	}
		    	else{
		    		chooseInstructor = new ModalDialog(primaryStage, "ȷ��ѡ��õ�ʦ��", this.userId);
		    	}
	    	}
	    	
	    });
	    
		
	}
	
	public static void main(String[] args) {
		launch(args);
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
	 * ��ѯ�����ѧ����Ϣ
	 */
	public void queryInStu(String sql) {
		// ����statement����
		try {
			sta = con.createStatement();
			// ִ�в�ѯ����
			rs = sta.executeQuery(sql);
			// ��ʾ���
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
			System.out.println("��ѯ����" + e.getMessage());
		}
	}
	/*
	 * ��ѯstudent��
	 */
	public void queryStu(String sql) {
		// ����statement����
		try {
			sta = con.createStatement();
			// ִ�в�ѯ����
			rs = sta.executeQuery(sql);
			stuList.clear();
			// ��ʾ���
			while(rs.next()){
				if((rs.getString("state")).equals("ѡ��")){
					stuList.add(rs.getString("name"));
					System.out.println(stuList.size());
				}
			}
			
		} catch (SQLException e) {
			System.out.println("��ѯ����" + e.getMessage());
		}
	}
	/*
	 * ����student���е�������Ϣ
	 */
	public void updateInStu(String sql){
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
				String name = rs.getString("name");
				int count = Integer.parseInt(rs.getString("sure_num"));
				int choose = Integer.parseInt(rs.getString("pair_num")) - count;
				insList.add((new Instructor(name, count, choose)));
			}
			
		} catch (SQLException e) {
			System.out.println("��ѯ����" + e.getMessage());
		}
	}
	
	/*
	 * ��ѯ����ĵ�ʦ��Ϣ
	 */
	public void queryIns(String sql) {
		// ����statement����
		try {
			sta = con.createStatement();
			// ִ�в�ѯ����
			rs = sta.executeQuery(sql);
			ins.clear();
			// ��ʾ���
			while(rs.next()){
				String id = rs.getString("ins_id");
				String sex = rs.getString("sex");
				String title = rs.getString("title");
				String research = rs.getString("research");
				String telephone = rs.getString("telephone");
				ins.add((new Ins(id, sex, title, research, telephone)));
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
	 * ѡ��ʦ
	 */
	private Node chooseIns() {
		
		chooseInsPane = new BorderPane();
		chooseInsPane.setPadding(new Insets(15, 35, 15, 15));
		
		//��������
		searchPane = new FlowPane();
		searchPane.setPadding(new Insets(0, 5, 8, 5));
		searchPane.setHgap(8);
		status = new ComboBox<>(); 
		status.setPrefWidth(100); 
		ObservableList<String> items = FXCollections.observableArrayList(chooseType);
		status.setItems(items);
		HBox queryHBox = new HBox();
		queryHBox.setPadding(new Insets(0, 0, 0, 90));
		query = new Button("��ѯ");
		queryHBox.getChildren().add(query);
		searchPane.getChildren().addAll(new Label("��ʦ״̬"), status, queryHBox);
	    
		//�������
	    insResult = new TableView<Instructor>();
	    insResult.setId("stuInsResult");
		TableColumn<Instructor, String> tColName = new TableColumn<Instructor, String>("��ʦ����");
		tColName.setPrefWidth(100);
		TableColumn<Instructor, String> tColCount = new TableColumn<Instructor, String>("�Ѷ�����");
		tColCount.setPrefWidth(100);
		TableColumn<Instructor, String> tColChoose = new TableColumn<Instructor, String>("��ѡ����");
		tColChoose.setPrefWidth(100);
		insResult.getColumns().addAll(tColName, tColCount, tColChoose);
		
		//��insResultע������¼�
	    insResult.setOnMouseClicked(new insMouseClickedListener());
		
		forChoose = new VBox();
		HBox namePane = new HBox();
		name = new Label();
		name.setText("");
		name.setFont(Font.font("����", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 20));
		Label instructor = new Label("��ʦ");
		instructor.setPadding(new Insets(8, 0, 0, 8));
		namePane.getChildren().addAll(name, instructor);
		
		//��ʦ��ϸ��Ϣ��չʾ
		insIntro = new GridPane();
		insIntro.setHgap(8);
		insIntro.setVgap(8);
		
		//��ʦ����
		insIntro.add(new Label("����"), 0, 2);
	    insId = new TextField();
	    insId.setEditable(false);
	    insIntro.add(insId, 1, 2);
	    
	    //��ʦ�Ա�
	    insIntro.add(new Label("�Ա�"), 0, 3);
	    insSex = new TextField();
	    insSex.setEditable(false);
	    insIntro.add(insSex, 1, 3);
	    
	    //��ʦְ��
	    insIntro.add(new Label("ְ��"), 0, 4);
	    insTitle = new TextField();
	    insTitle.setEditable(false);
	    insIntro.add(insTitle, 1, 4);
	    
	    //��ʦ�о�����
	    insIntro.add(new Label("�о�����"), 0, 5);
	    insResearch = new TextField();
	    insResearch.setEditable(false);
	    insIntro.add(insResearch, 1, 5);
	    
	    //��ʦ��ϵ�绰
	    insIntro.add(new Label("��ϵ�绰"), 0, 6);
	    insTelephone = new TextField();
	    insTelephone.setEditable(false);
	    insIntro.add(insTelephone, 1, 6);
	    
	    //������Щͬѧѡ�˸�λ��ʦ
	    insIntro.add(new Label("��ѡѧ��"), 0, 7);
	    studentList = new ListView<String>(stuList);
		studentList.setPrefWidth(100);
		studentList.setPrefHeight(120);
		insIntro.add(studentList, 1, 7);
	    
	    HBox buttonPane = new HBox();
	    buttonPane.setSpacing(15);
	    buttonPane.setAlignment(Pos.CENTER_RIGHT);
	    choose = new Button("ѡ��");
	    choose.setPrefSize(70, 25);
	    buttonPane.getChildren().add(choose);
	    insIntro.add(buttonPane, 1, 9);
	    
	    forChoose.getChildren().addAll(namePane, insIntro);
	    
	    chooseInsPane.setTop(searchPane);
	    chooseInsPane.setLeft(insResult);
	    chooseInsPane.setRight(forChoose);
	    
	    /*Ϊѡ��ʽ����ӵ���¼�*/
		status.setOnAction(e->{
			mode = items.indexOf(status.getValue());
			if(mode == 0){
				sqlStr = "select * from instructor";
			}
			else if(mode == 1){
				sqlStr = "select * from instructor where " + "state = '����ѡ'";
			}
			else if(mode == 2){
				sqlStr = "select * from instructor where " + "state = '������'";
			}
			System.out.println(sqlStr);
		});
	    
	    //��ѯ��ʦ��ť
	    query.setOnAction(e -> {
	    	ForStudent queryIns = new ForStudent();
			queryIns.connect();
			insList.clear();   //ˢ������
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
	 * ������Ϣ
	 */
	private Node stuInfo() {
		stuInfoPane = new BorderPane();
		stuInfoPane.setPadding(new Insets(20,30,0,30));
		
		stuInformation = new GridPane();
		stuInformation.setHgap(2);
		stuInformation.setVgap(8);
		
		topPane = new HBox();
		topPane.setPadding(new Insets(0, 0, 25, 0));
		Label title = new Label("��ǰ��״̬Ϊ��");
		title.setFont(Font.font("����", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 13));
	
		
		stuStatus = new Label(this.userState);
		stuStatus.setFont(Font.font("����", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 18));
		stuStatus.setPadding(new Insets(-5,0,0,0));
		topPane.getChildren().addAll(title, stuStatus);
		
		Label message = new Label("������Ϣ");
		message.setFont(Font.font("����", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 16));
		stuInformation.add(message, 0, 0);
		
		//ѧ����Ϣ
		stuInformation.add(new Label("ѧ��"), 0, 2);
	    stuId = new TextField();
	    stuId.setText(this.userId);
	    stuId.setEditable(false);
	    stuInformation.add(stuId, 1, 2);
	    
	    //������Ϣ
	    stuInformation.add(new Label("����"), 0, 3);
	    stuName = new TextField();
	    stuName.setText(this.userName);
	    stuName.setEditable(false);
	    stuInformation.add(stuName, 1, 3);
	    
	    //�Ա�
	    stuInformation.add(new Label("�Ա�"), 0, 4);
	    stuSex = new TextField();
	    stuSex.setText(this.userSex);
	    stuSex.setEditable(false);
	    stuInformation.add(stuSex, 1, 4);
	    
	    //ѧ��רҵ
	    stuInformation.add(new Label("רҵ"), 0, 5);
	    stuMajor = new TextField();
	    stuMajor.setText(this.userMajor);
	    stuMajor.setEditable(false);
	    stuInformation.add(stuMajor, 1, 5);
	    
	    //ѧ���༶
	    stuInformation.add(new Label("�༶"), 0, 6);
	    stuClasses = new TextField();
	    stuClasses.setText(this.userClasses);
	    stuClasses.setEditable(false);
	    stuInformation.add(stuClasses, 1, 6);   
	    
	    //ѧ����ϵ�绰
	    stuInformation.add(new Label("�ֻ�����"), 0, 7);
	    stuTelephone = new TextField();
	    stuTelephone.setText(this.userTelephone);
	    stuTelephone.setEditable(false);
	    stuInformation.add(stuTelephone, 1, 7);
	    
	    //ѧ����ʦ
	    stuInformation.add(new Label("��ʦ"), 0, 8);
	    stuIns = new TextField();
	    if(this.userState.equals("ѡ��")){
	    	stuIns.setText(this.userIns);
	    }
	    stuIns.setEditable(false);
	    stuInformation.add(stuIns, 1, 8);
	    
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
		
	    stuInfoPane.setTop(topPane);
	    stuInfoPane.setLeft(stuInformation);
	    stuInfoPane.setRight(changePwd);
	    
	    //ȷ�ϰ�ť�ĵ���¼�
	    change.setOnAction(e -> {
	    	if(oldPwd.getText().equals(this.userPwd)){
	    		if(newPwd.getText().equals(surePwd.getText())){
	    			if(newPwd.getText().matches("^.*[a-zA-Z]+.*$") && newPwd.getText().matches("^.*[0-9]+.*$") && newPwd.getText().matches("^.{5,}$")){
	    				//�������ݿ��е�������Ϣ
			    		ForStudent changePwd = new ForStudent();
			    		changePwd.connect();
			    		sqlStr = "update student set password = " + newPwd.getText() + " where stu_id = " + this.userId;
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
	    
		return stuInfoPane;
	}
	
	
	//����tableView��ʾ��Instructor��
	public static class Instructor{
		private String name;       //��ʦ����
		private int count;         //��ǰ���ж���ѧ��ѡ��õ�ʦ
		private int choose;      //��ǰ������ѡ���ٸ�ѧ��
		
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
		private String id;       //��ʦ����
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
	
	//tableview�ĵ���¼�
	public class insMouseClickedListener implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent arg0) {
			// �õ��û�ѡ��ļ�¼
			selectedRow = insResult.getSelectionModel().getSelectedIndex();
			
			// ���ȷʵѡȡ��ĳ����¼
			if(selectedRow!=-1){
				// ��ȡѡ��ļ�¼
				Instructor insObj = insList.get(selectedRow);
				ForStudent queryIns = new ForStudent();
				queryIns.connect();
				//stuList.clear();   //ˢ������
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
				//stuList.clear();   //ˢ������
				sqlStr = "select * from student where instructor = '" + insObj.getName() + "'";			
				queryStu.queryStu(sqlStr);
				System.out.println(stuList.size());
				studentList.setItems(stuList);
				queryStu.closeConnection();
			
			// if������
			}
		}
	}
	
	/*
	 * ѡ��ʦʱ������ģ̬����
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
			cancelBtn = new Button("ȡ��");
			cancelBtn.setFont(Font.font(13));
			okBtn = new Button("ȷ��");
			okBtn.setFont(Font.font(13));
			btnPane.getChildren().addAll(cancelBtn, okBtn);

			VBox mainPane = new VBox();
			mainPane.setAlignment(Pos.CENTER);
			VBox.setMargin(btnPane, new Insets(20,0,20,0));		
			mainPane.getChildren().addAll(label, btnPane);
			
			Stage stage = new Stage(); 
			stage.initModality(Modality.APPLICATION_MODAL); // �趨Ϊģ̬����
			stage.initOwner(stg); // ����������
			
			Scene scene = new Scene(mainPane, 220, 100);
			stage.setScene(scene);
			stage.show();
			
			// ע������ȷ������ť�¼�
			okBtn.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					
					//����student���е�״̬��Ϣ
		    		ForStudent updateStu = new ForStudent();
		    		updateStu.connect();
		    		sqlStr = "update student set instructor = '" + name.getText() 
		    		                         + "', state = '����'"
		    		                         + " where stu_id = " + userId;
		    		updateStu.updateInStu(sqlStr);
		    		updateStu.closeConnection();
		    		stage.hide();  // �رնԻ���
				}
			});
			
			// ע������ȡ������ť�¼�
			cancelBtn.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent arg0) {
					stage.hide();  // �رնԻ���
				}
			});
		}
	}
}
