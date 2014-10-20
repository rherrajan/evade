package evade;

public class BouncingBallSprite extends BallSprite {

	private float ballSpeed;

	BouncingBallSprite(float angle, float ballSpeed) {
		super(angle, ballSpeed);
		this.ballSpeed = ballSpeed;
		makeGreen();
	}

	protected boolean checkBorder() {
		boolean hitBorder = super.checkBorder();

		if (hitBorder) {

			xDirection = (ballSpeed + 3 * xDirection) % (ballSpeed) + xDirection/2;
			yDirection = (ballSpeed + 3 * yDirection) % (ballSpeed) + yDirection/2;
			
		}

		return hitBorder;
	}
}
