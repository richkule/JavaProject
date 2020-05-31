/**
 * ����� ������������ ������� ������������, �������������
 * ������� � ���������� ������������. �������� ����������� ��������
 * ������ ������������ ����� ��������� ������ � �������
 * x1 y1 z1 x2 y2 z2 x3 y3 z3, ��� x,y,z - ��������������� ����������
 * 1,2,3 - ������������ ������ ����� � ������� ��������� ����������
 */
public class Lab1 {
	public static void main(String[] args) {
		/** ���������� X ������ ����� **/
		double xCoord1 = Double.parseDouble(args[0]);
		/** ���������� Y ������ ����� **/
		double yCoord1 = Double.parseDouble(args[1]);
		/** ���������� Z ������ ����� **/
		double zCoord1 = Double.parseDouble(args[2]);
		/** ���������� X ������ ����� **/
		double xCoord2 = Double.parseDouble(args[3]);
		/** ���������� Y ������ ����� **/
		double yCoord2 = Double.parseDouble(args[4]);
		/** ���������� Z ������ ����� **/
		double zCoord2 = Double.parseDouble(args[5]);
		/** ���������� X ������ ����� **/
		double xCoord3 = Double.parseDouble(args[6]);
		/** ���������� Y ������ ����� **/
		double yCoord3 = Double.parseDouble(args[7]);
		/** ���������� Z ������ ����� **/
		double zCoord3 = Double.parseDouble(args[8]);
		/** ������������� ������ ����� **/
		Point3d point1 = new Point3d(xCoord1,yCoord1,zCoord1);
		/** ������������� ������ ����� **/
		Point3d point2 = new Point3d(xCoord2,yCoord2,zCoord2);
		/** ������������� ������ ����� **/
		Point3d point3 = new Point3d(xCoord3,yCoord3,zCoord3);
		// �������� �� ��������� �� �����
		if (testEqual(point1,point2,point3)) System.out.println("������� ������������ " + computeArea(point1,point2,point3));
		else System.out.println("������ ������� �����������, ��������� ����� ���������");
	}
	/** ����� ����������� ������� ������������ �� ������� ������ **/
	public static double computeArea(Point3d point1,Point3d point2,Point3d point3)
	{
		// ���������� ���� ������ ������������
		double a = point1.distanceTo(point2);
		double b = point1.distanceTo(point3);
		double c = point2.distanceTo(point3);
		// ���������� �������������
		double p = (a+b+c)/2;
		// ���������� ������� ������������
		return Math.sqrt(p*(p-a)*(p-b)*(p-c));
		
	}
	/** ����� ������������ true, ���� ��� ����� ������, ����� false **/
	public static boolean testEqual(Point3d point1, Point3d point2, Point3d point3)
	{
		if (point1.equals(point2)) return false;
		if (point1.equals(point3)) return false;
		if (point2.equals(point3)) return false;
		return true;
	}

}
