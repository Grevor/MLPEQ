package mlp.server.chunk;

import java.util.LinkedList;

import javax.media.opengl.GL2;

import viking.game.movement.Traversal;
import mlp.core.EQGraphics;
import mlp.core.Entity;
import mlp.server.Terrain;

public class StandardChunk implements Chunk {
	TerrainChunk terrainLayer1, terrainLayer2;
	private final int positionX;
	private final int positionY;
	
	protected LinkedList<Entity> entities = new LinkedList<>();

	public StandardChunk(int x, int y) {
		positionX = x;
		positionY = y;
	}

	/* (non-Javadoc)
	 * @see mlp.server.Chunk#getTerrainHandleLayer1(int, int)
	 */
	@Override
	public long getTerrainHandle(int x, int y) {
		return terrainLayer1.getTerrainHandle(x, y);
	}

	/* (non-Javadoc)
	 * @see mlp.server.Chunk#getTerrainHandleLayer2(int, int)
	 */
	@Override
	public long getTerrainHandleLayer2(int x, int y) {
		return terrainLayer2.getTerrainHandle(x, y);
	}







	public void render(GL2 gl) {
		renderTerrain(gl);
		renderEntities(gl);
	}

	private void renderEntities(GL2 gl) {
		for(Entity e : entities) {
			EQGraphics g = e.getGraphics();
			g.getTexture().bindGLTexture(gl);
			gl.glBegin(GL2.GL_QUADS);
			g.render(gl);
			gl.glEnd();
			g.getTexture().unbindGLTexture(gl);
		}
	}

	private void renderTerrain(GL2 gl) {
		for (int x = 0; x < SIZE_X; x++) {
			for (int y = 0; y < SIZE_Y; y++) {
				long terrainDesc = getTerrainHandle(x, y);
				EQGraphics g = Terrain.getTerrain(terrainDesc).getGraphics();
				g.setPosition(positionX + x, positionY + y);
				g.getTexture().bindGLTexture(gl);
				gl.glBegin(GL2.GL_QUADS);
				g.render(gl);
				gl.glEnd();
				g.getTexture().unbindGLTexture(gl);
			}
		}

		for (int x = 0; x < SIZE_X; x++) {
			for (int y = 0; y < SIZE_Y; y++) {
				long terrainDesc = getTerrainHandleLayer2(x, y);
				EQGraphics g = Terrain.getTerrain(terrainDesc).getGraphics();
				g.setPosition(positionX + x, positionY + y);
				g.getTexture().bindGLTexture(gl);
				gl.glBegin(GL2.GL_QUADS);
				g.render(gl);
				gl.glEnd();
				g.getTexture().unbindGLTexture(gl);
			}
		}
	}

	public void setTerrain(int x, int y, long terrainHandle) {
		terrainLayer1.setTerrain(x, y, terrainHandle);
	}
	
	@Override
	public void setTerrainLayer2(int x, int y, long terrainHandle) {
		terrainLayer2.setTerrain(x, y, terrainHandle);
	}

	@Override
	public boolean isImmutable() {
		return false;
	}

	@Override
	public int getXPosition() {
		return positionX;
	}

	@Override
	public int getYPosition() {
		return positionY;
	}

	@Override
	public void tick(long delta) {
		terrainLayer1.tick(delta);
		terrainLayer2.tick(delta);
		
		for(Entity e : entities)
			e.tick(delta);
	}

	@Override
	public void addEntity(Entity e) {
		if(entities == EMPTY_ENTITY_LIST) {
			entities = new LinkedList<>();
		}
		entities.add(e);
	}

	@Override
	public void removeEntity(Entity e) {
		entities.remove(e);
		if(entities.isEmpty()) entities = EMPTY_ENTITY_LIST;
	}
	
	/**
	 * Here follows the static part of the StandardChunk. This is an interned list which is empty.
	 */
	private static final LinkedList<Entity> EMPTY_ENTITY_LIST = new LinkedList<Entity>();

	@Override
	public boolean canTraverse(int x, int y, Traversal t) {
		return terrainLayer1.canTraverse(x, y, t)
				&& terrainLayer2.canTraverse(x, y, t)
				&& noEntityCollision(x, y, t);
	}

	private boolean noEntityCollision(int x, int y, Traversal t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInChunk(int x, int y) {
		int cx = getXPosition();
		int cy = getYPosition();
		return 
				cx <= x 
				&& x < cx + SIZE_X 
				&& cy <= y 
				&& y < cy + SIZE_Y; 
	}

	@Override
	public double getTraversalCost(int x, int y, Traversal t) {
		return Math.max(this.terrainLayer1.getTraversalCost(x, y, t), this.terrainLayer2.getTraversalCost(x, y, t));
	}
}
