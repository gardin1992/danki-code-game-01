package com.devdragons.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.devdragons.main.Game.State;
public class Menu {
	
	public static final String NEW_GAME = "Novo Jogo",
			LOAD = "Carregar",
			OPTIONS = "Opções",
			EXIT = "Sair",
			CONTINUE = "Continuar";
	
	public int currentOption = 0;
	public int maxOption;
	
	public boolean up, down, action, inGame = false;
	
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
		
		if (action) {
			action = false;
			
			List<String> options = getList();
			
			switch (options.get(currentOption)) {
			case CONTINUE: 	
			case NEW_GAME: 
					Game.state = State.NORMAL;
					inGame = true;
					break;
				case LOAD: Game.state = State.NORMAL; break;
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
