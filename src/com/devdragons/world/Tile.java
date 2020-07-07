package com.devdragons.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.devdragons.main.Game;

public class Tile {
	
	public static BufferedImage TILE_FLOOR = Game.spritesheet.getSprite(0, 0, World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage TILE_WALL = Game.spritesheet.getSprite(16, 0, World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage TILE_WATER = Game.spritesheet.getSprite(80, 384, World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage TILE_FLOOR_SAND = Game.spritesheet.getSprite(64, 496, World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage TILE_FLOOR_GRASS = Game.spritesheet.getSprite(112, 448, World.TILE_SIZE, World.TILE_SIZE);
	
	private BufferedImage sprite;
	private int x, y;
	
	public Tile(int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, x  - Camera.x, y  - Camera.y, null);
	}

}
