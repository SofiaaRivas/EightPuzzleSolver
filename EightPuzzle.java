import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.ArrayList;
import java.util.Stack;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.lang.Math;

public class EightPuzzle{
    private int[][] state;
    private Random random = new Random();
    
    // Constructor for seed
    public EightPuzzle(int[][] startState, long seed) {
        this.state = startState;
        this.random = new Random(seed); // Initialize Random with a seed
    }

    public EightPuzzle(int[][] startState) {
        this(startState, new Random().nextLong()); 
    }

// Convert 2d array into string representation for hashing 
    private String getStateKey(int[][] state) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : state) {
            for (int num : row) {
                sb.append(num).append(",");
            }
        }
        return sb.toString();
    }

// setState method
    public void setState(String puzzleStateString){
        String[] elements = puzzleStateString.split(" ");
        if (elements.length != 9){ // ensures valid configuration of 9 values
            System.out.println("Error: invalid puzzle state");
            return;
        }

        int[][] newState = new int[3][3]; // create 2d array that holds 8-puzzle state
        int sum = 0; 

        // populate 2d array
        try {
            for (int i = 0; i < state.length; i++) {
                for (int j = 0; j < state[i].length; j++) {
                    // Converts the string into an integer and calculates the index of 1D array 
                    // to its corresponding index in the 2D array
                    int number = Integer.parseInt(elements[i * 3 + j]); // 
                    newState[i][j] = number;
                    sum += number;
                }
            }
        }
        catch (NumberFormatException e) { // check for invalid inputs
            System.out.println("Error: Invalid number format.");
            return;
        }

        // Checks state is valid configuration of 9 elements from 0 - 8
        if (sum != 36) {
            System.out.println("Error: Invalid puzzle state");
            return;
        }
        // Set the state of the puzzle once it has checked it's a valid state
        this.state = newState;
    }

//move method
   public void move(String direction){
    int originalRow = -1;
    int originalCol = -1;

    // Find the indices of the 0 i.e blank space
    for (int i = 0; i < state.length; i++) {
        for (int j = 0; j < state[i].length; j++) {
            if (state[i][j] == 0) {
                originalRow = i;
                originalCol = j;
                break; // break if 0 is found
            }
        }
        if (originalRow != -1){
            break; // break if 0 is found
        }
    }

    // initialize variables of the new position
    int targetRow = originalRow;
    int targetCol = originalCol;

    // Determine the direction to move
    if (direction.equals("up")){
        targetRow--;
    }
    else if (direction.equals("down")){
        targetRow++;
    }
    else if (direction.equals("right")){
        targetCol++;
    }
    else if (direction.equals("left")){
        targetCol--;
    }
    else{
        System.out.println("Error: Invalid Move");
    }

    if (targetRow >= 0 && targetRow < state.length && targetCol >= 0 && targetCol < state.length){ // Check move is not out of array's bounds
        // Swap elements
        state[originalRow][originalCol] = state[targetRow][targetCol];
        state[targetRow][targetCol] = 0; 
    }
    else{
        System.out.println("Error: Invalid Move, out of bounds");
        return;
    }
}

// scrambleState method
    public void scrambleState(String scrambleString) {
        int[][] goalState = {
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8}
        };

        this.state = goalState;

        try {
            int scrambleAmount = Integer.parseInt(scrambleString); // turn string to int
            
            // Perform specified amount of random moves
            for (int i = 0; i < scrambleAmount; i++) {
                String direction = randomMove(); // call method
                if (direction != null) {
                    move(direction);
                }
            }
        } 
        catch (NumberFormatException e) {
            System.out.println("Error: Invalid number format");
        }
    }
    
