public class Rule {
    private Variable inVariable1;
    private fuzzySet firstSet;
    private Operator operator;
    private Variable inVariable2;
    private fuzzySet secondSet;
    private Variable outVariable;
    private fuzzySet outputSet;

    double value;


    public Rule(Variable inVariable1, fuzzySet firstSet, Operator operator, Variable inVariable2
            , fuzzySet secondSet, Variable outVariable, fuzzySet thirdSet) {
        this.inVariable1 = inVariable1;
        this.firstSet = firstSet;
        this.operator = operator;
        this.inVariable2 = inVariable2;
        this.secondSet = secondSet;
        this.outVariable = outVariable;
        this.outputSet = thirdSet;
    }

    public void Inference() {
        if (this.operator == Operator.and) {
            value = Math.min(firstSet.degreeOfMembership, secondSet.degreeOfMembership);
        } else if (this.operator == Operator.or) {
            value = Math.max(firstSet.degreeOfMembership, secondSet.degreeOfMembership);
        } else if (this.operator == Operator.and_not) {
            value = Math.min(firstSet.degreeOfMembership, 1 - secondSet.degreeOfMembership);
        } else if (this.operator == Operator.or_not) {
            value = Math.max(firstSet.degreeOfMembership, 1 - secondSet.degreeOfMembership);
        }
        outputSet.degreeOfMembership = Math.max(outputSet.degreeOfMembership,value);


    }
}
