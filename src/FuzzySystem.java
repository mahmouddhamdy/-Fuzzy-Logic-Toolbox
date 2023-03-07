import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class FuzzySystem {
    private final String name;
    private final String description;
    ArrayList<Variable> variables = new ArrayList<>();


    ArrayList<Rule> rules = new ArrayList<>();

    public FuzzySystem(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void menu() {
        boolean flag = true;
        while (flag) {
            System.out.println("""
                    Main Menu:
                    ==========
                    1- Add variables.
                    2- Add fuzzy sets to an existing variable.
                    3- Add rules.
                    4- Run the simulation on crisp values.
                    """);

            Scanner scanner = new Scanner(System.in);
            String input;
            input = scanner.next();
            boolean isInt = false;
            if (isInteger(input)) {
                isInt = true;
                if (Integer.parseInt(input) == 1) {
                    variableMenu();
                } else if (Integer.parseInt(input) == 2) {
                    setsMenu();
                } else if (Integer.parseInt(input) == 3) {
                    rulesMenu();
                } else if (Integer.parseInt(input) == 4) {
                    runSimulation();
                    flag = false;
                } else {
                    System.out.println("Please enter a valid input! \n" + "--------------------------");
                }
            }
            if (input.equalsIgnoreCase("Close")) {
                flag = false;
            } else if (!isInt) {
                System.out.println("Please enter a valid input! \n" + "--------------------------");
            }


        }

    }

    public boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    private void variableMenu() {
        Scanner scanner;
        try {
            System.out.println("""
                    Enter the variable’s name, type (IN/OUT) and range ([lower, upper]):
                    (Press x to finish)
                    --------------------------
                    """);

            scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            while (!Objects.equals(line, "x")) {
                String[] split = line.split(" ");
                if (!split[1].equalsIgnoreCase("In") && !split[1].equalsIgnoreCase("Out")) {
                    System.out.println("Invalid type! (Must be IN or OUT)");
                    variableMenu();
                }
                split[2] = split[2].substring(1, split[2].length() - 1);
                split[3] = split[3].substring(0, split[3].length() - 1);
                Variable variable = new Variable(split[0], split[1], Integer.parseInt(split[2]), Integer.parseInt(split[3]));
                variables.add(variable);
                line = scanner.nextLine();
            }
        } catch (ArrayIndexOutOfBoundsException exception) {
            System.out.println("Please make sure to leave a space before the upper range!");
            variableMenu();
        }
    }

    private void setsMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the variable’s name:\n" + "--------------------------");

        String varName = scanner.nextLine();
        boolean found = false;
        Variable associatedVar = new Variable("", "", 0, 0);
        for (int i = 0; i < variables.size(); i++) {
            if (Objects.equals(variables.get(i).getName(), varName)) {
                found = true;
                associatedVar = variables.get(i);
                break;
            }
        }
        if (!found) {
            System.out.println("Invalid Variable name! Please try again \n");
            setsMenu();
        }
        System.out.println("Enter the fuzzy set name, type (TRI/TRAP) and values: (Press x to finish)\n" + "-----------------------------------------------------");
        scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        while (!Objects.equals(line, "x")) {
            String[] split = line.split(" ");
            if (!split[1].equalsIgnoreCase("TRI") && !split[1].equalsIgnoreCase("TRAP")) {
                System.out.println("Invalid type! (Must be Tri or TRAP)");
                setsMenu();
            }
            ArrayList<Integer> values = new ArrayList<>();
            try {
                for (int i = 2; i < split.length; i++) {
                    values.add(Integer.parseInt(split[i]));
                }
            } catch (NumberFormatException exception) {
                System.out.println("Invalid character! please enter an integer value!");
                setsMenu();
            }

            fuzzySet set = new fuzzySet(split[0], split[1], values, associatedVar);
            for (int i = 0; i < associatedVar.sets.size(); i++) {
                if (Objects.equals(associatedVar.sets.get(i).getSetName(), set.getSetName())) {
                    System.out.println("Duplicate set! Please try again!");
                    setsMenu();
                }
            }
            associatedVar.sets.add(set);
            line = scanner.nextLine();
        }
    }

    private void rulesMenu() {
        Scanner scanner;
        System.out.println("""
                Enter the rules in this format: (Press x to finish)
                IN_variable set operator IN_variable set => OUT_variable set
                ------------------------------------------------------------""");

        scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        while (!Objects.equals(line, "x")) {
            String[] split = line.split(" ");
            String inVariable1 = split[0];
            String firstSetName = split[1];
            String operatorName = split[2];
            String inVariable2 = split[3];
            String secondSetName = split[4];
            String outVariable = split[6];
            String thirdSetName = split[7];

            if (!Objects.equals(split[5], "=>")) {
                System.out.println("Invalid format! Please try again!");
                rulesMenu();
            }

            boolean firstVariableValidity = checkIfVariableIsValid(inVariable1);
            boolean secondVariableValidity = checkIfVariableIsValid(inVariable2);
            boolean thirdVariableValidity = checkIfVariableIsValid(outVariable);
            if (!firstVariableValidity || !secondVariableValidity || !thirdVariableValidity) {
                System.out.println("Invalid variable name! Please try again!");
                rulesMenu();
            }
            Variable firstVariable = findVariable(inVariable1);
            Variable secondVariable = findVariable(inVariable2);
            Variable thirdVariable = findVariable(outVariable);


            assert firstVariable != null;
            boolean firstSetValidity = checkIfSetIsValid(firstSetName, firstVariable);
            assert secondVariable != null;
            boolean secondSetValidity = checkIfSetIsValid(secondSetName, secondVariable);
            assert thirdVariable != null;
            boolean thirdSetValidity = checkIfSetIsValid(thirdSetName, thirdVariable);
            if (!firstSetValidity || !secondSetValidity || !thirdSetValidity) {
                System.out.println("Invalid set! Please try again!");
                rulesMenu();
            }

            fuzzySet firstSet = findSet(firstSetName, firstVariable);
            fuzzySet secondSet = findSet(secondSetName, secondVariable);
            fuzzySet thirdSet = findSet(thirdSetName, thirdVariable);

            boolean operatorValidity = checkIfOperatorIsValid(operatorName);
            if (!operatorValidity) {
                System.out.println("Invalid operator! Please try again!");
                rulesMenu();
            }
            Operator operator = findOperator(operatorName);

            Rule rule = new Rule(firstVariable, firstSet, operator,
                    secondVariable, secondSet, thirdVariable, thirdSet);

            rules.add(rule);
            line = scanner.nextLine();
        }
    }

    private boolean checkIfVariableIsValid(String variableName) {
        boolean found = false;
        for (int i = 0; i < variables.size(); i++) {
            if (Objects.equals(variables.get(i).getName(), variableName)) {
                found = true;
                break;
            }
        }
        return found;
    }

    private Variable findVariable(String variableName) {
        for (int i = 0; i < variables.size(); i++) {
            if (Objects.equals(variables.get(i).getName(), variableName)) {
                return variables.get(i);
            }
        }
        return null;
    }

    private boolean checkIfSetIsValid(String setName, Variable var) {
        boolean found = false;
        for (int i = 0; i < var.sets.size(); i++) {
            if (Objects.equals(var.sets.get(i).getSetName(), setName)) {
                found = true;
                break;
            }
        }
        return found;
    }

    private fuzzySet findSet(String setName, Variable variable) {
        for (int i = 0; i < variable.sets.size(); i++) {
            if (Objects.equals(variable.sets.get(i).getSetName(), setName)) {
                return variable.sets.get(i);
            }
        }
        return null;
    }

    private boolean checkIfOperatorIsValid(String operator) {
        boolean found = false;
        for (Operator value : Operator.values()) {
            if (value.name().equalsIgnoreCase(operator)) {
                found = true;
                break;
            }
        }
        return found;
    }

    private Operator findOperator(String operatorName) {
        for (Operator value : Operator.values()) {
            if (value.name().equalsIgnoreCase(operatorName)) {
                return value;
            }
        }
        return null;
    }

    private void runSimulation() {
        Scanner scanner;

        System.out.println("Enter the crisp values: \n" +
                "-----------------------");

        for (int i = 0; i < variables.size(); i++) {
            if (variables.get(i).getType() == Variable.TYPE.IN) {
                System.out.print(variables.get(i).getName() + ":");
                scanner = new Scanner(System.in);
                int value = scanner.nextInt();
                variables.get(i).setCrispValue(value);
            }

        }

        simulate();
        System.out.println("""
                Running the simulation…
                Fuzzification => done
                Inference => done
                Defuzzification => done""");

        for (int i =0; i<variables.size();i++) {
            if (variables.get(i).getType() == Variable.TYPE.OUT) {
                System.out.println("Predicted value for " + variables.get(i).getName() +
                        " " + variables.get(i).getCrispValue());
            }
        }
    }

    private void simulate() {
        for (int i = 0; i < variables.size(); i++) {
            variables.get(i).fuzzification();
        }
        for (int i = 0; i < rules.size(); i++) {
            rules.get(i).Inference();
        }

        for (int i = 0; i < variables.size(); i++) {
            variables.get(i).defuzzification();
        }

    }

}
