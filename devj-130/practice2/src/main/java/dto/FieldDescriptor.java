package dto;

import java.util.Objects;

public class FieldDescriptor implements Comparable<FieldDescriptor> {
    private final String name;
    private final Class<?> type;
    private final int order;
    private final boolean isPrimaryKey;
    private final boolean isMandatory;

    public FieldDescriptor(String name, Class<?> type, int order, boolean isPrimaryKey, boolean isMandatory) {
        this.name = name;
        this.type = type;
        this.order = order;
        this.isPrimaryKey = isPrimaryKey;
        this.isMandatory = isMandatory;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public int getOrder() {
        return order;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldDescriptor field = (FieldDescriptor) o;
        return order == field.order &&
                name.equals(field.name);
    }


    @Override
    public int hashCode() {
        return Objects.hash(name, order);
    }

    @Override
    public int compareTo(FieldDescriptor o) {
        if (this.order == o.order) {
            return 0;
        }
        int res = (this.order > o.order) ? 1 : -1;
        return res;
    }
}
