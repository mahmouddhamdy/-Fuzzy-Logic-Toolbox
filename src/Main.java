import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MainMenu();
    }

    private static void MainMenu() {
        boolean flag = true;
        Scanner scanner = new Scanner(System.in);
        int mainMenuInput;
        while (flag) {
            System.out.println("""
                    
                    Fuzzy Logic Toolbox
                    ===================
                    1- Create a new fuzzy system
                    2- Quit""");
            try {
                mainMenuInput = scanner.nextInt();
                if (mainMenuInput == 1) {
                    String systemName, systemDescription;
                    scanner = new Scanner(System.in);
                    System.out.println("Enter the system’s name and a brief description: \n" + "--------------------------");
                    systemName = scanner.nextLine();
                    systemDescription = scanner.nextLine();
                    FuzzySystem fuzzySystem = new FuzzySystem(systemName, systemDescription);
                    fuzzySystem.menu();
                } else if (mainMenuInput == 2) {
                    flag = false;

                } else {
                    System.out.println("Please enter a valid input! \n");
                }
            } catch (InputMismatchException exception) {
                System.out.println("Please enter an Integer! \n");
                MainMenu();
            }


        }
    }
}