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