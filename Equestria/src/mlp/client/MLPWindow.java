package mlp.client;

public class MLPWindow {
	static final long FPS = 30;
	static final long nsPerFrame = 1000000000 / FPS;
	
	Client client;
	
	public boolean notExited() {
		return client.stillPlaying();
	}
	
	public void loadAssets() {
		//TODO: load all needed assets of the game.
		client.loadAssets();
	}
	
	public void doGameLogic(long delta) {
		//TODO: add game logic for a game-loop here. The looping itself is handled by different code.
		client.doGameLogic(delta);
	}
	
	public void exitGame() {
		//TODO: unload all assets, save configurations, exit the game.
		client.unloadAssets();
	}
	
	
	
	/**
	 * Main Entry-point for the application. This controls the game loop, and starts a gamewindow.
	 * @param args
	 */
	public static void main(String[] args) {
		MLPWindow gameWindow = new MLPWindow();
		gameWindow.loadAssets();
		long timeDiff, 
		newTime, 
		previousTime = System.nanoTime(), 
		workTime = 0;
		while(gameWindow.notExited()) {

			
			newTime = System.nanoTime();
			timeDiff = newTime - previousTime;
			workTime += timeDiff;
			previousTime = newTime;
			while(workTime >= 0 && gameWindow.notExited()) {
				gameWindow.doGameLogic(Math.min(workTime, timeDiff));
				workTime -= nsPerFrame;
			}
			try {
				Thread.sleep(-workTime / 1000000);
			} catch(final InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		gameWindow.exitGame();
	}
}
