/*
 * Solution2.java
 *
 * Version:
 *     1.00
 *
 */


import java.util.ArrayList;

/**
 * @author: Renke Wang
 */

public class Solution implements Comparable<Solution>{
    ArrayList<Node> solution;
    double distance;
    Solution(ArrayList<Node> solution, double distance){
        this.solution=solution;
        this.distance=distance;
    }

    @Override
    public String toString() {
        return "Solution{" +
                "distance=" + distance +
                "}\n";
    }

    @Override
    public int compareTo(Solution o) {
        return Double.compare( this.distance, o.distance );
    }

}
