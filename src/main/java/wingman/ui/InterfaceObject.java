package wingman.ui;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.Observable;
import java.util.Observer;

public abstract class InterfaceObject implements Observer {
    Point location;
    ImageObserver observer;

    public abstract void draw(Graphics g, int x, int y);

    public void update(Observable o, Object arg) {
    }
}
