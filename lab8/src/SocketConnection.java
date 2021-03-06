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
 * ����� �������������� ������ � http ��������� � ������� ������
 */
public class SocketConnection 
{	
	/**
	 * ��� ������ �������
	 */
	public String code;
	/**
	 * ������������ ��� ���������� ��������
	 */
	private final String PROTOCOL = "HTTP/1.1";
	/**
	 * ����� �������� ������ �� ������� � ��.
	 */
	private int timedout;
	/**
	 * ���� �� �������� ���� ����������
	 */
	private final int CONNECTION_PORT = 80;
	/**
	 * PrintWriter, ����� ������� ������������ ������ �� ������
	 */
	public PrintWriter pw;
	/**
	 * BufferedReader, � ������� �������������� ������ ������ �������
	 */
	public BufferedReader br;
	/**
	 * domain - �����, � �������� ������� �����������
	 */
	public String domain;
	/**
	 * ���� �� �����, ��� ��������� �����
	 */
	public String path;
	/**
	 * ����� � ������� ������� �����
	 */
	private Socket sock;

	/**
	 * �������� ��������� ������ �� http ������
	 */
	public HashMap<String, String>  request;
	/**
	 * ����������� ���������� � �������� URLDepthPair
	 * @param url - ������ URLDepthPair, ���������� ������ � ����������� � �����
	 */
	public SocketConnection(URLDepthPair url, int timedout)
	{
		this.domain = url.domain;
		this.path = url.path;
		this.timedout = timedout;
	}
	/**
	 * @param domain - �������� ���
	 * @param path - ���� �� ����� ��� ��������� �����
	 */
	public SocketConnection(String domain, String path, int timedout)
	{
		this.domain = domain;
		this.path = path;
		this.timedout = timedout;
	}
	/**
	 * @param domain - �������� ���
	 */
	public SocketConnection(String domain, int timedout)
	{
		this.domain = domain;
		this.path = "/";
		this.timedout = timedout;
	}
	/**
	 * ����� ����������� � �������
	 * @return true, ��� ������� �����������
	 */
	public boolean connect()
	{
		try
		{
			this.sock = new Socket(domain,CONNECTION_PORT);
			sock.setSoTimeout(timedout);
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
	 * ����� ������������ GET ������ �� ������
	 * @throws IOException 
	 */
	public void sendGET () throws IOException 
	{
		pw.println("GET "+path+" " + PROTOCOL);
		pw.println("Host: "+ domain);
		pw.println("");
	}
	/**
	 * �����, �������� ���� ����������
	 * @return true, ���� ��� ����� 200 � ���������� �������
	 * ����� false
	 * @throws IOException 
	 */
	public boolean checkConnection()
	{
		try
		{
			String line = br.readLine();
			code = line.substring(PROTOCOL.length()+1,PROTOCOL.length()+4);
			// "������������ �� ������ html ���������"
			while (!(line = br.readLine()).equals(""));
			if(code.equals("200")) return true;
			else return false;
		}
		catch(IOException | IndexOutOfBoundsException | java.lang.NullPointerException e)
		{
			return false;
		}

	}
	/**
	 * ����� ����������� ���������� � �������
	 * @throws IOException
	 */
	public void close() throws IOException
	{
		this.sock.close();
	}
}
