package mlp.server.chunk;

import java.util.Hashtable;

import mlp.server.Terrain;
import viking.game.movement.Traversal;

/**
 * Class describing a TerrainChunk which contains only ONE type of terrain.
 * This block is thought to be immutable, and may therefore not be changed through the normal methods.
 * @author Grevor
 *
 */
public class SingleTerrainChunk extends TerrainChunkBase {
	long terrainHandle;

	private SingleTerrainChunk(long terrainHandle) {
		this.terrainHandle = terrainHandle;
	}

	@Override
	public long getTerrainHandle(int x, int y) {
		return terrainHandle;
	}

	@Override
	public void tick(long delta) {
		// TODO add ticking of terrain here. (that is: ticking of the single handle present in this terrain.)
		// This also need to make sure that the block is not ticked many times.
		//This type of block IS mostly to be used for terrain such as air and non-animated ground, but may find other uses.
		//If this is the case, this method must be VERY smart.
		
	}
	
	@Override
	public void setTerrain(int x, int y, long terrainHandle) { }

	@Override
	public boolean isImmutable() { return true; }
	
	
	/***********************************************************************************************************************************************
	 * This is the static part of this class. This exists to save memory for the server so that it does not need to have billions of chunks with, for example, only air.
	 * The memory-saving is achieved through interning.
	 ***********************************************************************************************************************************************/
	private static Hashtable<Long,TerrainChunk> internedChunks = new Hashtable<>();
	
	/**
	 * Gets a SingleTerrainChunk of the specified type.
	 * @param terrainHandle - The handle to have.
	 * @return
	 * A TerrainChunk with the specified properties. Please note that the implementation may, at its own discretion, give a reference to an already existing object.
	 */
	public static TerrainChunk getSingleTerrainChunk(long terrainHandle) {
		TerrainChunk chunk = internedChunks.get(terrainHandle);
		if(chunk == null) {
			chunk = new SingleTerrainChunk(terrainHandle);
			internedChunks.put(terrainHandle, chunk);
		}
		return chunk;
	}

	@Override
	public double getTraversalCost(int x, int y, Traversal t) {
		return Terrain.getTerrain(terrainHandle).getTraversalCost(t);
	}

}
