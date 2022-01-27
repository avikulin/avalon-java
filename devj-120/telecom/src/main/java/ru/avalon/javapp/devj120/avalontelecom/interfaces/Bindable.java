package ru.avalon.javapp.devj120.avalontelecom.interfaces;

import java.util.function.Consumer;

public interface Bindable<T> {
    void bind(Consumer<T> func);
}
