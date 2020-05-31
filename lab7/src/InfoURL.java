import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ����� ���������� ���������� � URL ��������
 */
public class InfoURL {
	/**
	 * url ������ ��������
	 */
	private String url;
	/**
	 * �������� ���������� ���������� ���� ���� ������ 2 
	 */
	private HashMap<String,Integer> wordMapCount = new HashMap<String,Integer>();
	/**
	 * ���������� ���� ����� ������ 2
	 */
	private int wordCount = 0;
	/**
	 * ���������� ������ �� ������ ������� �� ������ ����������
	 */
	private int quoteCount = 1;
	/**
	 * ���������� ������ �� url ������ ��������
	 */
	private int hrefCount = 0;
	/**
	 * ���������� ���������� �� ��������
	 */
	private int hCount = 0;
	/**
	 * "������ �������������" - ������ �������� ����������� �� ��������������� ������
	 */
	private double repeatIndex = 0;
	/**
	 * "������ ������" - ������ �������� ����������� �� ���������� ������ �� ������ ���������
	 * ������ ��������
	 */
	private double hrefIndex = 0;
	/**
	 * "������ ����������" - ������ �������� ����������� �� ���������� ����������
	 */
	private double hIndex = 0;
	/**
	 * "������ ��������" - ������ �������� ����������� �� �������� ���������� �������� �� ������ ��������
	 */
	private double contentIndex = 0;
	/**
	 * �������� ������ ��������
	 */
	private String title = "";
	/**
	 * �������� ������ ��������
	 */
	private String description = "";
	/**
	 * ����, ����������� ��� �������� �������� ��� �� �������
	 */
	private boolean readDescription = true;
	/**
	 * ����, ����������� ��� ������ ������ ������ ��������� ������ �������� ��������
	 */
	private boolean isReadDescription = false;
	/**
	 * ����, ����������� ��� ������ ������ ������ ��������� ������ ���� <style>
	 */
	private boolean isReadStyle = false;
	/**
	 * ����, ����������� ��� ������ ������ ��������� ������ ���� script
	 */
	private boolean isReadScript = false;
	/**
	 * ����, ���������� ��� ��������� �������� ��� �� ������
	 */
	private boolean readTitle = true;
	/**
	 * ������������ ������������� ����, ����� ������� � ������ �������������
	 */
	private final int REPEAT_COUNT= 5;
	/**
	 * ����������� �������� ���� � ������ ��������������
	 */
	private final int REPEAT_IMPORTANT= 1000;
	/**
	 * ����������� �������� ������ ��� ������ �������
	 */
	private final int HREF_IMPORTANT= 10000;
	/**
	 * ����������� �������� ���������� ��� ������ �������
	 */
	private final int H_IMPORTANT = 50000;
	/**
	 * ������ � ������ ��� ��������� ����������� title
	 */
	private final int START_TITLE = 7;
	/**
	 * ������ � ����� ��� ��������� ����������� title
	 */
	private final int TOEND_TITLE = 8;
	/**
	 * ������ ���������� ��������� ��� ������ ���� ���� � ���������
	 */
	public final String DESCPRITION_PATTERN = "(?i)(<meta.*?name=\"description\".*?content=\")";
	/**
	 * ������ ���������� ��������� ��� ������ ���� ���� � ����������
	 */
	public final String TITLE_PATTERN = "(?i)(<title>).*?(</title>)";
	/**
	 * ������ ���������� ��������� ��� ������ �����, ��� �� ����������� � �����
	 * script � �� ����������
	 */
	public final String UNTAG_PATTERN = "(?i)(<script.*?>.*?</script>)|(<(?!script).*?>)";
	/**
	 * ������ ���������� ��������� ��� ������ ������������ ���� script
	 */
	public final String SCRIPT_OPEN_PATTERN = "(?i)<script.*?>.*";
	/**
	 * ������ ���������� ��������� ��� ������ ������������ ���� sciprt
	 */
	public final String SCRIPT_CLOSE_PATTERN = "(?i)</script>";
	/**
	 * ������ ���������� ��������� ��� ������ ������������ ���� style
	 */
	public final String STYLE_OPEN_PATTERN = "(?i)<style.*?>";
	/**
	 * ������ ���������� ��������� ��� ������ ���� ����� � ����������
	 */
	public final String STYLE_PATTERN = "(?i)<style.*?>.*?</style>";
	/**
	 * ������ ���������� ��������� ��� ������ ������������ ���� style
	 */
	public final String STYLE_CLOSE_PATTERN = "(?i)</style>";
	/**
	 * ������ ���������� ��������� ��� ������ ����������
	 */
	public final String H_TAG_PATTERN = "(?i)<h[1-6]>";
	/**
	 * ������ ���������� ��������� ��� ������ ����
	 */
	public final String WORD_PATTERN = "[^�-��-�a-zA-Z]";
	/**
	 * ���������������� ������ ��� ������ ���� ���� � ���������
	 */
	private final Pattern DESCPRITION_PATTERN_COMPILE = Pattern.compile(DESCPRITION_PATTERN);
	/**
	 * ���������������� ������ ��� ������ ���� ���� � ����������
	 */
	private final Pattern TITLE_PATTERN_COMPILE = Pattern.compile(TITLE_PATTERN);
	/**
	 * ���������������� ������ ��� ������ �����, ��� �� ����������� � �����
	 * script � �� ����������
	 */
	private final Pattern UNTAG_PATTERN_COMPILE = Pattern.compile(UNTAG_PATTERN);
	/**
	 * ���������������� ������ ��� ������ ������������ ���� script
	 */
	private final Pattern SCRIPT_CLOSE_PATTERN_COMPILE = Pattern.compile(SCRIPT_CLOSE_PATTERN);
	/**
	 * ���������������� ������ ��� ������ ������������ ���� script
	 */
	private final Pattern SCRIPT_OPEN_PATTERN_COMPILE = Pattern.compile(SCRIPT_OPEN_PATTERN);	
	/**
	 * ���������������� ������ ��� ������ ������������ ���� style
	 */
	private final Pattern STYLE_CLOSE_PATTERN_COMPILE = Pattern.compile(STYLE_CLOSE_PATTERN);
	/**
	 * ���������������� ������ ��� ������ ������������ ���� style
	 */
	private final Pattern STYLE_OPEN_PATTERN_COMPILE = Pattern.compile(STYLE_OPEN_PATTERN);
	/**
	 * ���������������� ������ ��� ������ ���� style � ����������
	 */
	private final Pattern STYLE_PATTERN_COMPILE = Pattern.compile(STYLE_PATTERN);
	/**
	 * ���������������� ������ ��� ������ ����������
	 */
	private final Pattern H_TAG_PATTERN_COMPILE = Pattern.compile(H_TAG_PATTERN);
	/**
	 * ���������������� ������ ��� ������ ����
	 */
	private final Pattern WORD_PATTERN_COMPILE = Pattern.compile(WORD_PATTERN);
	
