package com.devdragons.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.devdragons.entities.*;
import com.devdragons.main.Game;

public class World {
	
	public static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static final int TILE_SIZE = 16;
	public static int zplayer = 0;
	
	public static final int COLOR_PLAYER		= 0xFF0026FF;
	public static final int COLOR_SWORD 		= 0xFF00FFFF;
	public static final int COLOR_WEAPON		= 0xFF0094FF;
	public static final int COLOR_BULLET		= 0xFFFFD800;
	// deixar apenas uma cor para inimigo e gerar os inimigos diferentes randonicamente
	public static final int COLOR_GOBLIN_ENEMY	= 0xFFFF6A00;
	public static final int COLOR_SLIME_ENEMY	= 0xFFFF0000;
	public static final int COLOR_LIFEPACK 		= 0xFF4CFF00;
	// ----- Sand
	public static final int COLOR_SAND_FLOR 	= 0xFF404040;
	public static final int COLOR_GRASS_FLOR 	= 0xFF000000;
	// ----- WATHER
	public static final int COLOR_WATHER_WALL 				= 0xFFF7FCFF;
	public static final int COLOR_WATHER_WALL_LEFT_TOP 		= 0xFFB5D2FF;
	public static final int COLOR_WATHER_WALL_RIGHT_TOP 	= 0xFFD0C1FF;
	public static final int COLOR_WATHER_WALL_TOP 			= 0xFFDDDDFF;
	public static final int COLOR_WATHER_WALL_LEFT 			= 0xFFD8F2FF;
	public static final int COLOR_WATHER_WALL_RIGHT 		= 0xFFCEE3FF;
	public static final int COLOR_WATHER_WALL_BOTTOM 		= 0xFFC9E5FF;
	public static final int COLOR_WATHER_WALL_LEFT_BOTTOM 	= 0xFFBACFFF;
	public static final int COLOR_WATHER_WALL_RIGHT_BOTTOM 	= 0xFFADC5FF;
	// ---- Wall
	public static final int COLOR_WALL 				= 0xFFFFFFFF;
	public static final int COLOR_WALL_TOP 			= 0xFFFF56B0;
	public static final int COLOR_WALL_TOP_LEFT 	= 0xFFFF93F2;
	public static final int COLOR_WALL_TOP_RIGHT 	= 0xFFFF77A4;
	public static final int COLOR_WALL_BOTTOM 		= 0xFFFFE8FA;
	public static final int COLOR_WALL_BOTTOM_LEFT 	= 0xFFFFF9FC;
	public static final int COLOR_WALL_BOTTOM_RIGHT = 0xFFFFEDF2;
	public static final int COLOR_WALL_LEFT 		= 0xFFFFC4FF;
	public static final int COLOR_WALL_RIGHT 		= 0xFFFFAAD2;
	/*
	public World(String path) {
		Game.player.setX(0);
		Game.player.setY(0);
		
		WIDTH = 100;
		HEIGHT = 100;
		tiles = new Tile[WIDTH*HEIGHT];
		
		for (int xx = 0; xx < WIDTH; xx++) {
			for (int yy = 0; yy < HEIGHT; yy++) {
				tiles[xx+yy*WIDTH] = new WallTile(xx*16,yy*16, Tile.TILE_WALL);
			}
		}
		
		int dir = 0;
		int xx = 0, yy = 0;
				
		for (int i = 0; i < 200; i++) {
			tiles[xx+yy*WIDTH] = new FloorTile(xx*16,yy*16, Tile.TILE_FLOOR);
			if (dir == 0) {
				// direita
				
				if (xx < WIDTH) {
					xx++;
				}
			} else if (dir == 1) {
				// esquerda
				if (xx > 0) {
					xx--;
				}
			} else if (dir == 2) {
				// baixo
				if (yy < HEIGHT) {
					yy++;
				}
			} else if (dir == 3) {
				// cima
				if (yy > 0) {
					yy--;
				}
			}
			
			if (Game.rand.nextInt(100) < 30 ) {
				dir = Game.rand.nextInt(4);
			}
			
		
		}
	}
	*/
	
