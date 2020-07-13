package com.devdragons.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.devdragons.main.Game;

public class Tile {
	
	public static BufferedImage TILE_FLOOR = Game.spritesheet.getSprite(0, 0, World.TILE_SIZE, World.TILE_SIZE);
	// ------ Wall
	public static BufferedImage TILE_WALL = Game.spritesheet.getSprite(16, 0, World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage TILE_WALL_TOP_LEFT = Game.spritesheet.getSprite(0, 240, World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage TILE_WALL_TOP_RIGHT = Game.spritesheet.getSprite(64, 240, World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage TILE_WALL_BOTTOM_LEFT = Game.spritesheet.getSprite(0, 272, World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage TILE_WALL_BOTTOM_RIGHT = Game.spritesheet.getSprite(64, 272, World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage TILE_WALL_TOP = Game.spritesheet.getSprite(16, 240, World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage TILE_WALL_BOTTOM = Game.spritesheet.getSprite(16, 272, World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage TILE_WALL_LEFT = Game.spritesheet.getSprite(0, 256, World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage TILE_WALL_RIGHT = Game.spritesheet.getSprite(64, 256, World.TILE_SIZE, World.TILE_SIZE);
	// ------ WATHER
	public static BufferedImage TILE_WATER = Game.spritesheet.getSprite(80, 384, World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage TILE_WATER_TOP_LEFT = Game.spritesheet.getSprite(64, 368, World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage TILE_WATER_TOP_RIGHT = Game.spritesheet.getSprite(96, 368, World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage TILE_WATER_BOTTOM_LEFT = Game.spritesheet.getSprite(64, 400, World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage TILE_WATER_BOTTOM_RIGHT = Game.spritesheet.getSprite(96, 400, World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage TILE_WATER_TOP = Game.spritesheet.getSprite(80, 368, World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage TILE_WATER_BOTTOM = Game.spritesheet.getSprite(80, 400, World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage TILE_WATER_LEFT = Game.spritesheet.getSprite(64, 384, World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage TILE_WATER_RIGHT = Game.spritesheet.getSprite(96, 384, World.TILE_SIZE, World.TILE_SIZE);
	
	public static BufferedImage TILE_FLOOR_SAND = Game.spritesheet.getSprite(64, 496, World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage TILE_FLOOR_GRASS = Game.spritesheet.getSprite(112, 448, World.TILE_SIZE, World.TILE_SIZE);
	
	public boolean show = false;
	
	private BufferedImage sprite;
	private int x, y;
	
	public Tile(int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void render(Graphics g) {
		if (show)
			g.drawImage(sprite, x  - Camera.x, y  - Camera.y, null);
	}

}
