package mlp.core;

import java.awt.geom.Rectangle2D;

import javax.media.opengl.GL2;

import viking.game.graphics2d.AnimatedTexture2D;
import viking.game.graphics2d.Vector2F;

public class EQGraphics {
	AnimatedTexture2D texture;
	Vector2F position = new Vector2F();
	Vector2F size = new Vector2F();
	String currentAnimation;
	long timeSinceAnimationStart;
	
	public EQGraphics(AnimatedTexture2D texture, String animation, Vector2F position, Vector2F size) {
		this.texture = texture;
		changeAnimation(animation);
		this.position = position.copy();
		this.size = size.copy();
	}
	
	/**
	 * Draws this object onto the screen.
	 * The correct texture must be bound for this to take place, and GL_QUADS must be the primitive-type.
	 * @param g - The GL-implementation to use.
	 */
	public void render(GL2 g) {
		Rectangle2D.Float textureRectangle = texture.getFrameRectangle(currentAnimation, timeSinceAnimationStart);
		float x = getX();
		float y = getY();
		float w = getWidth();
		float h = getHeight();
		
		float tx = textureRectangle.x;
		float ty = textureRectangle.y;
		float tw = textureRectangle.width;
		float th = textureRectangle.height;
		
		g.glVertex2f(x,y);
		g.glColor3b(Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE);
		g.glTexCoord2f(tx,ty);
	
		g.glVertex2f(x,y + h);
		g.glTexCoord2f(tx,ty + th);
		
		g.glVertex2f(x + w,y + h);
		g.glTexCoord2f(tx + tw,ty + th);
		
		g.glVertex2f(x + w,y);
		g.glTexCoord2f(tx + tw,ty);
	}
	
	public AnimatedTexture2D getTexture(){
		return texture;
	}
	
	public void changeAnimation(String animationName) {
		if(texture.hasAnimation(animationName)) {
			currentAnimation = animationName;
			timeSinceAnimationStart = 0;
		}
		else throw new IllegalArgumentException("Could not find any animation by the name " + animationName + ".");
	}
	
	public void changeAnimation(int animationIndex) {
		if(0 <= animationIndex && animationIndex < texture.getNumberOfAnimations()) {
			changeAnimation(texture.getAnimation(animationIndex).getName());
		}
		else {
			throw new IllegalArgumentException("Expected an animation-index in the range [0, " + texture.getNumberOfAnimations()+"), but found " + animationIndex);
		}
	}
	
	public void addAnimationTime(long delta) {
		setAnimationTime(timeSinceAnimationStart + delta);
	}
	
	public void setAnimationTime(long newTimeSinceAnimationStart) {
		if(newTimeSinceAnimationStart < 0) throw new IllegalArgumentException("Animation time must be positive, but found " + newTimeSinceAnimationStart + ".");
		this.timeSinceAnimationStart = newTimeSinceAnimationStart;
	}
	
	public void setPosition(float x, float y) {
		position.x = x;
		position.y = y;
	}
	
	public void translate(float x, float y) {
		position.translate(x, y);
	}
	
	public float getX(){ return position.x;}
	public float getY(){ return position.y;}
	
	public Vector2F getPosition() { return position; }
	
	public float getWidth(){ return size.x;}
	public float getHeight(){ return size.y;}
	
	public Vector2F getSize() { return size; }
}
