package week11;

public class Main {
    static void main(String[] ignoredArgs) {
        // Task 4: Shape Polymorphism
        Shape s1 = new Circle(5);
        Shape s2 = new Rectangle(4, 6);
        System.out.println("Circle area: " + s1.area());
        System.out.println("Rectangle area: " + s2.area());

        // Task 8: Interface Polymorphism
        Drawable d1 = new Circle(3);
        d1.draw();

        // Task 11: Multiple Interface
        Dog myDog = new Dog();
        myDog.draw();
        myDog.move();

        // Task 13: Integration
        Payment p = new Cash();
        p.pay();
        ((Loggable) p).log();
    }
}
