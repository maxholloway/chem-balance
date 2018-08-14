/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equation_solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ChemicalEquation {
    
    // just using some variables so that I don't have to override the '$' sign every time I use these strings
    private static final String POSEL = "$pos";
    private static final String NEGEL = "$neg";

    // making variables accessible to the static methods
    private static String parsableEquation;

    private static ArrayList<String> actualElementNames;
    private static ArrayList<String> parsableElementNames = new ArrayList<>();
    private static String[] realCompounds; // actual, unedited compounds
    public static int numReacts;
    public static int numProds;
    private static int numComps;

    // constructor that gives some basic information about the reaction; sets values
    public ChemicalEquation(String inp) {
        String inputString = inp;
        String equationWOAddition = removeAddition(inputString);
        String equationWHardCodedSignage = replacePositiveAndNegativeSigns(equationWOAddition);

        actualElementNames = getActualElementNames(equationWHardCodedSignage);
        
        String equationWChangedElementNames = editElementNames(equationWHardCodedSignage);

        parsableEquation = equationWChangedElementNames;
        realCompounds = getCompounds(equationWOAddition);
    }

    // this looks at the entire reaction and gets every element from it.
    protected static ArrayList<String> getActualElementNames(String reducedReaction) {
        Set<String> elementsSet = new HashSet<>(0); // keeps track of all elements without repeats
        reducedReaction = reducedReaction + " "; //to avoid out-of-bounds error
        char reactionChars[] = reducedReaction.toCharArray();

        // loop looks through the reaction to find where an element begins 
        // (where it's a capital letter) to where it ends (where it's not a
        // lowercase letter)
        int i = 0;
        while (i < reactionChars.length) {
            if (Character.isUpperCase(reactionChars[i])) {
                StringBuilder sb = new StringBuilder();
                sb.append(reactionChars[i]);
                int j = 1;
                while (Character.isLowerCase(reactionChars[i + j])) {
                    sb.append(reactionChars[i + j]);
                    j++;
                }
                elementsSet.add(sb.toString());
            }
            if (reactionChars[i] == '$') { // as in $pos and $neg, the charges
                char[] chargeArr = Arrays.copyOfRange(reactionChars, i, i + 4);
                String chargeStr = new String(chargeArr);
                if (chargeStr.equals(POSEL)) {
                    elementsSet.add(POSEL);
                } else if (chargeStr.equals(NEGEL)) {
                    elementsSet.add(NEGEL);
                }
            }
            i++;
        }

        return new ArrayList<>(elementsSet);
    }

    // method to replace a reaction string with a modified version that
    // has no plus-signs, so only the compounds and "~>" remain
    public static String removeAddition(String unModifiedStr) {
        return unModifiedStr.replaceAll(" \\+", "");
    }

    // makes it so that we can parse through and look for "$pos" and "$neg" instead
    // of looking for "+" and "-"; mostly for clarity, and also because the yield
    // sign has a "-" and the plus signs in the equation had a "+"
    private static String replacePositiveAndNegativeSigns(String strWOAddition) {
        strWOAddition = strWOAddition.replaceAll("\\+", "\\$pos");
        strWOAddition = strWOAddition.replaceAll("\\-", "\\$neg");
        return strWOAddition;
    }

    // basically so that the program can distinguish between "P" and "Pb", or some
    // other form of this; it takes in the a reaction without edited element names
    // and returns a reaction with edited element names
    private static String editElementNames(String reactionStr) {
        parsableElementNames.clear();
        // basically checks to see if the unedited elements have been set
        if (actualElementNames.size() >= 1) {
            for (int eNameIndx = 0; eNameIndx < actualElementNames.size(); eNameIndx++) {
                String elementName = actualElementNames.get(eNameIndx);
                
                int indx = reactionStr.indexOf(elementName);
                while (indx >= 0) {
                    if (indx + elementName.length() < reactionStr.length()) {
                        if (!Character.isLowerCase(reactionStr.charAt(indx + elementName.length()))) {
                            reactionStr = reactionStr.substring(0, indx + elementName.length()) + "j" + reactionStr.substring(indx + elementName.length(), reactionStr.length());
                        }

                        if(indx + 1 < reactionStr.length()) indx = reactionStr.indexOf(elementName, indx + 1);

                    } else if (indx + elementName.length() == reactionStr.length()){
                       reactionStr = reactionStr + "j";
                    } else {
                        indx = -1;    
                    }
                }
                if(!parsableElementNames.contains(elementName + "j")){
                    parsableElementNames.add(elementName + "j");
                }
            }
        }
        
        return reactionStr;
    }

    // method to get the compounds as a list and to get the number of reactants
    // and products; input MUST contain only compounds separated by spaces and 
    // the yields sign, and all of the plus and minus charges must be replaced
    // by their strings
    protected static String[] getCompounds(String modifiedStr) {
        Scanner sideScan = new Scanner(modifiedStr);
        sideScan.useDelimiter(" ~> "); // remember, this is what the yields symbol was replaced by

        ArrayList<String> compsArrList = new ArrayList<>();

        // for each side of the reaction... (we know there are only 2)		
        for (int i = 0; i < 2; i++) {
            String side = sideScan.next();

            Scanner compScan = new Scanner(side);

            int numCoeffsOnThisSide = 0;
            while (compScan.hasNext()) {
                compsArrList.add(compScan.next()); // assumes compounds are only separated by spaces
                numCoeffsOnThisSide++;
            }

            // if we're on the reactants side of the equation...
            if (i == 0) {
                numReacts = numCoeffsOnThisSide;
            } else {
                numProds = numCoeffsOnThisSide;
            }
        }
        
        numComps = compsArrList.size();

        return compsArrList.toArray(new String[compsArrList.size()]);
    }

    // subtracts the rows with positive signs and negative signs so that we can balance
    // the charges as if they were regular elements; takes in the coeffArray and the 
    // index of the positive row and the index of the negative row in that coeffArray
    private static double[][] refactorForCharge(double[][] coeffArray, int indxOfPos, int indxOfNeg) {
        double[][] newCoeffArray = new double[coeffArray.length - 1][coeffArray[0].length];

        double[] posRow = coeffArray[indxOfPos];
        double[] negRow = coeffArray[indxOfNeg];

        // loop to effectively remove the positive and negative rows from a matrix
        // and then shift the other rows up to take its place
        int i = 0;
        int numSkipped = 0;
        while (i < coeffArray.length) {
            if (i == indxOfPos | i == indxOfNeg) {
                i++;
                numSkipped++;
            }
            // must check again, because we may have incremented i to a higher
            // value than when this loop began
            if (i < coeffArray.length) {
                newCoeffArray[i - numSkipped] = coeffArray[i];
            }

            i++;
        }

        newCoeffArray[newCoeffArray.length - 1] = MatrixOperations.subtractRows(posRow, negRow);

        return newCoeffArray;
    }

    // connects the parsing with the math; a must-know for understanding the program
    public int[] getSolutionsArray() {
        // compounds edited to make parsing easier; I use these
        // for practical purposes, and they don't necessarily look like actual compounds
        String[] compsEdited = getCompounds(parsableEquation);

        // this loops to get the array that stores each compound's contribution
        // in a row of a matrix called coeffArrayTranspose; this is called this
        // because it is the transpose of the elements-as-rows and comps-as-cols
        // array that I use to find the solutions; namely, it is elements-as-columns
        // and comps-as-rows matrix;
        double[][] coeffArrayTranspose = new double[compsEdited.length][actualElementNames.size()];
        for (int i = 0; i < compsEdited.length; i++) {
            String compString = compsEdited[i];
            Compound c = new Compound(compString, parsableElementNames);
            if (i < numReacts) {
                coeffArrayTranspose[i] = c.getAllContributions();
            } else {
                coeffArrayTranspose[i] = MatrixOperations.multRowVecByScalar(c.getAllContributions(), -1);
            }
        }

        double[][] coeffArray = MatrixOperations.transpose(coeffArrayTranspose);
        int[] solutions;
        if (actualElementNames.contains(NEGEL) && actualElementNames.contains(POSEL)) {
            // run code that refactors matrix "coeffArray so that
            // it replaces the equations for pos and neg to instead
            // have one equation for pos + neg on each side.
            int indexOfPos = actualElementNames.indexOf(POSEL);
            int indexOfNeg = actualElementNames.indexOf(NEGEL);
            double[][] refactoredArray = refactorForCharge(coeffArray, indexOfPos, indexOfNeg);
            solutions = MatrixOperations.chemSolve(refactoredArray);
        } else {
            solutions = MatrixOperations.chemSolve(coeffArray);
        }

        return solutions;
    }

    // prints the solutions to a chemical equation; ONLY USED IN THE textUI CLASS
    public void printSolutions() {
        int[] solutions = getSolutionsArray();
        for (int i = 0; i < numComps; i++) {
            if (solutions[i] != 1) {
                if (i < numReacts - 1) {
                    System.out.print(solutions[i] + realCompounds[i] + " + ");
                } else if (i == numReacts - 1) {
                    System.out.print(solutions[i] + realCompounds[i] + " -> ");
                } else if (i < numComps - 1) {
                    System.out.print(solutions[i] + realCompounds[i] + " + ");
                } else {
                    System.out.println(solutions[i] + realCompounds[i]);
                }
            } else {
                if (i < numReacts - 1) {
                    System.out.print(realCompounds[i] + " + ");
                } else if (i == numReacts - 1) {
                    System.out.print(realCompounds[i] + " -> ");
                } else if (i < numComps - 1) {
                    System.out.print(realCompounds[i] + " + ");
                } else {
                    System.out.println(realCompounds[i]);
                }
            }
        }

        if (MatrixOperations.hasRegularSolution == false) {
            if (MatrixOperations.hasManySolutions) {
                System.out.println("Infinitely many solutions.");
            } else if (MatrixOperations.hasNoSolution) {
                System.out.println("Has no solutions.");
            }
        }
    }

    // gets solutions in a string that can then be printed to the screen in
    // a different method
    public String getPrintableSolutions() {
        int[] solutions = getSolutionsArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numComps; i++) {
            if (solutions[i] != 1) {
                if (i < numReacts - 1) {
                    sb.append(solutions[i]).append(realCompounds[i]).append(" + ");
                } else if (i == numReacts - 1) {
                    sb.append(solutions[i]).append(realCompounds[i]).append(" -> ");
                } else if (i < numComps - 1) {
                    sb.append(solutions[i]).append(realCompounds[i]).append(" + ");
                } else {
                    sb.append(solutions[i]).append(realCompounds[i]).append("\n");
                }
            } else {
                if (i < numReacts - 1) {
                    sb.append(realCompounds[i]).append(" + ");
                } else if (i == numReacts - 1) {
                    sb.append(realCompounds[i]).append(" -> ");
                } else if (i < numComps - 1) {
                    sb.append(realCompounds[i]).append(" + ");
                } else {
                    sb.append(realCompounds[i]).append("\n");
                }
            }
        }

        if (MatrixOperations.hasRegularSolution == false) {
            if (MatrixOperations.hasManySolutions) {
                sb.append("^^Infinitely many solutions.^^");
            } else if (MatrixOperations.hasNoSolution) {
                sb.append("^^Has no solutions.^^");
            }
        }

        return sb.toString();
    }

    protected static String[] getRealCompounds() {
        return realCompounds;
    }
    
    
}
