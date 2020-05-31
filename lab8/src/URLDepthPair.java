import java.net.MalformedURLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ����� ����������� ���� URL:Depth
 * URL - ����� �������� url
 * Depth - ������� ������, �� ������� ������� ������ ��������
 */
public class URLDepthPair 
{
	/**
	 * ������ ������ ������ � � ������ ���� 'href="URL" ' (� ����� �������� �� ����� ����� �������)
	 */
	public static final int START_HREF = 6;
	/**
	 * ���������� �������� �� ����� ������ �� ����� ������ � ������ ���� 'href="URL" '  (� ����� �������� �� ����� ����� �������)
	 */
	public static final int HREF_TOEND = 1;
	/**
	 * HTML ����������
	 */
	public static final String HTML_EXTENSION = ".html";
	/**
	 * ������� ������� � URL HTTP
	 */
	public static final String URL_PREFIX_HTTP = "http://";
	/**
	 * ������ ���������� ���������, ���������� ��� ������ ��������� http:// https:// 
	 */
	public static final String PREFIX_PATTERN = "(http://)";
	/**
	 * ������ ���������� ��������� ��������������� �������� ������
	 */
	public static final String DOMAIN_PATTERN = "([\\da-z�-�\\.-]+)\\.([a-z�-�\\.]{2,6})";
	/**
	 * ������ ���������� ���������, �������������� ����� �� �����
	 */
	public static final String PATH_PATTERN = "([/\\w�-�\\.-]*)*\\/?";
	/**
	 * ������ ���������� ���������, ������������ ���������� �����
	 */
	public static final String EXTENSION_PATTERN = "\\.[\\w�-�\\-]+$";
	/**
	 * ������ ���������� ���������, ��������������� url ������
	 */
	public static final String URL_PATTERN = PREFIX_PATTERN+DOMAIN_PATTERN+PATH_PATTERN;
	/**
	 * ���������� ���������� url
	 */
	public String url;
	/**
	 * ���������� ���������� ������� �������
	 */
	public String prefix;
	/**
	 * ���������� ���������� �������� ���
	 */
	public String domain;
	/**
	 * ���������� ���������� ����
	 */
	public String path;
	/**
	 * ���������� ���������� �������
	 */
	public int depth;
	/**
	 * ����������� ��� ������ ���������� url
	 * @param url - �������� ������, ���������� url
	 * @param depth - �������
	 * @throws MalformedURLException - ������ ������ �� �������� url
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
	 * ����������� ��� ������ ���� 'href="URL" '(� ����� �������� �� ����� ����� �������)
	 * @param href - ������������ ������
	 * @param depth - ������� ������, ��� ������� ��� �������
	 * @param domain - �����, �� ������� ������� ������� ������ ������
	 * @throws MalformedURLException - � ������, ���� ������ �� �������� ������ ��� ��������� ������
	 */
	public URLDepthPair(String href, int depth, String domain) throws MalformedURLException
	{
		String hrefContent = href.substring(START_HREF,href.length()-HREF_TOEND);
		// ���� ������ �������� ������ URL ������
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
		// ���� ������ ����� ��� ����
		catch (MalformedURLException e)
		{
			// �������� ������������ ������
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
	 * ����� �������� �������� �� ������ ������� url
	 * @param url - ����������� ������
	 */
	public boolean testURL(String url)
	{
		return Pattern.matches(URL_PATTERN, url);
	}
	/**
	 * ����� ������ URL �� �������� ������
	 * @param url - ������� ������
	 * @return String - ��������� ���������, ���������� url, ����� null
	 * @throws MalformedURLException - ������ ������ �� �������� url
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
	 * ����� �������� �� ��, ��� ���������� ����� �� ����� ���������� url
	 * ��� ����������� ��� �������� html �����������
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
