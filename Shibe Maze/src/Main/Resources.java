package Main;

import java.awt.Font;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.ResourceLoader;

import LevelStuff.Level;
import Tweens.LevelAccessor;
import Tweens.ObjectAccessor;
import Tweens.RectangleAccessor;
import Tweens.TextAccessor;
import Utilities.Text;
import aurelienribon.tweenengine.Tween;

//I might use this class, but I'd rather just load everything when I need it in the class itself

public class Resources {
	
	
	
	//In a Map, the first object (String) is used to get the second object (Image)
	private static Map<String, Image> images;
	private static Map<String, SpriteSheet> spriteSheets;
	private static Map<String, Sound> sounds;
	private static Map<String, Music> music;
	private static Map<String, TrueTypeFont> fonts;
	
	
	
	public Resources() {
		
		//Initialize hash maps
		images = new HashMap<String, Image>();
		spriteSheets = new HashMap<String, SpriteSheet>();
		sounds = new HashMap<String, Sound>();
		music = new HashMap<String, Music>();
		fonts = new HashMap<String, TrueTypeFont>();
		
		//Initialize tween accessors
		Tween.registerAccessor(Text.class, new TextAccessor());
		Tween.registerAccessor(Rectangle.class, new RectangleAccessor());
		Tween.registerAccessor(Object.class, new ObjectAccessor());
		Tween.registerAccessor(Level.class, new LevelAccessor());
		
	}
	
	
	
	public static void addImage(String path) throws SlickException {
		Image image = new Image(path, false, Image.FILTER_NEAREST);
		String hashID = path.substring(path.lastIndexOf("/")+1, path.length());
		if (getImage(hashID) == null)
			images.put(hashID, image);
	}
	
	public static void addSpriteSheet(String path, int tileWidth, int tileHeight) throws SlickException {
		SpriteSheet spriteSheet = new SpriteSheet(path, tileWidth, tileHeight);
		String hashID = path.substring(path.lastIndexOf("/")+1, path.length());
		if (getSpriteSheet(hashID) == null)
			spriteSheets.put(hashID, spriteSheet);
	}
	
	public static void addSound(String path) throws SlickException {
		Sound sound = new Sound(path);
		String hashID = path.substring(path.lastIndexOf("/")+1, path.length());
		if (getSound(hashID) == null)
			sounds.put(hashID, sound);
	}
	
	public static void addMusic(String path, String hashID) throws SlickException {
		Music track = new Music(path);
		if (getMusic(hashID) == null)
			music.put(hashID, track);
	}
	
	public static void addFont(String path, float fontSize) {
		
		try {
			
			InputStream inputStream	= ResourceLoader.getResourceAsStream(path);
			
			Font awtFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			awtFont = awtFont.deriveFont(fontSize); //set font size
			
			TrueTypeFont font = new TrueTypeFont(awtFont, false);
			
			String hashID = path.substring(path.lastIndexOf("/")+1, path.length());
			
			if (getFont(hashID) == null)
				fonts.put(hashID, font);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	//The boolean is there to give parameters that arent exactly like the ones in the above method
	public static void addFont(String fontToAdd, int fontSize, boolean lol) {
		Font awtFont = new Font(fontToAdd, Font.PLAIN, fontSize);
		TrueTypeFont textFont = new TrueTypeFont(awtFont, false);
		if (getFont(fontToAdd + ".ttf") == null)
			fonts.put(fontToAdd + ".ttf", textFont);
	}
	
	
	
	public static Image getImage(String hashID) {
		return images.get(hashID);
	}
	
	public static SpriteSheet getSpriteSheet(String hashID) {
		return spriteSheets.get(hashID);
	}
	
	public static Sound getSound(String hashID) {
		return sounds.get(hashID);
	}
	
	public static Music getMusic(String hashID) {
		return music.get(hashID);
	}
	
	public static TrueTypeFont getFont(String hashID) {
		return fonts.get(hashID);
	}
	
}
