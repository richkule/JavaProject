/**
 * ����� ����������� ���������� ����������
 */
public class Compl
{
	/**
	 * ����������, ����������� �������������� �����
	 */
	private double x;
	/**
	 * ����������, ����������� ������ �����
	 */
	private double iy;
	/**
	 *  ����������� ��� ������� ����������
	 */
	Compl()
	{
		this.x = 0;
		this.iy = 0;
	}
	/**
	 * ����������� ��������� ����������� ����� � ������������
	 * @param x - �������������� �����
	 * @param y - ������ �����
	 */
	Compl(double x, double y)
	{
		this.x = x;
		this.iy = y;
	}
	/**
	 * ����� ��������� �������������� �����
	 */
	public double getX()
	{
		return this.x;
	}
	/**
	 * ����� ��������� ������ �����
	 */
	public double getY()
	{
		return this.iy;
	}
	/**
	 * ����� ���������� ������������ ����� � �������
	 */
	public void squaring()
	{
		double newx = this.x*this.x-this.iy*this.iy;
		this.iy = 2*this.x*this.iy;
		this.x = newx;
	}
	/**
	 * ����� ���������� ����������� ������ ������������ �����
	 */
	public double squarModul()
	{
		return this.x*this.x +this.iy*this.iy;
	}
	/**
	 * ����� �������� ����������� �����
	 * @param add - ������ ���������
	 */
	public void addComlp(Compl add)
	{
		this.x += add.getX();
		this.iy += add.getY();
	}
}