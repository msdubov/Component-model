package ru.hse.se.parsers.test;

import ru.hse.se.nodes.Node;
import ru.hse.se.parsers.Parser;
import ru.hse.se.parsers.SyntaxError;
import ru.hse.se.types.ValueType;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Stack;

import junit.framework.TestCase;

public class ParserTest extends TestCase {
    
    public void parserTest(Parser parser, String fileName) throws FileNotFoundException {

        ArrayList<Node> result = null;
        
        try {
            result = parser.parse(new FileReader(fileName));
            
            System.out.println();

            if (result != null) {
                // PARSING SUCCEEDED,
                // Print the textual scene graph representation
                introspectNodes(result);
                
            } else {
                // PARSING FAILED,
                // Print the errors list
                for (Error e : parser.getParsingErrors()) {
                    if (e instanceof SyntaxError) {
                        System.out.print("** SYNTAX ERROR **\t");
                    } else {
                        System.out.print("** ERROR **\t");
                    }
                    
                    System.out.println(e.getMessage());
                }
            }
            
        } catch (IOException e) {
            System.out.println("\n* Something bad with the file *");
        }
    }
    
    protected void introspectNodes(ArrayList<Node> result) {
        System.out.println("\nresult.size = " + result.size() + "\n");
        
        nodes = new Stack<Node>();
        
        for (int i = 0; i < result.size(); i++) {
            System.out.print((i+1) + ". ");
            introspect(result.get(i));
            System.out.println();
        }
    }
    
    
    private void introspect(Node n) {
        
        nodes.push(n);

        for (int i = 0; i < 2*(nodes.size()-1); i++) {
            System.out.print("   ");
        }
        System.out.println(n.getClass().getSimpleName() + " = ");
        
        try {
            
            Method[] methods = n.getClass().getDeclaredMethods();
            for (Method m : methods) {
                
                if (m.getName().startsWith("get")) {
                    
                    String field = Character.toLowerCase(m.getName().charAt(3)) + 
                                    m.getName().substring(4);
                    
                    // Node type => process recursively
                    if (Node.class.isAssignableFrom(m.getReturnType())) {
                        
                        for (int i = 0; i < 2*(nodes.size()) - 1; i++) {
                            System.out.print("   ");
                        }
                        System.out.println(field + " : ");
                        Node child = (Node)m.invoke(n);
                        introspect(child);
                    }
                    
                    // Value type => print value
                    else if (ValueType.class.isAssignableFrom(m.getReturnType())) {
                        
                        ValueType value = (ValueType)m.invoke(n);
                        for (int i = 0; i < 2*(nodes.size()) - 1; i++) {
                            System.out.print("   ");
                        }
                        System.out.println(field + " : " + value);
                    }
                }
            }
        } catch (Exception e) {}
        
        
        nodes.pop();
    }
    
    
    private Stack<Node> nodes;
}