// Helper method for scrambleState to get valid move
   private String randomMove() {
    // Find the index of the 0 
    int zeroRow = -1;
    int zeroCol = -1;
    for (int i = 0; i < state.length; i++) {
        for (int j = 0; j < state[i].length; j++) {
            if (state[i][j] == 0) {
                zeroRow = i;
                zeroCol = j;
                break;
            }
        }
        if (zeroRow != -1) break;
    }

    // List of all possible moves
    String[] directions = {"up", "down", "left", "right"};
    String[] validDirections = new String[directions.length];
    int count = 0;

    // check valid directions based on current position of 0 
    for (String direction : directions) {
        int targetRow = zeroRow;
        int targetCol = zeroCol;

        if (direction.equals("up")) {
            targetRow--;
        } 
        else if (direction.equals("down")) {
            targetRow++;
        } 
        else if (direction.equals("left")) {
            targetCol--;
        } 
        else if (direction.equals("right")) {
            targetCol++;
        }

        // Check if the target position is within bounds
        if (targetRow >= 0 && targetRow < state.length && targetCol >= 0 && targetCol < state[0].length) {
            validDirections[count++] = direction;
        }
    }

    // Choose random valid move
    if (count > 0) {
        return validDirections[random.nextInt(count)];
    } 
    else {
        return null;
    }
}

