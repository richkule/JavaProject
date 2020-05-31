/**
 *  ласс возвращающий площадь треугольника, образованного
 * точками в трехмерном пространстве. ¬ходными параметрами €вл€ютс€
 * —троки передающиес€ через командную строку в формате
 * x1 y1 z1 x2 y2 z2 x3 y3 z3, где x,y,z - соответствующие координаты
 * 1,2,3 - соответсвуют номеру точки к которой относитс€ координата
 */
public class Lab1 {
	public static void main(String[] args) {
		/** координата X первой точки **/
		double xCoord1 = Double.parseDouble(args[0]);
		/** координата Y первой точки **/
		double yCoord1 = Double.parseDouble(args[1]);
		/** координата Z первой точки **/
		double zCoord1 = Double.parseDouble(args[2]);
		/** координата X второй точки **/
		double xCoord2 = Double.parseDouble(args[3]);
		/** координата Y второй точки **/
		double yCoord2 = Double.parseDouble(args[4]);
		/** координата Z второй точки **/
		double zCoord2 = Double.parseDouble(args[5]);
		/** координата X третей точки **/
		double xCoord3 = Double.parseDouble(args[6]);
		/** координата Y третей точки **/
		double yCoord3 = Double.parseDouble(args[7]);
		/** координата Z третей точки **/
		double zCoord3 = Double.parseDouble(args[8]);
		/** »нициализаци€ первой точки **/
		Point3d point1 = new Point3d(xCoord1,yCoord1,zCoord1);
		/** »нициализаци€ второй точки **/
		Point3d point2 = new Point3d(xCoord2,yCoord2,zCoord2);
		/** »нициализаци€ третей точки **/
		Point3d point3 = new Point3d(xCoord3,yCoord3,zCoord3);
		// ѕроверка не совпадают ли точки
		if (testEqual(point1,point2,point3)) System.out.println("ѕлощадь треугольника " + computeArea(point1,point2,point3));
		else System.out.println("ƒанные введены некорректно, некоторые точки совпадают");
	}
	/** ћетод вычисл€ющий площадь треугольника по формуле √ерона **/
	public static double computeArea(Point3d point1,Point3d point2,Point3d point3)
	{
		// ¬ычисление длин сторон треугольника
		double a = point1.distanceTo(point2);
		double b = point1.distanceTo(point3);
		double c = point2.distanceTo(point3);
		// ¬ычисление полупериметра
		double p = (a+b+c)/2;
		// ¬ычисление площади треугольника
		return Math.sqrt(p*(p-a)*(p-b)*(p-c));
		
	}
	/** ћетод возвращающий true, если все точки разные, иначе false **/
	public static boolean testEqual(Point3d point1, Point3d point2, Point3d point3)
	{
		if (point1.equals(point2)) return false;
		if (point1.equals(point3)) return false;
		if (point2.equals(point3)) return false;
		return true;
	}

}
