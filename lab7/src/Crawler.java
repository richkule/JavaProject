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
 * ����� ����������� ������ ���-�������
 *
 */
public class Crawler 
{
	/**
	 * ���� ������, � ������� ������� ����������� ��� ������
	 */
	private DBWriteURL db;

	
	/**
	 * ������ �������
	 */
	private LinkedList<MyRunnable> threadArray;
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
	 * ��������� ���� ��������� �������, ��� �������� ������������ ������ ������
	 */
	private HashMap<String,InfoURL> allURL = new HashMap<String,InfoURL>();
	/**
	 * ��������� ���� ��������� �������, ����������� ��� "������" ������ ����� ��������
	 * ��� ��������
	 */
	private HashSet<String> allDomain = new HashSet<String>();
	/**
	 * ������ �� �����������, �� �������� url �������
	 */
	private LinkedBlockingDeque <URLDepthPair> unCheckedList = new LinkedBlockingDeque <URLDepthPair>();
	/**
	 * ������ ����������� url �������
	 */
	private LinkedList <URLDepthPair> checkedList = new LinkedList <URLDepthPair>();
	/**
	 * ������ ������������� �����
	 */
	private LinkedList <URLDepthPair> noneCheckedList = new LinkedList <URLDepthPair>();
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
	 * �����  ������
	 */
	public class MyRunnable implements Runnable {

	    /**
	     * ����, ����������� �������� �� � ������ ������ �����
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
	 * ����� ����������� ������������ � ��������� �������
	 * @param n - ���������� �������
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
	 * ����� ����������� ������������
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
 * ����� ������ url ��������
 * @param urlpair - �������� ����� url ��������
 * @return - true, ���� ����������� ������, ����� false
 * @throws IOException
 * @throws SQLException 
 */
	private boolean readURL(URLDepthPair urlpair) throws IOException
	{
		System.out.println(urlpair.url);
		InfoURL info = allURL.get(urlpair.url);
		SocketConnection conn = new SocketConnection(urlpair);
		// ������� false, ���� ���������� ��������
		if (!conn.connect()) return false;
		conn.sendGET();
		if (conn.code.equals("200"))
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
			// ���������� ���������� "����������� ��������", ���� ��� ��� ���� � allURL
			// ����� ���������� � allURL
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
				// ���������� � ����� ������, ����������� �������, ���� ��� ��������
				// � �� ���������� �������, ����� � ������ ������
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
	 * @return checkedList - ������ ����������� url
	 */
	public LinkedList <URLDepthPair> get�heckedSites()
	{
		return checkedList;
	}
	/**
	 * @return noneCheckedList - C����� ������������� url
	 */
	public LinkedList <URLDepthPair> getUn�heckedSites()
	{
		return noneCheckedList;
	}
	/**
	 * ����� ������������ ���������� � �����
	 * @param udp - URLDepthPair ���������� url �������������� ����� 
	 * @return - InfoURL ������� �����
	 */
	public InfoURL getInfo(URLDepthPair udp)
	{
		return this.allURL.get(udp.url);
	}
	/**
	 * ����� ������������ ���������� � ���� ������
	 * @return - HashMap<String,InfoURL> ���������� ���������� � ���� ��������� ������
	 */
	public HashMap<String,InfoURL> getInfo()
	{
		return this.allURL;
	}

}
