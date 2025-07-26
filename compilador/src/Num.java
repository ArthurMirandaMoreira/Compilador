public class Num extends Token {
    public final float value;

    // construtores
    public Num(float value) {
        super(Tag.CONST_FLOAT);
        this.value = value;
    }

    public Num(int value) {
        super(Tag.CONST_INT);
        this.value = value;
    }

    // converte em string
    public @Override String toString() {
        if (tag == Tag.CONST_INT) {
            return "" + (int) value;
        }
        return "" + value;
    }

}
