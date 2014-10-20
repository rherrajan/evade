package evade;

import javax.microedition.lcdui.game.Sprite;

public class TargetingBallSprite extends BallSprite {
	
	private Sprite eye;

	private float maxDif;
	private float ballSpeed;

	private CharacterSprite player;



	TargetingBallSprite(float ballSpeed, CharacterSprite localPlayer, Sprite eye, float agility) {
		super();
		this.ballSpeed = ballSpeed;
		this.player = localPlayer;
		this.maxDif = agility;
		makeBlack();
		
		this.eye = eye;
		setDirections(0, ballSpeed);
	}

	void move() {

		float actAngle = actualAngle();
		float optAngle = optimalAngle();

		float dif = getDif(actAngle, optAngle);

		
		if (dif > maxDif) {
			dif = maxDif;
		} else if (dif < -maxDif) {
			dif = -maxDif;
		}
		
		float newAngle = actAngle - dif;
		

		setDirections(newAngle, ballSpeed);
		super.move();
	}
	
	private float getDif(float actAngle, float optAngle) {
		float dif = actAngle - optAngle;

		if(Math.abs(dif) > Math.PI){
			if(dif < 0){
				dif += 2 * Math.PI;
			} else {
				dif -= 2 * Math.PI;
			}
		}

		return dif;
	}

	protected void setDirections(float angle, float ballSpeed) {
		super.setDirections(angle, ballSpeed);
		int eyeX = (int)(this.x + eye.getWidth()/2f  + 3*this.xDirection);
		int eyeY = (int)(this.y + eye.getHeight()/2f + 3*this.yDirection);
		
		eye.setPosition(eyeX, eyeY);
	}
	
	private float actualAngle() {
		return getAngle(xDirection, yDirection);
	}

	private float optimalAngle() {

		
		final float bdx = player.getWidth()/2 - this.getWidth()/2;
		final float bdy = player.getHeight()/2 - this.getHeight()/2;
		
		float dx = player.getX() - this.getX() + bdx;
		float dy = player.getY() - this.getY() + bdy;
		
		return getAngle(dx, dy);
	}

	private float getAngle(float dx, float dy) {
		double ratio = dy / Math.sqrt(dx*dx + dy*dy);
		
		if(ratio>1){
			// the asin for 1 is infinite
			ratio = 0.99;
		}
		
		float alpha = (float) Math2.asin(ratio);
		
		if(alpha == (0.0/0.0)){
			String error = "alpha: " + alpha + "\t" + "ratio: " + ratio;
			
//			throw new RuntimeException(error);
			
			System.err.println(error);
			alpha = 0;	
		}
		
		// I have no idea, why this is necessary
		if(dx < 0){
			alpha = -(float)Math.PI -alpha;
		}
		
		return alpha;
	}

	private void makeBlack() {
		setImage(ImageResources.ball_black);
	}

	protected boolean checkBorder() {
		// We can cross all borders
		return false;
	}

}
