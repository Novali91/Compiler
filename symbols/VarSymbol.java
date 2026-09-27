package symbols;

public class VarSymbol extends Symbol {

    public VarSymbol(String type) {
        super(type);
    }

    public VarSymbol(String type, int size) {
        super(type, size);
    }
}