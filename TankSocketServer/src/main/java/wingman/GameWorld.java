package wingman;

import wingman.game.BackgroundObject;
import wingman.game.Bullet;

import javax.swing.*;

import tank.TankWorld;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observer;

public abstract class GameWorld extends JPanel implements Runnable, Observer {
    public static final GameSounds sound = new GameSounds();
    public static final GameClock clock = new GameClock();
    public static HashMap<String, Image> sprites = new HashMap<String, Image>();
    
    private static Point speed = new Point(0, 0);
    protected BufferedImage bimg;
    protected volatile ArrayList<BackgroundObject> background;
    public TankWorld tankWorld=TankWorld.getInstance();
    
    
    
    public static Point getSpeed() {
        return new Point(GameWorld.speed);
    }

    public static void setSpeed(Point speed) {
        GameWorld.speed = speed;
    }

    public abstract void addBullet(Bullet... newObjects);

    public abstract void addClockObserver(Observer obs);

    abstract protected void loadSprites();

    public Graphics2D createGraphics2D(int w, int h) {
        Graphics2D g2 = null;
        if (bimg == null || bimg.getWidth() != w || bimg.getHeight() != h) {
            bimg = (BufferedImage) createImage(w, h);
        }
        g2 = bimg.createGraphics();
        g2.setBackground(getBackground());
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2.clearRect(0, 0, w, h);
        return g2;
    }

    public Image getSprite(String name) {
        URL url = WingmanWorld.class.getResource(name);
        Image img = java.awt.Toolkit.getDefaultToolkit().getImage(url);
        try {
            MediaTracker tracker = new MediaTracker(this);
            tracker.addImage(img, 0);
            tracker.waitForID(0);
        } catch (Exception e) {
        }
        return img;
    }

	
	/*public ArrayList<Bullet> getBullet() {
		// TODO Auto-generated method stub
		return tankWorld.getBullet();
	}*/
}
