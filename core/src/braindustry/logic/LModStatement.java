package braindustry.logic;

import braindustry.gen.ModLogicIO;
import mindustry.logic.LStatement;

public abstract class LModStatement extends LStatement {
    @Override
    public void write(StringBuilder builder) {
        ModLogicIO.write(this, builder);
    }
}
