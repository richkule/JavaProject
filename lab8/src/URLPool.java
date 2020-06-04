import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingDeque;
/**
 * �����, ���������� � ���������� URL ��������
 */
public class URLPool {
	/**
	 * ���������� ��������� �������
	 */
	public int waitCount = 0;
	/**
	 * ��������� ���� ��������� �������, ��� �������� ������������ ������ ������
	 */
	public HashMap<String,InfoURL> allURL = new HashMap<String,InfoURL>();
	/**
	 * ��������� ���� ��������� �������, ����������� ��� "������" ������ ����� ��������
	 * ��� ��������
	 */
	public HashSet<String> allDomain = new HashSet<String>();
	/**
	 * ������ �� �����������, �� �������� url �������
	 */
	public LinkedList <URLDepthPair> unCheckedList = new LinkedList <URLDepthPair>();
	/**
	 * ������ ����������� url �������
	 */
	public LinkedList <URLDepthPair> checkedList = new LinkedList <URLDepthPair>();
	/**
	 * ������ ������������� �����
	 */
	LinkedList <URLDepthPair> noneCheckedList = new LinkedList <URLDepthPair>();
	/**
	 * ��������� ����� � ������ ������������� url
	 * @param urlpair
	 */
	public void addNoneCheckedURL (URLDepthPair urlpair)
	{
		noneCheckedList.addLast(urlpair);
	}
	/**
	 * ��������� ����� � ������ �����������
	 * @param urlpair - ����������� ����
	 */
	public void addCheckedURL(URLDepthPair urlpair)
	{
		checkedList.addLast(urlpair);
	}
	/**
	 * ����� ����������� ����� URL ���� � ���
	 * @param urlpair - ����������� ����
	 * @return true, ���� ���� ��� �� ���������� � ���� ����� false
	 */
	public synchronized  boolean addURL(URLDepthPair urlpair)
	{
		if (allURL.containsKey(urlpair.url))  return false;
		else
		{
			allURL.put(urlpair.url, new InfoURL(urlpair.url));
			// ���������� � ����� ������, ����������� �������, ���� ��� ��������
			// � �� ���������� �������, ����� � ������ ������
			if (allDomain.contains(urlpair.domain)) unCheckedList.addLast(urlpair);
			else 
			{
				unCheckedList.addFirst(urlpair);
				allDomain.add(urlpair.domain);
			}
			this.notify();
			return true;
		}
	}
	/**
	 * ����� ������������ ���������� � ������ url
	 * @param urlpair - ���� ��� ������� ������������� ����������
	 * @return InfoURL - ���������� � ������ ����
	 */
	public InfoURL getInfo(URLDepthPair urlpair)
	{
		return allURL.get(urlpair.url);
	}
	/**
	 * ����� ��������� URL �� ������ �� �����������
	 * @return URLDepthPair - url ���� ��� ������������
	 * @throws InterruptedException 
	 */
	public synchronized URLDepthPair getURL() throws InterruptedException
	{
		if (unCheckedList.isEmpty())
		{
			waitCount++;
			this.wait();
			waitCount--;
			return unCheckedList.pollFirst();
		}
		return unCheckedList.pollFirst();
	}
}
