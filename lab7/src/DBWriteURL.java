import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Класс для записи полученных URL и информации о них в базу данных
 */
public class DBWriteURL {
	/**
	 * Экзлемпляр объекта Connection базы данных к которой ведется подключение
	 */
	private  Connection conn;
	/**
	 * Экзлемпляр объекта Statement базы данных к которой ведется подключение
	 */
	private  Statement statmt;
	/**
	 * Используемый драйвер JDBC для подключения к базе данных
	 */
	private static final String DRIVER = "org.sqlite.JDBC";
	/**
	 * Префикс драйвера в пути для подключения к базе данных
	 */
	private static final String DRIVER_PREFIX = "jdbc:sqlite:";
	/**
	 * Путь к файлу БД
	 */
	private static final String PATH = "db\\";
	/**
	 * Имя БД, к которой ведется подключение
	 */
	private static final String DB = "parseurl.db";
	/**
	 * Полный путь к БД для подключения
	 */
	private static final String FULLPATH = DRIVER_PREFIX+PATH+DB; 
	
	/**
	 * Метод осуществляющий подключение к БД
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
	 * Метод осуществляющий создание таблицы
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
	 * Создание в базе данных записи о проверенной странице
	 * @param info - класс, содержащий информацию о сайте на данном url 
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
	 * Обновление количества "Цитирования" данной страницы другими
	 * @param info - класс, содержащий информацию о сайте на данном url
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
	 * Метод закрывающий подключение к БД
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
