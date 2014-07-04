package mlp.core;

/**
 * Interface describing an entity in the game-world.
 * @author Grevor
 *
 */
public interface Entity extends Renderable {
	int getXPosition();
	int getYPosition();
	int getZPosition();
	
	void tick(long delta);
	void moveTo(int x, int y, int z);
}
