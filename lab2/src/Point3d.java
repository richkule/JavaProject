/**
* ����� ����������� �����.
**/
public class Point3d 
{
	/** ���������� X **/
	private double xCoord;
	/** ���������� Y **/
	private double yCoord;
	/** ���������� Z **/
	private double zCoord;
	/** ����������� ������������� **/
	public Point3d ( double x, double y, double z) 
	{
		xCoord = x;
		yCoord = y;
		zCoord = y;
	}
	/** ����������� �� ���������. **/
	public Point3d () 
	{
	//�������� ����������� � ����� ����������� � ���������� ��������.
		this(0, 0, 0);
	}
	/** ����������� ���������� X **/
	public double getX() 
	{
		return xCoord;
	}
	/** ����������� ���������� Y **/
	public double getY() 
	{
		return yCoord;
	}
	/** ����������� ���������� Z **/
	public double getZ() 
	{
		return zCoord;
	}
	/** ��������� �������� ���������� X. **/
	public void setX ( double val) 
	{
		xCoord = val;
	}
	/** ��������� �������� ���������� Y. **/
	public void setY (double val) 
	{
		yCoord = val;
	}
	/** ��������� �������� ���������� Z. **/
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
