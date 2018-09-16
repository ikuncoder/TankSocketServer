package wingman.game;

import wingman.WingmanWorld;
import wingman.modifiers.motions.MotionController;

import java.awt.*;

/*Bullets fired by player and enemy weapons*/
public class Bullet extends MoveableObject {
    protected PlayerShip owner;
    boolean friendly;
    public  int BulletID;
    private static int i;
    
    public Bullet(Point location, Point speed, int strength, MotionController motion, GameObject owner) {
        super(location, speed, WingmanWorld.sprites.get("enemybullet1"));
        this.strength = strength;
        if (owner instanceof PlayerShip) {
            this.owner = (PlayerShip) owner;
            this.friendly = true;
            this.setImage(WingmanWorld.sprites.get("bullet"));
        }
        i++;
        this.motion = motion;
        this.BulletID=i;
        motion.addObserver(this);
    }

    
   
    
    
    
    public int getBulletID() {
		return BulletID;
	}

	public void setBulletID(int bulletID) {
		BulletID = bulletID;
	}




	public PlayerShip getOwner() {
        return owner;
    }

    public boolean isFriendly() {
        if (friendly) {
            return true;
        }
        return false;
    }
}
