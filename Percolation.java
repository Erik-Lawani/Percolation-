import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int len; // Size of the grid (n)
    private boolean[][] grid; // 2D array to represent the open/close status of sites
    private int openSites; // Number of open sites


    private WeightedQuickUnionUF union; // Union-find data structure for connectivity
    private WeightedQuickUnionUF backWashing; // Union-find for backwash prevention
    private int top; // Virtual top site to help check percolation
    private int bottom; // Virtual bottom site to help check percolation

    /*
     * Constructor to create an n-by-n grid with all sites initially blocked.
     * Initializes union-find structures, virtual top and bottom sites,
     * and the openSites counter.
     *
     * n is the size of the grid.
     * IllegalArgumentException if n is less than or equal to 0.
     */
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n has to be greater than 0");
        this.len = n;
        grid = new boolean[n][n];
        openSites = 0;
        int m = n * n;

        // Extra 2 sites for virtual top and bottom
        union = new WeightedQuickUnionUF(m + 2);

        // Extra 1 site for backwash prevention
        backWashing = new WeightedQuickUnionUF(m + 1);
        top = m;
        bottom = m + 1;
    }

    /*
     * Opens the site (row, col) if it is not open already.
     * Connects the site with its open neighbors and virtual top/bottom
     * sites as necessary.
     * row: The row of the site.
     * col: The column of the site.
     */
    public void open(int row, int col) {
        checkIllegal(row, col);

        if (!isOpen(row, col)) grid[col][row] = true;
        else return;

        openSites++;

        int index = convert(row, col);

        // Connect with neighboring open sites
        connectWithNeighbor(row + 1, col, index);
        connectWithNeighbor(row - 1, col, index);
        connectWithNeighbor(row, col + 1, index);
        connectWithNeighbor(row, col - 1, index);

        // Connect with virtual top and bottom sites
        if (row == 0) {
            union.union(index, top);
            backWashing.union(index, top);
        }

        if (row == len - 1) union.union(index, bottom);
    }

    /*
     * Checks if the site (row, col) is open.
     */
    public boolean isOpen(int row, int col) {
        checkIllegal(row, col);
        return grid[col][row];
    }

    /*
     * Checks if the site (row, col) is full.
     * A site is full if it is connected to the virtual top site.
     * return true if the site is full and false otherwise
     */
    public boolean isFull(int row, int col) {
        checkIllegal(row, col);
        int index = convert(row, col);
        return backWashing.find(index) == backWashing.find(top);
    }

    /*
     * Returns the number of open sites.
     */
    public int numberOfOpenSites() {
        return this.openSites;
    }

    /*
     * Checks if the system percolates.
     * The system percolates if the virtual top and bottom sites are connected.
     */
    public boolean percolates() {
        return union.find(bottom) == union.find(top);
    }

    /*
     * Helper method to check if a site is within bounds.
     * Throw an illegal argument if the site is out of bounds.
     */
    private void checkIllegal(int row, int col) {
        if (row < 0 || row > len - 1 || col < 0 || col > len - 1) {
            throw new IllegalArgumentException("One value is out of bound");
        }
    }

    /*
     * Helper method to convert 2D coordinates to 1D index.
     * return the 1D index corresponding to the 2D coordinates.
     */
    private int convert(int row, int col) {
        return this.len * row + col;
    }

    /*
     * Helper method to check if a site is within bounds.
     * return True if the site is within bounds, false otherwise.
     */
    private boolean bounded(int row, int col) {
        return !(row < 0 || row > len - 1 || col < 0 || col > len - 1);
    }

    /*
     * Helper method to connect a site with its neighbor if open.
     */
    private void connectWithNeighbor(int row, int col, int index) {
        if (bounded(row, col) && isOpen(row, col)) {
            int index2 = convert(row, col);
            union.union(index2, index);
            backWashing.union(index2, index);
        }
    }

    /*
     * Unit testing method to check the functionality of the Percolation class.
     */
    public static void main(String[] args) {
        int n = 5;
        Percolation percolation = new Percolation(n);
        percolation.open(2, 3);
        percolation.open(0, 3);
        percolation.open(1, 3);
        StdOut.println(percolation.isFull(0, 3));
        StdOut.println(percolation.percolates());
        percolation.open(3, 3);
        percolation.open(4, 3);
        StdOut.println(percolation.percolates());
        StdOut.println(percolation.numberOfOpenSites());
        StdOut.println(percolation.isOpen(4, 4));
    }
}
