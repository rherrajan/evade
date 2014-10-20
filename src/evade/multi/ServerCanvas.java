package evade.multi;

import java.util.Date;

import evade.CharacterSprite;

/**
 * This game will react differently sometimes, cause its the server.
 * 
 * @author Roland
 */
public class ServerCanvas extends MultiplayerCanvas {

	private int lvl;

	public ServerCanvas(BTMain btMain, int level) {
		super(btMain);
		this.lvl = level;
	}

	protected void readyForGame() {
		startGame();
	}

	private void startGame() {
		long seed = new Date().getTime();
		btMain.send("start", String.valueOf(seed) + "/" + String.valueOf(lvl));
		startGame(seed, lvl);
	}

	protected CharacterSprite createPlayer() {
		return player = createPlayerSprite(true);
	}

	void processCommand(String cmdString, String valueStr, int fromWhom) {

		if (cmdString.equals("ready")) {
			startGame();

		} else if (cmdString.equals("register")) {
			remotePlayers[fromWhom] = createRemotePlayer(false);
			sendRegistrations(fromWhom);
			System.out.println("registered Player Nr " + fromWhom);

		} else if (cmdString.equals("pos")) {
			forwardMessage(cmdString, valueStr, fromWhom);
			super.processCommand(cmdString, valueStr, fromWhom);

		} else if (cmdString.equals("hit")) {
			forwardMessage(cmdString, valueStr, fromWhom);

			boolean isHit = valueStr.equals(String.valueOf(true));
			remotePlayers[fromWhom].setHit(isHit);
			if (gameEnds()) {
				endGame();
			}

		} else {
			super.processCommand(cmdString, valueStr, fromWhom);
		}
	}

	/**
	 * The previous players didn't recognize the last registration. We have to
	 * inform them and not the new player.
	 * 
	 * The new player has to now how many players there are.
	 */
	private void sendRegistrations(int fromWhom) {

		int nextPlayer = fromWhom - 1;

		// Don't send to channel 0. Thats the server
		while (nextPlayer >= 1) {
			System.out.println("reg from " + fromWhom + " to " + nextPlayer);
			btMain.send("register", String.valueOf(fromWhom), nextPlayer);
			btMain.send("register", String.valueOf(nextPlayer), fromWhom);
			nextPlayer--;
		}
	}

	private void forwardMessage(String cmdString, String valueStr, int fromWhom) {
		for (int i = 0; i < remotePlayers.length; i++) {
			if (remotePlayers[i] != null && fromWhom != i) {
				btMain.send("other", fromWhom + "/" + cmdString + "/" + valueStr, i);
			}
		}
	}

	protected void hit() {

		super.hit();

		if (gameEnds()) {
			endGame();
		}
	}

	private boolean gameEnds() {

		if (!player.getIsHit()) {
			return false;
		}

		for (int i = 0; i < remotePlayers.length; i++) {
			PlayerSprite player = remotePlayers[i];

			if (player != null && !player.getIsHit()) {
				return false;
			}
		}

		return true;
	}

	protected void endGame() {
		btMain.send("end", String.valueOf(elapsedSteps));

		super.endGame();
	}

	protected void input() {
		super.input();

		// 4 times in a second the clients are informed about their latency
		if (elapsedSteps % (250 / timeStep) == 0) { 
			btMain.send("elapsedTime", String.valueOf(elapsedSteps));
		}
	}
}
