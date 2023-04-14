Multi-threaded Matrix Multiplier
====================================================

This program reads two matrices from files called `matrix_A.txt` and `matrix_B.txt`, checks to make sure they have the same dimensions, creates a group of threads, and then multiplies the matrices together using threading. Every thread is responsible for multiplying a single cell of the resulting matrix. Finally, the program stores the resulting matrix in a file called `matrix_C.txt`.
