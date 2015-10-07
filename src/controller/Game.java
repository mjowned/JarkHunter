package controller;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.util.Random;

import behaviours.BehaviourFactory;
import behaviours.MoveTrailBehaviour;
import model.BasicEnemy;
import model.Handler;
import model.ID;
import model.UnitFactory;
import view.Window;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 688707883072222376L;

	public static final int WIDTH = 640, HEIGHT = WIDTH / 12 * 9;
	private Thread thread;
	private boolean running = false;

	private UnitFactory unitFactory;
	private BehaviourFactory behaviourFactory;

	private Handler handler;
	private State state;
	private Window window;

	public Game() {
		handler = new Handler();
		this.addMouseListener(new MouseInput(handler));

		behaviourFactory = new BehaviourFactory();
		unitFactory = new UnitFactory(handler, behaviourFactory);
		unitFactory.createLevel1(); // change position? TODO

		window = new Window(WIDTH, HEIGHT, "JarkHunt", this);

	}

	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
		state = state.RUNNING;
	}

	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		// this.requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		
		while (running/* state != ended */) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				tick(delta);
				delta--;
			}
			// switch
			if (running) {
				if (handler.getInput().size() > 0) {
					handler.handleInput();					
				}
				render();
				//
			}
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				window.UPSCounter.setText("UPS: "+frames);
				// System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
		stop();
	}

	private void tick(double delta) {
		handler.tick(delta);
	}

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		Graphics2D g2d = (Graphics2D) bs.getDrawGraphics();

		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, WIDTH, HEIGHT);

		handler.render(g2d);
		window.infoLabel.setText("Score: " + handler.getScore());

		g2d.dispose();
		bs.show();
	}

	public static int clamp(int var, int min, int max) {
		if (var >= max) {
			return var = max;
		} else if (var <= min) {
			return var = min;
		} else {
			return var;
		}
	}

	public static void main(String[] args) {
		new Game();
	}
}
