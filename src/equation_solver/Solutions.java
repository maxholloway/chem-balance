package equation_solver;

//import equation_solver.ChemicalEquation;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Solutions{
    
    // Class with main method that takes input reaction(s) from the user
    // and gives the solutions back.
    
    // method that gets the solutions as a string
    public static String getManualSolutionString(String reactionString) {
        reactionString = reactionString.replace(" -> ", " ~> ");
        
        StringBuilder balancedReaction = new StringBuilder();
        if (isAReaction(reactionString)) {
            balancedReaction.append("Balanced Equation: " + (new ChemicalEquation(reactionString)).getPrintableSolutions() + "\n");
        } else {
            balancedReaction.append("That was not a reaction.\n");
        }
        return balancedReaction.toString();
    }
    
    // method that gets the solutions as a string
    public static String getTextfileSolutionString(String path) throws IOException {
        StringBuilder balancedReactions = new StringBuilder();
        
        // get all of the reactions in an array
        String[] allReactionsArray = getReactionArrrayFromTextFile(path);

        // for each of the reactions...
        for (int reacIndex = 0; reacIndex < allReactionsArray.length; reacIndex++) {
            String reactionString = allReactionsArray[reacIndex];
            reactionString = reactionString.replace(" -> ", " ~> ");
            if (isAReaction(reactionString)) {
                balancedReactions.append("Reaction number " + (reacIndex + 1) + ": ");
                balancedReactions.append((new ChemicalEquation(reactionString)).getPrintableSolutions() + "\n");
            } else {
                balancedReactions.append("Reaction number " + (reacIndex + 1) + " has an error.\n");
            }
        }

        return balancedReactions.toString();
    }
    
    
    // gets an array where each entry is a full reaction
    public static String[] getReactionArrrayFromTextFile(String filePath) throws IOException {
        String[] allReactionsArr = null;
        try (Scanner fileScan = new Scanner(new File(filePath))) {
            ArrayList<String> allReactions = new ArrayList<>();
            while(fileScan.hasNextLine()){
                String theNextLine = fileScan.nextLine();
                theNextLine = theNextLine.replaceAll("->", "~>"); // this helps when parsing the negative signs later in the program
                allReactions.add(theNextLine);
            }
            allReactionsArr = new String[allReactions.size()];
            for(int i = 0; i < allReactions.size(); i++){
                allReactionsArr[i] = allReactions.get(i);			
            }
        } catch (Exception e) {
            System.out.println("That was not a valid path.");
            // note that allReactionsArr is not set to anything, so its value is null
        }
        
        if(allReactionsArr != null){
            return allReactionsArr;
        } else {
            String[] substituteArr = {"Nothing ~> Nothing"};
            return substituteArr;
        }
    }

    
    //<editor-fold defaultstate="collapsed" desc="Methods to check if the entered reaction(s) is/are legit.">
    // checks to see if a line from the text file is actually a reaction
    public static boolean isAReaction(String potentialReaction) {
        if(!potentialReaction.contains(" ~> ")) return false; // checks to make sure something is yielding something else
        if(potentialReaction.length()<1) return false; // checks to make sure there's actually text
        if(hasLonelyLowercaseLetters(potentialReaction)) return false;
        
        //<editor-fold defaultstate="collapsed" desc="checks each compound for a capital letter, because all elements have capital letters">
        String[] comps = ChemicalEquation.getCompounds(ChemicalEquation.removeAddition(potentialReaction));
        for (String comp : comps) {
            boolean containsACapitalLetter = false;
            for (int i = 65; i <= 90; i++) {
                String thisCapitalLetter = Character.toString((char) i);
                if (comp.contains(thisCapitalLetter)) {
                    containsACapitalLetter = true;
                    break;
                }
            }
            if(!containsACapitalLetter) return false;
        }
//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="checks the reaction for any unsupported character (like a '#' or a '&')">
        for (int i = 0; i <= 47; i++) { // note, the reaction may contain a '0'
            if (i != 32 && i != 40 && i != 41 && i != 43 && i != 45) {
                String thisBadCharacter = Character.toString((char) i);
                if (potentialReaction.contains(thisBadCharacter)) {
                    return false;
                }
            }
        }

        for (int i = 58; i <= 64; i++) {
            String thisBadCharacter = Character.toString((char) i);
            if (i != 62) {
                if (potentialReaction.contains(thisBadCharacter)) {
                    return false;
                }
            }
        }

        for (int i = 91; i <= 96; i++) {
            String thisBadCharacter = Character.toString((char) i);
            if (potentialReaction.contains(thisBadCharacter)) {
                return false;
            }
        }

        for (int i = 123; i <= 127; i++) {
            String thisBadCharacter = Character.toString((char) i);
            if (i != 126) {
                if (potentialReaction.contains(thisBadCharacter)) {
                    return false;
                }
            }
        }

        int indexOfZero = potentialReaction.indexOf("0");
        while (indexOfZero >= 0) {
            if (!Character.isDigit(potentialReaction.charAt(indexOfZero - 1))) {
                return false;
            }
            indexOfZero = potentialReaction.indexOf("0", indexOfZero + 1);
        }

        //</editor-fold>

        // if it's gotten this far, it can't break the ChemicalEquation class,
        // so we can use those methods and not be worried.

        //<editor-fold defaultstate="collapsed" desc="checks element name info to be correct">
        ArrayList<String> elementNames = ChemicalEquation.getActualElementNames(potentialReaction); // doesn't include charges
        String firstHalfReaction = potentialReaction.substring(0, potentialReaction.indexOf(" ~> ") + 1);
        String secondHalfReaction = potentialReaction.substring(potentialReaction.indexOf(" ~> ") + 4, potentialReaction.length());
        for(String elementName : elementNames){
            if(elementName.length() > 3) return false; // checks for element name greater than 3 characters
            if(elementName.contains("j")) return false; // checks if the element name contains a 'j'
            if(!(firstHalfReaction.contains(elementName) && secondHalfReaction.contains(elementName))){
                return false;
            }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="checks if the number of plus signs is right">
        ChemicalEquation ct = new ChemicalEquation(potentialReaction);
        int numComps = ct.getRealCompounds().length;
        int numExpectedPlusSigns = numComps-2;

        String phrase = " + ";
        int actualNumPlusSigns = (potentialReaction.length() - potentialReaction.replace(phrase, "").length())/phrase.length();

        String phraseTwo = " +";
        int actualNumPlusSignsTwo = (potentialReaction.length() - potentialReaction.replace(phraseTwo, "").length())/phraseTwo.length();

        if(actualNumPlusSigns != actualNumPlusSignsTwo) return false;

        if(numExpectedPlusSigns != actualNumPlusSigns) return false;
        //</editor-fold>

        // if none of the checks showed that it wasn't a reaction, then it is
        // a reaction
        return true;

    }
    
    // this makes sure that any lowercase letters are actually part of a compound,
    // and not just on their own.
    private static boolean hasLonelyLowercaseLetters(String comp) {
        char[] compCharArray = comp.toCharArray();
        for (int i = 0; i < compCharArray.length; i++) {
            char compChar = compCharArray[i];
            if (compChar >= 97 && compChar <= 122) {
                if (i > 0) {
                    
                    char charBehind = compCharArray[i - 1];
                    int numTimesWhileLoopRan = 0;
                    while (charBehind >= 97 && charBehind <= 122) {
                        numTimesWhileLoopRan++;
                        if (i - 1 - numTimesWhileLoopRan >= 0) {
                            charBehind = compCharArray[i - 1 - numTimesWhileLoopRan];
                        } else {
                            return true;
                        }
                    }
                    
                    if (charBehind < 65 || charBehind > 90) {
                        return true;
                    }
                }
            }
        }
        
        return false;
        
    }
//</editor-fold>
      
}