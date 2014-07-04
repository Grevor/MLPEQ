package mlp.core;

import java.io.DataOutputStream;

import mlp.server.Layer;
import viking.game.graphics2d.Vector3I;
import viking.game.movement.Traversal;

public class MovementData {
	public static final long MOVEMENT_MASK_HOOF = 0x1 << 0;
	public static final long MOVEMENT_MASK_SWIM = 0x1 << 1;
	public static final long MOVEMENT_MASK_FLIGHT = 0x1 << 2;
	public static final long MOVEMENT_MASK_PROJECTILE_HORIZONTAL = 0x1 << 3;
	public static final long MOVEMENT_MASK_PROJECTILE_VERTICAL = 0x1 << 4;
	private static final double SQRT_OF_TWO = Math.sqrt(2);
	
	public enum MovementDirectionType {
		Straight,
		Diagonal,
		UpOrDownFlight
	}
	
	public enum MovementDirection {
		N,
		NE,
		E,
		SE,
		S,
		SW,
		W,
		NW,
		Up,
		Down
	}
	
	public static MovementDirection getMoveDirection(int x, int y) {
		if(x < 0) {
			if(y < 0) {
				return MovementDirection.NW;
			}else if(y > 0) {
				return MovementDirection.SW;
			} else {
				return MovementDirection.W;
			}
		} else if(x > 0) {
			if(y < 0) {
				return MovementDirection.NE;
			}else if(y > 0) {
				return MovementDirection.SE;
			} else {
				return MovementDirection.E;
			}
		}
		else {
			if(y < 0) {
				return MovementDirection.N;
			}else if(y > 0) {
				return MovementDirection.S;
			} else {
				return null;
			}
		}
	}
	
	public static double getMovementFactor(MovementDirection dir) {
		switch(dir) {
		case E:
		case W:
		case S:
		case N:
			return 1;
		case NE:
		case NW:
		case SE:
		case SW:
		case Up:
		case Down:
			return SQRT_OF_TWO;
		default:
			return Double.POSITIVE_INFINITY;
		
		}
	}
	
	Vector3I position;
	
	long timeSinceLastMove;
	long timePerSquare;
	long timeUntilMoveOK;
	Vector3I destination;
	Entity target;
	Traversal collisionData;
	
	public MovementData(long timePerSquareMoved, int x, int y, int z) {
		setTimePerSquare(timePerSquareMoved);
		destination = new Vector3I(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
		position = new Vector3I(x, y, z);
		target = null;
	}
	
	public void setTimePerSquare(long time) {
		timePerSquare = time;
	}
	
	boolean hasDestination() {
		return destination.x != Integer.MAX_VALUE && destination.y != Integer.MAX_VALUE && destination.z != Integer.MAX_VALUE;
	}
	
	boolean hasTarget() {
		return target != null;
	}
	
	public void setDestination(int x, int y, int z) {
		destination.x = x;
		destination.y = y;
		destination.z = z;
	}
	
	public void setPosition(int x, int y, int z) {
		position.x = x;
		position.y = y;
		position.z = z;
	}
	
	void setDestinationToTargetEntity() {
		setDestination(target.getXPosition(),target.getYPosition(), target.getZPosition());
	}
	
	public void tick(long delta, Layer[] layer) {
		if(hasTarget()) setDestinationToTargetEntity();
		
	}
	
	public void setTarget(Entity e) {
		target = e;
		if(e != null && e.getZPosition() != this.getZ()) target = null;
	}
	
	/**
	 * Sync the movement data of this entity with the server. Currently this means only to hard set it to the server values.
	 */
	public void syncMovementData() {
		
	}
	
	/**
	 * Sends this movementData from the server to any requesting client.
	 */
	public void sendMovementData(DataOutputStream connection, long serverTick) {
		try{
			connection.writeLong(serverTick);
			connection.writeInt(position.x);
			connection.writeInt(position.y);
			connection.writeInt(position.z);
			connection.writeLong(timeSinceLastMove);
			connection.writeLong(timeUntilMoveOK);
			connection.writeLong(timePerSquare);
		} catch(Exception e) {

		}
	}
	
	public int getX() { return position.x; }
	public int getY() { return position.y; }
	public int getZ() { return position.z; }
	
}
