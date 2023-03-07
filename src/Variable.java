import java.util.ArrayList;

public class Variable {
    public enum TYPE {
        IN,
        OUT,
    }

    private String name;
    private TYPE type;
    private int lowerRange;
    private int higherRange;

    private double crispValue;

    ArrayList<fuzzySet> sets = new ArrayList<>();

    public Variable(String name, String type, int lowerRange, int higherRange) {
        this.name = name;
        if (type.equalsIgnoreCase("IN")) {
            this.type = TYPE.IN;
        }
        else {
            this.type = TYPE.OUT;
        }
        this.lowerRange = lowerRange;
        this.higherRange = higherRange;
    }

    public String getName() {
        return name;
    }

    public double getCrispValue() {
        return crispValue;
    }

    public void setCrispValue(int crispValue) {
        this.crispValue = crispValue;
    }

    public TYPE getType() {
        return type;
    }

    public void fuzzification() {
        for (int i=0; i<this.sets.size();i++){
            if (this.type == TYPE.IN) {
                if (sets.get(i).values.get(0) > this.crispValue) {
                    break;
                }
                for (int j = 1; j < this.sets.get(i).points.size(); j++) {
                    if (this.getCrispValue() < this.sets.get(i).points.get(j).x) {
                        Point p1 = sets.get(i).points.get(j);
                        Point p2 = sets.get(i).points.get(j - 1);
                        double slope = (p2.y - p1.y) / (p2.x - p1.x);
                        double c = p1.y - slope * p1.x;
                        this.sets.get(i).degreeOfMembership = slope * this.getCrispValue() + c;
                        break;

                    }
                }
            }
        }
    }

    public void defuzzification() {
        double result;
        double sum = 0;
        double degreesSum = 0;
        if (this.type == TYPE.OUT) {
            for (int i =0; i<this.sets.size();i++) {
                this.sets.get(i).calculateCentroid();
                sum += this.sets.get(i).centroid * this.sets.get(i).degreeOfMembership;
                degreesSum += this.sets.get(i).degreeOfMembership;
            }
            result = sum / degreesSum;
            this.crispValue = result;
        }

    }

    @Override
    public String toString() {
        return "Variable {" +
                "name='" + name + '\'' +
                " , crispValue=" + crispValue +
                '}';
    }

    /* public void belongsToSet() {
        for (int i=0;i<this.sets.size();i++) {
            for (int j=0;j<this.sets.get(i).values.size();j++) {
                if (this.getCrispValue() == this.sets.get(i).values.get(j)) {
                    this.sets.get(i).degreeOfMembership = 1;
                    break;
                }
            }
        }

        for (int i=0;i<this.sets.size();i++) {
            if (crispValue> this.sets.get(i).values.get(0) &&
                    crispValue < this.sets.get(i).values.get(this.sets.get(i).values.size()-1)) {

                allocatedSets.add(this.sets.get(i));

            }
        }
    } */
}

