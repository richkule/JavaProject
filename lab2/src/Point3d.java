/**
* класс трехменрной точки.
**/
public class Point3d 
{
	/** координата X **/
	private double xCoord;
	/** координата Y **/
	private double yCoord;
	/** координата Z **/
	private double zCoord;
	/** Конструктор инициализации **/
	public Point3d ( double x, double y, double z) 
	{
		xCoord = x;
		yCoord = y;
		zCoord = y;
	}
	/** Конструктор по умолчанию. **/
	public Point3d () 
	{
	//Вызовите конструктор с тремя параметрами и определите источник.
		this(0, 0, 0);
	}
	/** Возвращение координаты X **/
	public double getX() 
	{
		return xCoord;
	}
	/** Возвращение координаты Y **/
	public double getY() 
	{
		return yCoord;
	}
	/** Возвращение координаты Z **/
	public double getZ() 
	{
		return zCoord;
	}
	/** Установка значения координаты X. **/
	public void setX ( double val) 
	{
		xCoord = val;
	}
	/** Установка значения координаты Y. **/
	public void setY (double val) 
	{
		yCoord = val;
	}
	/** Установка значения координаты Z. **/
	public void setZ (double val) 
	{
		zCoord = val;
	}
	 public boolean equals(Point3d val)
	 {
		 if (this.xCoord!= val.getX()) return false;
		 if (this.yCoord!= val.getY()) return false;
		 if (this.zCoord!= val.getZ()) return false;
		 return true;
	 }
	 public double distanceTo(Point3d val)
	 {
		 return Math.sqrt(Math.pow(this.xCoord-val.getX(),2)+Math.pow(this.yCoord-val.getY(),2)+Math.pow(this.zCoord-val.getZ(),2));
	 }
}
