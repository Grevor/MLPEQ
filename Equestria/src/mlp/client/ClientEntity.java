package mlp.client;

import mlp.core.ClientEntityStats;
import mlp.core.EQGraphics;
import mlp.core.Entity;
import mlp.core.MovementData;

public class ClientEntity implements Entity {
	EQGraphics graphicsModule;
	ClientEntityStats stats;
	MovementData movement;
	
	public EQGraphics getGraphics() {
		return graphicsModule;
	}
	
	public ClientEntityStats getStats() {
		return stats;
	}
	
	public MovementData getMovementData() {
		return movement;
	}

	@Override
	public int getXPosition() {
		return movement.getX();
	}

	@Override
	public int getYPosition() {
		return movement.getY();
	}
	
	@Override
	public int getZPosition() {
		return movement.getZ();
	}

	@Override
	public void tick(long delta) {
		graphicsModule.addAnimationTime(delta);
	}

	

	@Override
	public void moveTo(int x, int y, int z) {
		movement.setPosition(x,y,z);
	}
}
