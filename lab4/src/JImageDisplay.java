
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
/** 
 * Класс для управления изображением на форме Swing
 */
public class JImageDisplay extends JComponent {
	/** Экземпляр управляемого изображения **/
	private BufferedImage bImage;
	/** Конструктор, принимает значения
	 * int width - ширина изображения
	 * int height - высота изображения
	 */
	public JImageDisplay(int width, int height) {
		this.bImage = new BufferedImage(width,height,1);
		super.setPreferredSize(new java.awt.Dimension(width,height));
		
	}
	/** Переопределение родительского метода,
	 * Выводит на экран данные изображения
	 */
	 @Override
	public void paintComponent(Graphics g)
	{	
		 super.paintComponents(g);
		 g.drawImage(this.bImage, 0, 0, this.bImage.getWidth(), this.bImage.getHeight(), null);
	}
	 /** Метод, очищающий изображение
	  * Устанавливая все пиксели в черный цвет
	  */
	 public void clearImage()
	 {
		 for(int x = 0;x<this.bImage.getWidth();x++)
		 {
			 for (int y = 0;y<this.bImage.getHeight();y++)
			 {
				 this.bImage.setRGB(x, y, 0);
			 }
		 }
	 }
	 /** Метод устанавливающий пиксель в определенный цвет**/
	 public void drawPixel(int x, int y, int rgbColor)
	 {
		 this.bImage.setRGB(x, y, rgbColor);
		 
	 }
}