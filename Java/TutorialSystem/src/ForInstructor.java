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
	private String userId;     //��ǰʹ�ø�ϵͳ���û�
	
	private TabPane tabPane;     
	private Tab chooseStu;    
	private Tab insInfo;

	//ѡ��ѧ��
	private BorderPane chooseStuPane;
	private FlowPane topPane;
	private Label notice;
	private ComboBox<String> status;
	private String[] chooseType = {"ȫ��ѧ��",  "��ȷ��", "��ѡ��" };
	private int mode = -1;  //0��ʾδѡ��1��ʾ������2��ʾѡ��
	private Button query;
	private TableView<Student> stuResult;
	private static ObservableList<Student> stuList = FXCollections.observableArrayList();
	private HBox bottomPane;
	private TextField stuName;
	private Button sure;
	private Button out;            //��̭
	private ModalDialog chooseStudent;
	 
	
	//��Ϣά��
	private BorderPane insInfoPane;
	private GridPane insInformation;
	private TextField insId;           //��ʦ����
	private TextField insName;         //��ʦ����
	private TextField insSex;          //��ʦ�Ա�
	private TextField insTitle;        //��ʦְ��
	private TextField insResearch;    //��ʦ�о�����
	private TextField insTelephone;    //��ʦ��ϵ�绰
	private TextField insPairNum;      //��ʦ�ɴ�ѧ����
	private GridPane changePwd;
	private TextField oldPwd;          //������
	private TextField newPwd;          //������
	private TextField surePwd;         //ȷ������
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
	static private String userTitle;
	static private String userResearch;
	static private String userTelephone;
	static private String userPwd;
	static private String userPair;
	static private String userSure;
	
	static private int selectedRow;   //��ǰѡ�е����ĸ�ѧ��
	
	
	protected BorderPane getPane() {
		
		insPane = new BorderPane();
		insPane.setPadding(new Insets(0, 0, 0, 0));
		
		//�����tab
		tabPane = new TabPane();
		//tabPane.setId("stuTab");
		chooseStu = new Tab("ѡ��ѧ��");
		chooseStu.setContent(chooseStu());
		insInfo = new Tab("��Ϣά��");
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
		
		//��̭��ť�¼�
		out.setOnAction(e -> {
			String str = "ȷ����̭ѧ��" + stuName.getText() + "��";
			chooseStudent = new ModalDialog(primaryStage, str);			
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
			System.out.println("��ѯ����" + e.getMessage());
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
				String state = rs.getString("state");
				stuList.add((new Student(id, name, sex, major, classes, telephone, state)));
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
	 * ѡ��ѧ��
	 */
	private Node chooseStu() {
		
		chooseStuPane = new BorderPane();
		chooseStuPane.setPadding(new Insets(16, 10, 10, 10));
		
		topPane = new FlowPane();
		topPane.setPadding(new Insets(0, 5, 8, 5));
		topPane.setHgap(8);
		notice = new Label("ѧ��״̬");
		status = new ComboBox<>(); 
		status.setPrefWidth(100);   
		ObservableList<String> items = FXCollections.observableArrayList(chooseType);
		status.setItems(items);
		query = new Button("��ѯ");
		topPane.getChildren().addAll(notice, status, query);
		
		stuResult = new TableView<Student>();
		stuResult.setId("insStuResult");
		
		TableColumn<Student, String> tColId = new TableColumn<Student, String>("ѧ��");
		tColId.setPrefWidth(100);
		TableColumn<Student, String> tColName = new TableColumn<Student, String>("����");
		tColName.setPrefWidth(70);
		TableColumn<Student, String> tColSex = new TableColumn<Student, String>("�Ա�");
		tColSex.setPrefWidth(57);
		TableColumn<Student, String> tColMajor = new TableColumn<Student, String>("רҵ");
		tColMajor.setPrefWidth(90);
		TableColumn<Student, String> tColClasses = new TableColumn<Student, String>("�༶");
		tColClasses.setPrefWidth(90);
		TableColumn<Student, String> tColTele = new TableColumn<Student, String>("�绰");
		tColTele.setPrefWidth(120);
		TableColumn<Student, String> tColState = new TableColumn<Student, String>("״̬");
		tColState.setPrefWidth(70);
	    stuResult.getColumns().addAll(tColId, tColName, tColSex, tColMajor, tColClasses, tColTele, tColState);
	    
	    //��stuResultע������¼�
	    stuResult.setOnMouseClicked(new stuMouseClickedListener());
	    
		bottomPane = new HBox(8);
		bottomPane.setPadding(new Insets(15, 0, 0, 0));
		bottomPane.setSpacing(10);
		stuName = new TextField();
		stuName.setPrefWidth(100);
		stuName.setEditable(false);
		sure = new Button("ѡ��");
		out = new Button("��̭");
		bottomPane.getChildren().addAll(new Label("����ǰѡ���ѧ���ǣ�"), stuName, sure, out);
		
		
		chooseStuPane.setTop(topPane);
		chooseStuPane.setCenter(stuResult);
		
		//һ��ʼ�Ȳ�add��ֻ��ѡ�е��Ǹ�ѧ��״̬�Ǵ�����ʱ���add
		//chooseStuPane.setBottom(bottomPane);
		
		/*Ϊѡ��ʽ����ӵ���¼�*/
		status.setOnAction(e->{
			mode = items.indexOf(status.getValue());
			if(mode == 0){
				sqlStr = "select * from student where instructor = '" + this.userName + "'";
			}
			else if(mode == 1){
				sqlStr = "select * from student where instructor = '" + this.userName + "' and state = '����'";
			}
			else if(mode == 2){
				sqlStr = "select * from student where instructor = '" + this.userName + "' and state = 'ѡ��'";
			}
			System.out.println(sqlStr);
		});
		
		
		//��ѯ��ť�¼�
		query.setOnAction(e -> {
			ForInstructor queryStu = new ForInstructor();
			queryStu.connect();
			stuList.clear();   //ˢ������
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
		
		//ѡ����ť�¼�
		sure.setOnAction(e -> {
		
			// ����tableView�е�����
			if(selectedRow!=-1){
				Student chooseObj = stuList.get(selectedRow);
				Student stuObj = new Student(chooseObj.getId(), chooseObj.getName(),chooseObj.getSex(), chooseObj.getMajor(), chooseObj.getClasses(), chooseObj.getTelephone(), "ѡ��");
				stuList.set(selectedRow, stuObj);
			}
			
			//����student���е�����
    		ForInstructor sureStu = new ForInstructor();
    		sureStu.connect();
    		sqlStr = "update student set state = 'ѡ��'" + " where name = '" + stuName.getText() + "'";
    		sureStu.updateData(sqlStr);
    		sureStu.closeConnection();
    		
    		//����instructor���е�����
    		ForInstructor sureIns = new ForInstructor();
    		sureIns.connect();
    		int sureNum = Integer.parseInt(this.userSure) + 1;
    		//˵���õ�ʦ������ѧ����������
    		if(sureNum == Integer.parseInt(this.userPair)){
    			sqlStr = "update instructor set sure_num = '"  + sureNum + "', state = '������' " + "where ins_id = '" + this.userId + "'";
    		}
    		else{
    			sqlStr = "update instructor set sure_num = '"  + sureNum + "' where ins_id = " + this.userId;
    		}
    		sureIns.updateData(sqlStr);
    		sureIns.closeConnection();
    		
    		//������ʾ��Ϣ
    		Alert warning = new Alert(Alert.AlertType.INFORMATION);
    		String str = "���ѳɹ�ѡ��ѧ��" + stuName.getText() + "��";
    		warning.setHeaderText(str);
    		warning.showAndWait();
		});
		
		return chooseStuPane;
	}
	
	
	
	/*
	 * ��Ϣά��
	 */
	private Node insInfo(String name){
		insInfoPane = new BorderPane();
		insInfoPane.setPadding(new Insets(40,30,0,30));
		
		insInformation = new GridPane();
		insInformation.setHgap(2);
		insInformation.setVgap(8);
		
		Label message = new Label("������Ϣ");
		message.setFont(Font.font("����", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 16));
		insInformation.add(message, 0, 0);
		
		//������Ϣ
		insInformation.add(new Label("����"), 0, 2);
	    insId = new TextField();
	    insId.setText(this.userId);
	    insId.setEditable(false);
	    insInformation.add(insId, 1, 2);
	    
	    //������Ϣ
	    insInformation.add(new Label("����"), 0, 3);
	    insName = new TextField();
	    insName.setText(this.userName);
	    insName.setEditable(false);
	    insInformation.add(insName, 1, 3);
	    
	    //��ʦ�Ա�
	    insInformation.add(new Label("�Ա�"), 0, 4);
	    insSex = new TextField();
	    insSex.setText(this.userSex);
	    insSex.setEditable(false);
	    insInformation.add(insSex, 1, 4);
	    
	    //��ʦְ��
	    insInformation.add(new Label("ְ��"), 0, 5);
	    insTitle = new TextField();
	    insTitle.setText(this.userTitle);
	    insTitle.setEditable(false);
	    insInformation.add(insTitle, 1, 5);
	    
	    //��ʦ�о�����
	    insInformation.add(new Label("�о�����"), 0, 6);
	    insResearch = new TextField();
	    insResearch.setText(this.userResearch);
	    insResearch.setEditable(false);
	    insInformation.add(insResearch, 1, 6);   
	    
	    //��ʦ��ϵ�绰
	    insInformation.add(new Label("�ֻ�����"), 0, 7);
	    insTelephone = new TextField();
	    insTelephone.setText(this.userTelephone);
	    insTelephone.setEditable(false);
	    insInformation.add(insTelephone, 1, 7);
	    
	    //��ʦ�ɴ���ѧ����
	    insInformation.add(new Label("��ѧ����"), 0, 8);
	    insPairNum = new TextField();
	    insPairNum.setText(this.userPair);
	    insPairNum.setEditable(false);
	    insInformation.add(insPairNum, 1, 8);
	    
	    //�޸�����
	    changePwd = new GridPane();
	    changePwd.setHgap(5);
	    changePwd.setVgap(8);
	    
	    Label password = new Label("�޸�����");
		password.setFont(Font.font("����", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 16));
		changePwd.add(password, 0, 0);
		
		//ԭ����
		changePwd.add(new Label("����ԭ����"), 0, 4);
	    oldPwd = new TextField();
	    changePwd.add(oldPwd, 1, 4);
	    
	    //������
	    changePwd.add(new Label("����������"), 0, 5);
	    newPwd = new TextField();
	    changePwd.add(newPwd, 1, 5);
	    
	    //ȷ��������
	    changePwd.add(new Label("ȷ��������"), 0, 6);
	    surePwd = new TextField();
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

        insInfoPane.setLeft(insInformation);	
        insInfoPane.setRight(changePwd);  
	    
	    //ȷ�ϰ�ť�ĵ���¼�
	    change.setOnAction(e -> {
	    	if(oldPwd.getText().equals(this.userPwd)){
	    		if(newPwd.getText().equals(surePwd.getText())){
	    			if(newPwd.getText().matches("^.*[a-zA-Z]+.*$") && newPwd.getText().matches("^.*[0-9]+.*$") && newPwd.getText().matches("^.{5,}$")){
	    				//�������ݿ��е�������Ϣ
	    	    		ForStudent changePwd = new ForStudent();
	    	    		changePwd.connect();
	    	    		sqlStr = "update instructor set password = " + newPwd.getText() + " where ins_id = " + this.userId;
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
	 * Student�����¼�����
	 */
	
	//tableview�ĵ���¼�
	public class stuMouseClickedListener implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent arg0) {
			// �õ��û�ѡ��ļ�¼
			selectedRow = stuResult.getSelectionModel().getSelectedIndex();
			
			// ���ȷʵѡȡ��ĳ����¼
			if(selectedRow!=-1){
				// ��ȡѡ��ļ�¼
				Student stuObj = stuList.get(selectedRow);
			
				// ���û�ѡ��ļ�¼�е����ݷֱ���ӵ���Ӧ���ı�����
				stuName.setText(stuObj.getName());
				if(stuObj.getState().equals("����")){
					chooseStuPane.setBottom(bottomPane);
				}
				else if(stuObj.getState().equals("ѡ��")){
					stuName.setText("");
					chooseStuPane.getChildren().remove(bottomPane);
				}
			
			// if������
			}
		}
	}
	
	/*
	 * ��̭ѧ��ʱ������ģ̬����
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
			
			Scene scene = new Scene(mainPane, 250, 100);
			stage.setScene(scene);
			stage.show();
			
			// ע������ȷ������ť�¼�
			okBtn.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					
					if(selectedRow!=-1){
						//�ӱ��ģ����ɾ��
						stuList.remove(selectedRow);
						
						//����student���ݱ��е���Ϣ
						ForInstructor outIns = new ForInstructor();
			    		outIns.connect();
			    		sqlStr = "update student set state = 'δѡ'" 
			    				                 + ", instructor = null"
			    		                         + " where name = '" + stuName.getText() + "'";
			    		outIns.updateData(sqlStr);
			    		outIns.closeConnection();
			    		stage.hide();  // �رնԻ���
					}
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
