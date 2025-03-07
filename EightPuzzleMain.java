import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class EightPuzzleMain {
    public static void main(String[] args) {
        long seed = 12345L;
        EightPuzzle puzzle;
 
        // Check for seed argument
        if (args.length > 1) {
            try {
                seed = Long.parseLong(args[1]); 
            }
            catch (NumberFormatException e) {
                System.out.println("Invalid seed");
            }
        }
        puzzle = new EightPuzzle(new int[3][3], seed); // Create an instance of EightPuzzle with seed
        
        // Checks if file name is provided 
        if(args.length > 0) {
            String filename = args[0];
            cmdfile(filename, puzzle);
        }
        processUserInput(puzzle);
    }

// Takes commands from user directly
    public static void processUserInput(EightPuzzle puzzle) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter command (type 'quit' when done):");
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("quit")) {  // Exits program
                break; 
            }
            puzzle.cmd(input); // Execute the command
        }
        scanner.close();
    }

// Ignore comments and empty lines method 
    public static boolean comment(String line) {
        line = line.trim(); 
        return line.startsWith("//") || line.isEmpty();
    }

// Open and read commands from a file function
    public static void cmdfile(String filename, EightPuzzle puzzle) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (comment(line)) { // skips comments or empty lines
                    System.out.println(line);
                    continue;
                }
                System.out.println(line);
                puzzle.cmd(line); // Use the existing cmd(string) method to execute the command
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}


