public class Character {

    public String name;
    public int hp;
    public int mp;

    public Character(String name, int hp, int mp) {
        this.name = name;
        this.hp = hp;
        this.mp = mp;
    }

    public void takeDamage(int amount) {
        this.hp = Math.max(0, this.hp - amount);
    }

    public void heal(int amount) {
        this.hp = this.hp + amount;
    }
}
