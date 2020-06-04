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
	 * ���������� html �����
	 */
	public static final String HTML_EXTENSION = ".html";
	/**
	 * ���������� ����������
	 */
	public static final String ACCEPTABLE_EXTENSION_PATTERN = "(.html?)|(.php)";
	/**
	 * ������� ������� � URL HTTP
	 */
	public static final String URL_PREFIX_HTTP = "http://";
	/**
	 * ������ ���������� ���������, ���������� ��� ������ ��������� http://
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
	 * ��������������� ������ ���������� ����������
	 */
	public  final Pattern ACCEPTABLE_EXTENSION_PATTERN_COMPILE = Pattern.compile(ACCEPTABLE_EXTENSION_PATTERN);
	/**
	 * ���������������� ������ ���������� ���������, ���������� ��� ������ ��������� http://
	 */
	public  final Pattern PREFIX_PATTERN_COMPILE = Pattern.compile(PREFIX_PATTERN);
	/**
	 * ���������������� ������ ���������� ��������� ��������������� �������� ������
	 */
	public  final Pattern DOMAIN_PATTERN_COMPILE = Pattern.compile(DOMAIN_PATTERN);
	/**
	 * ���������������� ������ ���������� ���������, �������������� ����� �� �����
	 */
	public  final Pattern PATH_PATTERN_COMPILE = Pattern.compile(PATH_PATTERN);
	/**
	 * ���������������� ������  ���������� ���������, ������������ ���������� �����
	 */
	public  final Pattern EXTENSION_PATTERN_COMPILE = Pattern.compile(EXTENSION_PATTERN);
	/**
	 * ���������������� ������ ���������� ���������, ��������������� url ������
	 */
	public  final Pattern URL_PATTERN_COMPILE = Pattern.compile( URL_PATTERN);
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
		if (URL_PATTERN_COMPILE.matcher(url).matches()) this.url= url;
		else this.url = searchPattern(url,URL_PATTERN_COMPILE); 
		Pattern p = this.DOMAIN_PATTERN_COMPILE;
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
			this.url = searchPattern(hrefContent,URL_PATTERN_COMPILE);
			Pattern p = this.DOMAIN_PATTERN_COMPILE;
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
			if (!DOMAIN_PATTERN_COMPILE.matcher(domain).matches()) throw new MalformedURLException("Wrong URL");
			hrefContent = searchPattern(hrefContent,PATH_PATTERN_COMPILE);
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
	 * ����� ������ ��������� ��������������� �������
	 * @param url - ������� ������
	 * @param pattern - ������� ������
	 * @return String - ��������� ���������, ���������� url, ����� null
	 * @throws MalformedURLException - ������ ������ �� �������� url
	 */
	public String searchPattern(String url,Pattern p) throws MalformedURLException
	{
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
	 * ��� ����������� ��� �������� ����������, � ��������� ������ ������ ������
	 * @throws MalformedURLException 
	 */
	private void checkExtension() throws MalformedURLException
	{
		String extension = null;
		try {
			 extension = searchPattern(path,EXTENSION_PATTERN_COMPILE);
		} 
		catch (MalformedURLException e) 
		{
			 extension = HTML_EXTENSION;
		}
		finally
		{
			if (!ACCEPTABLE_EXTENSION_PATTERN_COMPILE.matcher(extension).matches()) throw new MalformedURLException("Wrong extension");
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
