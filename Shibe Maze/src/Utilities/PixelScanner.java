package Utilities;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class PixelScanner {
	
	private int pixelX;
	private int pixelY;
	
	public void scanImageForPixel(Image imageToScan, Color colorToScanFor) {
		for (int i = 0; i < Main.Main.WIDTH; i++) {
			for (int j = 0; j < Main.Main.HEIGHT; j++) {
				if (imageToScan.getColor(i, j).equals(colorToScanFor)) {
					System.out.println("Found pixel at " + i + ", " + j);
					pixelX = i;
					pixelY = j;
					return;
				}
			}
		}
		System.out.println("Did not find pixel :(");
	}
	
	public int getX() { return pixelX; }
	public int getY() { return pixelY; }
	
}