package symbol_table;

import java.util.HashMap;
import symbols.*;

public class SymbolTable {

    HashMap<String, FuncSymbol> globals;
    HashMap<String, VarSymbol> locals;
    public FuncSymbol curFunc;

    public SymbolTable(HashMap<String, FuncSymbol> globals, HashMap<String, VarSymbol> locals, FuncSymbol curFunc) {
        this.globals = globals;
        this.locals = locals;
        this.curFunc = curFunc;
    }

    public Symbol lookup(String name) {
        Symbol val = locals.get(name);
        if (val != null) {
            return val;
        }

        val = globals.get(name);

        if (val != null) {
            return val;
        }

        return null;
    }

    // Returns false if this name already  (the caller handles errors)
    public boolean insert(String name, VarSymbol val) {
        if (globals.get(name) != null || locals.get(name) != null) {
            return false;
        }
        
        locals.put(name, val);
        return true;
    }

}