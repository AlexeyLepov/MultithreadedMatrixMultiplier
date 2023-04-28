package Archive;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MatrixReader {
    public static int[][] readMatrix(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line = reader.readLine(); // read first line
        String[] parts = line.split(" "); // split by spaces
        int numRows = 1; // we already read the first row
        int numCols = parts.length;
        while ((line = reader.readLine()) != null) {
            numRows++;
        }
        reader.close(); // close the file

        int[][] matrix = new int[numRows][numCols]; // create matrix

        reader = new BufferedReader(new FileReader(filename)); // reopen the file
        int row = 0;
        while ((line = reader.readLine()) != null) {
            parts = line.split(" ");
            for (int col = 0; col < numCols; col++) {
                matrix[row][col] = Integer.parseInt(parts[col]);
            }
            row++;
        }
        reader.close(); // close the file again
        return matrix;
    }

    public static void main(String[] args) throws IOException {
        int[][] matrixA = readMatrix("matrix_A.txt");
        for (int[] row : matrixA) {
            for (int num : row) {
                System.out.print(num + " ");
            }
            System.out.println();
        }
    }
}
