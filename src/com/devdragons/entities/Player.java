package com.devdragons.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.devdragons.entities.Weapon.WeaponTypes;
import com.devdragons.main.Game;
import com.devdragons.main.Sound;
import com.devdragons.world.Camera;
import com.devdragons.world.World;

public class Player extends Entity {

	public boolean right, up, left, down;
	// criar um enum
	public int right_dir = 0, left_dir = 1, up_dir = 2, down_dir = 3;
	public int dir = 0;
	private double speed = 1.4;
	
	private int frames = 0, maxFrames = 20, index = 0, maxIndex = 3;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	
	private BufferedImage playerRightDamage;
	private BufferedImage playerLeftDamage;

	private boolean hasGun = false;
	
	public int ammo = 0;
	
	public boolean isDamaged = false;
	private int damageFrames = 0;
	
	public double life = 100, maxLife = 100, damage = 10;
	public boolean shoot = false, mouseShoot = false;
	public int mx, my;
	
	// --- jump
	public boolean jump = false, 
			isJumping = false, 
			jumpUp = false, 
			jumpDown = false;
	public int z = 0;
	public int jumpFrames = 50, jumpCur = 0;
	public int jumpSpeed = 2;
	
	// ----- attack deve ficar na arma
	public boolean inAttack = false;
	private BufferedImage[] spritesAttackRight;
	private BufferedImage[] spritesAttackLeft;
	protected int framesAttack = 0, maxFramesAttack = 3, indexAttack = 0, maxIndexAttack = 2;
	// ------ inventory
	private List<Weapon> weapons; 
	private int currentWeapon = 0;
	private Weapon.WeaponTypes currentWeaponType;
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param sprite
	 */
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		
		playerRightDamage = Game.spritesheet.getSprite(0, 32, World.TILE_SIZE, World.TILE_SIZE);
		playerLeftDamage = Game.spritesheet.getSprite(16, 32, World.TILE_SIZE, World.TILE_SIZE);
		
		spritesAttackRight = new BufferedImage[3];
		spritesAttackLeft = new BufferedImage[3]; 
		weapons = new ArrayList<Weapon>();
		
