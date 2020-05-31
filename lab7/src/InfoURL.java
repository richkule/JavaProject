import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс собирающий информацию о URL странице
 */
public class InfoURL {
	/**
	 * url данной страницы
	 */
	private String url;
	/**
	 * Содержит количество повторений слов длин больше 2 
	 */
	private HashMap<String,Integer> wordMapCount = new HashMap<String,Integer>();
	/**
	 * Количество слов длины больше 2
	 */
	private int wordCount = 0;
	/**
	 * Количество ссылок на данную страниц из других источников
	 */
	private int quoteCount = 1;
	/**
	 * Количество ссылок на url внутри страницы
	 */
	private int hrefCount = 0;
	/**
	 * Количество заголовков на странице
	 */
	private int hCount = 0;
	/**
	 * "Индекс повторяемости" - индекс косвенно указывающий на неповторяемость текста
	 */
	private double repeatIndex = 0;
	/**
	 * "Индекс ссылок" - Индекс косвенно указывающий на количество ссылок на другие источники
	 * внутра страницы
	 */
	private double hrefIndex = 0;
	/**
	 * "Индекс заголовков" - Индекс косвенно указывающий на количество заголовков
	 */
	private double hIndex = 0;
	/**
	 * "Индекс контента" - Индекс косвенно указывающий на качество содержания контента на данной странице
	 */
	private double contentIndex = 0;
	/**
	 * Заголвок данной страницы
	 */
	private String title = "";
	/**
	 * Описание данной страницы
	 */
	private String description = "";
	/**
	 * Флаг, указывающий что описание страницы еще не найдено
	 */
	private boolean readDescription = true;
	/**
	 * Флаг, указывающий что начало данной строки находится внутри метатега описания
	 */
	private boolean isReadDescription = false;
	/**
	 * Флаг, указывающий что начало данной строки находится внутри тега <style>
	 */
	private boolean isReadStyle = false;
	/**
	 * Флаг, указывающий что начало строки находится внутри тега script
	 */
	private boolean isReadScript = false;
	/**
	 * Флаг, укаывающий что заголовок страницы еще не найден
	 */
	private boolean readTitle = true;
	/**
	 * Максимальная повторяемость слов, чтобы попасть в список неповторяемых
	 */
	private final int REPEAT_COUNT= 5;
	/**
	 * Коэффициент важности слов с низкой повторяемостью
	 */
	private final int REPEAT_IMPORTANT= 1000;
	/**
	 * Коэффициент важности ссылок для общего индекса
	 */
	private final int HREF_IMPORTANT= 10000;
	/**
	 * Коэффициент важности заголовков для общего индекса
	 */
	private final int H_IMPORTANT = 50000;
	/**
	 * Отступ в начале для получения содержимого title
	 */
	private final int START_TITLE = 7;
	/**
	 * Отступ в конце для получения содержимого title
	 */
	private final int TOEND_TITLE = 8;
	/**
	 * Шаблон регулярных выражений для поиска мета тега с описанием
	 */
	public final String DESCPRITION_PATTERN = "(?i)(<meta.*?name=\"description\".*?content=\")";
	/**
	 * Шаблон регулярных выражений для поиска мета тега с заголовком
	 */
	public final String TITLE_PATTERN = "(?i)(<title>).*?(</title>)";
	/**
	 * Шаблон регулярных выражений для поиска тегов, без их содержимого и тегов
	 * script с их содержимым
	 */
	public final String UNTAG_PATTERN = "(?i)(<script.*?>.*?</script>)|(<(?!script).*?>)";
	/**
	 * Шаблон регулярных выражений для поиска открывающего тега script
	 */
	public final String SCRIPT_OPEN_PATTERN = "(?i)<script.*?>.*";
	/**
	 * Шаблон регулярных выражения для поиска закрывающего тега sciprt
	 */
	public final String SCRIPT_CLOSE_PATTERN = "(?i)</script>";
	/**
	 * Шаблон регулярных выражений для поиска открывающего тега style
	 */
	public final String STYLE_OPEN_PATTERN = "(?i)<style.*?>";
	/**
	 * Шаблон регулярных выражений для поиска тега стайл с содержимым
	 */
	public final String STYLE_PATTERN = "(?i)<style.*?>.*?</style>";
	/**
	 * Шаблон регулярных выражения для поиска закрывающего тега style
	 */
	public final String STYLE_CLOSE_PATTERN = "(?i)</style>";
	/**
	 * Шаблон регулярных выражений для поиска заголовков
	 */
	public final String H_TAG_PATTERN = "(?i)<h[1-6]>";
	/**
	 * Шаблон регулярных выражения для поиска слов
	 */
	public final String WORD_PATTERN = "[^а-яА-Яa-zA-Z]";
	/**
	 * Скомпилированный шаблон для поиска мета тега с описанием
	 */
	private final Pattern DESCPRITION_PATTERN_COMPILE = Pattern.compile(DESCPRITION_PATTERN);
	/**
	 * Скомпилированный шаблон для поиска мета тега с заголовком
	 */
	private final Pattern TITLE_PATTERN_COMPILE = Pattern.compile(TITLE_PATTERN);
	/**
	 * Скомпилированный шаблон для поиска тегов, без их содержимого и тегов
	 * script с их содержимым
	 */
	private final Pattern UNTAG_PATTERN_COMPILE = Pattern.compile(UNTAG_PATTERN);
	/**
	 * Скомпилированный шаблон для поиска открывающего тега script
	 */
	private final Pattern SCRIPT_CLOSE_PATTERN_COMPILE = Pattern.compile(SCRIPT_CLOSE_PATTERN);
	/**
	 * Скомпилированный шаблон для поиска закрывающего тега script
	 */
	private final Pattern SCRIPT_OPEN_PATTERN_COMPILE = Pattern.compile(SCRIPT_OPEN_PATTERN);	
	/**
	 * Скомпилированный шаблон для поиска открывающего тега style
	 */
	private final Pattern STYLE_CLOSE_PATTERN_COMPILE = Pattern.compile(STYLE_CLOSE_PATTERN);
	/**
	 * Скомпилированный шаблон для поиска закрывающего тега style
	 */
	private final Pattern STYLE_OPEN_PATTERN_COMPILE = Pattern.compile(STYLE_OPEN_PATTERN);
	/**
	 * Скомпилированный шаблон для поиска тега style с содержимым
	 */
	private final Pattern STYLE_PATTERN_COMPILE = Pattern.compile(STYLE_PATTERN);
	/**
	 * Скомпилированный шаблон для поиска заголовков
	 */
	private final Pattern H_TAG_PATTERN_COMPILE = Pattern.compile(H_TAG_PATTERN);
	/**
	 * Скомпилированный шаблон для поиска слов
	 */
	private final Pattern WORD_PATTERN_COMPILE = Pattern.compile(WORD_PATTERN);
	
