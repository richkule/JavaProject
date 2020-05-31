import java.awt.geom.Rectangle2D.Double;
/**
 * Класс реализующий методы необходимые для генерации
 * фрактала Мандельброта
 */
public class Mandelbrot extends FractalGenerator {
    	/** Константа определяющая максимальное количество
    	 * итераций при вычислении фрактала
    	 */
    	public static final int MAX_ITERATIONS = 2000;
    	public Mandelbrot() {
    		// TODO Auto-generated constructor stub
    	}
    	/** Метод, определяющий область комплексной
    	 * плоскости для фрактала Мандельброта
    	 */
    	@Override
    	public void getInitialRange(Double range) {
    		range.x = -2;
    		range.y = -1.5;
    		range.width = 3;
    		range.height = 3;
    	}

    	@Override
    	public int numIterations(double x, double y) {
    		// Счетчик итераций
    		int iter = 0;
    		// Комлпексная переменная c
    		Compl c = new Compl(x,y);
    		// Комплексная переменная изменяемая, на каждой итераци
    		Compl z = new Compl();
    		while (true)
    		{
    			z.Squaring();
    			z.AddComlp(c);
    			iter++;
    			if (z.SquarModul()>4) return iter;
    			if (iter>MAX_ITERATIONS) return -1;
    		}
    	}
    	/**
    	 * @return Название фрактала - Mandelbrot
    	 */
    	public String toString()
    	{
    		return "Mandelbrot";
    	}
    }
