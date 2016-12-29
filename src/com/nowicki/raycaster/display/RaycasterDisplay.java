package com.nowicki.raycaster.display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.nowicki.raycaster.engine.Camera;
import com.nowicki.raycaster.engine.Engine;
import com.nowicki.raycaster.engine.Level;
import com.nowicki.raycaster.engine.Settings;

public class RaycasterDisplay extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	
	public static final String FRAME_TITLE = "Raycaster demo";
	
	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	public static final int FPS_LIMIT = 50;
	public static final long FRAME_TIME_MILIS = 1000 / FPS_LIMIT;

	private boolean running;
	private Thread thread;
	private BufferedImage frame;
	
	private Engine engine;
	private Camera camera;
	
	private long fps;

	public RaycasterDisplay() {
		Level level = new Level();
		engine = new Engine(WIDTH, HEIGHT);
		engine.setLevel(level);
		
		camera = new Camera(3, 10);
		addKeyListener(camera);
		
		frame = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		engine.setBuffer(((DataBufferInt)frame.getRaster().getDataBuffer()).getData());

		setSize(WIDTH, HEIGHT);
		setVisible(true);
		setResizable(false);
		setTitle(FRAME_TITLE);
		setDefaultLookAndFeelDecorated(true);
		setBackground(Color.black);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		thread = new Thread(this);
		thread.setDaemon(true);
		thread.start();
	}
	
	@Override
	public void run() {
		running = true;
		runDemo();
	}
	
	public void runDemo() {
		long start, diff, sleepTime;

		while (running) {
			
			start = System.nanoTime();
	
			engine.tick(camera);
			drawFrame();
			
			diff = System.nanoTime() - start;
			sleepTime = FRAME_TIME_MILIS - (diff / 1000000);
			if (sleepTime < 0) {
				sleepTime = 0;
			}
			
			if (Settings.debug) {
				fps = (diff != 0) ? 1000000000 / diff : FPS_LIMIT;
				if (fps > FPS_LIMIT) {
					fps = FPS_LIMIT;
				}
			}
			
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
			}
		}
	}
	
	private void drawFrame() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			bs = getBufferStrategy();
		}
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(frame, 0, 0, null);
		if (Settings.debug) {
			drawDebugInfo(g);
		}
		bs.show();
	}

	private void drawDebugInfo(Graphics g) {
		String debugInfo = ((fps < 10) ? "0" : "") + fps + " fps";
		g.setColor(Color.YELLOW);
		g.drawString(debugInfo, 600, 30);
	}

}