	/**
	 * Конструктор класса
	 * @param url - url данного сайта
	 */
	public InfoURL(String url)
	{
		this.url = url;
	}
	
	/**
	 * Метод добавляющий количество "цитирования" данной страницы другими
	 */
	public void addQuoute()
	{
		quoteCount+=1;
	}
	/**
	 * Метод добавляющий количество ссылок на url на данной странице
	 */
	public void addHref()
	{
		hrefCount+=1;
	}
	/**
	 * Метод занимающийся поиском описания страницы
	 * @param line - передаваемая строка страницы
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
	 * Метод занимающийся поиском заголовка title страницы
	 * @param line - передаваемая строка страницы
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
	 * Метод занимающийся поиском заголовков на странице
	 * @param line - передаваемая строка страницы
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
	 * Метод занимающийся поиском повторяемости слов
	 * @param line - передаваемая строка страницы
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
	 * Метод для удаления тега style из строки или игнорирования данной строки
	 * если тег на ней не заканчивается
	 * @param line - передаваемая строка
	 * @return новая строка без тега style
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
	 * Метод рассчитывающий индексы содержания страницы
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
	 * Метод читающий передаваемую строку
	 * @param line - передаваемая строка страницы
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
	 * @return Индекс повторяемости
	 */
	public double getRepeatIndex()
	{
		return this.repeatIndex;
	}
	/**
	 * @return Индекс ссылок
	 */
	public double getHrefIndex()
	{
		return this.hrefIndex;
	}
	/**
	 * @return Индекс заголовков
	 */
	public double getHIndex()
	{
		return this.hIndex;
	}
	/**
	 * @return Индекс содержимого страницы
	 */
	public double getContentIndex()
	{
		return this.contentIndex;
	}
	/**
	 * @return Количество "цитирования" страницы
	 */
	public int getQuoteCount()
	{
		return this.quoteCount;
	}
	/**
	 * @return Заголовок страницы
	 */
	public String getTitle()
	{
		return this.title;
	}
	/**
	 * @return Описание страницы
	 */
	public String getDescription()
	{
		return this.description;
	}
	/**
	 * @return url страницы
	 */
	public String getURL()
	{
		return this.url;
	}
}
