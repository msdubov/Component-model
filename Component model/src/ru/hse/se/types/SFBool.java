package ru.hse.se.types;

import ru.hse.se.parsers.SyntaxError;
import ru.hse.se.parsers.VRMLParser;

public class SFBool extends ValueType {
    
    public SFBool(boolean value) {
        this.value = value;
    }
    
    public boolean getValue() {
        return value;
    }
    
    /**
     * Parses a boolean / SFBool value from the stream.
     * 
     ***************************************
     * sfboolValue ::=                     *
     *         TRUE |                      *
     *         FALSE                       *
     ***************************************
     */
    public static SFBool parse(VRMLParser parser) throws SyntaxError {

        boolean res;
        
        if (parser.lookahead("TRUE")) {
            parser.match("TRUE");
            res = true;
        } else if (parser.lookahead("FALSE")) {
            parser.match("FALSE");
            res = false;
        } else {
            throw new SyntaxError("Expected 'TRUE' or 'FALSE'", parser.tokenizer().lineno());
        }
        
        return new SFBool(res);
    }
    
    @Override
    public String toString() {
        return value ? "TRUE" : "FALSE";
    }
    
    
    private boolean value;
}
