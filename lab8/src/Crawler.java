import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс реализующий сканер веб-страниц
 *
 */
public class Crawler 
{
	/**
	 * Время ожидания запроса от сервера в мс
	 */
	public int timedout;
	/**
	 * Пул, в котором содержиться все используемые и найденные адреса
	 */
	public URLPool urlPool;
	/**
	 * База данных, к которой ведется подключение для записи
	 */
	private DBWriteURL db;

	/**
	 * Шаблон регулярных выражений для поиска ссылок href
	 */
	static final String HREF_PATTERN = "(href=\").*?\"";
	/**
	 * Шаблон регулярных выражений для поиска тега a
	 */
	static final String TAG_A_PATTERN = "(<a).*?>";
	/**
	 * Скомплированный шаблон поиска тега а
	 */
	private static final Pattern TAG_A_PATTEN_COMPILE = Pattern.compile(TAG_A_PATTERN);
	/**
	 * Скомплированный шаблон поиска href
	 */
	private static final Pattern HREF_PATTERN_COMPILE = Pattern.compile(HREF_PATTERN);

	/**
	 * Максимальная глубина поиска
	 */
	private int maxDepth;
	/**
	 * Конструктор
	 * @param url - Входящая url строка
	 * @param maxDepth - Максимальная глубина поиска
	 * @throws IOException
	 * @throws UnknownHostException 
	 * @throws ClassNotFoundException 
	 */
	public Crawler(String url, int maxDepth, int timedout) throws UnknownHostException, IOException, ClassNotFoundException, SQLException
	{
		this.timedout = timedout;
		URLDepthPair urlpair = new URLDepthPair(url,0);
		urlPool = new URLPool();
		urlPool.addURL(urlpair);
		this.maxDepth = maxDepth;
		db = new DBWriteURL();
	}
	/**
	 * Класс  потока
	 */
	public class CrawlerTask implements Runnable {


	    public void run()
	    {
	    	while(true)
	    	{
	    		try {
					startCrawl();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
	    }

	}
	/**
	 * Метод запускающий сканирование в несколько потоков
	 * @param n - количество потоков
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public void startMultiThreadCrawl(int n) throws ClassNotFoundException
	{
		db.conn();
		db.createDB();
		for(int i = 0;i<n;i++)
		{
			CrawlerTask run = new CrawlerTask();
			Thread myThread = new Thread(run,"Thread" + i);
			myThread.start();
		}
		while(true)
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (urlPool.waitCount == n)
			{
				db.closeDB();
				System.exit(0);
			}
		}
	}
	/**
	 * Метод запускающий сканирование
	 * @throws IOException
	 * @throws SQLException 
	 * @throws InterruptedException 
	 */
	private void startCrawl() throws IOException, InterruptedException
	{
		URLDepthPair urlpair = urlPool.getURL();
		if (urlpair.depth<=maxDepth) 
		{
			if(readURL(urlpair)) urlPool.addCheckedURL(urlpair);
			System.out.println(urlpair.url + " " + urlpair.depth);
		}
		else urlPool.addNoneCheckedURL(urlpair);
	}
/**
 * Метод чтения url страницы
 * @param urlpair - содержит адрес url страницы
 * @return - true, если подключение удачно, иначе false
 * @throws IOException
 * @throws SQLException 
 */
	@SuppressWarnings("finally")
	private boolean readURL(URLDepthPair urlpair) throws IOException
	{
		InfoURL info = urlPool.getInfo(urlpair);
		SocketConnection conn = new SocketConnection(urlpair,timedout);
		// вернуть false, если соединение неудачно
		if (!conn.connect()) return false;
		conn.sendGET();
		if (conn.checkConnection())
		{
			try
			{
				String line;
				BufferedReader br = conn.br;
				while (!(line = br.readLine()).equals("0")) 
				{
					// Если строка содержит url, то увеличить количество ссылок
					if (ParseNewURL(line,urlpair)) info.addHref();
					info.readLine(line);
			    }
				db.writeDB(info);
				conn.close();
				return true;
			}
			finally
			{
				conn.close();
				return false;
			}
			
		}
		// Если код ответа, не равен 200(OK)
		else return false;
	}
	/**
	 * Метод парсинга строки на наличие уникальной url ссылки,
	 * если таковая существует на заданной строке, то добавляет
	 * новый url в необходимые элементы
	 * @param line - Входящая строка
	 * @param url - URLDepthPair по адресу которого была получена входящая строка
	 * @return true, если строка содержит url, иначе false
	 * @throws SQLException 
	 */
	private boolean ParseNewURL(String line, URLDepthPair url)
	{
		try
		{
			// Поиск тега а
			Matcher m = TAG_A_PATTEN_COMPILE.matcher(line);
			m.find();
			String tagA = line.substring(m.start(),m.end());
			// Поиск ссылки href в теге а
			Matcher m2 = HREF_PATTERN_COMPILE.matcher(tagA);
			m2.find();
			String href = tagA.substring(m2.start(),m2.end());
			URLDepthPair newURL = new URLDepthPair(href,url.depth+1,url.domain);
			// Увеличение количества "Цитирования страницы", если она уже есть в пуле
			if(!urlPool.addURL(newURL))
			{
				InfoURL info = urlPool.getInfo(newURL);
				info.addQuoute();
				db.updateQuoteDB(info);
			}		
			return true;
			
		}
		catch (IllegalStateException e)
		{
			return false;
		} 
		catch (MalformedURLException e) 
		{
			return false;
		}
	}

}
