package braindustry.logic;

import braindustry.gen.BDLogicIO;
import mindustry.logic.LStatement;

public abstract class LModStatement extends LStatement {
    @Override
    public void write(StringBuilder builder) {
        BDLogicIO.write(this,builder);
    }
}
