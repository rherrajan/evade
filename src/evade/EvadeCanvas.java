package evade;

import java.io.IOException;
import java.util.Random;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.Sprite;

/**
 * This is the canvas of the game. Here the graphic is painted. It is a
 * runnable. So you can start the game in a separated thread.
 * 
 * @author Roland
 */
public class EvadeCanvas extends GameCanvas implements Runnable, CommandListener {

	/** width of the field in which the sprites can move */
	protected static final int width = 200;

	/** height of the field in which the sprites can move */
	protected static final int height = 200;

	/** if a frame is faster than the value, it will wait */
	protected static final int timeStep = 40;
	
	/** if there are more balls than this value, the oldest one will be removed */
	private static final int maxBalls = 15;

	/** balls can't have less than this angle at start, because they would be boring :) */
	private static final float deadAngle = (float) 0.1;
	
	/** The amount of steps till the game started */
	protected int elapsedSteps;

	/**
	 * The difference to the correct elapsed steps. Can be set to slow down or
	 * speed up the game
	 */
	protected int error;

	protected LayerManager layerManager;
	protected Random rand = new Random();

	/** Time you have to stay alive before you can go to the next level */
	public static int timeForLevel = 100;
	
	/** The time between new balls will appear (at start) */
	public static int ballTime = 10000;
	
	/** How fast the Ball respawn time will slow down (in percent) */
	public static int ballTime_increase = 30;

	private static final int charSpeed = 5;
	private static final float ballSpeed = 3;



	private final Main menu;

	private CharacterSprite localPlayer;

	private final BallSprite[] balls = new BallSprite[maxBalls];
	private int nextball;

	private volatile String message;
	private int bgColor;
	private volatile boolean isRunning;

	private int nextBallStep;
	private int increase;

	private int startLevel = 0;
	private int startSeconds = 0;

	private boolean isEnding = false;

