package wingman.modifiers.weapons;

import wingman.game.Ship;

public class NullWeapon extends AbstractWeapon {
    @Override
    public void fireWeapon(Ship theShip) {
        return;
    }

    @Override
    public void read(Object theObject) {
    }

}
