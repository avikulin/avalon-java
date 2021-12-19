package ru.avalon.javaapp.dev120.phonenum.demo.models;

import java.util.Objects;

public class PhoneNumber {
    private final String areaCode;
    private final String localNum;

    public PhoneNumber(String areaCode, String localNum) {
        checkValue(areaCode, "area");
        checkValue(localNum, "local number");
        this.areaCode = areaCode;
        this.localNum = localNum;
    }

    private void checkValue(String value, String partName) {
        if (value == null) {
            throw new NullPointerException(String.format("%s is null", partName));
        }

        if (value.isEmpty()) {
            throw new IllegalArgumentException(String.format("%s is empty string", partName));
        }

        for (int i = 0; i < value.length(); i++) {
            if (!Character.isDigit(value.charAt(i))) {
                throw new IllegalArgumentException(String.format("%s contains unexpected characters ", partName));
            }
        }
    }

    public String getAreaCode() {
        return areaCode;
    }

    public String getLocalNum() {
        return localNum;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("(").append(areaCode).append(") ");
        int p = (localNum.length() % 2) + 2;
        if (p < localNum.length()) {
            builder.append(localNum, 0, p);
            while (localNum.length() - 2 >= p) {
                builder.append("-").append(localNum, p, p + 2);
                p = p + 2;
            }
        } else {
            builder.append(localNum);
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof PhoneNumber)) return false;
        PhoneNumber that = (PhoneNumber) o;
        return areaCode.equals(that.areaCode) &&
                localNum.equals(that.localNum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(areaCode, localNum);
    }
}
