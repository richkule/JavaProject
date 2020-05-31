import java.net.MalformedURLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс реализующий пару URL:Depth
 * URL - Адрес страницы url
 * Depth - Глубина поиска, на которой найдена данная страница
 */
public class URLDepthPair 
{
	/**
	 * Индекс начала ссылки в в строке вида 'href="URL" ' (С любым символом на конце после кавычек)
	 */
	public static final int START_HREF = 6;
	/**
	 * Количество символов до конца строки от конца ссылки в строке вида 'href="URL" '  (С любым символом на конце после кавычек)
	 */
	public static final int HREF_TOEND = 1;
	/**
	 * HTML расширение
	 */
	public static final String HTML_EXTENSION = ".html";
	/**
	 * Префикс доступа к URL HTTP
	 */
	public static final String URL_PREFIX_HTTP = "http://";
	/**
	 * Шаблон регулярных выражений, необходиый для поиска префиксов http:// https:// 
	 */
	public static final String PREFIX_PATTERN = "(http://)";
	/**
	 * Шаблон регулярных выражения соответствующий доменным именам
	 */
	public static final String DOMAIN_PATTERN = "([\\da-zа-я\\.-]+)\\.([a-zа-я\\.]{2,6})";
	/**
	 * Шаблон регулярных выражений, соответсвующий путям до файла
	 */
	public static final String PATH_PATTERN = "([/\\wа-я\\.-]*)*\\/?";
	/**
	 * Шаблон регулярных выражений, возвращающий расширение файла
	 */
	public static final String EXTENSION_PATTERN = "\\.[\\wа-я\\-]+$";
	/**
	 * Шаблон регулярных выражений, соответствующий url строке
	 */
	public static final String URL_PATTERN = PREFIX_PATTERN+DOMAIN_PATTERN+PATH_PATTERN;
	/**
	 * Переменная содержащая url
	 */
	public String url;
	/**
	 * Переменная содержащая префикс доступа
	 */
	public String prefix;
	/**
	 * Переменная содержащая доменное имя
	 */
	public String domain;
	/**
	 * Переменная содержащая путь
	 */
	public String path;
	/**
	 * Переменная содержащая глубину
	 */
	public int depth;
	/**
	 * Конструктор для строки содержащей url
	 * @param url - Входящая строка, содержащая url
	 * @param depth - Глубина
	 * @throws MalformedURLException - данная строка не содержит url
	 */
	public URLDepthPair(String url, int depth) throws MalformedURLException
	{
		if (testURL(url)) this.url= url;
		else this.url = searchPattern(url,URL_PATTERN); 
		Pattern p = Pattern.compile(DOMAIN_PATTERN);
		Matcher m = p.matcher(this.url);
		m.find();
		this.domain = this.url.substring(m.start(),m.end());
		this.path = this.url.substring(m.end());
		if (this.path.isEmpty()) this.path = "/";
		this.depth = depth;
		checkExtension();
	}
	/**
	 * Конструктор для строки типа 'href="URL" '(С любым символом на конце после кавычек)
	 * @param href - Передаваемая строка
	 * @param depth - Глубина поиска, нак которой она найдена
	 * @param domain - Домен, на котором найдена найдена данная строка
	 * @throws MalformedURLException - В случае, если строка не содержит прямой или косвенной ссылки
	 */
	public URLDepthPair(String href, int depth, String domain) throws MalformedURLException
	{
		String hrefContent = href.substring(START_HREF,href.length()-HREF_TOEND);
		// Если строка содержит полную URL ссылку
		try
		{
			this.url = searchPattern(hrefContent,URL_PATTERN);
			Pattern p = Pattern.compile(DOMAIN_PATTERN);
			Matcher m = p.matcher(this.url);
			m.find();
			this.domain = this.url.substring(m.start(),m.end());
			this.path = this.url.substring(m.end());
			if (this.path.isEmpty()) this.path = "/";
			this.depth = depth;
			checkExtension();
		}
		// Если строка имеет тип пути
		catch (MalformedURLException e)
		{
			// Проверка правильности домена
			if (!Pattern.matches(DOMAIN_PATTERN,domain)) throw new MalformedURLException("Wrong URL");
			hrefContent = searchPattern(hrefContent,PATH_PATTERN);
			if (hrefContent.isEmpty()) throw new MalformedURLException("Wrong URL");
			if (hrefContent.charAt(0) != '/') hrefContent = "/"+hrefContent;
			this.url = URL_PREFIX_HTTP+domain+hrefContent;
			this.domain = domain;
			this.path = hrefContent;
			this.depth = depth;
			checkExtension();
		}
	}
	/**
	 * Метод проверки является ли строка адресом url
	 * @param url - Проверяемая строка
	 */
	public boolean testURL(String url)
	{
		return Pattern.matches(URL_PATTERN, url);
	}
	/**
	 * Метод поиска URL во входящей строке
	 * @param url - входная строка
	 * @return String - найденная подстрока, содержащая url, иначе null
	 * @throws MalformedURLException - Данная строка не содержит url
	 */
	public String searchPattern(String url,String pattern) throws MalformedURLException
	{
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(url);
		try
		{
			m.find();
			return url.substring(m.start(),m.end());
		}
		catch (IllegalStateException e)
		{
			throw new MalformedURLException("Wrong URL");
		}
	}
	/**
	 * Метод проверки на то, что расширение файла на конце найденного url
	 * или отсутствует или является html расширением
	 * @throws MalformedURLException 
	 */
	private void checkExtension() throws MalformedURLException
	{
		String extension = null;
		try {
			 extension = searchPattern(path,EXTENSION_PATTERN);
		} 
		catch (MalformedURLException e) 
		{
			 extension = HTML_EXTENSION;
		}
		finally
		{
			if (!extension.equals(HTML_EXTENSION)) throw new MalformedURLException("Wrong extension");
		}
		
	}
	@Override
	public int hashCode()
	{
		return url.hashCode();
	}
	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		URLDepthPair other = (URLDepthPair) obj;
		if (url.equals(other.url)) return true;
		else return false;
	}
}
