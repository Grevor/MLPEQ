package mlp.server.chunk;

import mlp.server.Terrain;
import viking.game.movement.Traversal;
import viking.game.movement.Traversal.MatchType;

public abstract class TerrainChunkBase implements TerrainChunk {

	public TerrainChunkBase() {
		super();
	}

	@Override
	public boolean canTraverse(int x, int y, Traversal t) {
		return Terrain.getTerrain(getTerrainHandle(x, y)).getTraversalData().canTraverse(t, MatchType.OneOrMoreOverlap);
	}

}