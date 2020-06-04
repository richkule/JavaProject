import java.io.*;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Scanner;

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

		CloseThread run = new CloseThread();
		Thread myThread = new Thread(run,"CloseThread");
		myThread.start();
		
		String url = args[0];
		int depth = Integer.parseInt(args[1]);
		int timedout = Integer.parseInt(args[2]);
		Crawler crawl = new Crawler(url, depth,timedout);
		crawl.startMultiThreadCrawl(5);
		
 	}
	/**
	 * Поток необходимый для завершения программы при
	 * введении в консоль сообщения close
	 */
	public static class CloseThread implements Runnable {
	    public void run()
	    {
	        Scanner sc = new Scanner(System.in);
	        while(true) {
	            String str = sc.next();
	            System.out.println(str);
	            if("close".equals(str)) {
	                System.exit(0);
	            }
	        }
	    }

	}
}
