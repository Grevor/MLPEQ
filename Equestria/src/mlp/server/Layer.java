package mlp.server;

import java.io.InputStream;

import mlp.server.chunk.Chunk;

/**
 * Class representing a layer of the game-world.
 * @author Grevor
 *
 */
public class Layer {
	public static final int SIZE_X = 1000;
	public static final int SIZE_Y = 1000;
	
	private Chunk[] chunkArray = new Chunk[SIZE_X * SIZE_Y];

	private final int[][] chunkIndices = new int[SIZE_X][SIZE_Y];
	
	private Layer(InputStream stream) {
		// TODO Auto-generated constructor stub
	}

	public Chunk getChunk(int x, int y) {
		return chunkArray[chunkIndices[x][y]];
	}
	
	public Chunk getChunkFromWorldCoordinates(int x, int y) {
		return getChunk(x / Chunk.SIZE_X, y / Chunk.SIZE_Y);
	}
	
	public static Layer fromStream(InputStream stream) {
		return new Layer(stream);
	}
}
