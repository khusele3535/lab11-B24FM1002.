package week11;

abstract class Payment {
    abstract void pay();
}

interface Loggable {
    void log();
}

class Cash extends Payment implements Loggable {
    @Override
    void pay() {
        System.out.println("Paying with cash.");
    }

    @Override
    public void log() {
        System.out.println("Logging cash payment.");
    }
}