	public World(String path) {
		
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			
			for(int xx = 0; xx < map.getWidth(); xx++) {
				
				for (int yy = 0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels[xx + (yy * map.getWidth())];
					tiles[xx + (yy * WIDTH) ] = new FloorTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_FLOOR_GRASS);
					
					switch(pixelAtual) {
						
						case COLOR_SAND_FLOR:
							tiles[xx + (yy * WIDTH) ] = new FloorTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_FLOOR_SAND);
							break;	
						case COLOR_WALL:
							tiles[xx + (yy * WIDTH) ] = new WallTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_WALL);
							break;
						case COLOR_WALL_TOP_LEFT:
							tiles[xx + (yy * WIDTH) ] = new WallTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_WALL_TOP_LEFT);
							break;
						case COLOR_WALL_TOP_RIGHT:
							tiles[xx + (yy * WIDTH) ] = new WallTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_WALL_TOP_RIGHT);
							break;
						case COLOR_WALL_BOTTOM_LEFT:
							tiles[xx + (yy * WIDTH) ] = new WallTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_WALL_BOTTOM_LEFT);
							break;
						case COLOR_WALL_BOTTOM_RIGHT:
							tiles[xx + (yy * WIDTH) ] = new WallTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_WALL_BOTTOM_RIGHT);
							break;
						case COLOR_WALL_TOP:
							tiles[xx + (yy * WIDTH) ] = new WallTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_WALL_TOP);
							break;
						case COLOR_WALL_BOTTOM:
							tiles[xx + (yy * WIDTH) ] = new WallTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_WALL_BOTTOM);
							break;
						case COLOR_WALL_RIGHT:
							tiles[xx + (yy * WIDTH) ] = new WallTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_WALL_RIGHT);
							break;
						case COLOR_WALL_LEFT:
							tiles[xx + (yy * WIDTH) ] = new WallTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_WALL_LEFT);
							break;
							
						case COLOR_WATHER_WALL:
							tiles[xx + (yy * WIDTH) ] = new WallTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_WATER);
							break;
						case COLOR_WATHER_WALL_LEFT_TOP:
							tiles[xx + (yy * WIDTH) ] = new WallTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_WATER_TOP_LEFT);
							break;
						case COLOR_WATHER_WALL_RIGHT_TOP:
							tiles[xx + (yy * WIDTH) ] = new WallTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_WATER_TOP_RIGHT);
							break;
						case COLOR_WATHER_WALL_LEFT_BOTTOM:
							tiles[xx + (yy * WIDTH) ] = new WallTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_WATER_BOTTOM_LEFT);
							break;
						case COLOR_WATHER_WALL_RIGHT_BOTTOM:
							tiles[xx + (yy * WIDTH) ] = new WallTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_WATER_BOTTOM_RIGHT);
							break;
						case COLOR_WATHER_WALL_TOP:
							tiles[xx + (yy * WIDTH) ] = new WallTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_WATER_TOP);
							break;
						case COLOR_WATHER_WALL_BOTTOM:
							tiles[xx + (yy * WIDTH) ] = new WallTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_WATER_BOTTOM);
							break;
						case COLOR_WATHER_WALL_RIGHT:
							tiles[xx + (yy * WIDTH) ] = new WallTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_WATER_RIGHT);
							break;
						case COLOR_WATHER_WALL_LEFT:
							tiles[xx + (yy * WIDTH) ] = new WallTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_WATER_LEFT);
							break;
							
							
						case COLOR_PLAYER:
							Game.player.setX(xx*TILE_SIZE);
							Game.player.setY(xx*TILE_SIZE);
							break;
						case COLOR_SLIME_ENEMY:
							Enemy en = new EnemySlime(xx*TILE_SIZE, yy*TILE_SIZE, TILE_SIZE, TILE_SIZE, Enemy.SLIME_EN);
							Game.enemies.add(en);
							Game.entities.add(en);
							break;
						case COLOR_GOBLIN_ENEMY:
							Enemy goblin = new EnemyGoblin(xx*TILE_SIZE, yy*TILE_SIZE, TILE_SIZE, TILE_SIZE, Enemy.GOBLIN_EN);
							Game.enemies.add(goblin);
							Game.entities.add(goblin);
							break;
						case COLOR_WEAPON:
							WeaponGun weapon = new WeaponGun(xx*TILE_SIZE, yy*TILE_SIZE, TILE_SIZE, TILE_SIZE, Weapon.WEAPON_EN);
							Game.entities.add(weapon);
							Game.weapons.add(weapon);
							break;
						case COLOR_SWORD:
							WeaponSword sword = new WeaponSword(xx*TILE_SIZE, yy*TILE_SIZE, TILE_SIZE, TILE_SIZE, Weapon.SWORD_EN);
							Game.entities.add(sword);
							Game.weapons.add(sword);
							break;
						case COLOR_BULLET:
							Ammo bullet = new Ammo(xx*TILE_SIZE, yy*TILE_SIZE, TILE_SIZE, TILE_SIZE, Entity.BULLET_EN);
							Game.entities.add(bullet);
							Game.ammos.add(bullet);
							break;
						case COLOR_LIFEPACK:
							Lifepack lifepack = new Lifepack(xx*TILE_SIZE, yy*TILE_SIZE, TILE_SIZE,TILE_SIZE, Entity.LIFEPACK_EN);
							Game.lifepacks.add(lifepack);
							Game.entities.add(lifepack);
							break;
						case COLOR_GRASS_FLOR:
							tiles[xx + (yy * WIDTH) ] = new FloorTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_FLOOR_GRASS);
							break;
							 
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
		
		if (!((tiles[x1 + (y1*World.WIDTH)] instanceof WallTile)
				|| (tiles[x2 + (y2*World.WIDTH)] instanceof WallTile)
				|| (tiles[x3 + (y3*World.WIDTH)] instanceof WallTile)
				|| (tiles[x4 + (y4*World.WIDTH)] instanceof WallTile)
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
				
				if (xx < 0 || yy < 0 || xx  >= WIDTH || yy >= HEIGHT)
					continue;
				
				Tile tile = tiles[xx + (yy * WIDTH)];
				tile.render(g);	
			}
		}
	}
	
	public static void renderMiniMap() {
		for(int i = 0; i < Game.minimapPixels.length; i++) {
			Game.minimapPixels[i] = 0;
		}
		
		for(int xx = 0; xx < WIDTH; xx++) {
			for(int yy = 0; yy < WIDTH; yy++) {
				if (tiles[xx + (yy * WIDTH)] instanceof WallTile) {
					Game.minimapPixels[xx + (yy * WIDTH)] = 0xffff000;
				}
			}
		}
		
		int xPlayer = Game.player.getX()/16;
		int yPlayer = Game.player.getY()/16;
		Game.minimapPixels[xPlayer + (yPlayer * WIDTH)] = 0xff0000ff;
	}
}