	/**
	 * ����������� ������
	 * @param url - url ������� �����
	 */
	public InfoURL(String url)
	{
		this.url = url;
	}
	
	/**
	 * ����� ����������� ���������� "�����������" ������ �������� �������
	 */
	public void addQuoute()
	{
		quoteCount+=1;
	}
	/**
	 * ����� ����������� ���������� ������ �� url �� ������ ��������
	 */
	public void addHref()
	{
		hrefCount+=1;
	}
	/**
	 * ����� ������������ ������� �������� ��������
	 * @param line - ������������ ������ ��������
	 */
	private void readDescription(String line)
	{
		if (this.readDescription)
		{
			Matcher m = this.DESCPRITION_PATTERN_COMPILE.matcher(line);
			try
			{
				m.find();
				line = line.substring(m.end());
				this.isReadDescription = true;
				this.readDescription = false;
			}
			catch (IllegalStateException e)
			{
			}
		}
		if (isReadDescription) 
		{
			int index = line.indexOf("\"");
			if (index == -1)
			{
				this.description+=line;
			}
			else
			{
				this.description += line.substring(0, index);
				this.isReadDescription = false;
			}
		}
	}
	/**
	 * ����� ������������ ������� ��������� title ��������
	 * @param line - ������������ ������ ��������
	 */
	private void readTitle(String line)
	{
		if (this.readTitle)
		{
			Matcher m = this.TITLE_PATTERN_COMPILE.matcher(line);
			try
			{
				m.find();
				this.title = line.substring(m.start()+this.START_TITLE,m.end()-this.TOEND_TITLE);
				this.readTitle = false;
			}
			catch (IllegalStateException e)
			{
			}
		}
	}
	/**
	 * ����� ������������ ������� ���������� �� ��������
	 * @param line - ������������ ������ ��������
	 */
	private void parseH(String line)
	{
		Matcher m = this.H_TAG_PATTERN_COMPILE.matcher(line);
		while(m.find()) 
		{
	         this.hCount++;
	    }
	}
	/**
	 * ����� ������������ ������� ������������� ����
	 * @param line - ������������ ������ ��������
	 */
	private void parseWord(String line)
	{
		if (this.isReadScript)
		{
			Matcher m = this.SCRIPT_CLOSE_PATTERN_COMPILE.matcher(line);;
			try
			{
				m.find();
				line = line.substring(m.end());
				this.isReadScript = false;
			}
			catch (IllegalStateException e)
			{
			}
		}
		if(!this.isReadScript)
		{
			Matcher m = this.UNTAG_PATTERN_COMPILE.matcher(line);
			String parseLine = m.replaceAll(" ");
			m = this.SCRIPT_OPEN_PATTERN_COMPILE.matcher(parseLine);
			if (m.find())
			{
				parseLine = m.replaceFirst("");
				this.isReadScript = true;
			}
			m = this.WORD_PATTERN_COMPILE.matcher(parseLine);
			parseLine = m.replaceAll(" ");
			for (String var : parseLine.split(" "))
			{
				if(var.length() > 2)
				{
					if(wordMapCount.get(var)!=null) wordMapCount.put(var, wordMapCount.get(var)+1);
					else wordMapCount.put(var, 1);
					wordCount++;
				}
			}
		}
	}
	/**
	 * ����� ��� �������� ���� style �� ������ ��� ������������� ������ ������
	 * ���� ��� �� ��� �� �������������
	 * @param line - ������������ ������
	 * @return ����� ������ ��� ���� style
	 */
	private String readStyle(String line)
	{
		if (this.isReadStyle)
		{
			Matcher m = this.STYLE_CLOSE_PATTERN_COMPILE.matcher(line);
			try
			{
				m.find();
				line = line.substring(m.end());
				this.isReadStyle = false;
			}
			catch (IllegalStateException e)
			{
			}
		}
		if(!this.isReadScript)
		{
			Matcher m = this.STYLE_PATTERN_COMPILE.matcher(line);
			String parseLine = m.replaceAll(" ");
			m = this.STYLE_OPEN_PATTERN_COMPILE.matcher(parseLine);
			if (m.find())
			{
				parseLine = m.replaceFirst("");
				this.isReadScript = true;
			}
		}
		return line;
	}
	/**
	 * ����� �������������� ������� ���������� ��������
	 */
	private void calculateIndex()
	{
		if(wordCount > 0)
		{
			int sum = 0;
			for (Integer var : wordMapCount.values())
			{
				if(var<this.REPEAT_COUNT)
				{
					sum+=var;
				}
			}
			if(sum>0)
			{
				repeatIndex = this.REPEAT_IMPORTANT*sum/this.wordCount;
			}
			hrefIndex = this.HREF_IMPORTANT*this.hrefCount/this.wordCount;
			hIndex =  this.H_IMPORTANT*this.hCount/this.wordCount;
			contentIndex  = repeatIndex+hrefIndex+hIndex;	
		}
	}
	/**
	 * ����� �������� ������������ ������
	 * @param line - ������������ ������ ��������
	 */
	public void readLine(String line)
	{
		line = readStyle(line);
		if(!this.isReadStyle)
		{
			readDescription(line);
			readTitle(line);
			parseH(line);
			parseWord(line);
			calculateIndex();
		}
	}
	/**
	 * @return ������ �������������
	 */
	public double getRepeatIndex()
	{
		return this.repeatIndex;
	}
	/**
	 * @return ������ ������
	 */
	public double getHrefIndex()
	{
		return this.hrefIndex;
	}
	/**
	 * @return ������ ����������
	 */
	public double getHIndex()
	{
		return this.hIndex;
	}
	/**
	 * @return ������ ����������� ��������
	 */
	public double getContentIndex()
	{
		return this.contentIndex;
	}
	/**
	 * @return ���������� "�����������" ��������
	 */
	public int getQuoteCount()
	{
		return this.quoteCount;
	}
	/**
	 * @return ��������� ��������
	 */
	public String getTitle()
	{
		return this.title;
	}
	/**
	 * @return �������� ��������
	 */
	public String getDescription()
	{
		return this.description;
	}
	/**
	 * @return url ��������
	 */
	public String getURL()
	{
		return this.url;
	}
}
