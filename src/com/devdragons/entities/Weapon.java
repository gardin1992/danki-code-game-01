package com.devdragons.entities;

import java.awt.image.BufferedImage;

import com.devdragons.main.Game;
import com.devdragons.world.World;

public class Weapon extends Entity {
	
	public static enum WeaponTypes {
		GUN, SWORD
	}
	
	public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(7*World.TILE_SIZE, 16, World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage SWORD_EN = Game.spritesheet.getSprite(6*World.TILE_SIZE, 16, World.TILE_SIZE, World.TILE_SIZE);
	
	// WEAPONG EQUIPED
	public static BufferedImage GUN_RIGHT = Game.spritesheet.getSprite(8*World.TILE_SIZE, 16, World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage GUN_LEFT = Game.spritesheet.getSprite(9*World.TILE_SIZE, 16, World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage SWORD_RIGHT = Game.spritesheet.getSprite(8*World.TILE_SIZE, 32, World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage SWORD_LEFT = Game.spritesheet.getSprite(9*World.TILE_SIZE, 32, World.TILE_SIZE, World.TILE_SIZE);
	
	public double damage = 0;
	public WeaponTypes weaponType;
	
	public Weapon(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}
}
