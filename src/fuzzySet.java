import java.util.ArrayList;

public class fuzzySet {

    private enum TYPE {
        TRI,
        TRAP,
    }

    private String setName;
    private TYPE type;
    public ArrayList<Integer> values = new ArrayList<>();
    private Variable associatedVariable;

    public ArrayList<Point> points = new ArrayList<>();

    public double degreeOfMembership = 0;

    public double centroid = 0;

    public fuzzySet(String setName, String type, ArrayList<Integer> values, Variable associatedVariable) {
        this.setName = setName;
        if (type.equalsIgnoreCase("TRI")) {
            this.type = TYPE.TRI;
        } else {
            this.type = TYPE.TRAP;
        }
        this.values = values;
        this.associatedVariable = associatedVariable;
        this.setPoints();
    }

    public String getSetName() {
        return setName;
    }

    public TYPE getType() {
        return type;
    }

    public ArrayList<Integer> getValues() {
        return values;
    }

    public Variable getAssociatedVariable() {
        return associatedVariable;
    }

    private void setPoints() {
        if (this.type == TYPE.TRAP) {
            Point p1 = new Point(values.get(0), 0);
            this.points.add(p1);
            Point p2 = new Point(values.get(1), 1);
            this.points.add(p2);
            Point p3 = new Point(values.get(2), 1);
            this.points.add(p3);
            Point p4 = new Point(values.get(3), 0);
            this.points.add(p4);

        }
        if (this.type == TYPE.TRI) {
            Point p1 = new Point(values.get(0), 0);
            this.points.add(p1);
            Point p2 = new Point(values.get(1), 1);
            this.points.add(p2);
            Point p3 = new Point(values.get(2), 0);
            this.points.add(p3);
        }
    }

    public void calculateCentroid() {
        if (this.type == TYPE.TRI) {
            centroid = (values.get(0) + values.get(1) + values.get(2)) / 3.0;
        } else if (this.type == TYPE.TRAP) {
            double a = 0;
            double c = 0;
            for (int i =1; i <points.size();i++) {
                a+= (points.get(i-1).x *points.get(i).y) - (points.get(i).x * points.get(i-1).y);
            }
            for (int i =1; i <points.size();i++) {
                c+= (points.get(i-1).x + points.get(i).x) *
                        (points.get(i-1).x*points.get(i).y - points.get(i).x * points.get(i-1).y);
            }

            c *= 1 / (6*a);
            this.centroid = c;

        }
    }


}
