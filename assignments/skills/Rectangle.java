package week11;

class Rectangle extends Shape implements Drawable {
    double width, height;

    Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    double area() {
        return width * height;
    }

    @Override
    public void draw() {
        System.out.println("Drawing a Rectangle.");
    }
}
