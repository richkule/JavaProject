import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ����� ����������� ������ ���-�������
 *
 */
public class Crawler 
{
	/**
	 * ����� �������� ������� �� ������� � ��
	 */
	public int timedout;
	/**
	 * ���, � ������� ����������� ��� ������������ � ��������� ������
	 */
	public URLPool urlPool;
	/**
	 * ���� ������, � ������� ������� ����������� ��� ������
	 */
	private DBWriteURL db;

	/**
	 * ������ ���������� ��������� ��� ������ ������ href
	 */
	static final String HREF_PATTERN = "(href=\").*?\"";
	/**
	 * ������ ���������� ��������� ��� ������ ���� a
	 */
	static final String TAG_A_PATTERN = "(<a).*?>";
	/**
	 * ��������������� ������ ������ ���� �
	 */
	private static final Pattern TAG_A_PATTEN_COMPILE = Pattern.compile(TAG_A_PATTERN);
	/**
	 * ��������������� ������ ������ href
	 */
	private static final Pattern HREF_PATTERN_COMPILE = Pattern.compile(HREF_PATTERN);

	/**
	 * ������������ ������� ������
	 */
	private int maxDepth;
	/**
	 * �����������
	 * @param url - �������� url ������
	 * @param maxDepth - ������������ ������� ������
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
	 * �����  ������
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
	 * ����� ����������� ������������ � ��������� �������
	 * @param n - ���������� �������
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
	 * ����� ����������� ������������
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
 * ����� ������ url ��������
 * @param urlpair - �������� ����� url ��������
 * @return - true, ���� ����������� ������, ����� false
 * @throws IOException
 * @throws SQLException 
 */
	@SuppressWarnings("finally")
	private boolean readURL(URLDepthPair urlpair) throws IOException
	{
		InfoURL info = urlPool.getInfo(urlpair);
		SocketConnection conn = new SocketConnection(urlpair,timedout);
		// ������� false, ���� ���������� ��������
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
					// ���� ������ �������� url, �� ��������� ���������� ������
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
		// ���� ��� ������, �� ����� 200(OK)
		else return false;
	}
	/**
	 * ����� �������� ������ �� ������� ���������� url ������,
	 * ���� ������� ���������� �� �������� ������, �� ���������
	 * ����� url � ����������� ��������
	 * @param line - �������� ������
	 * @param url - URLDepthPair �� ������ �������� ���� �������� �������� ������
	 * @return true, ���� ������ �������� url, ����� false
	 * @throws SQLException 
	 */
	private boolean ParseNewURL(String line, URLDepthPair url)
	{
		try
		{
			// ����� ���� �
			Matcher m = TAG_A_PATTEN_COMPILE.matcher(line);
			m.find();
			String tagA = line.substring(m.start(),m.end());
			// ����� ������ href � ���� �
			Matcher m2 = HREF_PATTERN_COMPILE.matcher(tagA);
			m2.find();
			String href = tagA.substring(m2.start(),m2.end());
			URLDepthPair newURL = new URLDepthPair(href,url.depth+1,url.domain);
			// ���������� ���������� "����������� ��������", ���� ��� ��� ���� � ����
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
