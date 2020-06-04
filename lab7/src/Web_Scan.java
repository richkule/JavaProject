import java.io.*;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс запускающий сканер Веб-Страниц
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
		//http://www.777gid.ru/p/blog-page_30.html
		String url = args[0];
		int depth = Integer.parseInt(args[1]);
		Crawler crawl = new Crawler(url, depth);
		crawl.startMultiThreadCrawl(5);
 	}

}
