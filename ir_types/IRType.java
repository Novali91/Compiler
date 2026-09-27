package ir_types;

public class IRType {
    char type;
    boolean array;

    public IRType(char type, boolean array) {
        this.type = type;
        this.array = array;
    }

    public IRType(char type) {
        this.type = type;
        this.array = false;
    }

    public char getType() {
        return type;
    }

    public boolean getArray() {
        return array;
    }
}