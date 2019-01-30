package grocery;

public enum ConstraintType {

    Milk("milk"),
    Cereal("cereal"),
    BabyFood("baby food"),
    Diapers("diapers"),
    Bread("bread"),
    PeanutButter("peanut butter"),
    Jam("jelly/jam");

    private String typeName;

    ConstraintType(final String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return typeName;
    }
}
