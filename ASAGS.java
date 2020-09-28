/*
 * ASAGS.java
 *
 * Version:
 *     1.00
 *
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * @author: Renke Wang
 */

public class ASAGS {

    List<Node> data = new ArrayList<>();
    //  constants
    double alpha = 1200; //higher alpha, lower cool speed
    double beta = 3;  //higher beta, more times of greedy search

    //  The number of the cities
    int N;
    //	The optimal tour length
    int OPT = -1;
    //	The initial temperature
    double t_initial = 1000;
    //	The cool coefficient of the temperature
    double t_cool;
    //	The times of greedy search
    double t_greedy;
    //	The end temperature
    double t_end = 0.005;//or 0.0025
    //	The temperature of the current state
    double t_current;
    //	The times of compulsive accept
    double t_v;
    int G = 0;
    double distance;

    ASAGS(String path,String OPT) {
        this.OPT=Integer.parseInt(OPT);
        getData(path);
        t_cool = (alpha * Math.sqrt(N) - 1) / (alpha * Math.sqrt(N));
        t_greedy = beta * N;
        t_v = N / 10.0;
        t_current = t_initial;
    }

    public void getData(String path) {
        try(Scanner in = new Scanner(new File(path))) {
            while (in.hasNext()){
                String a = in.nextLine();
                System.out.println(a);
                String[] line=a.split(" ");
                data.add(new Node(line[0], Double.parseDouble(line[1]), Double.parseDouble(line[2])));
            }
            N = data.size();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void randomizeSolution() {
        Collections.shuffle(data);
    }

    void update_distance() {
        //distance between start and end
        distance = distance(data.get(0), data.get(N - 1));
        for (int i = 0; i < N - 1; i++) {
            //distance between other nodes
            distance += distance(data.get(i), data.get(i + 1));
        }
    }

    //distance between two nodes
    static double distance(Node n1, Node n2) {
        return Math.sqrt(Math.pow(n1.x - n2.x, 2) + Math.pow(n1.y - n2.y, 2));
    }

    Random rand = new Random();

    //vertex insert mutation
    void VI() {
        //get two random node
        int index1 = rand.nextInt(N);
        int index2 = rand.nextInt(N);
        while (index1 == index2 || index1 - index2 == 1 || (index1 == 0 && index2 == N - 1)) {
            index2 = rand.nextInt(N);
        }
        Node B = data.get(index2);
        data.remove(B);
        data.add(index1, B);
    }

    //block insert mutation
    void BI() {
        //get three random node
        //position of A-1
        int index1 = rand.nextInt(N);
        //position of C+1
        int index3 = index1 + 3 + rand.nextInt(N - 3);
        //position of B
        int index2 = index1 + 2;
        if (index3 - index1 - 3 > 0) {
            index2 += rand.nextInt(index3 - index1 - 3);
        }
        ArrayList<Node> Aset = new ArrayList<>();
        if (index2 < N) {
            for (int i = index1 + 1; i < index2; i++) {
                Aset.add(data.get(i));
            }
        } else {
            for (int i = index1 + 1; i < N; i++) {
                Aset.add(data.get(i));
            }
            for (int i = 0; i < index2 - N; i++) {
                Aset.add(data.get(i));
            }
        }
        data.removeAll(Aset);
        if (index3 >= N) {
            if (index2 >= N) {
                data.addAll(index3 - index2, Aset);
            } else {
                data.addAll(index3 - N, Aset);
            }
        } else {
            data.addAll(index3 - (index2 - index1 - 1), Aset);
        }
    }

    //block reverse mutation
    void BR() {
        //get two random node
        //index of A
        int index1 = rand.nextInt(N);
        //index of B
        int index2 = index1 + 3 + rand.nextInt(N - 3);
        ArrayList<Node> Aset = new ArrayList<>();
        if (index2 < N) {
            for (int i = index1 + 1; i < index2; i++) {
                Aset.add(data.get(i));
            }
        } else {
            for (int i = index1 + 1; i < N; i++) {
                Aset.add(data.get(i));
            }
            for (int i = 0; i < index2 - N; i++) {
                Aset.add(data.get(i));
            }
        }
        data.removeAll(Aset);
        Collections.reverse(Aset);
        if (index2 >= N) {
            data.addAll(index1 - (index2 - N) + 1, Aset);
        } else {
            data.addAll(index1 + 1, Aset);
        }

    }

    public static void main(String[] args) {
        ASAGS test = new ASAGS(args[0],args[1]);
        long startTime = System.currentTimeMillis();
        List<Solution> solutionSet = new ArrayList<>();
        test.update_distance();
        //generate a solution randomly
        test.randomizeSolution();
        test.update_distance();
        Solution x_0 = new Solution(new ArrayList<>(test.data), test.distance);
        double optimal = Double.MAX_VALUE;
        int count = 0;
        while (test.t_current >= test.t_end) {
            test.data = new ArrayList<>(x_0.solution);
            if (++count % 1000000 == 0) {
                System.out.println("time=" + count + " temp=" + test.t_current);
            }
            if (x_0.distance < optimal) {
                optimal = x_0.distance;
                System.out.println("new solution:" + optimal);
                long end = System.currentTimeMillis();
                System.out.println("time:" + (end - startTime));
            }
            //select one of mutation method VI/BI/BR
            double p_VI = 0.1;
            double p_BI = 0.01;
            double p_BR = 1-p_VI-p_BI;
            Random rand = new Random();
            double r = rand.nextDouble();
            if (r < p_VI) {
                test.VI();
            } else if (r < p_VI + p_BI) {
                test.BI();
            } else {
                test.BR();
            }

            //get a new solution
            test.update_distance();
            Solution x = new Solution(new ArrayList<>(test.data), test.distance);
            //compute delta_f
            double delta_f = x.distance - x_0.distance;
            if (delta_f <= 0) {
                x_0 = x;
            } else {
                test.G += 1;
                //compute f(X'G)???
                solutionSet.add(x);
                if (test.G < test.t_greedy) {
                    continue;
                }
                //select x_best with minimum distance
                try {
                    Collections.sort(solutionSet);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println(solutionSet);
                    break;
                }
                //accept by rho
                double rho = Math.pow(Math.E, -((solutionSet.get(0).distance - x_0.distance) / test.t_current) * (10.0 * test.N / test.OPT));
                r = rand.nextDouble();
                if (r < rho) {
                    x_0 = solutionSet.get(0);
                }
                //clear the solution set
                solutionSet = new ArrayList<>();
            }
            test.t_current = test.t_current * test.t_cool;
            test.G = 0;
        }
        System.out.println("final solution:" + x_0.distance);
        //show the result
        for(Node i:x_0.solution){
            System.out.print(i.name+">>>");
        }

    }
}
