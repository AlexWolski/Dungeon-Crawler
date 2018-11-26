package Tanks3D.Object;

public abstract class GameObject {
    protected boolean visible;
    public abstract double getWidth();
    public abstract double getHeight();

    protected GameObject() {
        visible = true;
    }

    public boolean getVisible() {
        return visible;
    }
}
