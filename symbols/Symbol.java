package symbols;

public class Symbol {

    public String type;
    // -1 if not an array type
    public int size;

    public Symbol(String type) {
        this.type = type;
        this.size = -1;
    }

    public Symbol(String type, int size) {
        this.type = type;
        this.size = size;
    }

}