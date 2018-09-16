package wingman.game;

import wingman.GameWorld;

import java.awt.*;

/*BackgroundObjects move at speed of 1 and are not collidable*/
public class BackgroundObject extends GameObject {
    public BackgroundObject(Point location, Image img) {
        super(location, GameWorld.getSpeed(), img);
    }

    public BackgroundObject(Point location, Point speed, Image img) {
        super(location, speed, img);
    }

}
