package wingman.modifiers.motions;

import java.util.Observable;

/*Motion controller that does nothing*/
public class NullMotion extends MotionController {

    public NullMotion() {
        super();
    }

    @Override
    public void update(Observable o, Object arg) {
    }

}
