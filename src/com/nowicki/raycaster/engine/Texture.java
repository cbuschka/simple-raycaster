package com.nowicki.raycaster.engine;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Texture {

	private final int[] pixels;
	private final int size;

	public Texture(String filename) throws IOException {
		BufferedImage image = ImageIO.read(new File(filename));
		this.pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
		this.size = image.getWidth();
	}

	public int getPixel(int u, int v) {
		return pixels[v * size + u];
	}
	
	public int getPixelWithFiltering(double u, double v) {
		u *= size;
		v *= size;
		int x = (int) Math.floor(u);
		int y = (int) Math.floor(v);
		double uRatio = u - x;
		double vRatio = v - y;
		double uOpposite = 1 - uRatio;
		double vOpposite = 1 - vRatio;
		
		Color c11 = new Color(getPixelClipping(x, y));
		Color c12 = new Color(getPixelClipping(x, y + 1));
		Color c21 = new Color(getPixelClipping(x + 1, y));
		Color c22 = new Color(getPixelClipping(x + 1, y + 1));
		
		int r = (int) ((c11.getRed() * uOpposite + c21.getRed() * uRatio) * vOpposite
				+ (c12.getRed() * uOpposite + c22.getRed() * uRatio) * vRatio);
		int g = (int) ((c11.getGreen() * uOpposite + c21.getGreen() * uRatio) * vOpposite
				+ (c12.getGreen() * uOpposite + c22.getGreen() * uRatio) * vRatio);
		int b = (int) ((c11.getBlue() * uOpposite + c21.getBlue() * uRatio) * vOpposite
				+ (c12.getBlue() * uOpposite + c22.getBlue() * uRatio) * vRatio);
		
		Color texel = new Color(r, g, b, 255);
		return texel.getRGB();
	}
	
	private int getPixelClipping(int u, int v) {
		return getPixel(u % size, v % size);
	}

	public int[] getPixels() {
		return pixels;
	}

	public int getSize() {
		return size;
	}
}
