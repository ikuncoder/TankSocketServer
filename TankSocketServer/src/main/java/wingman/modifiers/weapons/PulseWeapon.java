package wingman.modifiers.weapons;

import wingman.game.Bullet;
import wingman.game.PlayerShip;
import wingman.game.Ship;
import wingman.modifiers.motions.SimpleMotion;

import java.awt.*;

public class PulseWeapon extends AbstractWeapon {
    public void fireWeapon(Ship theShip) {
        super.fireWeapon(theShip);
        Point location = theShip.getLocationPoint();
        Point speed = theShip.getSpeed();
        Point offset = theShip.getGunLocation();
        if (theShip instanceof PlayerShip) {
            speed.y = -8;
        }
        location.x += offset.x;
        location.y += offset.y;

        bullets = new Bullet[]{
                new Bullet(location, new Point(3, 1 + speed.y), 1, new SimpleMotion(), theShip),
                new Bullet(location, new Point(-3, 1 + speed.y), 1, new SimpleMotion(), theShip),
                new Bullet(location, new Point(2, 2 + speed.y), 1, new SimpleMotion(), theShip),
                new Bullet(location, new Point(-2, 2 + speed.y), 1, new SimpleMotion(), theShip),
                new Bullet(location, new Point(1, 3 + speed.y), 1, new SimpleMotion(), theShip),
                new Bullet(location, new Point(-1, 3 + speed.y), 1, new SimpleMotion(), theShip),
        };

        this.setChanged();
        this.notifyObservers();

    }
}
