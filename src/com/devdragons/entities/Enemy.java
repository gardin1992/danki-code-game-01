package com.devdragons.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.devdragons.main.Game;
import com.devdragons.world.AStart;
import com.devdragons.world.Camera;
import com.devdragons.world.Vector2i;
import com.devdragons.world.World;

public class Enemy extends Entity {
	
	public static BufferedImage SLIME_EN = Game.spritesheet.getSprite(7*World.TILE_SIZE, 16, World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage GOBLIN_EN = Game.spritesheet.getSprite(7*World.TILE_SIZE, 16, World.TILE_SIZE, World.TILE_SIZE);
	
	// Feedback
	public static BufferedImage SLIME_RIGHT_FB = Game.spritesheet.getSprite(96, 64, World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage SLIME_LEFT_FB = Game.spritesheet.getSprite(96, 64 + World.TILE_SIZE, World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage GOBLIN_RIGHT_FB = Game.spritesheet.getSprite(96, 64 + (2 * World.TILE_SIZE), World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage GOBLIN_LEFT_FB = Game.spritesheet.getSprite(96, 64 + (3 * World.TILE_SIZE), World.TILE_SIZE, World.TILE_SIZE);
		
	protected BufferedImage[] rightSprites;
	protected BufferedImage[] leftSprites;
	
	protected BufferedImage rightDamage;
	protected BufferedImage leftDamage;
	
	public int dir = 0;
	public int right_dir = 0, left_dir = 1, up_dir = 2, down_dir = 3;

	// animation
	protected int frames = 0, maxFrames = 20, index = 0, maxIndex = 1;
	
	public double life = 10, damage = 1;
	
	private boolean isDamaged = false;
	private int damageFrames = 10, damageCurrent = 0;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		
		this.rightSprites = new BufferedImage[4];
		this.leftSprites = new BufferedImage[4];
		
		for(int i = 0; i < 4; i++) {
			this.rightSprites[i] = Game.spritesheet.getSprite(0 + (i*World.TILE_SIZE), 64, World.TILE_SIZE, World.TILE_SIZE);
			this.leftSprites[i] = Game.spritesheet.getSprite(0 + (i*World.TILE_SIZE), 80, World.TILE_SIZE, World.TILE_SIZE);
		}
	}
	
	@Override
	public void tick() {
		// ------ Movimento não inteligente - usar com muitos inimigos
		/*
		if (this.calculateDistance(this.getX(), this.getY(), Game.player.getX(), Game.player.getY()) < 120) {
			
		if (this.isCollidingWithPlayer() == false) {
		
			if ((int)x < Game.player.getX() 
				&& World.isFree((int)(x+speed),  this.getY())
				&& !isColliding((int)(x+speed),  this.getY())
				) {
				dir = right_dir;
				x += speed;	
			}
			else if ((int)x > Game.player.getX() 
				&& World.isFree((int)(x-speed),  this.getY())
				&& !isColliding((int)(x-speed),  this.getY())
			) {
				dir = left_dir;
				x -= speed;
			}
			
			if ((int)y < Game.player.getY() 
				&& World.isFree(this.getX(), (int)(x-speed))
				&& !isColliding(this.getX(), (int)(x-speed))
			) {
				// dir = up_dir;
				y += speed;	
			}
			else if ((int)y > Game.player.getY() 
				&& World.isFree(this.getX(),  (int)(x+speed))
				&& !isColliding(this.getX(), (int)(x+speed))
			) {
				// dir = down_dir;
				y -= speed;
			}
		}
		else {
			// estamos perto do player
			if (Game.rand.nextInt(100) < 10) {
				
				Game.player.life -= damage;
				Game.player.isDamaged = true;
			}
		}
		}
		*/
		
		// ------ movimento com inteligencia melhorada, pega o menor caminho
		if (this.calculateDistance(this.getX(), this.getY(), Game.player.getX(), Game.player.getY()) < 120) {
			if (path == null || path.size() == 0) {
				Vector2i start = new Vector2i((int) x/World.TILE_SIZE, (int) y/World.TILE_SIZE);
				Vector2i end = new Vector2i((int) Game.player.x/World.TILE_SIZE, (int) Game.player.y/World.TILE_SIZE);
				path = AStart.findPath(Game.world, start, end);
			}
			
			followPath(path);
		}
		
		frames++;
				
		if (frames == maxFrames) {
			frames = 0;
			index++;
			
			if (index > maxIndex) 
				index = 0;
		}	
		
		collidingBullet();
		checkCollisionSword();
		
		if (this.life <= 0) {
			Game.score.enemiesKill++;
			Game.enemies.remove(this);
			Game.entities.remove(this);
		}
		
		if (isDamaged) {
			this.damageCurrent++;
			if (this.damageCurrent == this.damageFrames) {
				this.damageCurrent = 0;
				this.isDamaged = false;
			}
		}
	}
	
	public void collidingBullet() {
		for (int i = 0; i < Game.bullets.size(); i++) {
			Entity e = Game.bullets.get(i);
			
			if (Entity.isColliding(this, e)) {
				this.isDamaged = true;
				this.life -= Game.player.damage;
				
				Game.bullets.remove(e);
				Game.entities.remove(e);
				return ;
			}
		}
		
		return ;
	}
	
	public void checkCollisionSword() {
		
		for(int i = 0; i < Game.swords.size(); i++) {
			Entity atual = Game.swords.get(i);
			
			if (Entity.isColliding(this, atual)) {
				this.isDamaged = true;
				this.life  -= Game.player.damage;
					
				Game.swords.remove(atual);
				Game.entities.remove(atual);
			}
		}
		
		return ;
	}
	
	public boolean isCollidingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskx, this.getY() + masky, mwidth, mheight);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), mwidth, mheight);
		
		return enemyCurrent.intersects(player);
	}
	
	public void render(Graphics g) {
		if (!isDamaged) {
			if (dir == right_dir) {
				g.drawImage(rightSprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			}
			else if (dir == left_dir) {
				g.drawImage(leftSprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			}	
		}
		else {
			if (dir == right_dir)
				g.drawImage(rightDamage, this.getX() - Camera.x, this.getY() - Camera.y, null);
			else if (dir == left_dir)
				g.drawImage(leftDamage, this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
	}
}
