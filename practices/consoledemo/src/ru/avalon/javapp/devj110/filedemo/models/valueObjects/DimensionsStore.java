package ru.avalon.javapp.devj110.filedemo.models.valueObjects;
/*
    Read-only value object.
 */
public class DimensionsStore {
    private int width;
    private int height;

    public DimensionsStore(int width, int height){
        setWidth(width);
        setHeight(height);
    }


    private void setWidth(int width) {
        if (width < 1){
            throw new IllegalArgumentException("Width must be at least 1 pix.");
        }
        this.width = width;
    }

    private void setHeight(int height) {
        if (height < 1){
            throw new IllegalArgumentException("Height must be at least 1 pix.");
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
    public String toString() {
        return String.format("%dx%d", width, height);
    }
}
