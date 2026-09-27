package ir_types;

import java.util.List;
import java.util.ArrayList;

public class IRFuncType {
    IRType returnType;
    ArrayList<IRType> paramTypes;

    public IRFuncType(IRType returnType, ArrayList<IRType> paramTypes) {
        this.returnType = returnType;
        this.paramTypes = paramTypes;
    }

    public ArrayList<IRType> getParams() {
        return this.paramTypes;
    }

    public IRType getType() {
        return returnType;
    }
}