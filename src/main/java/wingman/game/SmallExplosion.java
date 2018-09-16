package wingman.game;

import wingman.GameWorld;
import wingman.WingmanWorld;

import java.awt.*;

import lskServer.SocketServer;

/* Small explosions happen whenever an enemy dies */
public class SmallExplosion extends BackgroundObject {
	
	public SocketServer socketServer=new SocketServer();
     static Image animation[] = new Image[]{WingmanWorld.sprites.get("explosion1_1"),
            WingmanWorld.sprites.get("explosion1_2"),
            WingmanWorld.sprites.get("explosion1_3"),
            WingmanWorld.sprites.get("explosion1_4"),
            WingmanWorld.sprites.get("explosion1_5"),
            WingmanWorld.sprites.get("explosion1_6"),
            WingmanWorld.sprites.get("explosion1_7")};
    int timer;
    int frame;

    public SmallExplosion(Point location) {
        super(location, animation[0]);
        timer = 0;
        frame = 0;
        GameWorld.sound.play("Resources/snd_explosion2.wav");
    }

    public void update(int w, int h) {
        super.update(w, h);
        timer++;
        if (timer % 5 == 0) {
            frame++;
            if (frame < 6) {
            	this.img = animation[frame];
            	socketServer.sendHander("smallAnimation"+"+" + 0 +"+"+ this.getLocationPoint().x + "+" +this.getLocationPoint().y + "+" +frame+"+"+0+"+"+0+"+"+0+"+"+0);
            }
            else {
            	this.show = false;
            	socketServer.sendHander("RemoveSmallExplosion"+"+" + 0 +"+"+ this.getLocationPoint().x + "+" +this.getLocationPoint().y + "+" + 0+"+"+0+"+"+0+"+"+0+"+"+0);
            }
                
        }

    }

    public boolean collision(GameObject otherObject) {
        return false;
    }
}
