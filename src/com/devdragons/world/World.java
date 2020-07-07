package com.devdragons.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.devdragons.entities.*;
import com.devdragons.main.Game;

public class World {
	
	public static Tile[] tiles;
	public static int WIDHT, HEIGHT;
	public static final int TILE_SIZE = 16;
	public static int zplayer = 0;
	
	public static final int COLOR_FLOR = 0xFF000000;
	
	public World(String path) {
		
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			WIDHT = map.getWidth();
			HEIGHT = map.getHeight();
			
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			
			for(int xx = 0; xx < map.getWidth(); xx++) {
				
				for (int yy = 0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels[xx + (yy * map.getWidth())];
					tiles[xx + (yy * WIDHT) ] = new FloorTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_FLOOR);
					
					switch(pixelAtual) {
						case 0xFF000000: 
							// floor
							tiles[xx + (yy * WIDHT) ] = new FloorTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_FLOOR_GRASS); 
							break;
						case 0xFFfbefef:
							tiles[xx + (yy * WIDHT) ] = new FloorTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_FLOOR_SAND);
							break;	
						case 0xFFFFFFFF:
							// wall
							tiles[xx + (yy * WIDHT) ] = new WallTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_WALL);
							break;
						case 0xFF8ad4e4:
							// wather
							tiles[xx + (yy * WIDHT) ] = new WallTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_WATER);
							break;
						case 0xFF373f7E: 
							// player
							Game.player.setX(xx*TILE_SIZE);
							Game.player.setY(xx*TILE_SIZE);
							break;
						case 0xFFFF2600:
							// enemy
							Enemy en = new EnemySlime(xx*TILE_SIZE, yy*TILE_SIZE, TILE_SIZE, TILE_SIZE, Enemy.SLIME_EN);
							
							Game.enemies.add(en);
							Game.entities.add(en);
							break;
						case 0xFFE62200:
							// enemy goblin
							Enemy goblin = new EnemyGoblin(xx*TILE_SIZE, yy*TILE_SIZE, TILE_SIZE, TILE_SIZE, Enemy.GOBLIN_EN);
							
							Game.enemies.add(goblin);
							Game.entities.add(goblin);
							break;
						case 0xFFD76A3B:
							// weapon
							WeaponGun weapon = new WeaponGun(xx*TILE_SIZE, yy*TILE_SIZE, TILE_SIZE, TILE_SIZE, Weapon.WEAPON_EN);
							Game.entities.add(weapon);
							Game.weapons.add(weapon);
							break;
						case 0xFF6d57A0:
							WeaponSword sword = new WeaponSword(xx*TILE_SIZE, yy*TILE_SIZE, TILE_SIZE, TILE_SIZE, Weapon.SWORD_EN);
							Game.entities.add(sword);
							Game.weapons.add(sword);
							break;
						case 0xFFFFDA5A:
							// bullets
							Ammo bullet = new Ammo(xx*TILE_SIZE, yy*TILE_SIZE, TILE_SIZE, TILE_SIZE, Entity.BULLET_EN);
							Game.entities.add(bullet);
							Game.ammos.add(bullet);
							break;
						case 0xFFC46390:
							// lifepack
							Lifepack lifepack = new Lifepack(xx*TILE_SIZE, yy*TILE_SIZE, TILE_SIZE,TILE_SIZE, Entity.LIFEPACK_EN);
							Game.lifepacks.add(lifepack);
							Game.entities.add(lifepack);
							break;
						default:
							tiles[xx + (yy * WIDHT) ] = new FloorTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_FLOOR); 
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void restartGame(String level) {
		Game.initialized(level);
	}
	
	public void tick () {
		
		zplayer = Game.player.z;
	}
	
	public static boolean isFree(int xnext, int ynext) {
		int x1=  xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;
		
		int x2 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;
		
		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		int x4 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y4 = (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		if (!((tiles[x1 + (y1*World.WIDHT)] instanceof WallTile)
				|| (tiles[x2 + (y2*World.WIDHT)] instanceof WallTile)
				|| (tiles[x3 + (y3*World.WIDHT)] instanceof WallTile)
				|| (tiles[x4 + (y4*World.WIDHT)] instanceof WallTile)
				))
		{
			return true;
		}
		
		if (zplayer > 0)
			return true;
		return false;
	}
	
	public void render(Graphics g) {
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;
		
		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4);
		
		for(int xx = xstart; xx <= xfinal; xx++) {
			for(int yy = ystart; yy <= yfinal; yy++) {
				
				if (xx < 0 || yy < 0 || xx  >= WIDHT || yy >= HEIGHT)
					continue;
				
				Tile tile = tiles[xx + (yy * WIDHT)];
				tile.render(g);	
			}
		}
	}
}
