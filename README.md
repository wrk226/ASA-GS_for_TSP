# ASA-GS
It is the implementation of paper [1]. ASA-GS is the initial of adaptive simulated annealing algorithm with greedy search. This algorithm was created to solve TSP (travelling salesman problem).

# Introduction
This algorithm is based on the simulated annealing algorithm and will perform greedy search in mutation process to speed up the convergence rate.  
Here is the flow diagram of the ASA-GS. This implementation is following this diagram exactly.
![20200923095423](https://user-images.githubusercontent.com/7517810/94022155-cb809980-fd82-11ea-8bce-591983259047.png)

# How to use it
ASAGS.java will receive two arguments: path, OPT  
"path" is the path of data file  
"OPT" is the appproximate optimal distance  

There are two parameters in the program you might want to tune, they can change the accuracy and converge speed of the algorithm  
"alpha": higher alpha, lower cool speed  
"beta": higher beta, more times of greedy search  

You may found some useful information [from here](http://www.math.uwaterloo.ca/tsp/world/countries.html)  


## References
1.  **Solving the traveling salesman problem based on an adaptive simulated annealing algorithm with greedy search**<br />
    Xiutang Geng, Zhihua Chen, Wei Yang, Deqian Shi, Kai Zhao. <br />
    [[link]](https://www.sciencedirect.com/science/article/abs/pii/S1568494611000573). Applied Soft Computing, 11(4), 3680-3689.(2011)
