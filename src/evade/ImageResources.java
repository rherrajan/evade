package evade;

import java.io.IOException;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

public class ImageResources {

	public static class ImageResource{
		public final Image image;
		public final int width;
		public final int height;
		
		public ImageResource(String imgName, int width, int height) {
			this.image = loadImage(imgName);
			this.width = width;
			this.height = height;
		}
		
		private static Image loadImage(String filename) {
			Image i = null;
			try {
				i = Image.createImage(filename);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return i;
		}
		
		public Sprite asSprite(){
			return new Sprite(image, width, height);
		}
	}
	
	public static final ImageResource ball_blue = new ImageResource("/ball_blue.png", 18, 18);
	public static final ImageResource ball_red = new ImageResource("/ball_red.png", 18, 18);
	public static final ImageResource ball_deep_red = new ImageResource("/ball_deep_red.png", 18, 18);
	public static final ImageResource ball_green = new ImageResource("/ball_green.png", 18, 18);

	public static final ImageResource ball_black = new ImageResource("/ball_black.png", 18, 18);
	public static final ImageResource eyeIMG = new ImageResource("/eye.png", 9, 9);

	public static final ImageResource ring = new ImageResource("/ring_black.png", 18, 18);
	
	public static final ImageResource background = new ImageResource("/background.png", 200, 320);
	public static final ImageResource foreground = new ImageResource("/foreground.png", 285, 320);
	
	public static final ImageResource playerSingle = new ImageResource("/Maria.png", 32, 48);
	
	
	public static final ImageResource playerServer = new ImageResource("/Slade.png", 32, 48);
	public static final ImageResource playerServerHit = new ImageResource("/Slade_ghost.png", 32, 48);
	public static final ImageResource playerClient = new ImageResource("/Gilian.png", 32, 48);
	public static final ImageResource playerClientHit = new ImageResource("/Gilian_ghost.png", 32, 48);

	

	
}
