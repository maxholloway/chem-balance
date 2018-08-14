package equation_solver;

import java.util.ArrayList;

public class MatrixOperations {
    
    public static boolean hasRegularSolution;
    public static boolean hasManySolutions;
    public static boolean hasNoSolution;

    
    /*
    This is the method that brings it all together; it takes in
    the parsed matrix and outputs the coefficients as a rowIndx-vector
    NOTE: THIS USES NULLSPACE DIRECTLY, SO A REACTION:
    A + B -> C + D MUST BE INPUTTED AS: A + B - C - D = 0. */
    public static int[] chemSolve(double[][] aInp) {
        hasRegularSolution = true;
        hasManySolutions = false;
        hasNoSolution = false;
        double[][] a = aInp;
        double[][] N = nullspaceMatrix(a);
        // a zero matrix means there are no solutions, so if it is not a zero matrix
        // then we can check to see if it has many solutions or if it has one solution.
        // If it has many solutions, then we return a zero vector. If it is a zero
        // matrix and has no solutions, then we return a zero vector.
        if (totalNonZeroes(N) > 0) {
//            System.out.println("N" + Arrays.deepToString(N));
            int[][] NScaled = matScaleColumns(N); // get integers coeffs from decimals
//            System.out.println("NScaled: " + Arrays.deepToString(NScaled));
            int[][] NScaledTrans = transpose(NScaled); // this is the form of each solution as a rowIndx
            if(NScaledTrans.length == 1){
                return NScaledTrans[0];
            } else {
                hasRegularSolution = false;
                hasManySolutions = true;
                return new int[a[0].length];
            }
        } else {
            hasRegularSolution = false;
            hasNoSolution = true;
            return new int[a[0].length];
        }
        
    }
    
    //<editor-fold defaultstate="collapsed" desc="General Linear Algebra Methods">
    
    // finds the transpose of a matrix
    protected static double[][] transpose(double[][] aInp){
        double a[][] = aInp;
        double newA[][] = new double[a[0].length][a.length];
        
        for(int i = 0; i < a.length; i++){
            for(int j = 0; j < a[0].length; j++){
                newA[j][i] = a[i][j];
            }
        }
        a = newA;
        return a;
    }
    
