package evade;

import javax.microedition.lcdui.game.Sprite;

import evade.ImageResources.ImageResource;

/**
 * Represents a character in the game. The player can control it with the
 * keyboard.
 * 
 * @author Roland
 */
public class CharacterSprite extends Sprite {

	private static final int frames = 4;
	private static final int delay = 3;


	public static final int FRONT = 0;
	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	public static final int BACK = 3;
	
	private int visibleSide = FRONT;
	private int iterator = 0;
	private int frame = 0;

	protected int leftBorderPixel, rightBorderPixel;
	protected int topBorderPixel, bottomBorderPixel;

	private int x;
	private int y;


	protected CharacterSprite(ImageResource imageResource){
		super(imageResource.image, imageResource.width, imageResource.height);
	}
	
	/**
	 * Tells where the walls are. To calculate the exact pixel positions we need
	 * the size of the display and the field
	 */
	public void setBorder(int outerWidth, int outerHeight, int innerWidth, int innerHeight) {
		this.leftBorderPixel = (outerWidth - innerWidth) / 2;
		this.rightBorderPixel = leftBorderPixel + innerWidth - getWidth();
		this.topBorderPixel = (outerHeight - innerHeight) / 2;
		this.bottomBorderPixel = topBorderPixel + innerHeight - getHeight();

		x = leftBorderPixel + (rightBorderPixel - leftBorderPixel) / 2;
		y = topBorderPixel + (bottomBorderPixel - topBorderPixel) / 2;

		setPosition(x, y);
	}

	/**
	 * Moves the character. If it hits the wall it will not walk any further.
	 * 
	 * @param dx
	 * @param dy
	 */
	void moveInBorder(int dx, int dy) {

		int visibleSide = -1;

		if (dx > 0) {
			visibleSide = RIGHT;
		} else if (dx < 0) {
			visibleSide = LEFT;
		} else if (dy > 0) {
			visibleSide = FRONT;
		} else if (dy < 0) {
			visibleSide = BACK;
		} else {
			return;
		}

		setVisibleSide(visibleSide);
		nextFrame();

		x += dx;
		y += dy;

		checkBorder();
		setPosition(x, y);
	}

	private void checkBorder() {
		if (x < leftBorderPixel) {
			x = leftBorderPixel;
		} else if (x > rightBorderPixel) {
			x = rightBorderPixel;
		}

		if (y < topBorderPixel) {
			y = topBorderPixel;
		} else if (y > bottomBorderPixel) {
			y = bottomBorderPixel;
		}
	}

	/**
	 * Tells the character which sprite it should use to represents the
	 * charakter. Possible values are constants in this class
	 */
	public void setVisibleSide(int nextVisibleSide) {
		if (nextVisibleSide == visibleSide || nextVisibleSide == -1) {
			return;
		}

		this.visibleSide = nextVisibleSide;

		/* show the next frame without the delay */
		iterator = 0;
	}

	/**
	 * Returns the visible side of the character
	 */
	public int getVisibleSide() {
		return visibleSide;
	}

	public void nextFrame() {

		// don't show a new frame, if the last one was shown recently
		if (iterator % delay == 0) {

			// calculate which frame to show next
			frame = (frame + 1) % frames;

			/** The index of the next frame */
			int nextFrame = visibleSide * frames + frame;

			try {
				super.setFrame(nextFrame);
			} catch (IndexOutOfBoundsException e) {
				System.err.println("visibleSide: " + visibleSide);
				System.err.println("index: " + nextFrame + " = (" + visibleSide + "-1) * " + frames + " + " + frame + " ?");

				throw e;
			}
		}

		iterator++;
	}
}
