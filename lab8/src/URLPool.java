import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingDeque;
/**
 *  ласс, работающий с найденными URL адресами
 */
public class URLPool {
	/**
	 *  оличество ожидающих потоков
	 */
	public int waitCount = 0;
	/**
	 * ћножество всех найденных адресов, дл€ проверки уникальности нового домена
	 */
	public HashMap<String,InfoURL> allURL = new HashMap<String,InfoURL>();
	/**
	 * ћножество всех найденных доменов, необходимый дл€ "”много" выбора новой страницы
	 * дл€ парсинка
	 */
	public HashSet<String> allDomain = new HashSet<String>();
	/**
	 * —писок не проверенных, но найденны url адресов
	 */
	public LinkedList <URLDepthPair> unCheckedList = new LinkedList <URLDepthPair>();
	/**
	 * —писок проверенных url адресов
	 */
	public LinkedList <URLDepthPair> checkedList = new LinkedList <URLDepthPair>();
	/**
	 * —писок непровер€емых узлов
	 */
	LinkedList <URLDepthPair> noneCheckedList = new LinkedList <URLDepthPair>();
	/**
	 * ƒобавл€ет адрес в список непровер€емых url
	 * @param urlpair
	 */
	public void addNoneCheckedURL (URLDepthPair urlpair)
	{
		noneCheckedList.addLast(urlpair);
	}
	/**
	 * ƒобавл€ет адрес в список проверенных
	 * @param urlpair - добавл€ема€ пара
	 */
	public void addCheckedURL(URLDepthPair urlpair)
	{
		checkedList.addLast(urlpair);
	}
	/**
	 * ћетод добавл€ющий новую URL пару в пул
	 * @param urlpair - добавл€ема€ пара
	 * @return true, если пара еще не содержитс€ в пуле иначе false
	 */
	public synchronized  boolean addURL(URLDepthPair urlpair)
	{
		if (allURL.containsKey(urlpair.url))  return false;
		else
		{
			allURL.put(urlpair.url, new InfoURL(urlpair.url));
			// ƒобавление в конец списка, провер€емых страниц, если это страница
			// с не уникальным доменом, иначе в начало списка
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
	 * ћетод возвращающий информацию о данном url
	 * @param urlpair - пара дл€ которой запрашиваетс€ информаци€
	 * @return InfoURL - информаци€ о данной паре
	 */
	public InfoURL getInfo(URLDepthPair urlpair)
	{
		return allURL.get(urlpair.url);
	}
	/**
	 * ћетод получени€ URL из списка не проверенных
	 * @return URLDepthPair - url пара дл€ сканировани€
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