		for(int i = 0; i < 4; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32 + (World.TILE_SIZE*i), 0, World.TILE_SIZE, World.TILE_SIZE);
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (World.TILE_SIZE*i), 16, World.TILE_SIZE, World.TILE_SIZE);
		}
		
		for(int i = 0; i < 3; i++) {
			spritesAttackRight[i] = Game.spritesheet.getSprite(96 + (World.TILE_SIZE*i), 48, World.TILE_SIZE, World.TILE_SIZE);
			spritesAttackLeft[i] = Game.spritesheet.getSprite(48 + (World.TILE_SIZE*i), 48, World.TILE_SIZE, World.TILE_SIZE);
		}
	}
	
	private void tickJump() {
		if (jump) {
			if (isJumping == false) {
				jump = false;	
				isJumping = true;
				jumpUp = true;
				
			}
		}
		
		if (isJumping == true) {
				
				if (jumpUp) {
					jumpCur+= jumpSpeed;
				}
				else if (jumpDown) {
					jumpCur -= jumpSpeed;
					
					if (jumpCur <= 0) {
						isJumping = false;
						jumpUp = false;
						jumpDown = false;
					}
				}
				
				z = jumpCur;
				if (jumpCur >= jumpFrames) {
					jumpUp = false;
					jumpDown = true;
				}
		}
	}
	
	public void tick() {
		depth = 1;
		tickJump();
		
		moved = false;
		
		if (right && World.isFree((int)(x + speed), this.getY())) {
			moved = true;
			dir = right_dir;
			x += speed;
		}
		else if (left && World.isFree((int)(x-speed),this.getY())) {
			moved = true;
			dir = left_dir;
			x -= speed;
		}
		if (up && World.isFree(this.getX(), (int)(y-speed))) {
			moved = true;
			dir = up_dir;
			y -= speed;
		}
		else if (down && World.isFree(this.getX(), (int)(y+speed))) {
			moved = true;
			dir = down_dir;
			y += speed;
		}
		
		if (moved) {
			frames++;
			
			if (frames == maxFrames) {
				frames = 0;
				index++;
				
				if (index > maxIndex) 
					index = 0;
			}
		}
		
		tickAttack();
		
		this.checkCollisionGun();
		this.checkCollisionAmmo();
		this.checkCollisionLifepack();
		
		if (isDamaged) {
			this.damageFrames++;
			if (this.damageFrames == 8) {
				this.damageFrames = 0;
				isDamaged = false;
			}
		}
		
		if (shoot) {
			shoot = false;
			attackShoot();
		}
		
		if (mouseShoot) {
			mouseShoot = false;
			attackMouseShoot();
		}
		
		if (life <= 0) {
			life = 0; 
			Game.state = Game.State.GAME_OVER;
			Game.initialized("level1.png");
			return ;
		}
		
		updateCamera();
	}
	
	public void updateCamera() {
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDHT*16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT*16 - Game.HEIGHT);
	}
	
	private void tickAttack()
	{
		if (inAttack) {
			framesAttack++;
			
			if (framesAttack == maxFramesAttack) {
				framesAttack = 0;
				indexAttack++;
				
				if (indexAttack > maxIndexAttack) {
					indexAttack = 0;
					inAttack = false;
				}
			}
		}
	}
	
	private void attackMouseShoot() {
		if (!hasGun)
			return;
		
		if (currentWeaponType == WeaponTypes.GUN) {
			if(ammo > 0) {
				ammo--;
				double angle;
				int px = 0;
				int py = 8;
				if (dir == right_dir) {
					px = 14;
					angle = Math.atan2(my - (this.getY()+py-Camera.y), mx - (this.getX()+py-Camera.x));
				}
				else {
					px = -4;
					angle = Math.atan2(my - (this.getY()-py-Camera.y), mx - (this.getX()-py-Camera.x));
				}
				
				double dx = Math.cos(angle);
				double dy = Math.sin(angle);
				
				SkillBullet bullet = new SkillBullet(this.getX() + px, this.getY() + py, 3, 3, null, dx, dy);
				Game.bullets.add(bullet);
			}
		}
		else if (currentWeaponType == WeaponTypes.SWORD) {
			SkillSword sword = new SkillSword(this.getX(), this.getY(), 16, 16, null);
			Game.swords.add(sword);
		}
	}
	
	private void attackShoot() {
		int px = 8;
		int py = 8;
		
		if (!hasGun)
			return ;
		
		if (currentWeaponType == WeaponTypes.GUN) {
			if(ammo > 0) {
				ammo--;
				// criar bala e atirar
				int dx = 0;
				int dy = 0;
				if (dir == right_dir) {
					px = 14;
					dx = 1;
				}
				else {
					px = -4;
					dx = -1;
				}
				
				SkillBullet bullet = new SkillBullet(this.getX() + px, this.getY() + py, 3, 3, null, dx, dy);
				Game.bullets.add(bullet);
				
				Sound.shootEffect.play();
			}
		}
		else if (currentWeaponType == WeaponTypes.SWORD) {
			Sound.swordEffect.play();
			
			SkillSword sword = new SkillSword(this.getX(), this.getY(), 16, 16, null);
			Game.swords.add(sword);
		}
	}
	
	public void checkCollisionAmmo() {
		for(int i = 0; i < Game.ammos.size(); i++) {
			Entity atual = Game.ammos.get(i);
			
			if (Entity.isColliding(this, atual)) {
				ammo += 10;
				
				Game.ammos.remove(atual);
				Game.entities.remove(atual);
			}
		}
	}
	
	public void checkCollisionGun() {
		for(int i = 0; i < Game.weapons.size(); i++) {
			Weapon atual = Game.weapons.get(i);
			
			if (Entity.isColliding(this, atual)) {
				hasGun = true;
				currentWeaponType = atual.weaponType;
				
				Game.weapons.remove(atual);
				Game.entities.remove(atual);
				weapons.add(atual);
			}
		}
	}
	
	public void checkCollisionLifepack() {
		for(int i = 0; i < Game.lifepacks.size(); i++) {
			Entity atual = Game.lifepacks.get(i);
			
			if (Entity.isColliding(this, atual)) {
				life += 10;
				if (life >= maxLife)
					life = maxLife;
					
				Game.lifepacks.remove(atual);
				Game.entities.remove(atual);
			}
		}
	}
	
	/*------------- MOVE ----*/
	public void moveRight(Graphics g) {
		g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y -z, null);
		
		if (hasGun) {
			// Desenhar arma para direita
			switch (currentWeaponType) {
				case GUN:
					g.drawImage(Weapon.GUN_RIGHT, this.getX() + 8 - Camera.x, this.getY() - Camera.y -z, null); 
					break;
				case SWORD:
					g.drawImage(Weapon.SWORD_RIGHT, this.getX() + 8 - Camera.x, this.getY() - Camera.y -z, null); 
					
					if (inAttack) {
						g.drawImage(spritesAttackRight[indexAttack], this.getX() + 16 - Camera.x, this.getY() - Camera.y -z, null);
					}
					
					break;
				default:
					g.drawImage(Weapon.GUN_RIGHT, this.getX() + 8 - Camera.x, this.getY() - Camera.y -z, null);
			}
		}
	}
	
	public void moveLeft(Graphics g) {
		g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y -z, null);
		
		if (hasGun) {
			switch (currentWeaponType) {
			case GUN:
					g.drawImage(Weapon.GUN_LEFT, this.getX() - 8 - Camera.x, this.getY() - Camera.y -z, null);	
					break;
				case SWORD:
					g.drawImage(Weapon.SWORD_LEFT, this.getX() - 8 - Camera.x, this.getY() - Camera.y -z, null);
					
					if (inAttack) {
						g.drawImage(spritesAttackLeft[indexAttack], this.getX() - 16 - Camera.x, this.getY() - Camera.y -z, null);
					}
					break;
			}
		}
	}
	
	public void renderTakeDamege(Graphics g) {
		if (dir == right_dir)
			g.drawImage(playerRightDamage, this.getX() - Camera.x, this.getY() - Camera.y -z, null);
		else if (dir == left_dir)
			g.drawImage(playerLeftDamage, this.getX() - Camera.x, this.getY() - Camera.y -z, null);
	}
	
	public void renderTakeBody(Graphics g) {
		if (dir == right_dir) {
			
			moveRight(g);
		}
		else {
			moveLeft(g);
		}
	}
	
	public void render(Graphics g) {
		if (!isDamaged) {
			renderTakeBody(g);
			
		}
		else {
			renderTakeDamege(g);
		}
		
		if (isJumping) {
			g.setColor(Color.black);
			g.fillOval(this.getX() - Camera.x - 4, this.getY() - Camera.y - 4, 8, 8);
		}
	}
	
	/* ------ choose weapon */
	public void nextWeapon() {
		if (weapons.size() > 0) {
			currentWeapon++;
			
			if (currentWeapon == weapons.size()) {
				currentWeapon = 0;
			}	
			
			currentWeaponType = weapons.get(currentWeapon).weaponType;	
		}
		
	}
	
	public void prevWeapon() {
		if (weapons.size() > 0) {
			currentWeapon--;
			
			if (currentWeapon < 0) {
				currentWeapon = weapons.size() - 1;
			}	
			
			currentWeaponType = weapons.get(currentWeapon).weaponType;
		}
		
	}
}
