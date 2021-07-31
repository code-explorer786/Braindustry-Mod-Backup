package braindustry.type;

public class Rotor {
    public float x;
    public float y;
    public float size = -1;

    public Rotor(float x, float y, float size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public Rotor(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public interface DrillRotorCons {
        void get(float x, float y, float rotorSize);
    }
}
