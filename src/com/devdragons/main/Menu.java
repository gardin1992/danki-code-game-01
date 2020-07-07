package com.devdragons.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.devdragons.main.Game.State;
import com.devdragons.world.World;
public class Menu {
	
	public static final String NEW_GAME = "Novo Jogo",
			LOAD = "Carregar",
			OPTIONS = "Opções",
			EXIT = "Sair",
			CONTINUE = "Continuar";
	
	public int currentOption = 0;
	public int maxOption;
	
	public boolean up, down, action;
	public static boolean inGame = false;
	public static boolean saveExists = false, saveGame = false;
	
	public Menu() {
		maxOption = 4;
	}
	
	public List<String> getList() {
		
		List<String> options = new ArrayList<String>();
		
		if (inGame) {
			options.add(CONTINUE);	
		}
		else {
			options.add(NEW_GAME);	
		}
		
		options.add(LOAD);
		options.add(OPTIONS);
		options.add(EXIT);
		
		return options;
	}
	
	
	public void tick() {
		File file = new File(FILE_NAME);
		
		saveExists = file.exists();
		
		if (action) {
			action = false;
			
			List<String> options = getList();
			
			switch (options.get(currentOption)) {
				case CONTINUE: 
					Game.state = State.NORMAL;
					inGame = true;
					break;
				case NEW_GAME: 
					file.delete();
					Game.state = State.NORMAL;
					inGame = true;
					
					break;
				case LOAD: 
					file = new File(FILE_NAME);
					if (file.exists()) {
						String saver = loadGame(10);
						applySave(saver);
					}
					break;
				case OPTIONS: Game.state = State.NORMAL; break;
				case EXIT: System.exit(1); break;
			}
		}
		
		if (up) {
			up = false;
			currentOption--;
			if (currentOption < 0)
				currentOption = maxOption;
		}
		else if (down) {
			down = false;
			currentOption++;
			if (currentOption > maxOption)
				currentOption = 0;
		}
	}
	
	private static final String FILE_NAME = "data.dat";
	
	public static void applySave(String str) {
		String[] spr = str.split("/");
		for(int i = 0; i < spr.length; i++) {
			String[] spl2 = spr[i].split(":");
			switch (spl2[0]) {
			case "level":
				World.restartGame(Game.getLevelName(Integer.parseInt(spl2[1])));
				Game.state = Game.State.NORMAL;
				inGame = true;
				break;

			default:
				break;
			}
		}
	}
	public static String loadGame(int encode) {
		String line = "";
		File file = new File(FILE_NAME);
		if (file.exists()) {
			try {
				String singleLine = null;
				BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
				
				while((singleLine = reader.readLine()) != null) {
					String[] pieces = singleLine.split(":");
					char[] val = pieces[1].toCharArray();
					pieces[1] = "";
					for (int i = 0; i < val.length; i++) {
						val[i] -= encode;
						pieces[1] += val[i];
					}
					line += pieces[0];
					line += ":";
					line += pieces[1];
					line += "/";
				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO: handle exception
			}
		}

		return line;
	}
	
	public static void saveGame(String[] val1, int[] val2, int encode) {
		BufferedWriter write = null;
		
		try {
			write = new BufferedWriter(new FileWriter(FILE_NAME));
		
			for (int i = 0; i < val1.length; i++) {
				
				String current = val1[i];
				current += ":";
				char[] value = Integer.toString(val2[i]).toCharArray();
				for(int n = 0; n < value.length; n++) {
					value[n] += encode;
					current+=value[n];
				}
				
				write.write(current);
				if (i < val1.length -1)
					write.newLine();
			}
			write.flush();
			write.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0, 0, 0, 100));
		g2.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		
		g.setFont(new Font("Arial", Font.BOLD, 28));
		g.setColor(Color.white);
		g.drawString(">Danki.Code<", 80, 30);
		
		// ---- options
		g.setFont(new Font("Arial", Font.BOLD, 16));
		g.setColor(Color.white);
		
		List<String> options = getList();
		
		for(int i = 0; i < maxOption; i++) {
			if (i == currentOption) {
				
				g.setColor(Color.red);
				//g.fillRect(40, 60 + (i * 40), 40, 40);
				
				g.setColor(Color.yellow);
				g.fillRect(40, 70 + (i * 40), 10, 10);
			}
		}
		
		g.setFont(new Font("Arial", Font.BOLD, 16));
		g.setColor(Color.white);
		
		for(int i = 0; i < maxOption; i++) {
			g.drawString(options.get(i), 80, 80 + (i * 40));
		}
	}
}
