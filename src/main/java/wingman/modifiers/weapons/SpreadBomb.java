package wingman.modifiers.weapons;

import wingman.game.Bullet;
import wingman.game.Ship;
import wingman.modifiers.motions.SimpleMotion;

import java.awt.*;

public class SpreadBomb extends AbstractWeapon {

    @Override
    public void fireWeapon(Ship theShip) {
        super.fireWeapon(theShip);
        Point location = theShip.getLocationPoint();
        Point speed = theShip.getSpeed();
        Point offset = theShip.getGunLocation();
        location.x += offset.x;
        location.y += offset.y;
        Point left = new Point(3, speed.y / 2);
        Point right = new Point(-3, speed.y / 2);

        Bullet bullet0 = new Bullet(location, left, 10, new SimpleMotion(), theShip);
        Bullet bullet1 = new Bullet(location, right, 10, new SimpleMotion(), theShip);

        bullets = new Bullet[2];
        bullets[0] = bullet0;
        bullets[1] = bullet1;

        this.setChanged();
        this.notifyObservers();

    }

}

