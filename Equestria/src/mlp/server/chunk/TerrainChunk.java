package mlp.server.chunk;

import viking.game.movement.Traversal;

/**
 * Interface to a chunk of terrain. 
 * The terrain chunk will always have the world-positions like the chunk it is in, 
 * and will have the same SIZE_X and SIZE_Y as the chunk.
 * @author Grevor
 *
 */
public interface TerrainChunk {
	/**
	 * Gets the terrain for the specified position in chunk-coordinates.
	 * @param x - The x chunk-coordinate.
	 * @param y - The y chunk-coordinate.
	 * @return
	 * The terrain handle at the specified position.
	 */
	public abstract long getTerrainHandle(int x, int y);

	/**
	 * Ticks this chunk and all entities in it.
	 * @param delta - The time since last tick.
	 */
	public abstract void tick(long delta);
	
	/**
	 * Sets the terrain in this chunk at the specified position.
	 * @param x - The x chunk-coordinate.
	 * @param y - The x chunk-coordinate.
	 * @param terrainHandle - The new terrain handle to set to the position;
	 */
	public void setTerrain(int x, int y, long terrainHandle);
	
	/**
	 * Checks if this chunk is immutable.
	 * @return
	 * True if it is, else false.
	 */
	public abstract boolean isImmutable();
	
	/**
	 * Checks if the specified traversal can traverse the specified spot.
	 * @param x - The x to traverse, in chunk-coordinates.
	 * @param y - The y to traverse, in chunk-coordinates.
	 * @param t - The traversal.
	 * @return 
	 * True if it can, else false.
	 */
	public boolean canTraverse(int x, int y, Traversal t);
	
	public double getTraversalCost(int x, int y, Traversal t);
}
