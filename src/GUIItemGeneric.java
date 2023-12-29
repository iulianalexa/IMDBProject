import javax.swing.*;

abstract public class GUIItemGeneric<T> extends JFrame {
    private T item;

    public GUIItemGeneric(T item) {
        this.item = item;
    }

    void setItem(T item) {
        this.item = item;
    }

    T getItem() {
        return this.item;
    }
}
