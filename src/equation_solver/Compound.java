package equation_solver;

import java.util.ArrayList;
import java.util.Scanner;

public class Compound {

//----------------------- Some Useful Variables -----------------------\\

    private static ArrayList<String> parenthPhrases = new ArrayList<String>();
    private static int numEls;

    private static final char OPENINGPARENTHCHAR = 40;
    private static final char CLOSINGPARENTHCHAR = 41;
    private static final String OPENINGPARENTHSTR = Character.toString(OPENINGPARENTHCHAR);
    private static final String CLOSINGPARENTHSTR = Character.toString(CLOSINGPARENTHCHAR); 
    
    private static ArrayList<String> ELEMENTNAMES = new ArrayList<String>();
    private static String COMP;
    
    private static String assComp; // short for "ones-asserted compound"
    
    // this method should return zeros where it does not have a certain element,
    // so it is necessary to know all element when getting element contributions
    // from the compound.
    public Compound(String inpComp, ArrayList<String> inpElementNames){
        ELEMENTNAMES = inpElementNames;
        COMP = inpComp;
    }

//-------------------------- Asserting Ones ---------------------------\\
    
    // when entering equations, people often omit the 1 following a compound,
    // because by convention, if there is not a number telling how many atoms
    // are present in the reaction, it is assumed to be 1. However, my program
    // must treat this like a 1, and thus the first part "asserts" ones where the
    // user has not entered any numbers.
    private static void assertOnes(){
        String editedComp = COMP;
        
        // basically make an arraylist of things that must be followed by a number
        // to denote quantity. Usually these are elements, however closing parentheses
        // also need to have known quantity, so they are included in this arraylist.
        ArrayList<String> elementNamesWithParenth = MatrixOperations.clone(ELEMENTNAMES);
        elementNamesWithParenth.add(CLOSINGPARENTHSTR);
        
        for(String elementName : elementNamesWithParenth){
            int index = editedComp.indexOf(elementName);
            int elementNameLength = elementName.length();

            while(index >= 0){
                int indexOfFirstDigit = index + elementNameLength;
                if(indexOfFirstDigit < editedComp.length()){
                    char charInQuestion = editedComp.charAt(indexOfFirstDigit);
                    // if it's past the end of the element and the following character
                    // is not a digit, then a 1 must be asserted
                    if (!Character.isDigit(charInQuestion) & isPastEndOfElement(charInQuestion)) {
                        editedComp = editedComp.substring(0, indexOfFirstDigit) + "1" + editedComp.substring(indexOfFirstDigit, editedComp.length());
                    }
                    index = editedComp.indexOf(elementName, indexOfFirstDigit); // the next index must be after the indexOfFirstDigit
                } else if (indexOfFirstDigit == editedComp.length()){
                    // if the digit would be after the last index of the compound,
                    // then simply add it on to the end; we can do this because
                    // the fact that there is no index after the element implies
                    // that there is not a digit ther (duh), and we must assert it
                    editedComp = editedComp + "1";
                    break; // we're at the end of the compound
                }
            }
        }

        assComp = editedComp; // updates the state variable "assComp"
    }

    // method to deetermine if a character is past the end of an element
    private static boolean isPastEndOfElement(char theChar){
        String theCharStr = Character.toString(theChar);
        boolean troofValue = (Character.isDigit(theChar)) | (theChar == 40) | (theChar == 41) | (Character.isUpperCase(theChar)) | (theCharStr.equals(" ")) | (theCharStr.equals("$"));
        return troofValue;
    }


//----- Gets Contribution from the Statements Inside Parentheses ------\\
    // this method finds how much of each element is in all of the parentheses
    // of a compound combined
    private static int[] getParenthedContribution(){
        
        if(assComp.contains(OPENINGPARENTHSTR)){
            setParenthPhrases(assComp);

            // loop to get the total contribution from parenth phrases in the
            // entire compound
            numEls = ELEMENTNAMES.size();
            int[] innerSum = new int[numEls];
            for(int i = 0; i < parenthPhrases.size(); i ++){
                String parenthPhrase = parenthPhrases.get(i);
                int[] inners = getPhraseSubs(parenthPhrase, ELEMENTNAMES);
                int postParenthSubscript = getPostParenthSub(parenthPhrase);
                // multiply each element's sub (inner) by the parenth's sub
                for (int j = 0; j < numEls; j++) {
                    // however, we only care about the total contribution
                    // of parentheses, so really we increment the toal amount of
                    // each element in all parenthetical phrases of a compound
                    innerSum[j] += inners[j] * postParenthSubscript;
                }
            }

            return innerSum;
        } else {
            // if if does not have any parenthetical phrases,
            // then the contribution from the parentheses is 0.
            return new int[ELEMENTNAMES.size()];
        }
    }

