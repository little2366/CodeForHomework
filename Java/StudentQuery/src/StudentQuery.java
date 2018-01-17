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
//  1.����java.sql��
import java.sql.*;


public class StudentQuery extends Application {
    
	private BorderPane pane;
	private FlowPane topPane;
	private Label notice;
	private ComboBox<String> status;
	private String[] chooseType = { "δѡ", "����", "ѡ��" };
	private int mode = -1;  //0��ʾδѡ��1��ʾ������2��ʾѡ��
	private Button query;
	private TableView<Student> result;
	private static ObservableList<Student> obsList = FXCollections.observableArrayList();
	
	private Connection con;
	private Statement sta;
	private String driverName = "com.mysql.jdbc.Driver";
	private String url = "jdbc:mysql://127.0.0.1:3306/student";
	private String user = "root";
	private String password = "223366";
	
	private ResultSet rs;   // ��Ų�ѯ���ļ�¼��
	private String sqlStr;   //����ѡ��ķ�ʽ�޸Ĳ�ѯ���
	
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
		notice = new Label("ѧ��״̬");
		status = new ComboBox<>(); 
		status.setPrefWidth(100);   
		ObservableList<String> items = FXCollections.observableArrayList(chooseType);
		status.setItems(items);
		query = new Button("��ѯ");
		topPane.getChildren().addAll(notice, status, query);
		
		/*
		 * TableView��ʹ��
		 */
		
		// 1.��������ͼ
		result = new TableView<Student>();
		result.setId("my-table");
		// 2.�����ж���
		TableColumn<Student, String> tColId = new TableColumn<Student, String>("ѧ��");
		TableColumn<Student, String> tColName = new TableColumn<Student, String>("����");
		TableColumn<Student, String> tColSex = new TableColumn<Student, String>("�Ա�");
		TableColumn<Student, String> tColMajor = new TableColumn<Student, String>("רҵ");
		TableColumn<Student, String> tColClasses = new TableColumn<Student, String>("�༶");
		TableColumn<Student, String> tColTele = new TableColumn<Student, String>("�绰");
		TableColumn<Student, String> tColState = new TableColumn<Student, String>("״̬");
		TableColumn<Student, String> tColInstructor = new TableColumn<Student, String>("��ʦ");
		// 3.���ж�����ӵ�����ͼ
	    result.getColumns().addAll(tColId, tColName, tColSex, tColMajor, tColClasses, tColTele, tColState, tColInstructor);
	    
		
		pane.setTop(topPane);
		pane.setCenter(result);
		
		/*Ϊѡ��ʽ����ӵ���¼�*/
		status.setOnAction(e->{
			mode = items.indexOf(status.getValue());
			if(mode == 0){
				sqlStr = "select * from studentdata where " + "state = 'δѡ'";
			}
			else if(mode == 1){
				sqlStr = "select * from studentdata where " + "state = '����'";
			}
			else if(mode == 2){
				sqlStr = "select * from studentdata where " + "state = 'ѡ��'";
			}
			System.out.println(sqlStr);
		});
		
		query.setOnAction(e -> {
			StudentQuery jdbcTest = new StudentQuery();
			jdbcTest.connect();
			obsList.clear();   //ˢ������
			jdbcTest.queryInDB(sqlStr);
			System.out.println(obsList.size());
			// 5.�������б�ͱ���ͼ����
		    result.setItems(obsList);
		 	// 6.�������б���ж���������
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
		//����Զ���css��ʽ
		scene.getStylesheets().add("tableViewStyle.css");
		primaryStage.setTitle("��ѯ"); 
		primaryStage.setScene(scene); 
		primaryStage.show(); 
	}
	
	public void connect() {
		try {
			// 2.ʹ��Class���forName��������������������ص�JVM��Java���������
			Class.forName(driverName);

			// 3.ʹ��DriverManager��ľ�̬����getConnection��������Ӷ���
			con = DriverManager.getConnection(url, user, password);

			// ��ʾ���
			System.out.println("���ݿ����ӳɹ�");

		} catch (ClassNotFoundException e) {
			System.out.println("������������" + e.getMessage());
		} catch (SQLException e) {
			System.out.println("�������ݿ����" + e.getMessage());
		}
	}
	
	public void queryInDB(String sql) {
		// ����statement����
		try {
			sta = con.createStatement();
			// ִ�в�ѯ����
			rs = sta.executeQuery(sql);
			// ��ʾ���
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
			System.out.println("��ѯ����" + e.getMessage());
		}
	}

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
