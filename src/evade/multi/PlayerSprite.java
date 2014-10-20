package evade.multi;

import javax.microedition.lcdui.Image;

import evade.CharacterSprite;
import evade.ImageResources.ImageResource;

/**
 * This class represents a character sprite in a multiplayer game. It the player
 * is hit the sprite will become little.
 * 
 * @author Roland
 */
public class PlayerSprite extends CharacterSprite {

	private boolean isHit;

	private Image normalImage;
	private Image hitImage;

	PlayerSprite(ImageResource imageResource){
		super(imageResource);
		this.normalImage = imageResource.image;
	}

	void setHitImage(Image img) {
		this.hitImage = img;
	}

	/** Changes the sprite depending on the status */
	void setHit(boolean isHit) {
		if (this.isHit == isHit) {
			return;
		}

		this.isHit = isHit;

		if (isHit) {
			setImage(hitImage, getWidth(), getHeight());
		} else {
			setImage(normalImage, getWidth(), getHeight());
		}

	}

	/**
	 * this method override the standard method to calculate a new border
	 * every time the sprite changes
	 */
	public void setImage(Image img, int frameWidth, int frameHeight) {
		// we have to recalculate the borders, cause the image might be a
		// different size
		rightBorderPixel += getWidth();
		bottomBorderPixel += getHeight();
		super.setImage(img, frameWidth, frameHeight);
		rightBorderPixel -= getWidth();
		bottomBorderPixel -= getHeight();
	}

	boolean getIsHit() {
		return isHit;
	}

	/**
	 * Sets the position of the sprite relative to the top left pixel of the
	 * border
	 */
	void setRelativePosition(int x, int y) {
		setPosition(x + leftBorderPixel, y + topBorderPixel);
	}

	int getRelativeX() {
		return getX() - leftBorderPixel;
	}

	int getRelativeY() {
		return getY() - topBorderPixel;
	}
}