	protected EvadeCanvas(Main menu) {
		super(true); // suppressKeyEvents = true

		this.menu = menu;

		addCommand(new Command("Back", Command.BACK, 0));
		addCommand(new Command("Pause", Command.STOP, 0));
		setCommandListener(this);

		try {
			layerManager = new LayerManager();
			

			Sprite background = ImageResources.background.asSprite();
			layerManager.append(background);
			
			int bx = (getWidth() - width) / 2;
			int by = (getHeight() - height) / 2 - 100; // 120 = Pixel above the accessible ground	
			background.setPosition(bx, by);
			

			Sprite foreground = ImageResources.foreground.asSprite();
			layerManager.insert(foreground, 0);

			int fx = (getWidth() - width) / 2 - 45; // 45 = Pixel left from the accessible ground
			int fy = (getHeight() - height) / 2 - 100; // 120 = Pixel above the accessible ground	
			foreground.setPosition(fx, fy);


			localPlayer = createPlayer();

			elapsedSteps = 0;
			bgColor = 0x000000; // black

			isRunning = true;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected CharacterSprite createPlayer() throws IOException {
		CharacterSprite player = new CharacterSprite(ImageResources.playerSingle);
		layerManager.insert(player, 1);
		player.setBorder(getWidth(), getHeight(), width, height);

		return player;
	}

	public void commandAction(Command c, Displayable s) {
		if (c.getCommandType() == Command.BACK) {
			endGame();
		} else if (c.getCommandType() == Command.STOP) {
			if (isRunning) {
				pause();
			} else {
				resume();
				new Thread(this).start();
			}
		}
	}

	public void run() {
		try {
			run_intern();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run_intern() {

		while (isRunning) {

			int steps = 1;
			if (error > 5) {
				steps = 2;
				error--;
			} else if (error < -5) {
				steps = 0;
				error++;
			}

			int duration = tik(steps);

			if (duration < timeStep) {
				try {
					Thread.sleep(timeStep - duration);
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
			}
		}

		render();

		if(isEnding){
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			menu.gameEnds(elapsedSteps * timeStep);			
		}
	}

	protected void setLevel(int lvl, int seconds) {
		this.startLevel = lvl;
		this.startSeconds = seconds;
	}

	public void populateGame() {

		// start time is the half of the best time in each level

		elapsedSteps = (startLevel * timeForLevel + startSeconds) * (1000 / timeStep);

		nextBallStep = elapsedSteps - startSeconds * (1000 / timeStep);
		increase = ballTime / timeStep;

	}

	private int tik(int count) {
		long start = System.currentTimeMillis();

		for (int i = 0; i < count; i++) {
			elapsedSteps++;
			if (elapsedSteps % 10 == 0) {
				checkNewBall();
			}
			input();
			moveBalls();
		}

		render();

		long end = System.currentTimeMillis();

		return (int) (end - start);
	}

	private void checkNewBall() {
		if (elapsedSteps > nextBallStep) {
			increase *= 1+(float)ballTime_increase/100 ;
			nextBallStep += increase;
			createNewBall();
		}
	}

	private void createNewBall() {

		if (balls[nextball] != null) {
			layerManager.remove(balls[nextball]);
		}

		BallSprite ball;
		float angle = rand.nextFloat() * (float)Math.PI/2 * (1 - 2 * deadAngle) + deadAngle;

		switch (startLevel) {

		case 0:
			ball = new BallSprite(angle, ballSpeed);

			break;

		case 1:
			ball = makeRandomSpeedBall(angle);
			break;
			
		case 2:
			ball = new BouncingBallSprite(angle, ballSpeed);
			break;
			
		case 3:
			if(nextball%2 == 0){
				ball = makeTargetSprite(0.06f);
			} else {
				float speed = (ballSpeed / 2) + ballSpeed * rand.nextFloat();
				ball = new WallBallSprite(angle, speed);
			}
			break;
			
		case 4:
			
			int godsChoice = rand.nextInt(3);
			if(godsChoice == 0){
				ball = makeRandomSpeedBall(angle);
			} else if(godsChoice == 1){
				ball = new BouncingBallSprite(angle, ballSpeed);
			} else {
				ball = makeTargetSprite(0.03f);
			}
			
			break;

		default:
			
			System.err.println("Unknown level: " + startLevel);
			ball = new BallSprite(angle, ballSpeed);
			break;

		}

		ball.setBorder(getWidth(), getHeight(), width, height);
		
		if(ball.getClass() == TargetingBallSprite.class){
			// The eye has to be displayed in front of the ball
			layerManager.insert(ball, 2);
		} else {
			layerManager.insert(ball, 1);
		}

		
		balls[nextball] = ball;
		nextball = (nextball + 1) % maxBalls;
	}

	private BallSprite makeTargetSprite(float avgAgility) {
		Sprite eye = new Sprite(ImageResources.eyeIMG.image, ImageResources.eyeIMG.width, ImageResources.eyeIMG.height);
		layerManager.insert(eye, 1);
		
		
		float speed = ballSpeed/2 + rand.nextFloat() * ballSpeed;
		float agility = avgAgility * (ballSpeed/speed); // More Agility comes with less speed
		
		return new TargetingBallSprite(speed, this.localPlayer, eye, agility);
	}

	private BallSprite makeRandomSpeedBall(float angle) {

		float speed = (ballSpeed / 2) + ballSpeed * 1.5f * rand.nextFloat();
		BallSprite ball = new BallSprite(angle, speed);
		if (speed > ballSpeed) {
			ball.makeRed();
			if (speed > 1.5 * ballSpeed) {
				ball.makeDeepRed();
			}
		}
		return ball;
	}

	private void moveBalls() {

		for (int i = 0; i < balls.length; i++) {
			BallSprite ball = balls[i];
			if (ball != null) {
				ball.move();
				if (ball.collidesWith(localPlayer, true)) {
					hit();
				}
			}
		}
	}

	protected void input() {
		int keyStates = getKeyStates();

		int dx = 0, dy = 0;

		if ((keyStates & GameCanvas.LEFT_PRESSED) != 0) {
			dx--;

		} else if ((keyStates & GameCanvas.RIGHT_PRESSED) != 0) {
			dx++;
		}

		if ((keyStates & GameCanvas.DOWN_PRESSED) != 0) {
			dy++;

		} else if ((keyStates & GameCanvas.UP_PRESSED) != 0) {
			dy--;
		}

		if (dx != 0 || dy != 0) {
			movePlayer(dx, dy);
		}
	}

	protected void movePlayer(int dx, int dy) {
		localPlayer.moveInBorder(charSpeed * dx, charSpeed * dy);
	}

	private void render() {
		Graphics g = getGraphics();

		int w = getWidth();
		int h = getHeight();

		g.setColor(bgColor);
		g.fillRect(0, 0, w, h);

		layerManager.paint(g, 0, 0);

		g.setColor(0xfffffff); // white
		if (message != null) {
			g.drawString(message, 0, 0, Graphics.TOP | Graphics.LEFT);
		} else {
			long dif = elapsedSteps * timeStep / 1000;
			g.drawString("Seconds: " + String.valueOf(dif), 0, 0, Graphics.TOP | Graphics.LEFT);
		}

		if (error > 0) {
			g.setColor(0xff0000); // red
			final int errorX = 1 + width + (w - width) / 2;
			final int errorY = (h - height) / 2 + (height / 2);
			g.drawRect(errorX, errorY, 1, error * 10);

		} else if (error < 0) {
			g.setColor(0x00ff00); // green
			final int errorX = 1 + width + (w - width) / 2;
			final int errorY = (h - height) / 2 + (height / 2);
			g.drawRect(errorX, errorY + 10 * error, 1, -10 * error);
		}

//		g.setColor(0x999999); // grey
//		g.drawRect((w - width) / 2, (h - height) / 2, width, height);

		flushGraphics();
	}

	protected void hit() {
		endGame();
	}

	/** Just stop the Thread. It will be restarted later */
	protected void pause() {
		isRunning = false;
	}
	
	public void resume() {
		isRunning = true;
	}
	
	/** Stop the Thread and show the menu*/
	protected void stop() {
		isEnding = true;
		isRunning = false;
	}

	/** Ends the game. Probably, because the player was hit by a ball */
	protected void endGame() {
		long msec = elapsedSteps * timeStep;

		bgColor = 0xff0000; // red
		message = "Game ends after " + msec / 1000.0 + " sec";

		stop();
	}

}
