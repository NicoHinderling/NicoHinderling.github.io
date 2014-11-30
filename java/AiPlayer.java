import java.util.*;

    public class Player{
      //D - This is our class variable to hold the depth that the method will search to
      private int D = 10;

      //chooseMove - This method will utilize eval and minMax to decide what move
      // would be the best decision by the computer. It assesses different values for
      // every positibility of that the game could go to, and keep track of the best decision.
      public Move chooseMove(Graph G) { 
        int max = -10000;

        Move best = new Move(0, 1);
        for(int mmm = 0; mmm < 5; mmm++){
            for(int nnn = 0; nnn < 5; nnn++){
                if(G.isEdge(mmm, nnn) == false && mmm != nnn){
                    best = new Move(mmm, nnn);
            }
          }
        }

        for(int i = 0; i < G.sizeOfGraph(); i++){
          for(int j = i+1; j < G.sizeOfGraph(); j++){
            if(i != j && G.isEdge(i, j) == false){
              System.out.println(i + j + " are I and J");
              G.addEdge(i, j, -1);
              Move m = new Move(i, j);
              int val = minMax( G, 1, 0, 0);
              //System.out.println(val + " | " + i +  " " + j);
              if(val >= max) {
                            System.out.println("New best");
                best = m;
                max = val; 
              }
              G.removeEdge(i, j);
            }
          }
        }
        System.out.println(best);
        return best; 
      }

      //eval - This method will assign values to each possibility within the tree and
      // dictate which move would be the best for the computer to make.
      private int eval(Graph G){
        int i = 0;
        for(int j = 0; j < G.sizeOfGraph(); j++){
          if(G.degree(j, 1) == 1)
            i += 1;
          if(G.degree(j, 1) > 1)
            i += 6;
          if(G.degree(j, -1) == 1)
            i -= 1;
          if(G.degree(j, -1) > 1)
            i -= 4;
        }
        if(G.isCycleOfLength(3, 1))
          i = 1000000000;
        if(G.isCycleOfLength(3, -1))
          i = -1000000000;
        return i;
      }

      //minMax - This method will take in a couple of different parameters and construct
      // a tree with the different possibilities. This method will also preform alpha beta 
      // pruning to make the tree traversing more efficient.
      int minMax(Graph G, int depth, int alpha, int beta ) {  
        if(depth == D || G.isFull())  
          return eval(G); // stop searching and return eval  
        else if(depth%2 == 0) {  
          int val = -100000000;  
          for(int i = 0; i < G.sizeOfGraph(); i++){
            for(int j = i+1; j < G.sizeOfGraph(); j++){
              if(!G.isEdge(i, j)){
                alpha = Math.max(alpha, val); // update alpha with max so far  
                if(beta < alpha) break; // terminate loop  
                G.addEdge(i, j, -1);
                val = Math.max(val, minMax( G, depth+1, alpha, beta ));
                G.removeEdge(i, j);
              }
            }
          }  
          return val;  
        } else { // is a min node  
          int val = 10000000;  
          for(int i = 0; i < G.sizeOfGraph(); i++){
            for(int j = i+1; j < G.sizeOfGraph(); j++){
              if(!G.isEdge(i, j)){
                beta = Math.min(beta, val); // update beta with min so far  
                if(beta < alpha) break; // terminate loop  
                G.addEdge(i, j, 1);
                val = Math.min(val, minMax( G, depth+1, alpha, beta ) );
                G.removeEdge(i, j);
              }
            }
          }
          return val;  
        } 
      }
    }

public class Graph {
    
    //Class arrays for Graph
    private static int[][] B;               // 0 = no edge; 1 = red edge; -1 = blue edge
    private static boolean [] visited;
    
    public Graph(int N){                            // a constructor for a instance of the class with N vertices 
        B = new int[N][N];
        visited = new boolean[B.length];
    }
    
    public void addEdge(int u, int v, int w){       // add an edge from vertex u to v with value w (which in this hw will be  only 0, -1, or 1)
        this.B[u][v] = w;
        this.B[v][u] = w;
    }
    
    public void removeEdge(int u, int v){         // remove the edge from u to v and the (duplicate) edge from v to u
        this.B[u][v] = 0;
        this.B[v][u] = 0;
    }
    
    public int getEdge(int u, int v){               // return the value (-1, 0, or 1) of the edge that goes from u to v
        return this.B[u][v];
    }
    
    public boolean isEdge(int u, int v){            // return true or false depending on whether there is an edge (of either color) from u to v
        if(this.B[u][v] != 0){
            return true;
        }else{
            return false;
        }
    }
    
    public int degree(int v){                       // return the number of edges of either color connected to vertex v
        int counter = 0;
        for(int i = 0; i < this.B[v].length; ++i){
            if(this.B[v][i] != 0){
                counter++;
            }
        }
        return counter;
    }
    
