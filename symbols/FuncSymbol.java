package symbols;

import java.util.List;
import java.util.ArrayList;

public class FuncSymbol extends Symbol {

    public ArrayList<VarSymbol> params;

    public FuncSymbol(String type, ArrayList<VarSymbol> params) {
        super(type);
        this.params = params;
    }

    public FuncSymbol(String type, int size, ArrayList<VarSymbol> params) {
        super(type, size);
        this.params = params;
    }

}