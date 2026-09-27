package type_equiv;

import symbols.*;

public final class TypeEquiv {
    private TypeEquiv() {}

    public static boolean equiv(Symbol s1, Symbol s2) {
        boolean equivType = (s1.type.equals(s2.type));

        if ((s1.size == -1) && (s2.size == -1)) {
            return equivType;
        }
        else {
            return (equivType && (s1.size == s2.size));
        }
    }

    public static boolean subtype(Symbol s1, Symbol s2) {
        if (equiv(s1, s2)) {
            return true;
        } else {
            return ((s1.type.equals("float")) && s2.type.equals("int"));
        }
    }
}