import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Node implements Comparable<Node> {
    int[][] state;
    List<String> moves;
    int zeroRow;
    int zeroCol;
    int cost;
    int heuristic;
    

    public Node(int[][] state, List<String> moves, int cost, int heuristic) {
        this.state = state;
        this.moves = moves;
        this.cost = cost;
        this.heuristic = heuristic;

        // Find position of zero
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                if (state[i][j] == 0) {
                    zeroRow = i;
                    zeroCol = j;
                    return;
                }
            }
        }
    }

// Check if the current state matches the goal state
    public boolean isGoalState() {
        int[][] goalState = {
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8}
        };

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                if (state[i][j] != goalState[i][j]) {
                    return false; // if any of the numbers don't match up then exit
                }
            }
        }
        return true;
    }

// Duplicate the state
    public int[][] duplicateState(int[][] originalState) {
        int[][] newState = new int[originalState.length][];
        for (int i = 0; i < originalState.length; i++) {
            newState[i] = originalState[i].clone();
        }
        return newState;
    }

// Get the list of valid moves from the current state
    public String[] getValidMoves() {
        String[] directions = {"left", "right", "up", "down"};
        String[] validDirections = new String[directions.length];
        int count = 0;

        for (String direction : directions) {
            int targetRow = zeroRow;
            int targetCol = zeroCol;

            if (direction.equals("left")) {
                targetCol--;
            } else if (direction.equals("right")) {
                targetCol++;
            } else if (direction.equals("up")) {
                targetRow--;
            } else if (direction.equals("down")) {
                targetRow++;
            }

            if (targetRow >= 0 && targetRow < state.length && targetCol >= 0 && targetCol < state[0].length) {
                validDirections[count++] = direction;
            }
        }
        return Arrays.copyOf(validDirections, count);
    }


// Apply move and return a new Node
    public Node move(String direction) {
        int[][] newState = duplicateState(state);
        int targetRow = zeroRow;
        int targetCol = zeroCol;

        // Determine the new position of the zero tile
        if (direction.equals("left")) {
            targetCol--;
        } 
        else if (direction.equals("right")) {
            targetCol++;
        } 
        else if (direction.equals("up")) {
            targetRow--;
        } 
        else if (direction.equals("down")) {
            targetRow++;
        }

        // Swap the zero tile with the target tile
        newState[zeroRow][zeroCol] = newState[targetRow][targetCol];
        newState[targetRow][targetCol] = 0;

        // Create a new node with the updated state
        List<String> newMoves = new ArrayList<>(moves);
        newMoves.add(direction);
        return new Node(newState, newMoves, this.cost + 1, this.heuristic);
    }

// Provides current state
    public int[][] getState() {
        return state;
    }    

// Provides list of move sequence
    public List<String> getMoves() {
        return this.moves;
    }
    
// Provides cost for a* search
    public int getCost() {
        return cost;
    }

// Calculate total cost for a* search
    public int getTotalCost() {
        return cost + heuristic;
    }

// Determines order of nodes for priority queue by comparing total cost of two nodes
    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.getTotalCost(), other.getTotalCost());
    }
}


