package mlp.server.chunk;

import mlp.core.Entity;

public interface Chunk extends TerrainChunk {

	/**
	 * The size of one chunk in the West-East direction, given in tiles.
	 */
	public static final int SIZE_X = 20;
	/**
	 * The size of one chunk in the North-South direction, given in tiles.
	 */
	public static final int SIZE_Y = 20;

	/**
	 * Gets the terrain in layer 2 for the specified position in chunk-coordinates.
	 * @param x - The x chunk-coordinate.
	 * @param y - The y chunk-coordinate.
	 * @return
	 * The terrain handle at the specified position.
	 */
	public abstract long getTerrainHandleLayer2(int x, int y);

	/**
	 * Gets the x world-coordinate of the upper left tile of this chunk.
	 * @return
	 * The x world-Coordinate of this chunks upper-left tile.
	 */
	public abstract int getXPosition();
	
	/**
	 * Gets the y world-coordinate of the upper left tile of this chunk.
	 * @return
	 * The y world-Coordinate of this chunks upper-left tile.
	 */
	public abstract int getYPosition();
	
	/**
	 * Sets the terrain in this chunk at the specified position.
	 * @param x - The x chunk-coordinate.
	 * @param y - The x chunk-coordinate.
	 * @param terrainHandle - The new terrain handle to set to the position;
	 */
	public void setTerrainLayer2(int x, int y, long terrainHandle);
	
	/**
	 * Adds the specified entity to this chunk.
	 * This entity must actually be in the chunk, or artifacts will occur.
	 * @param e - The entity to add.
	 */
	public void addEntity(Entity e);
	
	/**
	 * Removes the specified entity from this chunk.
	 * @param e - The entity to remove.
	 */
	public void removeEntity(Entity e);
	
	/**
	 * Checks if the specified world-coordinate is in the chunk.
	 * @param x
	 * @param y
	 * @return
	 * True if it is, else false.
	 */
	public boolean isInChunk(int x, int y);
}