public class Token {
    public final int tag; // constante que representa o token

    // construtor
    public Token(int t) {
        tag = t;
    }

    // método get
    public int getTag() {
        return tag;
    }
    
    // converte em string
    @Override public String toString() {
        return "" + tag;
    }
}