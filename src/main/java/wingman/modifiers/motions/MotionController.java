package wingman.modifiers.motions;

import wingman.GameWorld;
import wingman.WingmanWorld;
import wingman.game.Ship;
import wingman.modifiers.AbstractGameModifier;

import java.util.Observable;
import java.util.Observer;

/*MotionControllers move around objects!*/
public abstract class MotionController extends AbstractGameModifier implements Observer {
    int fireInterval;

    public MotionController() {
        this(WingmanWorld.getInstance());
        fireInterval = -1;
    }

    public MotionController(GameWorld world) {
        world.addClockObserver(this);
    }

    public void delete(Observer theObject) {
        WingmanWorld.getInstance().removeClockObserver(this);
        this.deleteObserver(theObject);
    }

    /*Motion Controllers observe the GameClock and fire on every clock tick
     * The default controller doesn't do anything though*/
    public void update(Observable o, Object arg) {
        this.setChanged();
        this.notifyObservers();
    }

    public void read(Object theObject) {
        Ship object = (Ship) theObject;
        object.move();

        if (WingmanWorld.getInstance().getFrameNumber() % fireInterval == 0) {
            object.fire();
        }
    }
}
