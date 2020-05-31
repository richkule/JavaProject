import java.io.*;
import java.net.UnknownHostException;
import java.sql.SQLException;

/**
 * Класс реализующий сканер Веб-Страниц
 *
 */
public class Web_Scan {
/**
 * @param url - Адрес веб-страницы с которой начинается поиск
 * @param depth - Максимальная глубина поиска
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
