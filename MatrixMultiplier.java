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
    public static void main(String[] args)
    {
        // read matrix A from file
        int[][] matrixA = readMatrixFromFile("matrix_A.txt");
        // read matrix B from file
        int[][] matrixB = readMatrixFromFile("matrix_B.txt");
        // check if matrices have same dimensions
        if (matrixA[0].length != matrixB.length)
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
    public static int[][] readMatrixFromFile(String fileName)
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName)))
        {
            String line;
            int rowCount = 0;
            int[][] matrix = null;
            while ((line = reader.readLine()) != null)
            {
                String[] values = line.split(" ");
                if (matrix == null)
                {
                    matrix = new int[values.length][];
                }
                matrix[rowCount] = new int[values.length];
                for (int i = 0; i < values.length; i++)
                {
                    matrix[rowCount][i] = Integer.parseInt(values[i]);
                }
                rowCount++;
            }
            return matrix;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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
