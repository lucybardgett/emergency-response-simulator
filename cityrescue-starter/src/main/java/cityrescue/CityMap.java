package cityrescue;

import cityrescue.exceptions.*;
import cityrescue.enums.*;

// represents the 2D grid map
// Tracks grid dimensions and which cells are blocked by obstacles
public class CityMap {
    private int width;
    private int height;
    private boolean[][] blocked;

    public CityMap(int width, int height) {
        this.width = width; //number of columns
        this.height = height; //number of rows
        this.blocked = new boolean[width][height];
    }

// checks if coordinate is within bounds
// x = column, y = row and will return true if in bounds
    public boolean isInBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

//checks if cell is blocked by obstacle
    public boolean isBlocked(int x, int y) {
        return blocked[x][y];
    }

//blocks or unblocks cell (true = block, false = unblock)
    public void setBlocked(int x, int y, boolean value) {
        blocked[x][y] = value;
    }

//count number of blocked cells
    public int countObstacles() {
        int count = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (blocked[x][y]) count++;
            }
        }
        return count;
    }

// Resets all cells to unblocked
    public void clearObstacles() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                blocked[x][y] = false;
            }
        }
    }
//  grid width
    public int getWidth() {
        return width;
    }

// grid height
    public int getHeight() {
        return height;
    }
}