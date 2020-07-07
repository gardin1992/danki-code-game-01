package com.devdragons.main;

import java.applet.Applet;
import java.applet.AudioClip;

public class Sound {
	
	private AudioClip clip;
	
	public static final Sound musicBackground = new Sound("/accion.mid");
	public static final Sound shootEffect = new Sound("/Hit_Shot.wav");
	public static final Sound swordEffect = new Sound("/Hit.wav");
	
    private Sound(String name) {
        try {
        	clip = Applet.newAudioClip(Sound.class.getResource(name));
        	clip.play();
        }
        catch (Throwable e) {
			
		}
    }

    public void play() {
    	try {
    		System.out.print("play");
    		new Thread() {
    			@Override
				public void run() {
					clip.play();
				}
			}.start();
		} catch (Throwable e) {
			System.out.print("Falhou o play");
		}
    }
	
	public void loop() {
		try {
			
			new Thread() {
				
				public void run() {
					clip.loop();
				}
			}.start();
			
		} catch (Throwable e) {
			System.out.print("Falhou o play");
		}
	}

}
