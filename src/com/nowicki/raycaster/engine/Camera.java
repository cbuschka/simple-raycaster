package com.nowicki.raycaster.engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.nowicki.raycaster.display.RaycasterDisplay;

public class Camera implements KeyListener {

	// position on the map in double precision coordinates
	protected double xPos;
	protected double yPos;
	
	// view direction <-1, +1> x & y must be perpendicular
	protected double xDir = -1;
	protected double yDir = 0;
	
	// camera plane
	protected double xPlane = 0;
	protected double yPlane = 0.66;

	public final double MOVE_SPEED = 0.08;
	public final double ROTATION_SPEED = .040;
	
	private boolean rotatingLeft, rotatingRight, movingForward, movingBackward;
	private RaycasterDisplay display;

	public Camera(double xPos, double yPos, RaycasterDisplay display) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.display = display;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			rotatingLeft = true;
			break;
		case KeyEvent.VK_RIGHT:
			rotatingRight = true;
			break;
		case KeyEvent.VK_UP:
			movingForward = true;
			break;
		case KeyEvent.VK_DOWN:
			movingBackward = true;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			rotatingLeft = false;
			break;
		case KeyEvent.VK_RIGHT:
			rotatingRight = false;
			break;
		case KeyEvent.VK_UP:
			movingForward = false;
			break;
		case KeyEvent.VK_DOWN:
			movingBackward = false;
			break;
		case KeyEvent.VK_D:
			Settings.debug = !Settings.debug;
			break;
		case KeyEvent.VK_F:
			Settings.toggleFloor();
			break;
		case KeyEvent.VK_W:
			Settings.toggleWalls();
			break;
		case KeyEvent.VK_Q:
			display.stop();
			break;
		case KeyEvent.VK_ENTER:
			display.toggleFullscreen();
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	public void update(int[][] map) {
		
		if (Settings.debug) {
			System.out.println("Pos ("+xPos+","+yPos+") Dir ("+xDir+","+yDir+") l r u d "+rotatingLeft+" "+rotatingRight+" "+movingForward+" "+movingBackward);
		}
		
		if (movingForward) {
			if (map[(int) (xPos + xDir * MOVE_SPEED)][(int) yPos] == 0)
				xPos += xDir * MOVE_SPEED;
			if (map[(int) xPos][(int) (yPos + yDir * MOVE_SPEED)] == 0)
				yPos += yDir * MOVE_SPEED;
		}
		if (movingBackward) {
			if (map[(int) (xPos - xDir * MOVE_SPEED)][(int) yPos] == 0)
				xPos -= xDir * MOVE_SPEED;
			if (map[(int) xPos][(int) (yPos - yDir * MOVE_SPEED)] == 0)
				yPos -= yDir * MOVE_SPEED;
		}
		
		if (rotatingRight) {
			rotateZ(-ROTATION_SPEED);
		}
		if (rotatingLeft) {
			rotateZ(ROTATION_SPEED);
		}
	}
	
	/**
	 * Multuply by rotation matrix
	 * 
	 * [ cos(angle) -sin(angle) ]
	 * [ sin(angle)  cos(angle) ]
	 * 
	 * @param angle
	 */
	public void rotateZ(double angle) {
		double oldxDir = xDir;
		xDir = xDir * Math.cos(angle) - yDir * Math.sin(angle);
		yDir = oldxDir * Math.sin(angle) + yDir * Math.cos(angle);
		double oldxPlane = xPlane;
		xPlane = xPlane * Math.cos(angle) - yPlane * Math.sin(angle);
		yPlane = oldxPlane * Math.sin(angle) + yPlane * Math.cos(angle);
	}

}
