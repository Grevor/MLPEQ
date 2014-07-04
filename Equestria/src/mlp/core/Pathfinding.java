package mlp.core;

import java.util.BitSet;
import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Map.Entry;

import mlp.core.MovementData.MovementDirection;
import mlp.server.Layer;
import mlp.server.chunk.Chunk;
import viking.game.graphics2d.Vector3I;
import viking.game.movement.Traversal;

public class Pathfinding {
	static final int upperXLimit = Layer.SIZE_X * Chunk.SIZE_X, upperYLimit = Layer.SIZE_Y * Chunk.SIZE_Y;
	static Hashtable<Long, Hashtable<Chunk, BitSet>> bitmapHash = new Hashtable<>();
	static Layer[] layers;
	
	public static void setUpPathfinder(Layer[] layers) {
		if(Pathfinding.layers != null) Pathfinding.layers = layers;
	}
	
	/**
	 * Clears the pathfinders inner buffers and bitmaps. This can be used if 
	 */
	public static void clearPathfinder() {
		bitmapHash.clear();
	}
	
	private static BitSet createCollisionBitmap(Chunk c, Traversal t) {
		Hashtable<Chunk, BitSet> hash = bitmapHash.get(t.getBitMasks());
		if(hash == null) {
			hash = new Hashtable<>();
			bitmapHash.put(t.getBitMasks(), hash);
		}
		
		BitSet bitmap = hash.get(c);
		if(bitmap == null) {
			bitmap = new BitSet(Chunk.SIZE_X * Chunk.SIZE_Y);
			hash.put(c, bitmap);
		}
		
		for(int x = 0; x < Chunk.SIZE_X; x++) {
			for(int y = 0; y < Chunk.SIZE_Y; y++) {
				bitmap.set(x * Chunk.SIZE_Y + y, c.canTraverse(x, y, t));
			}
		}
		return bitmap;
	}
	
	private static BitSet getBitmap(Chunk c, Traversal t) {
		Hashtable<Chunk, BitSet> bitHash = bitmapHash.get(t.getBitMasks());
		if(bitHash == null) return null;
		return bitHash.get(c);
	}
	
	public static boolean canMoveTo(Traversal t, Vector3I pos) {
		return canMoveTo(t,pos.x,pos.y,pos.z);
	}
	public static boolean canMoveTo(Traversal t, int x, int y, int z) {
		if(!withinBounds(x, y)) return false;
		Chunk requestedChunk = layers[z].getChunkFromWorldCoordinates(x, y);
		BitSet set = getBitmap(requestedChunk, t);
		if(set == null) set = createCollisionBitmap(requestedChunk, t);
		return set.get((x - requestedChunk.getXPosition() * Chunk.SIZE_Y + (y - requestedChunk.getYPosition())));
	}
	
	
	private static boolean withinBounds(int x, int y) {
		return 0 <= x && x < upperXLimit && 0 <= y && y < upperYLimit;
	}

	/**
	 * Gets the movement direction of a certain entity in the game. This entity is then thought to have been moved, forcing a rebuilding of the chunks bitmap.
	 * @param t - The traversal of the object which is traveling.
	 * @param start - The start position of the search.
	 * @param end - The end position of the search.
	 * @return
	 */
	public static MovementData.MovementDirection aStarSearch(Traversal t, Vector3I start, Vector3I end) {
		final int z = start.z;
		if(start.z != end.z || (z < 0 && z > layers.length) || canMoveTo(t, end)) return null;
		PriorityQueue<MovementPath> paths = new PriorityQueue<>();
		//Add the first square .
		paths.add(new MovementPath(null, 0, start.x, start.y, 0));
		
		while(!paths.isEmpty()) {
			MovementPath path = paths.poll();
			
			for(int x = -1; x < 2; x++) {
				for(int y = -1; y < 2; y++) {
					//If we get (0,0), skip as it will just become a headache.
					if(x == 0 && y == 0) continue;
					int newX = path.x + x;
					int newY = path.y + y;
					//If done, end the search. If not, add path of traversal is possible.
					if(end.x == newX && end.y == newY) return path.initialDirection;
					if(canMoveTo(t,newX,newY,z)) paths.add(
							new MovementPath(path.initialDirection == null ? MovementData.getMoveDirection(x, y) : path.initialDirection, 
									path.cost + getTerrainCost(t, newX,newY,z) * MovementData.getMovementFactor(MovementData.getMoveDirection(x, y)), 
									newX, newY, getDistance(end.x,end.y,newX,newY)));
				}
			}
		}
		return null;
	}
	
	private static double getDistance(int x, int y, int dx, int dy) {
		double xx = x - dx;
		double yy = y - dy;
		return Math.sqrt(xx * xx + yy * yy);
	}
	
	private static double getTerrainCost(Traversal t, int newX, int newY, int z) {
		Chunk c = layers[z].getChunkFromWorldCoordinates(newX, newY);
		return c.getTraversalCost(newX - c.getXPosition(), newY - c.getYPosition(), t);
	}

	/**
	 * Recalculate the specified points in the game-world, updating their pathfinding-status.
	 * @param xSrc
	 * @param ySrc
	 * @param zSrc
	 * @param xDest
	 * @param yDest
	 * @param zDest
	 */
	public static void recalculatePoints(int xSrc, int ySrc, int zSrc, int xDest, int yDest, int zDest) {
		Chunk srcChunk = layers[zSrc].getChunk(xSrc, ySrc);
		Chunk destChunk = layers[zDest].getChunk(xDest, yDest);
		//Set the bitmaps for each of the traversal types, given that the bit set actually exist for the specified position.
		for(Entry<Long, Hashtable<Chunk, BitSet>> entry : bitmapHash.entrySet()) {
			Hashtable<Chunk, BitSet> hash = entry.getValue();
			BitSet bitmap = hash.get(srcChunk);
			if(bitmap != null) {
				int x = (xSrc - srcChunk.getXPosition());
				int y = ySrc - srcChunk.getYPosition();
				bitmap.set(x * Chunk.SIZE_Y + y, srcChunk.canTraverse(x, y, new Traversal(entry.getKey())));
			}
			
			bitmap = hash.get(destChunk);
			if(bitmap != null) {
				int x = (xDest - destChunk.getXPosition());
				int y = yDest - destChunk.getYPosition();
				bitmap.set(x * Chunk.SIZE_Y + y, destChunk.canTraverse(x, y, new Traversal(entry.getKey())));
			}
		}
	}
	
	private static class MovementPath implements Comparable<MovementPath> {
		final MovementDirection initialDirection;
		final double cost, estimate;
		final int x, y;
		
		public MovementPath(MovementDirection dir, double cost, int x, int y, double estimate) {
			initialDirection = dir;
			this.cost = cost;
			this.x = x; 
			this.y = y;
			this.estimate = estimate;
		}

		@Override
		public int compareTo(MovementPath arg0) {
			return (int)Math.signum((this.cost + estimate) - (arg0.cost + arg0.estimate));
		}
	}
}