    public int degree(int v, int w){                // return the number of edges of color w connected to vertex v
        int counter = 0;
        for(int i = 0; i < this.B[v].length; ++i){
            if(this.B[v][i] == w){
                counter++;
            }
        }
        return counter;
    }
    
    public int sizeOfGraph(){
        return B.length;
    }
    
    public boolean isFull(){
        for(int i = 0; i < B.length; i++){
            if(degree(i) < B.length - 1)
                return false;
        }
        return true;                
    }
    
    public void printEdges(){                       // print out the edge matrix, as shown above; this is only for debugging
        System.out.print("     ");
        for(int i = 1; i < this.B.length +1; ++i){
            System.out.print(i+"   ");
        }
        System.out.println();
        System.out.print("     ");
        for(int i = 0; i < this.B.length; ++i){
            System.out.print("-   ");
        }
        System.out.println();
        for(int i = 0; i < this.B.length; ++i){
            System.out.print((i+1)+" |  ");
            for(int n = 0; n < this.B.length; ++n){
                if(n<5 && this.B[i][n+1] == -1){
                    System.out.print(this.B[i][n]+"  ");
                }else{
                System.out.print(this.B[i][n]+"   ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    
    public boolean isCycleOfLength(int n, int w){  // return true iff there is a cycle of length n among edges of color w 
        for(int i = 0; i < B.length; i++){
            visited[i] = true;
            for(int j = 0; j < B.length; j++){
                visited[j] = true;
                if(getEdge(i, j) == w){
                    if(isCycleHelper(j, i, n, w))
                        return true;
                }
                visited[j] = false;
            }
            visited[i] = false;
        }
        return false;
    }
    
    private boolean isCycleHelper(int u, int v, int n, int w){
        if(n == 2){
            if(getEdge(u, v) == w)
                return true;
            else
                return false;
        }else{
            for(int i = 0; i < B.length; i++){
                if(getEdge(u, i) == w && !visited[i]){
                    visited[i] = true;
                    boolean temp = isCycleHelper(i, v, --n, w);
                    visited[i] = false;
                    return temp;
                }
            }
        }
        return false;
    }
    
    public static void main(String[] args){
        Graph G = new Graph(6);
        
        //addEdge - add an edge from vertex u to v with value w (which in this hw will be  only 0, -1, or 1)
        System.out.println("Going to draw lines of red among points 1, 2, and 3");
        System.out.println(); 
        G.addEdge(0, 1, 1);
        G.addEdge(1, 2, 1);
        G.addEdge(0, 2, 1);
        G.printEdges();
        
        //removeEdge - remove the edge from u to v and the (duplicate) edge from v to u
        System.out.println("Going to remove lines of red among points 2 and 3");
        System.out.println();
        G.removeEdge(1, 2);
        G.printEdges();
        
        //getEdge - return the value (-1, 0, or 1) of the edge that goes from u to v
        System.out.println("Going to get the value of the edge from 1 to 2");
        System.out.println();
        G.printEdges();
        System.out.println("1");
        System.out.println(G.getEdge(0, 1));
        System.out.println();
        
        //isEdge - return true or false depending on whether there is an edge (of either color) from u to v
        System.out.println("Going to see if there is an edge between 1 and 2");
        System.out.println();
        G.printEdges();
        System.out.println("true");
        System.out.println(G.isEdge(0, 1));
        System.out.println();
        System.out.println("Going to see if there is an edge between 5 and 6");
        System.out.println();
        G.printEdges();
        System.out.println("false");
        System.out.println(G.isEdge(4, 5));
        System.out.println();
        
        System.out.println("Adding more vertices: Blue 4 to 5, Blue 5 to 6, and Blue 3 to 5");
        G.addEdge(3, 4, -1);
        G.addEdge(4, 5, -1);
        G.addEdge(2, 4, -1);
        System.out.println();
        
        //degree (with one parameter) - return the number of edges of either color connected to vertex v
        System.out.println("Going to see the number of edges connected to vertex 3");
        System.out.println();
        G.printEdges();
        System.out.println("2");
        System.out.println(G.degree(2));
        System.out.println();
        
        //degree (with two parameters) - return the number of edges of color w connected to vertex v
        System.out.println("Going to see the number of Blue edges connected to vertex 3");
        System.out.println();
        G.printEdges();
        System.out.println("1");
        System.out.println(G.degree(2, 1));
        System.out.println();
        
        
         //isCycleOfLength - return true iff there is a cycle of length n among edges of color w 
        System.out.println("Going to see if there are any red triangles amongst the current graph");
        System.out.println();
        G.printEdges();
        System.out.println("false");
        System.out.println(G.isCycleOfLength(3, 1));
        System.out.println();
        
        System.out.println("Now going to add a triangle and see if a blue cycle is found");
        G.addEdge(2, 5, -1);
        G.addEdge(0, 5, -1);
        G.addEdge(0, 2, -1);
        System.out.println();
        System.out.println("true");
        System.out.println(G.isCycleOfLength(3, -1));

    }
    
}