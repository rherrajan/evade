package evade;

import javax.microedition.lcdui.game.Sprite;

import evade.ImageResources.ImageResource;

/**
 * Represents a Ball in the game. The position and direction are calculated in
 * the class.
 * 
 * @author Roland
 */
public class BallSprite extends Sprite {

	protected float xDirection;
	protected float yDirection;

	protected float x;
	protected float y;
	
	protected int leftBorderPixel;
	protected int rightBorderPixel;
	protected int topBorderPixel;
	protected int bottomBorderPixel;

	protected BallSprite(){
		this(ImageResources.ball_blue);
	}
	
	protected BallSprite(ImageResource imageResource){
		super(imageResource.image, imageResource.width, imageResource.height);
	}
	
	BallSprite(float angle, float ballSpeed) {
		this();
		setDirections(angle, ballSpeed);
	}
	
	protected void setDirections(float angle, float ballSpeed){	
		this.xDirection = ballSpeed * (float)Math.cos(angle);
		this.yDirection = ballSpeed * (float)Math.sin(angle);
	}
	
	
	void makeRed(){
		setImage(ImageResources.ball_red);
	}
	
	void makeDeepRed(){
		setImage(ImageResources.ball_deep_red);
	}
	
	void makeGreen(){
		setImage(ImageResources.ball_green);
	}
	
	protected void setImage(ImageResource imageResource){
		setImage(imageResource.image, imageResource.width, imageResource.height);
	}
	
	/**
	 * Tells where the walls are. To calculate the exact pixel positions
	 * we need the size of the display and the field
	 */
	void setBorder(int outerWidth, int outerHeight, int innerWidth, int innerHeight) {
		this.leftBorderPixel = (outerWidth - innerWidth) / 2;
		this.rightBorderPixel = leftBorderPixel + innerWidth - getWidth();
		this.topBorderPixel = (outerHeight - innerHeight) / 2;
		this.bottomBorderPixel = topBorderPixel + innerHeight - getHeight();
		
		this.x = leftBorderPixel - getWidth();
		this.y = topBorderPixel - getHeight();
	}

	/**
	 * let the ball move a little further. When he hits a wall he will change his
	 * direction
	 */
	void move() {
		x += xDirection;
		y += yDirection;
		
		checkBorder();
		setPosition((int) x, (int) y);
	}

	protected boolean checkBorder() {
		boolean hit = false;
		if (x < leftBorderPixel && xDirection < 0) {
			xDirection *= -1;
			hit = true;
		} else if (x > rightBorderPixel && xDirection > 0) {
			xDirection *= -1;
			hit = true;
		}

		if (y < topBorderPixel && yDirection < 0) {
			yDirection *= -1;
			hit = true;
		} else if (y > bottomBorderPixel && yDirection > 0) {
			yDirection *= -1;
			hit = true;
		}
		
		return hit;
	}
}
