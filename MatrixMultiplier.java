import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MatrixMultiplier
{
    ///////////////////////
    //                   //
    //    main method    //
    //                   //
    ///////////////////////
    public static void main(String[] args)throws IOException
    {
        // Read matrices from a file
        int[][] matrixA = readMatrixFromFile("matrix_A.txt");
        int[][] matrixB = readMatrixFromFile("matrix_B.txt");
        // check the condition of matrix multiplicity
        if (matrixA[0].length != matrixB.length && matrixB[0].length != matrixA.length)
        {
            System.out.println("Matrices have different dimensions and cannot be multiplied.");
            return;
        }
        // create a thread pool
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        // create matrix C       
        int[][] matrixC = new int[matrixA.length][matrixB[0].length];
        // multiply matrices using threading
        for (int i = 0; i < matrixA.length; i++)
        {
            for (int j = 0; j < matrixB[0].length; j++)
            {
                executor.execute(new MatrixMultiplierThread(matrixA, matrixB, matrixC, i, j));
            }
        }
        // shut down the thread pool
        executor.shutdown();
        // wait for all threads to finish
        while (!executor.isTerminated()) {}
        // write matrix C to file
        writeMatrixToFile(matrixC, "matrix_C.txt");
        System.out.println("Matrices multiplied successfully!");
    }
    
    /////////////////////////////////////////////////////////////////////////////////
    //                                                                             //
    //    reading a matrix from file and returning it as a 2D array of integers    //
    //                                                                             //
    /////////////////////////////////////////////////////////////////////////////////
    public static int[][] readMatrixFromFile(String fileName) throws IOException
    {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line = reader.readLine(); // read first line
        String[] parts = line.split(" "); // split by spaces
        int numRows = 1; // we already read the first row
        int numCols = parts.length;
        while ((line = reader.readLine()) != null) {
            numRows++;
        }
        reader.close(); // close the file

        int[][] matrix = new int[numRows][numCols]; // create matrix

        reader = new BufferedReader(new FileReader(fileName)); // reopen the file
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
    
    //////////////////////////////////////////////////////////////////////////////////////
    //                                                                                  //
    //    writing a matrix to file in the format of rows of space-separated integers    //
    //                                                                                  //
    //////////////////////////////////////////////////////////////////////////////////////
    public static void writeMatrixToFile(int[][] matrix, String fileName)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName)))
        {
            for (int i = 0; i < matrix.length; i++)
            {
                for (int j = 0; j < matrix[i].length; j++)
                {
                    writer.write(matrix[i][j] + " ");
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //////////////////////////////////////////////////////////////
    //                                                          //
    //    A thread that multiplies a single cell of matrix C    //
    //                                                          //
    //////////////////////////////////////////////////////////////
    private static class MatrixMultiplierThread implements Runnable 
    {
        private int[][] matrixA;
        private int[][] matrixB;
        private int[][] matrixC;
        private int i;
        private int j;
        
        public MatrixMultiplierThread(int[][] matrixA, int[][] matrixB, int[][] matrixC, int i, int j)
        {
            this.matrixA = matrixA;
            this.matrixB = matrixB;
            this.matrixC = matrixC;
            this.i = i;
            this.j = j;
        }
    
        @Override
        public void run()
        {
            int sum = 0;
            for (int k = 0; k < matrixA[0].length; k++)
            {
                sum += matrixA[i][k] * matrixB[k][j];
            }
            matrixC[i][j] = sum;
        }
    }
}