    // gets all of the "(COMPOUND)DIGITS" phrases, with all digits after the parenth
    private static void setParenthPhrases(String comp) {
        // some stuff to make parsing easier
        comp = comp.replaceAll("\\(", " " + OPENINGPARENTHSTR);
        comp = comp + "     ";

        parenthPhrases = new ArrayList<>();


        // loop to get inner-—parentheses phrases
        int indexOfOpeningParenth = comp.indexOf(OPENINGPARENTHSTR); // where to start the first parenth phrase
        while (indexOfOpeningParenth >= 0) {

            // gets the next closing parenthesis
            int indexOfClosingParenth = comp.indexOf(CLOSINGPARENTHSTR, indexOfOpeningParenth + 1);

            // loopy stuff ot get how many digits are after the parentheses
            int numDigitsAfterParenth = 0;
            char nextChar = comp.charAt(indexOfClosingParenth + 1);
            while (Character.isDigit(nextChar)) {
                numDigitsAfterParenth++;
                nextChar = comp.charAt(indexOfClosingParenth + 1 + numDigitsAfterParenth);
            }

            int indexOfLastPostParenthDigit = indexOfClosingParenth + numDigitsAfterParenth;

            // getting the parenth phrase with 
            String parenthPhrase = comp.substring(indexOfOpeningParenth, indexOfLastPostParenthDigit + 1);
            parenthPhrase = parenthPhrase.replaceAll(" ", ""); // it's in a compound, so clean up any excess spaces
            parenthPhrases.add(parenthPhrase); //it's really a property of the compound, so it's a field

            indexOfOpeningParenth = comp.indexOf(OPENINGPARENTHSTR, indexOfLastPostParenthDigit + 1);

        }
    }

    // parses to find how much of each element in a phrase contributes
    private static int[] getPhraseSubs(String chemicalPhrase, ArrayList<String> elementNames) {

        int[] uneditedContributions = new int[elementNames.size()];

        for (int i = 0; i < elementNames.size(); i++) {
            String elementName = elementNames.get(i);
            int index = chemicalPhrase.indexOf(elementName);
            // while there are more instances of this element name
            while (index >= 0) {
                String remainingPhrase = chemicalPhrase.substring(index + elementName.length(), chemicalPhrase.length());
                Scanner subScan = new Scanner(remainingPhrase);
                subScan.useDelimiter("\\D");
                uneditedContributions[i] += Integer.parseInt(subScan.next());
                index = chemicalPhrase.indexOf(elementName, index + 1);
            }
        }

        return uneditedContributions;
    }

    // gets the subscripts that follow parenthetical phrases
    private static int getPostParenthSub(String parenthPhrase) {
        parenthPhrase = parenthPhrase + " "; // for parsing
        int index = parenthPhrase.indexOf(CLOSINGPARENTHSTR);

        if (index >= 0) {
            StringBuilder postParenthSubSb = new StringBuilder();
            int nextCharIndex = index + 1;
            char nextChar = parenthPhrase.charAt(nextCharIndex);
            while (Character.isDigit(nextChar)) {
                postParenthSubSb.append(nextChar);
                nextCharIndex++;
                nextChar = parenthPhrase.charAt(nextCharIndex);
            }
            return Integer.parseInt(postParenthSubSb.toString());
        } else {
            System.out.println("Something bad happened in the getPostParenthSub method");
            return 0; // return 0 as the post-parentheses subscript, because it was not found
        }
    }



//----- Gets Contribution from the Statements Outside Parentheses -----\\

    // this just uses the compound's total unparenthed phrase (everything but
    // the parenthed phrases) and finds their contributions to the total of the
    // compound.
    private static int[] getUnparenthedContribution(){
        String nonParenthPhrase = getUnparenthedPhrase(assComp);
        return getPhraseSubs(nonParenthPhrase, ELEMENTNAMES);
    }
    
    // method that removes all of the parentheses phrases and leaves only the
    // non-parenthesesed phrases, and then returns those A(B)5D(C)2 - (B)5(C)2 -> AD
    private static String getUnparenthedPhrase(String comp){
        for (String parenthPhrase : parenthPhrases) {
            comp = comp.replace(parenthPhrase, "");
        }
        return comp;
    }
    
    
//--------------- Last methods to tie it all together ---------------\\

    // method that returns the number of atoms of each element in a given compound
    public double[] getAllContributions(){
        assertOnes();

        int[] parenthContribution = getParenthedContribution();

        int[] nonparenthContributions = getUnparenthedContribution();

        int[][] combinedMat = {parenthContribution, nonparenthContributions};
        double[] allElContributions = MatrixOperations.sumColumnsOf2Dint(combinedMat);

        return allElContributions;
    }
}