    // overloading for long input
    private static int[][] transpose(int[][] a) {
        int newA[][] = new int[a[0].length][a.length];
        
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                newA[j][i] = a[i][j];
            }
        }
        return newA;
    }
    
    // exchanges two specified rows of a matrix
    private static double[][] rowExchange(double[][] aInp, int rowM1Indx, int rowM2Indx){
        double[][] a = aInp;
        
        double[] oldRowM1 = a[rowM1Indx];
        
        a[rowM1Indx] = a[rowM2Indx];
        a[rowM2Indx] = oldRowM1;
        
        return a;
    }
    
    /////// NOOOTTTT MMMYYYY METTTHOOODDD!!!!!!
    private static void multiplyRow(double [][] m, int row, double scalar){
        for(int c1 = 0; c1 < m[0].length; c1++) {
            m[row][c1] *= scalar;
        }
    }
    
    /////// NOOOTTTT MMMYYYY METTTHOOODDD!!!!!!
    private static void subtractRows(double [][] m, double scalar, int subtract_scalar_times_this_row, int from_this_row){
        for(int c1 = 0; c1 < m[0].length; c1++) {
            m[from_this_row][c1] -= scalar * m[subtract_scalar_times_this_row][c1];
        }
    }
    
    /////// NOOOTTTT MMMYYYY METTTHOOODDD!!!!!!
    private static double[][] rref(double [][] m){
        int lead = 0;
        int numRows = m.length;
        int numColumns = m[0].length;
        int i;
        boolean quit = false;
        
        // for each row in the maxtrix...
        for(int rowIndx = 0; rowIndx < numRows && !quit; rowIndx++)
        {
            
            if(numColumns <= lead)
            {
                quit = true;
                break;
            }
            
            i=rowIndx;
            
            while(!quit && m[i][lead] == 0)
            {
                i++;
                if(numRows == i)
                {
                    i=rowIndx;
                    lead++;
                    
                    if(numColumns == lead)
                    {
                        quit = true;
                        break;
                    }
                }
            }
            
            if(!quit)
            {
                rowExchange(m, i, rowIndx);
                
                // if it's approximately zero, then make it zero altogether
                if(Math.abs(m[rowIndx][lead]) <= Math.pow(10, -10)){
                    m[rowIndx][lead] = 0;
                }
                
                if(m[rowIndx][lead] != 0) {
                    multiplyRow(m, rowIndx, 1.0 / m[rowIndx][lead]);
                }
                
                for(i = 0; i < numRows; i++)
                {
                    if(i != rowIndx){
                        subtractRows(m, m[i][lead], rowIndx, i);
                    }
                }
            }
        }
        return m;
    }

    // removes decimal parts from a matrix by scaling it up
    private static int[][] matScaleColumns(double[][] matInp) {
        double[][] mat = matInp;
        double[][] matTrans = transpose(mat);
        int[][] newMatTrans = new int[matTrans.length][matTrans[0].length];
        for (int i = 0; i < matTrans.length; i++) {
            newMatTrans[i] = NateReduce.reduce(matTrans[i]);
        }
        
        return transpose(newMatTrans);
    }
    
    // counts how many non-zero integers are in an integer array
    private static int totalNonZeroes(double[][] aInp) {
        double[][] a = aInp;
        int sum = 0;
        for (double[] a1 : a) {
            for (int j = 0; j < a[0].length; j++) {
                if (a1[j] != 0) {
                    sum++;
                }
            }
        }
        return sum;
    }
    
    // reports if a rowIndx is only zeroes
    private static boolean isZeroRow(double[] rowInp){
        double[] row = rowInp;
        
        boolean troofValue = false;
        
        int zeroCount = 0;
        for(double element : row){
            if(element == 0){
                zeroCount++;
            }
        }
        
        if(zeroCount == row.length){
            troofValue = true;
        }
        
        return troofValue;
    }
    
    // returns how many rows of an array are filled with zeroes
    private static int numZeroRows(double[][] aInp){
        double[][] a = aInp;
        
        int zeroRowCount = 0;
        for(double[] row : a){
            if(isZeroRow(row)){
                zeroRowCount++;
            }
        }
        
        return zeroRowCount;
    }
    
    // generates an identity matrix of a given dimension
    private static double[][] identityGen(int numRowsInp){
        int numRows = numRowsInp;
        
        double[][] identityMatrix = new double[numRows][numRows];
        
        for(int i = 0; i < numRows; i++){
            identityMatrix[i][i] = 1;
        }
        
        return identityMatrix;
    }
    
    protected static double[] multRowVecByScalar(double[] rowVec, double scalar) {
        for (int i = 0; i < rowVec.length; i++) {
            rowVec[i] = rowVec[i] * scalar;
        }
        return rowVec;
    }
    
    protected static int[] multRowVecByScalar(int[] rowVec, int scalar) {
        for (int i = 0; i < rowVec.length; i++) {
            rowVec[i] = rowVec[i] * scalar;
        }
        return rowVec;
    }
    
    protected static long[] multRowVecByScalar(long[] rowVec, long scalar) {
        for (int i = 0; i < rowVec.length; i++) {
            rowVec[i] = rowVec[i] * scalar;
        }
        return rowVec;
    }
    
    protected static ArrayList<String> clone(ArrayList<String> al0) {
        ArrayList<String> clone = new ArrayList<>(al0.size());
        for (int i = 0; i < al0.size(); i++) {
            clone.add(al0.get(i));
        }
        return clone;
    }
    
    protected static double[] subtractRows(double[] posRow, double[] negRow) {
        double[] sum = new double[negRow.length];
        for (int i = 0; i < posRow.length; i++) {
            sum[i] = posRow[i] - negRow[i];
        }
        return sum;
    }
    
    protected static int[] addRows(int[] posRow, int[] negRow) {
        int[] sum = new int[negRow.length];
        for (int i = 0; i < posRow.length; i++) {
            sum[i] = posRow[i] + negRow[i];
        }
        return sum;
    }
    
    protected static long[] addRows(long[] posRow, long[] negRow) {
        long[] sum = new long[negRow.length];
        for (int i = 0; i < posRow.length; i++) {
            sum[i] = posRow[i] + negRow[i];
        }
        return sum;
    }
    
    protected static double[] sumColumnsOf2Dint(int[][] mat) {
        double sum[] = new double[mat[0].length];
        for (int[] mat1 : mat) {
            for (int j = 0; j < mat[0].length; j++) {
                sum[j] += mat1[j];
            }
        }
        
        return sum;
    }
    
        /*
    Returns a matrix that, when multiplied by a given matrix 'A',
    will result in a zero-matrix. Each column of this matrix
    represents an 'x' that solves the equation 'Ax = 0'*/
    private static double[][] nullspaceMatrix(double[][] aInp) {
        double[][] a = aInp; // this is the matrix of coefficients given
//        System.out.println("coeffArray: " + Arrays.deepToString(a));

        // gets the reduced rowIndx echelon form of that matrix
        double[][] r = rref(a);
//        System.out.println("rref: " + Arrays.deepToString(RUnedited));

        // gets the permutation matrix to adjust the rref to the
        // form of [[I, F], [0, 0]]
//        double[][] P = getP(RUnedited);
//        System.out.println("P: " + Arrays.deepToString(P));
        // multiplies the unedited rref by the permutation matrix
        // to get into the aforementioned form
//        double[][] REdited = getEditedRREF(RUnedited, P);
//        System.out.println("redited: " + Arrays.deepToString(REdited));
//        double[][] negFreeVariableVectors = negativeFreeMatrix(REdited);
        double[][] negFreeVariableVectors = negativeFreeMatrix(r);
        if (negFreeVariableVectors != null) {
            int numFreeRows = negFreeVariableVectors.length;
            int numIdentityRows = negFreeVariableVectors[0].length;

            double[][] identityArray = identityGen(numIdentityRows);

            double[][] nullspaceMatrix = new double[numFreeRows + numIdentityRows][numIdentityRows];

            // 2D array copy
            for (int i = 0; i < numFreeRows; i++) {
                System.arraycopy(negFreeVariableVectors[i], 0, nullspaceMatrix[i], 0, numIdentityRows);
            }

            for (int i = numFreeRows; i < nullspaceMatrix.length; i++) {
                nullspaceMatrix[i] = identityArray[i - numFreeRows];
            }

            return nullspaceMatrix;

        } else {
            double zeroes[][] = new double[a.length][1];
            return zeroes;
        }
    }

    // gets a matrix with free-variable columns
    private static double[][] negativeFreeMatrix(double[][] rInp) {
        double[][] r = rInp;

        // gets a version of r without the rowIndx of zeroes, because
        // the free variables do not take into account zero rows
        double[][] rWO0 = new double[r.length - numZeroRows(r)][r[0].length];
        System.arraycopy(r, 0, rWO0, 0, rWO0.length); // literally gets all of r without the zero rows

        // it is known that the number of rows of the identity matrix in
        // reduced rowIndx echelon form is the same as the total number of rows
        // minus the number of zero rows
        int numIdentityRows = rWO0.length;

        double[][] rWO0Trans = transpose(rWO0);

        double[][] negFTrans = new double[rWO0Trans.length - numIdentityRows][rWO0Trans[0].length];

        // for each rowIndx in negFTranspose
        for (int i = 0; i < negFTrans.length; i++) {
            // loop to get negFTrans (disregarding identity rows)
            for (int j = 0; j < rWO0Trans[i + numIdentityRows].length; j++) {

                negFTrans[i][j] = -rWO0Trans[i + numIdentityRows][j];
            }
        }
        if (negFTrans.length > 0 && negFTrans[0].length > 0) {
            double[][] negFreeVariableVectors = transpose(negFTrans);
            return negFreeVariableVectors;
        } else {
            return null;
        }
    }
    
    
//</editor-fold>    
}