/*
 * Node.java
 *
 * Version:
 *     1.00
 *
 */


import java.util.ArrayList;
import java.util.List;

/**
 * @author: Renke Wang
 */

public class Node {
    String name;
    double x;
    double y;

    public Node(String name, double x, double y ) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Node2{" +
                "name='" + name  +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
