import java.io.*;
import java.net.UnknownHostException;
import java.sql.SQLException;

/**
 * ����� ����������� ������ ���-�������
 *
 */
public class Web_Scan {
/**
 * @param url - ����� ���-�������� � ������� ���������� �����
 * @param depth - ������������ ������� ������
 * @throws IOException 
 * @throws UnknownHostException 
 * @throws SQLException 
 * @throws ClassNotFoundException 
 */
	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, SQLException 
	{
		String test = "http://www.777gid.ru/p/blog-page_30.html";
		Crawler crawl = new Crawler(test, 5);
		crawl.startMultiThreadCrawl(5);
		
 	}

}
