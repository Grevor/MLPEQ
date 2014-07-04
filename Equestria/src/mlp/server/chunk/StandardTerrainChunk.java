package mlp.server.chunk;

public class StandardTerrainChunk extends TerrainChunkBase {
	final long[][] terrainHandles = new long[Chunk.SIZE_X][Chunk.SIZE_Y];

	@Override
	public long getTerrainHandle(int x, int y) {
		return terrainHandles[x][y];
	}

	@Override
	public void tick(long delta) {
		//TODO: add ticking of terrain here.
	}

	@Override
	public void setTerrain(int x, int y, long terrainHandle) {
		this.terrainHandles[x][y] = terrainHandle;
		
	}

	@Override
	public boolean isImmutable() {
		return false;
	}

}
