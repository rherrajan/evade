package evade.multi;

import java.util.Random;

import evade.CharacterSprite;
import evade.EvadeCanvas;
import evade.ImageResources;
import evade.ImageResources.ImageResource;

/**
 * This is the canvas of the game. Here the graphic is painted. It is a
 * runnable. So you can start the game in a separated thread.
 * 
 * @author Roland
 */
public class MultiplayerCanvas extends EvadeCanvas {

	private static final long timeTillNotHit = 5000 / timeStep;
	private int hitTime;

	private static final int posCycle = 3;
	private int cycleIterator = 0;

	private final Thread game;

	BTMain btMain;

	PlayerSprite[] remotePlayers = new PlayerSprite[8];
	PlayerSprite player;

	protected MultiplayerCanvas(BTMain btMain) {
		super(btMain);
		this.btMain = btMain;
		game = new Thread(this);
	}

	protected CharacterSprite createPlayer() {
		return player = createPlayerSprite(false);
	}

	protected PlayerSprite createPlayerSprite(boolean isServer) {
		PlayerSprite player;
		ImageResource normalImage, hitImage;

		if (isServer) {
			normalImage = ImageResources.playerServer;
			hitImage = ImageResources.playerServerHit;

		} else {
			normalImage = ImageResources.playerClient;
			hitImage = ImageResources.playerClientHit;
		}

		player = new PlayerSprite(normalImage);
		player.setHitImage(hitImage.image);
		player.setBorder(getWidth(), getHeight(), width, height);

		layerManager.insert(player, 1);
		return player;
	}

	PlayerSprite createRemotePlayer(boolean isServer) {

		PlayerSprite remotePlayer = createPlayerSprite(isServer);

		remotePlayer.setBorder(getWidth(), getHeight(), width, height);

		layerManager.insert(remotePlayer, 1);

		return remotePlayer;
	}

	protected void readyForGame() {
		btMain.send("ready", Boolean.TRUE.toString());
	}

	protected void startGame(long seed, int level) {
		rand = new Random(seed);
		btMain.setCurrentDisplayable(this);
		
		setLevel(level, 0);
		
		game.start();
		System.out.println("Seed: " + seed);

		btMain.send("register", String.valueOf(0));
	}

	protected void input() {
		super.input();

		if (player.getIsHit()) {
			hitTime++;

			if (hitTime > timeTillNotHit) {
				btMain.send("hit", String.valueOf(false));
				player.setHit(false);
			}
		}
	}

	protected void movePlayer(int dx, int dy) {
		super.movePlayer(dx, dy);

		if (cycleIterator == 0) {
			sendPosition(player.getRelativeX(), player.getRelativeY(), player.getVisibleSide());
		}

		cycleIterator = cycleIterator % posCycle;
	}

	private void sendPosition(int x, int y, int charakterSide) {
		btMain.send("pos", String.valueOf(x) + "/" + String.valueOf(y) + "/" + String.valueOf(charakterSide));
	}

	void processCommand(String cmdString, String valueStr, int fromWhom) {

		int indx;

		if (cmdString.equals("register")) {

			int playerNr = Integer.parseInt(valueStr);
			remotePlayers[playerNr] = createRemotePlayer(playerNr == 0);
			System.out.println("registered Player Nr " + playerNr);

		} else if (cmdString.equals("start")) {

			long seed;
			int level;

			indx = valueStr.indexOf('/');
			seed = Long.parseLong(valueStr.substring(0, indx));
			valueStr = valueStr.substring(indx + 1);
			level = Integer.parseInt(valueStr);

			startGame(seed, level);
			
		} else if (cmdString.equals("pos")) {

			int x, y, side;

			indx = valueStr.indexOf('/');
			x = Integer.parseInt(valueStr.substring(0, indx));
			valueStr = valueStr.substring(indx + 1);

			indx = valueStr.indexOf('/');
			y = Integer.parseInt(valueStr.substring(0, indx));
			valueStr = valueStr.substring(indx + 1);

			side = Integer.parseInt(valueStr);

			PlayerSprite sender = remotePlayers[fromWhom];

			if (sender != null) {
				sender.setRelativePosition(x, y);
				sender.setVisibleSide(side);
				sender.nextFrame();
			} else {
				System.err.println("recived message from unregistered player nr " + fromWhom);
			}

		} else if (cmdString.equals("hit")) {

			boolean isHit = valueStr.equals(String.valueOf(true));
			remotePlayers[fromWhom].setHit(isHit);

		} else if (cmdString.equals("end")) {
			int time = Integer.parseInt(valueStr);
			elapsedSteps = time;
			endGame();

		} else if (cmdString.equals("elapsedTime")) {
			int time = Integer.parseInt(valueStr);

			// System.out.println("error: " + error + " => " + (time -
			// elapsedSteps));
			error = time - elapsedSteps;

		} else if (cmdString.equals("other")) {
			// The server did forward a message from another client

			int playerNr;
			String realCmd, realValue;

			indx = valueStr.indexOf('/');
			playerNr = Integer.parseInt(valueStr.substring(0, indx));
			valueStr = valueStr.substring(indx + 1);

			indx = valueStr.indexOf('/');
			realCmd = valueStr.substring(0, indx);
			realValue = valueStr.substring(indx + 1);

			// System.out.println("realCmd: " + realCmd);
			// System.out.println("realValue: " + realValue);
			// System.out.println("playerNr: " + playerNr);

			processCommand(realCmd, realValue, playerNr);

		} else {
			System.err.println("unknown command: " + cmdString + " in " + this.getClass());
		}

	}

	protected void hit() {

		if (!player.getIsHit()) {
			btMain.send("hit", String.valueOf(true));
		}

		player.setHit(true);

		hitTime = 0;
	}

}
