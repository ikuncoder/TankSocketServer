package wingman.game.enemy;

import wingman.WingmanWorld;
import wingman.game.Ship;
import wingman.modifiers.motions.CentralHorizontalHover;
import wingman.modifiers.weapons.SpreadWeapon;

import java.awt.*;

public class HoverEnemy extends Ship {
    public HoverEnemy(int location) {
        this(location, new Point(3, 3));
    }

    public HoverEnemy(int location, Point speed) {
        super(location, speed, 20, WingmanWorld.sprites.get("enemy3"));
        strength = 10;
        health = 10;
        this.weapon = new SpreadWeapon();

        motion = new CentralHorizontalHover();
    }
}
