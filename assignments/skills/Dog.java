package week11;

class Dog implements Drawable, Movable {
    @Override
    public void draw() {
        System.out.println("Drawing a Dog.");
    }

    @Override
    public void move() {
        System.out.println("Dog is moving.");
    }
}
