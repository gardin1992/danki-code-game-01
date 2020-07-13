package com.devdragons.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.devdragons.entities.*;
import com.devdragons.graficos.Spritesheet;
import com.devdragons.graficos.UI;
import com.devdragons.world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;
	
	public static enum State {
		NORMAL,
		GAME_OVER,
		MENU
	}

	public static Game instance; 
	/**
	 * initial constructor
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Game game = new Game();
		Game.instance = game;
		game.start();
	}
	
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning;
	
	// ------ IFRAME
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;
	
	// ------ LEVEL
	private int CUR_LEVEL = 1, MAX_LEVEL = 3;
	private BufferedImage image;
	
	// ------ LISTS
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<Lifepack> lifepacks;
	public static List<Ammo> ammos;
	public static List<Weapon> weapons;
	public static List<SkillBullet> bullets;
	public static List<SkillSword> swords;
	
	// ------ BASIC
	public static Spritesheet spritesheet;
	public static World world;
	public static Player player;
	public static Random rand;
	public static State state = State.MENU;
	public UI ui;
	// ------ Score
	public static Score score;
	// ------ Game Over 
	private boolean showMessageGameOver = true, restartGame = false;
	private int framesGameOver = 0, maxFramesGameOver = 30;
	
	// ------ Menu
	private Menu menu;
	public boolean saveGame = false;
	
	public InputStream fontStream1 = ClassLoader.class.getResourceAsStream("/pixelart.ttf");
	public InputStream fontStream2 = ClassLoader.class.getResourceAsStream("/pixelart.ttf");
	public InputStream fontStream3 = ClassLoader.class.getResourceAsStream("/pixelart.ttf");
	public Font font16;
	public Font fontTitle;
	public Font fontText;
	
	public int[] pixels;
	public BufferedImage lightMap;
	public int[] lightMapPixels;
	public static int[] minimapPixels; 
	public static BufferedImage minimapa;
	
	public int mx,my;
	
	public BufferedImage sprite1;
	public BufferedImage sprite2;
	public int[] pixels1;
	public int[] pixels2;
	public int x1 = 30, y1 = 90;
	public int x2 = 100, y2 = 100;
	
	public Game() {
		
		rand = new Random();
		
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		// setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
		setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		initFrame();
		// init objects
		menu = new Menu();
		ui = new UI();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		try {
			lightMap = ImageIO.read(getClass().getResource("/light.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		lightMapPixels = new int[lightMap.getWidth() * lightMap.getHeight()];
		lightMap.getRGB(0, 0, lightMap.getWidth(), lightMap.getHeight(), lightMapPixels, 0, lightMap.getWidth());
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		score = new Score();
		
		initializeFonts();
		initialized(getLevelName(CUR_LEVEL));
		
		// pixel perfect
		try {
			sprite1 = ImageIO.read(getClass().getResource("/sprite1.png")); 
			sprite2 = ImageIO.read(getClass().getResource("/sprite2.png"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		pixels1 = new int[sprite1.getWidth()*sprite1.getHeight()];
		sprite1.getRGB(0, 0, sprite1.getWidth(), sprite2.getHeight(), pixels1, 0, sprite1.getWidth());
		pixels2 = new int[sprite2.getWidth()*sprite2.getHeight()];
		sprite2.getRGB(0, 0, sprite2.getWidth(), sprite1.getHeight(), pixels2, 0, sprite2.getWidth());
		
		if (pixels1[0] == 0x00000000) {
			System.out.println("É transparente");
		}
	}
	
	public void initializeFonts() {

		try {
			
			fontTitle = Font.createFont(Font.TRUETYPE_FONT, fontStream1).deriveFont(28f);
			fontText = Font.createFont(Font.TRUETYPE_FONT, fontStream3).deriveFont(16f);
			font16 = Font.createFont(Font.TRUETYPE_FONT, fontStream2).deriveFont(16f);
			
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void initialized(String level)
	{
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		// attacks/skill/shoots
		bullets = new ArrayList<SkillBullet>();
		swords = new ArrayList<SkillSword>();
		// items
		lifepacks = new ArrayList<Lifepack>();
		ammos = new ArrayList<Ammo>();
		weapons = new ArrayList<Weapon>();
		
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);
		world = new World("/"  + level);
		minimapa = new BufferedImage(World.WIDTH, World.HEIGHT, BufferedImage.TYPE_INT_RGB);
		minimapPixels = ((DataBufferInt) minimapa.getRaster().getDataBuffer()).getData();
		
	}
	
	public void initFrame() {
		frame = new JFrame("Game #1");
		frame.add(this);
		// frame.setUndecorated(true);
		frame.setResizable(false);
		frame.pack();

		
		// Icone da janela
		//Image image = null;
		/*try {
			// image = ImageIO.read(getClass().getResource("/icon.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		*/
		//frame.setIconImage(image);
		//frame.setAlwaysOnTop(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		// cursor
		//Toolkit toolkit = Toolkit.getDefaultToolkit();
		// Image image = toolkit.getImage(getClass().getResource("/icon.png"));
		// Cursor c = toolkit.createCustomCursor(image, new Point(0,0), "img");
		// frame.setCursor(c);
			
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
		
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}
	
	public static String getLevelName(int level) {
		return "level"+level+".png";
	}
	
	public void tick() {
		
		switch (state) {
			case GAME_OVER: 
				framesGameOver++;
				if (framesGameOver == maxFramesGameOver) {
					framesGameOver = 0;
					showMessageGameOver = !showMessageGameOver;
				}
				
				if (restartGame) {
					restartGame = false;
					state = State.NORMAL;
					CUR_LEVEL = 1;
					World.restartGame(getLevelName(CUR_LEVEL));
				}
				break;
			
			case MENU:
				player.updateCamera();
				menu.tick();
				break;
				
			default:
				world.tick();
				score.tick();
				restartGame = false;
				
				if (this.saveGame) {
					this.saveGame = false;
					
					String[] opt1 = {"level"};
					int[] opt2 = {this.CUR_LEVEL};
					Menu.saveGame(opt1, opt2, 10);
					System.out.println("Jogo salvo com sucesso");
				}
				
				for (int i = 0; i < entities.size(); i++)
				{
					Entity e = entities.get(i);
					e.tick();
				}
				
				for (int i = 0; i < bullets.size(); i++)
				{
					bullets.get(i).tick();
				}
				
				if (enemies.size() == 0) {
					
					CUR_LEVEL++;
					if (CUR_LEVEL > MAX_LEVEL) {
						CUR_LEVEL = 1;
					}
					
					String newWorld  = "level" + CUR_LEVEL + ".png";
					World.restartGame(newWorld);
				}
			break;
		}
	}
	
	public void renderGameOver(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0, 0, 0, 100));
		g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
		g.setFont(fontTitle);
		g.setColor(Color.white);
		g.drawString("Game Over", (WIDTH*SCALE/2) - 80, (HEIGHT*SCALE/2));
		
		if (showMessageGameOver)
			g.drawString(">Presseione Enter para Reiniciar!<", (WIDTH*SCALE/2) - 20 - 200, (HEIGHT*SCALE/2) + 40);
	}
	
	// ------ trabalhando com pixels
	public void applyLight() {
		for(int xx = 0; xx < Game.WIDTH; xx++) {
			for(int yy = 0; yy < Game.HEIGHT; yy++) {
				
				if (lightMapPixels[xx+(yy * Game.WIDTH)] == 0xffffffff) {
					int pixel = Pixel.getLightBlend(pixels[xx+(yy*Game.WIDTH)], 0x808080, 0);
					pixels[xx+(yy*Game.WIDTH)] = pixel;
				}
			}
		}
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		
		if(bs == null) {
			this.createBufferStrategy(3);
			return ;
		}
		
		Graphics g = image.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.dispose();

		g.drawImage(sprite1, x1, y1, null);
		
		/**
		/* Render the game /
		world.render(g);
		Collections.sort(entities, Entity.nodeSorter);
		for (int i = 0; i < entities.size(); i++)
		{
			Entity e = entities.get(i);
			e.render(g);
		}
		
		for (int i = 0; i < bullets.size(); i++)
		{
			bullets.get(i).render(g);
		}
		ui.render(g);
		applyLight();
		
		g.dispose();
		g = bs.getDrawGraphics();

		// Render GAME Full screen
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		// g.drawImage(image, 0,0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height, null);

		g.setFont(fontText);
		g.setColor(Color.white);
		g.drawString("Level " + CUR_LEVEL, 360, 20);
		
		g.setFont(fontText);
		g.setColor(Color.white);
		g.drawString("Ammo " + player.ammo, 500, 20);
		
		switch(state) {
			case GAME_OVER: renderGameOver(g); break;
			case MENU: menu.render(g); break;
			default: score.render(g); break;
		}
		
		// --- rotação acompanhando o mouse
		/*
		Graphics2D g2 = (Graphics2D) g;
		double angleMouse = Math.atan2(200+25 - my, 200+25 + mx);
		g2.rotate(angleMouse, 200 + 25, 200 + 25);
		g.setColor(Color.red);
		g.fillRect(200, 200, 50, 50);
		*/
		// World.renderMiniMap();
		// g.drawImage(minimapa, 480,80, World.WIDTH*5, World.HEIGHT*5, null);
		bs.show();
	}
	
	@Override
	public void run() {
		requestFocus();
		Sound.music.loop();
		
		long lastTime = System.nanoTime();
		final double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		// Debug
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while(isRunning) {
			
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			if (delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			// Debug
			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + frames);
				frames = 0;
				timer += 1000;
			}
		}
		
		stop();
	}
	
	
	/* TECLADO */
	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			player.jump = true;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			
			player.right = true;
		}
		else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
			
			if (state == State.MENU) {
				menu.up = true;
			}
		}
		else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
			
			if (state == State.MENU) {
				menu.down = true;
			}
		}
		
		if (e.getKeyCode() == KeyEvent.VK_F) {
			player.inAttack = true;
			player.shoot = true;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_J) {
			if (state == State.NORMAL )
				this.saveGame = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		}
		else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
			
			if (state == State.MENU) {
				menu.up = false;
			}
		}
		else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
			
			if (state == State.MENU) {
				menu.down = false;
			}
		}
		
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			restartGame = true;
			
			if (state == State.MENU) {
				menu.action = true;
			}
		}
		
		// choose weapon
		if (e.getKeyCode() == KeyEvent.VK_Q || e.getKeyCode() == KeyEvent.VK_NUMPAD0) {	
			player.prevWeapon();			
		}
		else if (e.getKeyCode() == KeyEvent.VK_E || e.getKeyCode() == KeyEvent.VK_NUMPAD1) {
			player.nextWeapon();
		}
		
		// toggle menu
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			state = state == State.MENU ? State.NORMAL : State.MENU;		
			System.out.println(state);
		}
	}

	/* MOUSE */
	@Override
	public void mouseClicked(MouseEvent e) {
		
		player.inAttack = true;
		player.mouseShoot = true;
		player.mx = (e.getX()/SCALE);
		player.my = (e.getY()/SCALE);
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// ------- pegar posição do mouse
		this.my = e.getX();
		this.my = e.getY();
	}
	
	// pixels perfect
	public boolean isColliding(int x1, int y1, int x2, int y2, int[] pixels1, int[] pixels2) {
		return false;
	}
}
