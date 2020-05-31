import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * ����� ��� ������ ���������� URL � ���������� � ��� � ���� ������
 */
public class DBWriteURL {
	/**
	 * ���������� ������� Connection ���� ������ � ������� ������� �����������
	 */
	private  Connection conn;
	/**
	 * ���������� ������� Statement ���� ������ � ������� ������� �����������
	 */
	private  Statement statmt;
	/**
	 * ������������ ������� JDBC ��� ����������� � ���� ������
	 */
	private static final String DRIVER = "org.sqlite.JDBC";
	/**
	 * ������� �������� � ���� ��� ����������� � ���� ������
	 */
	private static final String DRIVER_PREFIX = "jdbc:sqlite:";
	/**
	 * ���� � ����� ��
	 */
	private static final String PATH = "db\\";
	/**
	 * ��� ��, � ������� ������� �����������
	 */
	private static final String DB = "parseurl.db";
	/**
	 * ������ ���� � �� ��� �����������
	 */
	private static final String FULLPATH = DRIVER_PREFIX+PATH+DB; 
	
	/**
	 * ����� �������������� ����������� � ��
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void conn()
	   {
		   conn = null;
		   try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   try {
			conn = DriverManager.getConnection(FULLPATH);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   }
	
	/**
	 * ����� �������������� �������� �������
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void createDB()
	   {
		try {
			statmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			statmt.executeUpdate("CREATE TABLE if not exists 'url' "
					+ "('id' INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "'url' text,"
					+ "'quouteCount' INT,"
					+ "'contentIndex' INT,"
					+"'title' text,"
					+ "'desciption' text);");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   }
	

	/**
	 * �������� � ���� ������ ������ � ����������� ��������
	 * @param info - �����, ���������� ���������� � ����� �� ������ url 
	 * @throws SQLException
	 */
	public void writeDB(InfoURL info)
	{
		   try {
			statmt.executeUpdate("INSERT INTO 'url'"
			   		+ " ('url', 'quouteCount','contentIndex','title','desciption') VALUES "
			   		+ "('"+info.getURL().replaceAll("'", "\"")+"',"+info.getQuoteCount()+","+info.getContentIndex()+",'"+info.getTitle().replaceAll("'", "\"")+"','"+info.getDescription().replaceAll("'", "\"")+"');");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * ���������� ���������� "�����������" ������ �������� �������
	 * @param info - �����, ���������� ���������� � ����� �� ������ url
	 * @throws SQLException
	 */
	public void updateQuoteDB(InfoURL info)
	{
		try {
			statmt.executeUpdate("UPDATE 'url' "
					+ "SET 'quouteCount' = "+info.getQuoteCount()
					+ " WHERE 'url' = '"+info.getURL()+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * ����� ����������� ����������� � ��
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void closeDB()
	   {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			statmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   }
	

}
