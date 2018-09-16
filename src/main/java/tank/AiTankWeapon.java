package tank;

import java.awt.Point;

import wingman.game.Bullet;
import wingman.game.Ship;
import wingman.modifiers.weapons.AbstractWeapon;

public class AiTankWeapon extends AbstractWeapon{
	 	public AiTankWeapon() {
	        super(TankWorld.getInstance());
	    }

	    public void fireWeapon(Ship theTank) {
	        super.fireWeapon(theTank);
	        Point location = theTank.getLocationPoint();
	        Point offset = theTank.getGunLocation();
	        location.x += offset.x;
	        location.y += offset.y;
	        Point speed = new Point(0, -15 * direction);
	        int strength = 10;
	        reload = 15;
	        AiTankBullet bullet = new AiTankBullet(location, speed, strength, (AiTank) theTank);
	        bullets = new Bullet[1];
	        bullets[0] = bullet;
	        this.setChanged();

	        this.notifyObservers();
	    }
}
