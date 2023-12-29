import javax.swing.*;

abstract public class GUIItemGeneric<T> extends JFrame {
    private T item = null;

    public GUIItemGeneric() {}

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }
}
