import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс осуществляющий работу с http запросами с помощью сокета
 */
public class SocketConnection 
{	
	/**
	 * Код ответа сервера
	 */
	public String code;
	/**
	 * Используемый при соеденении протокол
	 */
	private final String PROTOCOL = "HTTP/1.1";
	/**
	 * Время ожидания ответа от сервара в мс.
	 */
	private final int READ_TIMEOUT = 20000;
	/**
	 * Порт по которому идет соединение
	 */
	private final int CONNECTION_PORT = 80;
	/**
	 * PrintWriter, через который отправляются данные на сервер
	 */
	public PrintWriter pw;
	/**
	 * BufferedReader, в которой осуществляется чтение ответа сервера
	 */
	public BufferedReader br;
	/**
	 * domain - домен, к которому ведется подключение
	 */
	public String domain;
	/**
	 * Путь до файла, без доменного имени
	 */
	public String path;
	/**
	 * Сокет с которым ведется связь
	 */
	private Socket sock;

	/**
	 * Содержит заголовки ответа на http запрос
	 */
	public HashMap<String, String>  request;
	/**
	 * Конструктор работающий с объектом URLDepthPair
	 * @param url - Объект URLDepthPair, содержащий данные о подключения к сайту
	 */
	public SocketConnection(URLDepthPair url)
	{
		this.domain = url.domain;
		this.path = url.path;
	}
	/**
	 * @param domain - Доменное имя
	 * @param path - Путь до файла без доменного имени
	 */
	public SocketConnection(String domain, String path)
	{
		this.domain = domain;
		this.path = path;
	}
	/**
	 * @param domain - Доменное имя
	 */
	public SocketConnection(String domain)
	{
		this.domain = domain;
		this.path = "/";
	}
	/**
	 * Метод подключения к серверу
	 * @return true, при удачном подключении
	 */
	public boolean connect()
	{
		try
		{
			this.sock = new Socket(domain,CONNECTION_PORT);
			sock.setSoTimeout(READ_TIMEOUT);
			OutputStream os = sock.getOutputStream();
			this.pw = new PrintWriter(os,true);
			InputStream is = sock.getInputStream();
			InputStreamReader in  = new InputStreamReader(is);
			this.br = new BufferedReader(in);
			return true;
		}
		catch (UnknownHostException e)
		{
			return false;
		}
		catch (IOException e)
		{
			return false;
		}
	}
	/**
	 * Метод отправляющий GET запрос на сервер
	 * @throws IOException 
	 */
	public void sendGET () throws IOException 
	{
		pw.println("GET "+path+" " + PROTOCOL);
		pw.println("Host: "+ domain);
		pw.println("");
		getCode();
	}
	/**
	 * Метод, получающий заголовки ответа
	 * @throws IOException 
	 */
	private void getCode() throws IOException
	{
		String line = br.readLine();
		code = line.substring(PROTOCOL.length()+1,PROTOCOL.length()+4);
		// "Пролистываем не нужные html заголовки"
		while (!(line = br.readLine()).equals(""));
	}
	/**
	 * Метод закрывающий соединение с сокетом
	 * @throws IOException
	 */
	public void close() throws IOException
	{
		this.sock.close();
	}
}
