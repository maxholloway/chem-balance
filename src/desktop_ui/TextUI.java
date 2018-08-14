package desktop_ui;

import equation_solver.Solutions;
import equation_solver.ChemicalEquation;
import java.io.IOException;
import java.util.Scanner;

public class TextUI {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter \"textfile\" to get the reactions from a textfile or enter \"manual\" to enter a reaction manually: ");
        String option = sc.nextLine();

        while (!option.equals("get me out of here")) {
            switch (option) {
                case "textfile":

                    Scanner kbReader = new Scanner(System.in);
                    System.out.print("What is the path of the reaction on your computer? ");
                    String path = kbReader.nextLine();
                    System.out.print("\n");
//                    String path = "/Users/maxwellholloway/Dropbox/Chem. Independent Study/ReactionTests/RedOx.txt";

                    // get all of the reactions in an array
                    String[] allReactionsArray = Solutions.getReactionArrrayFromTextFile(path);
                    // for each of the reactions...
                    for (int reacIndex = 0; reacIndex < allReactionsArray.length; reacIndex++) {
                        String reactionString = allReactionsArray[reacIndex];
                        reactionString = reactionString.replace(" -> ", " ~> ");
                        if (Solutions.isAReaction(reactionString)) {
                            System.out.print("Reaction number " + (reacIndex + 1) + ": ");
                            (new ChemicalEquation(reactionString)).printSolutions();
                        } else {
                            System.out.println("Reaction number " + (reacIndex + 1) + " has an error.");
                        }

                    }
                    System.out.print("Continue using textfile? Enter 'Y' for yes and 'N' for no. ");
                    String toContinueOrNot = sc.nextLine();
                    if (toContinueOrNot.equals("N")) {
                        System.out.print("Enter \"textfile\" to get the reactions from a textfile or enter \"manual\" to enter a reaction manually: ");
                        option = sc.nextLine();
                    }

                    break;
                case "manual":
                    // code to run if the person wants to enter the reaction manually
                    // assuming the reactionInp is given by the user
                    Scanner reactInpSc = new Scanner(System.in);
                    System.out.print("Enter a reaction: ");
                    String reactionInp = reactInpSc.nextLine();
                    while (!reactionInp.equals("get me out of here")) {
                        reactionInp = reactionInp.replace(" -> ", " ~> ");
                        if (Solutions.isAReaction(reactionInp)) {
                            System.out.print("Balanced Equation: ");
                            (new ChemicalEquation(reactionInp)).printSolutions();
                        } else {
                            System.out.println("That was not a reaction.");
                        }
                        System.out.print("Enter a reaction: ");
                        reactionInp = reactInpSc.nextLine();
                    }

                    System.out.print("Continue entering equations manually? Enter 'Y' for yes and 'N' for no. ");
                    String toContinueWithManualOrNot = sc.nextLine();
                    if (toContinueWithManualOrNot.equals("N")) {
                        System.out.print("Enter \"textfile\" to get the reactions from a textfile or enter \"manual\" to enter a reaction manually: ");
                        option = sc.nextLine();

                    }

                    break;
                case "get me out of here":
                    System.exit(69);
                    break;
                default:
                    System.out.println("What you entered was not a proper answer.");
                    System.out.print("Enter \"manual\" or \"textfile\": ");
                    option = sc.nextLine();
                    break;
            }
        }
    }
}