// PrintState method
    public void printState() {
        for (int i = 0; i < state.length; i++){
            for(int j = 0; j < state[i].length; j++){
                if (state[i][j] == 0){
                    System.out.print("  ");
                }
                else{ 
                    System.out.print(state[i][j] +  " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

// Helper method for search algorithms that checks maxnodes input 
    public int maxNodesInput(String input){
        int maxNodes = 1000; 
        if (input == null){ // Provides default maxNodes value if not provided 
            return maxNodes;
        }
        try{
            // Parse the max nodes if there is an argument provided
            if (input.contains("maxnodes=")) {
                String[] parts = input.split("maxnodes=");
                maxNodes = Integer.parseInt(parts[1].trim());
                return maxNodes;
            }
            else{ 
                return maxNodes = 0;
            }
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Error: Invalid Command");
        }
    }

// DFS method
    public void DFS(String command) {
        int maxNodes = maxNodesInput(command);
        if (maxNodes == 0){
            System.out.println("Error: Invalid Command");
            return;
        }

        Stack<Node> stack = new Stack<>();
        int nodesCreated = 0;
        int visitedCTR = 0;

        Node startNode = new Node(state, new ArrayList<>(), 0 , 0);
        Set<String> visited = new HashSet<>(); // create set to track repeated states

        stack.push(startNode);
        visited.add(getStateKey(startNode.getState())); // add to visited
        nodesCreated++;

        while (!stack.isEmpty()) {
            Node currentNode = stack.pop();

            if (currentNode.isGoalState()) {
                System.out.println("\nNodes created during search: " + nodesCreated);
                System.out.println("Solution length: " + currentNode.moves.size());
                System.out.println("Move sequence:");
                for (String move : currentNode.moves) {
                    System.out.println("move " + move);
                }
                System.out.println("Number of repeated states encountered: " + visitedCTR + "\n");
                return;
            }

            for (String move : currentNode.getValidMoves()) {
                Node successorNode = currentNode.move(move);
                String successorStateKey = getStateKey(successorNode.getState()); 
                if (!visited.contains(successorStateKey)) { // checks if this is a repeated state
                    if (nodesCreated < maxNodes) {
                        stack.push(successorNode); // adds node to stack
                        visited.add(getStateKey(successorNode.getState())); // add to visited
                        nodesCreated++;
                    } 
                    else {
                        System.out.println("Error: maxnodes limit (" + maxNodes + ") reached");
                        return;
                    }
                }
                else{
                    visitedCTR++;
                }
            }
        }
    }

// BFS Method
    public void BFS(String command) {
        int maxNodes = maxNodesInput(command);
        if (maxNodes == 0){
            System.out.println("Error: Invalid Command");
            return;
        }

        Queue<Node> queue = new LinkedList<>();
        int nodesCreated = 0;
        
        Node startNode = new Node(state, new ArrayList<>(), 0, 0);
        queue.add(startNode);
        nodesCreated++;

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();

            if (currentNode.isGoalState()) {
                System.out.println("\nNodes created during search: " + nodesCreated);
                System.out.println("Solution length: " + currentNode.moves.size());
                System.out.println("Move sequence:");
                for (String move : currentNode.moves) {
                    System.out.println("move " + move);
                }
                return;
            }
            
            for (String move : currentNode.getValidMoves()) {
                Node successorNode = currentNode.move(move); 
                if (nodesCreated < maxNodes) {
                    queue.add(successorNode); // adds node to stack
                    nodesCreated++;
                } 
                else{
                    System.out.println("Error: maxnodes limit (" + maxNodes + ") reached");
                    return;
                }
            }
        }
    }

// method to apply heuristic to the current state
    @FunctionalInterface
    interface Heuristic {
        int apply(int[][] state); 
    }

// A* method
    public void aStar(String heuristicType, String nodes) {
        int maxNodes = maxNodesInput(nodes); // assigns maxNodes
        if (maxNodes == 0){
            System.out.println("Error: Invalid Command");
            return;
        }

        // sets appropiate heuristic function
        Heuristic heuristicFunction;
        if (heuristicType.equals("h1")) {
            heuristicFunction = this::heuristic1;
        } else if (heuristicType.equals("h2")) {
            heuristicFunction = this::heuristic2;
        } else {
            System.out.println("Error: Invalid Command");
            return;
        }

        // Initialize priority queue
        PriorityQueue<Node> frontier = new PriorityQueue<>(Comparator.comparingInt(Node::getTotalCost));
        Set<String> visited = new HashSet<>(); // create set to track repeated states
        int nodesCreated = 0;

        Node startNode = new Node(state, new ArrayList<>(), 0, 0); // create instance of node class
        startNode.heuristic = heuristicFunction.apply(startNode.getState());
        frontier.add(startNode);
        nodesCreated++;

        while (!frontier.isEmpty()) {
            Node currentNode = frontier.poll(); // takes node from front of priority queue

            // prints out solution when equal to goal state
            if (currentNode.isGoalState()) {

                System.out.println("\nNodes created during search: " + nodesCreated);
                System.out.println("Solution length: " + currentNode.getMoves().size());
                System.out.println("Move sequence:");
                for (String move : currentNode.getMoves()) {
                    System.out.println("move " + move);
                }
                return;
            }

            visited.add(getStateKey(currentNode.getState())); // add to visited

            for (String move : currentNode.getValidMoves()) {
                Node successorNode = currentNode.move(move);
                successorNode.cost = currentNode.getCost() + 1;
                successorNode.heuristic = heuristicFunction.apply(successorNode.getState());
                
                // turn successor node into hash key to compare it to the visited states hash set 
                String successorStateKey = getStateKey(successorNode.getState()); 
                if (visited.contains(successorStateKey)) { // checks if this is a repeated state
                    continue;
                }

                if (!frontier.contains(successorNode)) { 
                    frontier.add(successorNode);
                    nodesCreated++;
                } 
                else {
                    // Update the priority if this path is better
                    for (Node nodeInQueue : frontier) {
                        if (nodeInQueue.getState() == successorNode.getState() && successorNode.getTotalCost() < nodeInQueue.getTotalCost()) {
                            frontier.remove(nodeInQueue);
                            frontier.add(successorNode);
                            break;
                        }
                    }
                }
                // checks to make sure maxNodes is not exceeded 
                if (nodesCreated >= maxNodes) {
                    System.out.println("Error: maxnodes limit (" + maxNodes + ") reached");
                    return;
                }
            }
        }
    }

// heuristic 1 calculates # of tiles misplaced
    public int heuristic1(int[][] state) {
        int[][] goalState = {
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8}
        };

        int misplacedTiles = 0;
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                if (state[i][j] != goalState[i][j]) {
                    if (state[i][j] == 0){
                        continue; // skips the blank tile
                    }
                    misplacedTiles ++;
                }
            }
        }
        return misplacedTiles;
    }

// heuristic 2 calculates manhattan distance of tiles
    public int heuristic2(int[][] state) {
        int totalManhattanDistance = 0;
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                int tile = state[i][j];
                if (tile != 0){
                    int goalRow = tile / state.length; // finds position of row in goal state
                    int goalCol = tile % state[0].length; // finds position of col in goal state

                    int verticalDistance = Math.abs(i - goalRow);
                    int horizontalDistance = Math.abs(j - goalCol);

                    totalManhattanDistance += verticalDistance + horizontalDistance;
                }
            }
        }
        return totalManhattanDistance;
    }


