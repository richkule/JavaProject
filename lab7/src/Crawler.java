import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс реализующий сканер веб-страниц
 *
 */
public class Crawler 
{
	/**
	 * База данных, к которой ведется подключение для записи
	 */
	private DBWriteURL db;

	
	/**
	 * Список потоков
	 */
	private LinkedList<MyRunnable> threadArray;
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
	 * Множество всех найденных адресов, для проверки уникальности нового домена
	 */
	private HashMap<String,InfoURL> allURL = new HashMap<String,InfoURL>();
	/**
	 * Множество всех найденных доменов, необходимый для "Умного" выбора новой страницы
	 * для парсинка
	 */
	private HashSet<String> allDomain = new HashSet<String>();
	/**
	 * Список не проверенных, но найденны url адресов
	 */
	private LinkedBlockingDeque <URLDepthPair> unCheckedList = new LinkedBlockingDeque <URLDepthPair>();
	/**
	 * Список проверенных url адресов
	 */
	private LinkedList <URLDepthPair> checkedList = new LinkedList <URLDepthPair>();
	/**
	 * Список непроверяемых узлов
	 */
	private LinkedList <URLDepthPair> noneCheckedList = new LinkedList <URLDepthPair>();
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
	public Crawler(String url, int maxDepth) throws UnknownHostException, IOException, ClassNotFoundException, SQLException
	{
		URLDepthPair urlpair = new URLDepthPair(url,0);
		unCheckedList.add(urlpair);
		allURL.put(urlpair.url, new InfoURL(urlpair.url));
		allDomain.add(urlpair.domain);
		this.maxDepth = maxDepth;
		db = new DBWriteURL();

	}
	/**
	 * Класс  потока
	 */
	public class MyRunnable implements Runnable {

	    /**
	     * Флаг, указывающий работает ли в данный момент поток
	     */
	    public boolean isRun = false;
	    public void run()
	    {
	    	while(true)
	    	{
		    	this.isRun = true;
		    	try {
					startCrawl();
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	this.isRun = false;
		    	try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	boolean flag = true;
		    	for(MyRunnable var : threadArray)
		    	{
		    		if(var.isRun)
	    			{
		    			flag = false;
		    			break;
	    			}
		    	}
		    	if(flag) break;
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
		this.threadArray = new LinkedList<MyRunnable>();
		for(int i = 0;i<n;i++)
		{
			MyRunnable run = new MyRunnable();
			this.threadArray.add(run);
			Thread myThread = new Thread(run,"Thread" + i);
			myThread.start();
		}
		while(true)
		{
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	boolean flag = true;
	    	for(MyRunnable var : threadArray)
	    	{
	    		if(var.isRun) flag = false;
	    	}
	    	if(flag) break;
		}
		db.closeDB();
	}
	/**
	 * Метод запускающий сканирование
	 * @throws IOException
	 * @throws SQLException 
	 * @throws InterruptedException 
	 */
	private void startCrawl() throws IOException, InterruptedException
	{
		while(!unCheckedList.isEmpty())
		{
			URLDepthPair urlpair = unCheckedList.pollFirst();
			if (urlpair.depth<=maxDepth) 
			{
				if(readURL(urlpair)) checkedList.addLast(urlpair);
			}
			else noneCheckedList.add(urlpair);
		}
	}
/**
 * Метод чтения url страницы
 * @param urlpair - содержит адрес url страницы
 * @return - true, если подключение удачно, иначе false
 * @throws IOException
 * @throws SQLException 
 */
	private boolean readURL(URLDepthPair urlpair) throws IOException
	{
		System.out.println(urlpair.url);
		InfoURL info = allURL.get(urlpair.url);
		SocketConnection conn = new SocketConnection(urlpair);
		// вернуть false, если соединение неудачно
		if (!conn.connect()) return false;
		conn.sendGET();
		if (conn.code.equals("200"))
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
			// Увеличение количества "Цитирования страницы", если она уже есть в allURL
			// Иначе добавление в allURL
			if (allURL.containsKey(newURL.url))
			{
				InfoURL info = allURL.get(newURL.url);
				info.addQuoute();
				db.updateQuoteDB(info);
				return true;
			}
			else
			{
				allURL.put(newURL.url, new InfoURL(newURL.url));
				// Добавление в конец списка, проверяемых страниц, если это страница
				// с не уникальным доменом, иначе в начало списка
				if (allDomain.contains(newURL.domain)) unCheckedList.addLast(newURL);
				else 
				{
					unCheckedList.addFirst(newURL);
					allDomain.add(newURL.domain);
				}
					
				return true;
			}
			
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
	/**
	 * @return checkedList - Список проверенных url
	 */
	public LinkedList <URLDepthPair> getСheckedSites()
	{
		return checkedList;
	}
	/**
	 * @return noneCheckedList - Cписок непроверяемых url
	 */
	public LinkedList <URLDepthPair> getUnСheckedSites()
	{
		return noneCheckedList;
	}
	/**
	 * Метод возвращающий информацию о сайте
	 * @param udp - URLDepthPair содержащий url запрашиваемого сайта 
	 * @return - InfoURL данного сайта
	 */
	public InfoURL getInfo(URLDepthPair udp)
	{
		return this.allURL.get(udp.url);
	}
	/**
	 * Метод возвращающий информацию о всех сайтах
	 * @return - HashMap<String,InfoURL> содержащий информацию о всех найденных сайтах
	 */
	public HashMap<String,InfoURL> getInfo()
	{
		return this.allURL;
	}

}
