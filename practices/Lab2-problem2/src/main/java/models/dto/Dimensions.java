package models.dto;

public class Dimensions {
    private int width;
    private int height;

    public Dimensions(int width, int height) throws IllegalStateException {
        setWidth(width);
        setHeight(height);
    }

    private void setWidth(int width) throws IllegalStateException {
        if (width < 0) {
            throw new IllegalArgumentException("Width value mast be over zero");
        }
        this.width = width;
    }

    private void setHeight(int height) throws IllegalStateException {
        if (height < 0) {
            throw new IllegalArgumentException("Height value mast be over zero");
        }
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dimensions that = (Dimensions) o;
        return width == that.width &&
                height == that.height;
    }

    @Override
    public String toString() {
        return String.format("%dx%d", getWidth(), getHeight());
    }
}
