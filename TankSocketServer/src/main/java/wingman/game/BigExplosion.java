package wingman.game;

import wingman.GameWorld;

import java.awt.*;

import lskServer.SocketServer;


/* BigExplosion plays when player dies*/
public class BigExplosion extends BackgroundObject {
	
	public SocketServer socketServer=new SocketServer();
	
    static Image animation[] = new Image[]{GameWorld.sprites.get("explosion2_1"),
            GameWorld.sprites.get("explosion2_2"),
            GameWorld.sprites.get("explosion2_3"),
            GameWorld.sprites.get("explosion2_4"),
            GameWorld.sprites.get("explosion2_5"),
            GameWorld.sprites.get("explosion2_6"),
            GameWorld.sprites.get("explosion2_7")};
    int timer;
    int frame;

    public BigExplosion(Point location) {
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
            if (frame < 7) {
            	this.img = animation[frame];
            	socketServer.sendHander("bigAnimation"+"+" + 0 +"+"+ this.getLocationPoint().x + "+" +this.getLocationPoint().y + "+" +frame+"+"+0+"+"+0+"+"+0+"+"+0);
            }
            else {
            	this.show = false;
            	socketServer.sendHander("RemoveBigExplosion"+"+" + 0 +"+"+ this.getLocationPoint().x + "+" +this.getLocationPoint().y + "+" + 0+"+"+0+"+"+0+"+"+0+"+"+0);
            }
                
            
        }
    }

    public boolean collision(GameObject otherObject) {
        return false;
    }
}
