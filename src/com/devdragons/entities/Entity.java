package com.devdragons.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

import com.devdragons.main.Game;
import com.devdragons.world.Camera;
import com.devdragons.world.Node;
import com.devdragons.world.Vector2i;
import com.devdragons.world.World;

public class Entity {

	public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(6*World.TILE_SIZE, 0, World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(7*World.TILE_SIZE, 32, World.TILE_SIZE, World.TILE_SIZE);
	
	protected double x;
	protected double y;
	protected double z;
	protected int width;
	protected int height;

	protected int maskx = 0, masky = 0, mwidth, mheight;
	
	private BufferedImage sprite;
	
	protected List<Node> path;
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		this.maskx = 0;
		this.masky = 0;
		this.mheight = height;
		this.mwidth = width;
	}
	
	public void setMask(int maskx, int masky, int mwidth, int mheight) {
		this.maskx = maskx;
		this.masky = masky;
		this.mheight = mheight;
		this.mwidth = mwidth;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	public int getX()
	{
		return (int) this.x;
	}
	
	public int getY()
	{
		return (int) this.y;
	}
	
	public int getWidth()
	{
		return this.width;
	}
	
	public int getHeight()
	{
		return this.height;
	}
	
	public void tick() {
		
	}
	
	public double calculateDistance(int x1, int y1, int x2, int y2) {
		return Math.sqrt((x1 - x2 )* (x1 - x2) + (y1 - y2) * (y1 - y2));
	}
	
	public void followPath(List<Node> path) {
		
		if(path != null) {
			if (path.size() > 0) {
				Vector2i target = path.get(path.size() - 1).tile;
				// int xprev = x;
				// int yprev = y;
				
				if (x < target.x * World.TILE_SIZE)
					x++;
				else if (x > target.x * World.TILE_SIZE)
					x--;
				
				if(y < target.y * World.TILE_SIZE)
					y++;
				else if (y > target.y * World.TILE_SIZE)
					y--;
				
				if (x == target.x * World.TILE_SIZE && y == target.y * World.TILE_SIZE) {
					path.remove(path.size() -1);
				}
			}
		}
	}
	
	public boolean isColliding(int xnext, int ynext) {
		Rectangle enemyCurrent = new Rectangle(xnext + maskx, ynext + masky, mwidth, mheight);
		
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if (e == this)
				continue;
			
			Rectangle targetEnemy = new Rectangle(e.getX()+ maskx, e.getY() + masky, mwidth, mheight);
		
			if (enemyCurrent.intersects(targetEnemy)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isColliding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx, e1.getY() + e1.masky, e1.mwidth, e1.mheight);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx, e2.getY() + e2.masky, e2.mwidth, e2.mheight);
		
		if (e1Mask.intersects(e2Mask) && e1.z == e2.z)
			return true;
		return false;
	}

	public void render(Graphics g) {
		g.drawImage(sprite, (int) this.getX() - Camera.x, (int) this.getY() - Camera.y, null);
		// g.setColor(Color.RED);
		// g.fillRect((int) this.getX() + maskx - Camera.x, (int) this.getY() + masky - Camera.y, mwidth, mheight);
	}
	
}
