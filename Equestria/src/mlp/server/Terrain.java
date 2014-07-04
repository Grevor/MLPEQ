package mlp.server;

import java.util.ArrayList;

import viking.game.movement.Traversal;
import mlp.core.EQGraphics;
import mlp.core.Renderable;

public class Terrain implements Renderable {
	static final long INDEX_MASK = 			0x0000000000007fffL;
	static final long INVULNERABLE_MASK = 	0x0000000000008000L;
	static final long TIMESTAMP_MASK = 		0x00000000ffff0000L;
	static final long LIFE_MASK = 			0xffffffff00000000L;
	static final long INDEX_SHIFT = 		0;
	static final long INVULNERABLE_SHIFT = 	15;
	static final long TIMESTAMP_SHIFT = 	16;
	static final long LIFE_SHIFT = 			32;
	static ArrayList<Terrain> terrains = new ArrayList<Terrain>();
	
	/**
	 * This describes the allowed types to traverse this terrain.
	 */
	Traversal collisionData;
	double traversalCost;
	
	public Terrain(Traversal t, double cost) {
		collisionData = t;
		traversalCost = cost;
	}

	@Override
	public EQGraphics getGraphics() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Traversal getTraversalData() {
		return new Traversal(collisionData.getBitMasks());
	}

	/**
	 * Gets the terrain object which the specified handle points to.
	 * @param terrainHandle - The handle to the requested terrain.
	 * @return
	 * The terrain object which corresponds to the specified handle.
	 */
	public static Terrain getTerrain(long terrainHandle) {
		return terrains.get((int) getValue(terrainHandle, INDEX_MASK, INDEX_SHIFT));
	}
	
	/**
	 * Gets the life of the terrain object which the specified handle points to.
	 * @param terrainHandle - The handle to the requested terrain.
	 * @return
	 * The life of the terrain object which corresponds to the specified handle.
	 */
	public static float getLife(long terrainHandle) {
		return Float.intBitsToFloat((int) getValue(terrainHandle, LIFE_MASK, LIFE_SHIFT));
	}
	
	/**
	 * Gets the animation time of the terrain object which the specified handle points to.
	 * @param terrainHandle - The handle to the requested terrain.
	 * @return
	 * The animation time of the terrain object which corresponds to the specified handle.
	 */
	public static long getTimeInAnimation(long terrainHandle) {
		return getValue(terrainHandle, TIMESTAMP_MASK, TIMESTAMP_SHIFT);
	}
	
	/**
	 * Gets the invulnerability status of the terrain object which the specified handle points to.
	 * @param terrainHandle - The handle to the requested terrain.
	 * @return
	 * The invulnerability status of the terrain object which corresponds to the specified handle.
	 */
	public static boolean isInvulnerable(long terrainHandle) {
		return getValue(terrainHandle, INVULNERABLE_MASK, 0) != 0;
	}

	
	public static long setLife(long terrainHandle, float life) {
		long lifeBits = Float.floatToRawIntBits(life);
		return setValue(lifeBits,terrainHandle,LIFE_MASK,LIFE_SHIFT);
	}
	
	public static long setAnimationTime(long terrainHandle, int animationTime) {
		return setValue(animationTime,terrainHandle,TIMESTAMP_MASK,TIMESTAMP_SHIFT);
	}
	
	public static long setInvulnerable(long terrainHandle, boolean invulnerable) {
		long invulnerableBit = invulnerable ? 0x1 : 0x0;
		return setValue(invulnerableBit,terrainHandle,INVULNERABLE_MASK,INVULNERABLE_SHIFT);
	}
	
	
	private static long getValue(long value, long mask, long shift){
		return (value & mask) << shift;
	}
	
	private static long setValue(long value, long terrainHandle, long shift, long mask) {
		long returnValue = terrainHandle & ~mask;
		return returnValue |= value << shift;
	}

	public double getTraversalCost(Traversal t) {
		return 1;
	}
}
