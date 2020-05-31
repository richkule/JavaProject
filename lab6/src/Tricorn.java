import java.awt.geom.Rectangle2D.Double;
/**
 * ����� ����������� ������ ����������� ��� ���������
 * �������� ���� "���������"
 */
public class Tricorn extends FractalGenerator {
    	/** ��������� ������������ ������������ ����������
    	 * �������� ��� ���������� ��������
    	 */
    	public static final int MAX_ITERATIONS = 2000;
    	public Tricorn() {
    		// TODO Auto-generated constructor stub
    	}
    	/** �����, ������������ ������� �����������
    	 * ��������� ��� �������� ������������
    	 */
    	@Override
    	public void getInitialRange(Double range) {
    		range.x = -2;
    		range.y = -2;
    		range.width = 4;
    		range.height = 4;
    	}

    	@Override
    	public int numIterations(double x, double y) {
    		// ������� ��������
    		int iter = 0;
    		// ����������� ���������� c
    		Compl c = new Compl(x,y);
    		// ����������� ���������� ����������, �� ������ �������
    		Compl z = new Compl();
    		while (true)
    		{
    			z.Sopr();
    			z.Squaring();
    			z.AddComlp(c);
    			iter++;
    			if (z.SquarModul()>4) return iter;
    			if (iter>MAX_ITERATIONS) return -1;
    		}
    	}
    	/**
    	 * @return �������� �������� - Tricorn
    	 */
    	public String toString()
    	{
    		return "Tricorn";
    	}
    }
