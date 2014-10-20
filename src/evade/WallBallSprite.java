package evade;


public class WallBallSprite extends BallSprite {

	WallBallSprite(float angle, float ballSpeed) {
		super();
		if(angle < (float)Math.PI/4){
			this.xDirection = 0;
			this.yDirection = ballSpeed;
		} else { 
			this.xDirection = ballSpeed;
			this.yDirection = 0;
		}

		makeBlack();
	}

	private void makeBlack() {
		setImage(ImageResources.ring);
	}
	
	protected boolean checkBorder() {
		float yMid = topBorderPixel + 0.5f * (bottomBorderPixel - topBorderPixel); 
		float xMid = leftBorderPixel + 0.5f * (rightBorderPixel - leftBorderPixel); 
		
		boolean hit = false;
		if (x < leftBorderPixel && xDirection < 0) {
			float temp = xDirection;
			xDirection = 0;
			
			if(y < yMid){
				yDirection = -temp;
			} else {
				yDirection = +temp;
			}
			
			hit = true;
			
		} else if (x > rightBorderPixel && xDirection > 0) {
			float temp = xDirection;
			xDirection = 0;
			
			if(y < yMid){
				yDirection = +temp;
			} else {
				yDirection = -temp;
			}
			hit = true;
		}

		if (y < topBorderPixel && yDirection < 0) {
			float temp = yDirection;
			yDirection = 0;
			
			if(x < xMid){
				xDirection = -temp;
			} else {
				xDirection = +temp;
			}
			
			hit = true;
		} else if (y > bottomBorderPixel && yDirection > 0) {
			float temp = yDirection;
			yDirection = 0;
			
			if(x < xMid){
				xDirection = +temp;
			} else {
				xDirection = -temp;
			}
			hit = true;
		}
		
		return hit;
	}

}