// Method to calculate branching factor using newton method
    public void branchingFactor(String nodes, String d){
        int totalNodes = 0;
        int depth = 0;
        try{
            // Parse the input into totalnodes and depth
            if (nodes.contains("totalnodes=") && d.contains("depth=")) {
                String[] part1 = nodes.split("totalnodes=");
                String[] part2 = d.split("depth=");

                totalNodes = Integer.parseInt(part1[1].trim());
                depth = Integer.parseInt(part2[1].trim());
            }
            else {
                throw new IllegalArgumentException("Error: Missing totalnodes or depth.");
            }
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Error: Invalid Command");
        }

        double N = totalNodes++; // adds one to follow equation in textbook
        double precision = 0.001;
        double initialBF = 4.0; // max possible moves at a state is 4
        double currentBF = initialBF;
        double previousBF = 0.0;
        boolean converged = false;

        while(!converged){ 
            double regularSum = 0;
            double derivativeSum = 0;

            for (int i = 0; i <= depth; i++){
                regularSum += Math.pow(currentBF, i); // Sum of branching factor raised to depth f(b*) 
                if (i > 0){
                    derivativeSum += i * Math.pow(currentBF, i -1); // sum of derivative f'(b*)
                }
            }

            regularSum = regularSum - N;
            if (Math.abs(regularSum) < precision){
                System.out.println(Math.round(currentBF * 100.0) / 100.0);
                return;
            }

            previousBF = currentBF;
            currentBF = currentBF - (regularSum / derivativeSum); // b* - (f(b*)/f'(b*))

            if (Math.abs(currentBF - previousBF) < precision) { // If difference is within precision, we've found the branching factor
                converged = true; // Exit while loop
            }
        }
        System.out.println(Math.round(currentBF * 100.0) / 100.0);
    }

// input string commands method
    public void cmd(String command) {
        // Parse the command string and call appropriate methods
        String[] parts = command.split(" ");  // Split at the first space
        String commandName = parts[0]; 
        String commandInput = " "; // for move and scrambleState
        if (parts.length > 1) { // store the user input if there is any
            commandInput = parts[1];
        }
        

        // Determine which command to execute
        if (commandName.equals("setState")) {
            String stateInput = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
            setState(stateInput);
        }
        else if (commandName.equals("move")) {
            move(commandInput);
        }
        else if (commandName.equals("scrambleState")) {
            scrambleState(commandInput);
        }
        else if (commandName.equals("printState")) {
            printState(); // printState does not require input 
        }
        else if (commandName.equals("solve")) {
            if (commandInput.equals("DFS")){
                // checks if maxnodes arguement provided and calls DFS accordingly
                if (parts.length > 2){
                    String nodes = parts[2];
                    DFS(nodes);
                }
                else{
                    DFS(null);
                }
            }
            else if (commandInput.equals("BFS")) {
                // checks if maxnodes arguement provided and calls BFS accordingly
                if (parts.length > 2){
                    String nodes = parts[2];
                    BFS(nodes);
                }
                else{
                    BFS(null);
                }  
            }
            else if (commandInput.equals("A*")) {
                // checks if maxnodes arguement provided and calls aStar accordingly
                if (parts.length > 2){
                    String heuristicType = parts[2];
                    if (parts.length > 3){
                        String nodes = parts[3];
                        aStar(heuristicType, nodes);
                    }
                    else{
                        aStar(heuristicType, null);
                    }  
                }
                else{
                    return;
                }
            } 
        }
        else if (commandName.equals("heuristic")){
            if (commandInput.equals("h1")){
                System.out.println("Total misplaced tiles: " + heuristic1(state));
            }
            else if (commandInput.equals("h2")){
                heuristic2(state);
                System.out.println("Total Manhattan Distance: " + heuristic2(state));
            }
        }
        else if (commandName.equals("BF")){
            if (parts.length > 1){
                String nodes = parts[1];
                String d = parts[2];
                branchingFactor(nodes, d);
            }
        }
        else{
            System.out.println("Error: Invalid Command:"); 
        }
    }
}

