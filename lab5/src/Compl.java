/**
 * Класс описывающий коплексную переменную
 */
public class Compl
{
	/**
	 * Переменная, описывающая действительную часть
	 */
	private double x;
	/**
	 * Переменная, описывающая мнимую часть
	 */
	private double iy;
	/**
	 *  Конструктор без входных параметров
	 */
	Compl()
	{
		this.x = 0;
		this.iy = 0;
	}
	/**
	 * Конструктор создающий комплексную точку в пространстве
	 * @param x - действительная часть
	 * @param y - мнимая часть
	 */
	Compl(double x, double y)
	{
		this.x = x;
		this.iy = y;
	}
	/**
	 * Метод получения действительной части
	 */
	public double getX()
	{
		return this.x;
	}
	/**
	 * Метод получения мнимой части
	 */
	public double getY()
	{
		return this.iy;
	}
	/**
	 * Метод возведения комплексного числа в квадрат
	 */
	public void Squaring()
	{
		double newx = this.x*this.x-this.iy*this.iy;
		this.iy = 2*this.x*this.iy;
		this.x = newx;
	}
	/**
	 * Метод вычисления квадратного модуля комплексного числа
	 */
	public double SquarModul()
	{
		return this.x*this.x +this.iy*this.iy;
	}
	/**
	 * Метод сложения комплексных чисел
	 * @param add - второй слагаемое
	 */
	public void AddComlp(Compl add)
	{
		this.x += add.getX();
		this.iy += add.getY();
	}
	/**
	 * Метода реализующий комплексное сопряжение числа
	 */
	public void Sopr()
	{
		this.iy = -this.iy;
	}
	/**
	 * Метод изменяющий мнимиую и действительную часть
	 * на их абсолютное значение
	 */
	public void Abs()
	{
		this.iy = Math.abs(iy);
		this.x = Math.abs(x);
	}
}