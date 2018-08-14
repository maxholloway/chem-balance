package equation_solver;

//import java.util.Arrays;


public class NateReduce {
    // This is a class written by Maxwell Holloway, however the ideas that made
    // it happen were almost entirely from Nate Johnson. It is thus called 
    // NateReduce. 
    
    private static final int[] PRIMES = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31,
        37, 41, 43, 47, 53, 59, 61, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109,
        113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 193,
        197, 199, 211, 223, 227, 229,  233, 239, 241, 251, 257, 263, 269, 271};
    
    // this method is simple: find the smallest common factor to multiply each
    // number by such that they all come out to be integers. This is done by
    // finding the smallest number to multiply each individual mixed number by,
    // then finding the least common multiple of these numbers, and then finally
    // scalar-multiplying the input vector by the least common multiple, giving
    // the most reduced form of the vector.
    protected static int[] reduce(double[] vec) {
        int[] denoms = mixedToIntegerMultipliers(vec);
        int lcm = getLCM(denoms);

        int[] newVec = new int[vec.length];
        for (int i = 0; i < vec.length; i++) {
            newVec[i] = (int) Math.round((vec[i] * lcm));
        }

        return newVec;
    }

    // this method uses the idea that a mixed number * another number is only 
    // equal to an integer if the other number is at least a factor of the
    // denominator (i.e. 1/2 * 2 = 1 = an integer, or 2/4 * 2 = 1 = an integer,
    // or 4 / 67 * 67 = 4 = an integer). This, in effect, gives the least amount
    // by which we must multiply each mixed number in order to get an integer.
    private static int[] mixedToIntegerMultipliers(double[] vec) {
        int[] denoms = new int[vec.length];

        int[] firstPosInts = new int[10000000];

        for (int i = 0; i < firstPosInts.length; i++) {
            firstPosInts[i] = i + 1;
        }

        for (int i = 0; i < vec.length; i++) {

            double element = vec[i];

            double remainder;
            int j = 0;
            do {
                int possibleDenom = firstPosInts[j];
                double prod = element * possibleDenom;
                remainder = prod - Math.floor(prod);
                // System.out.println(remainder);
                j++;
                
                if(j >= firstPosInts.length){
                    System.out.println("There is an error in the 'Nate Reduce' class, multipliers method");
                }
            } while (Math.abs(remainder) > .00000000001 && Math.abs(remainder) < .99999999999);
//            System.out.println("j: " + j);
            denoms[i] = j;

        }
        return denoms;
    }

    
    // this method piggybacks on the mixedToIntegerMultipliers to then find the 
    // least common multiples of the original multiples. It does this by first
    // getting the prime factors of each of the vector's numbers. These are stored
    // in an array, where each row is a vector entry, each column is a prime number,
    // and each entry is how many factors of the prime number the vector entry has.
    // The LCM is just each prime number to the power of the greatest one it was used
    // in (e.g. 27 = 3^3, 63 = 3^2*7, so LCM(27, 63) = 3^3*7).
    private static int getLCM(int[] vec) {

        int[][] multiplesArray = new int[vec.length][PRIMES.length];

        for (int i = 0; i < vec.length; i++) {
            int element = vec[i];

            for (int j = 0; j < PRIMES.length; j++) {
                int prime = PRIMES[j];
                int numOfThisPrime = 0;
                while (element % prime == 0) {
                    element /= prime;
                    numOfThisPrime++;
                }
                multiplesArray[i][j] = numOfThisPrime;
            }
        }

        int[] lcmPowers = getGreatestFromEachColumn(multiplesArray);
        int currentLCM = 1;
        for (int i = 0; i < PRIMES.length; i++) {
            if (lcmPowers[i] != 0) {
                currentLCM *= Math.pow(PRIMES[i], lcmPowers[i]);
            }
        }

        int LCM = currentLCM;

        return LCM;
    }
    
    // gets the greatest entry in each column in a 2d array, and then returns
    // a 1d array with only the greatest entries in the index of their respective
    // columns.
    private static int[] getGreatestFromEachColumn(int[][] arry) {
        int[] greatests = new int[arry[0].length];
        for (int j = 0; j < arry[0].length; j++) {
            int colGreatest = -2147483648; // the current guess for the greatest number in the colum
            for (int[] row : arry) {
                if (row[j] > colGreatest) {
                    colGreatest = row[j];
                }
            }
            greatests[j] = colGreatest;
        }

        return greatests;
    }
}
