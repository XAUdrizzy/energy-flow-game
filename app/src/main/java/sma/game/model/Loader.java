package sma.game.model;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Loader {
    private final Scanner in;   // Scanner used to read the file
    private int lineNumber;     // Current line number
    private String line;        // Text of current line

    private Plant model;      // The loaded model
    private int height, width;   // Dimensions of current level

    public Loader(Scanner in) {
        this.in = in;
    }

    //Read level
    public Plant load(int level) throws LevelFormatException {
        findHeader(level);                  // Find the header line
        model = new Plant(height,width);    // Build the model
        loadGrid();                         // Load cells information
        return model;
    }

    //Read the square grid and instantiate each square
    private void loadGrid() throws LevelFormatException {
        for(int l=0; l<height ; ++lineNumber,++l) {
            line = in.nextLine();                       // Read a line of cells
            String[] cells = line.split("\\s+");        // Split by separators
            if (cells.length!=width)            // Verify number of cells in line
                error("Wrong number of cells in line");
            int c = 0;
            for(String word : cells) {                  // For each description
                char type = word.charAt(0);
                Cell cell = createCell(type);
                model.putCell(l,c++,cell);              // Add cell to the model
            }
        }
    }

    private Cell createCell(char type) throws LevelFormatException {
        Cell cell = Cell.newInstance(type);     // Create a cell identified by first char
        if (cell==null)
            error("Unknown cell type ("+type+")");
        return cell;
    }

    //Find the header line for the level
    private void findHeader(int level) throws LevelFormatException {
        try {
            int idx;
            for (lineNumber = 1; ; ++lineNumber) {
                line = in.nextLine();
                if (line.length() == 0 || line.charAt(0) != '#') continue;
                if ((idx = line.indexOf(' ')) <= 1) error("Invalid header line");
                if (Integer.parseInt(line.substring(1, idx)) == level) break;
            }
            int idxSep = line.indexOf('x',idx+1);
            if (idxSep<=0) error("Missing dimensions of level "+level);
            height = Integer.parseInt(line.substring(idx+1,idxSep).trim());
            width = Integer.parseInt(line.substring(idxSep+1).trim());
        } catch (NumberFormatException e) {
            error("Invalid number");
        } catch (NoSuchElementException e) {
            error("Level " + level + " not found");
        }
    }

    private void error(String msg) throws LevelFormatException {
        throw new LevelFormatException(msg);
    }

    public class LevelFormatException extends Exception {
        public LevelFormatException(String msg) {
            super(msg);
        }
        public int getLineNumber() { return lineNumber; }
        public String getLine() { return line; }
    }
}

