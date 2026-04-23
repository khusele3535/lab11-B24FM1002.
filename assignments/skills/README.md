# Lab 11 — Skill System (Abstraction)

**Нийт оноо:** 100 | **Сэдэв:** Abstraction (`abstract class`, `interface`, `default` methods)

## 🎭 Түүх

Баатрууд тулаанд орохдоо зөвхөн илд, сум биш, ур чадвар (skill) ашигладаг. Fireball нь дайснаа түлнэ, Heal нь биеэ эдгээнэ, Stealth нь сүүдэрт нуугдана. Бүгд **skill** боловч яг яаж `cast` хийгдэх нь өөр. Энэ бол **abstraction**-ийн онцлог: нэг нийтлэг загвар (abstract), олон хэрэгжүүлэлт (concrete).

---

## 📋 Өгөгдсөн: `Character.java`

Энэ нь **өгөгдсөн** dependency — өөрчлөхгүй:

```java
public class Character {
    public String name;
    public int hp;
    public int mp;

    public Character(String name, int hp, int mp) { ... }
    public void takeDamage(int amount) { /* hp -= amount, min 0 */ }
    public void heal(int amount) { /* hp += amount */ }
}
```

---

## 🟢 Core tasks (60 оноо)

### 1. `abstract class Skill`

```java
public abstract class Skill {
    protected String name;
    protected int mpCost;

    public Skill(String name, int mpCost) { ... }
    public abstract void cast(Character caster, Character target);
    public String getName() { return name; }
    public int getMpCost() { return mpCost; }
}
```

- **`abstract`** түлхүүр үг class declaration-д байх ёстой
- `cast` method нь **body-гүй abstract** — "яаж хийх" нь дэд ангид
- Шууд `new Skill(...)` гэж instance үүсгэх боломжгүй (тест reflection-оор шалгана)

### 2. `Fireball extends Skill`

```java
public class Fireball extends Skill {
    public Fireball() {
        super("Fireball", 30);
    }

    @Override
    public void cast(Character caster, Character target) {
        target.takeDamage(40);
        caster.mp -= 30;
    }
}
```

### 3. `Heal extends Skill`

- name = `"Heal"`, mpCost = `20`
- `cast` → `caster.heal(30)`, `caster.mp -= 20`
- `target` параметрийг үл тоомсорлоно (API-ийн төлөө зөвхөн)

### 4. `Stealth extends Skill`

- name = `"Stealth"`, mpCost = `10`
- `cast` → хохирол учруулахгүй, `caster.mp -= 10` л болно

### 5. Skill шууд instantiate хийх боломжгүй

Тест reflection-оор шалгана:
```java
Modifier.isAbstract(Skill.class.getModifiers()) == true
```

### 6. Дэд анги бүр `cast` method-ийг override

Тест `getDeclaredMethod("cast", Character.class, Character.class)`-ээр шалгана.

---

## 🟡 Stretch tasks (30 оноо)

### 7. `interface Usable`

```java
public interface Usable {
    void use(Character user);
}
```

- **`interface`** түлхүүр үг
- Method автоматаар `public abstract`

### 8. `Potion implements Usable`

- `use(Character user)` → `user.heal(50)`

### 9. `Scroll implements Usable`

- `use(Character user)` → `user.mp += 30`

---

## 🔴 Bonus tasks (10 оноо)

### 10. `Usable.announce()` default method

```java
public interface Usable {
    void use(Character user);

    default void announce() {
        System.out.println("Using item");
    }
}
```

- **`default`** түлхүүр үг — interface доторх хэрэгжүүлэлт
- Дэд анги override хийж болно
- Тест: `Usable u = new Potion(); u.announce();` — алдаагүй ажиллах ёстой

### 11. Class which extends Skill AND implements Usable

Нэг анги нь `Skill` abstract class болон `Usable` interface хоёроос удамших ёстой. Энэ нь Java-гийн multi-inheritance of **type** хэлбэр:

```java
public class HealingPotionSkill extends Skill implements Usable {
    public HealingPotionSkill() { super("HealingPotion", 0); }

    @Override
    public void cast(Character caster, Character target) {
        caster.heal(20);
    }

    @Override
    public void use(Character user) {
        user.heal(30);
    }
}
```

Тест `instanceof Skill` ба `instanceof Usable` хоёуланд нь `true` болохыг шалгана.

---

## 🧪 Тест ажиллуулах

```bash
# Бүх tier
bash scripts/run_tests.sh

# Зөвхөн core
bash scripts/run_tests.sh --tag core

# Зөвхөн stretch
bash scripts/run_tests.sh --tag stretch

# Зөвхөн bonus
bash scripts/run_tests.sh --tag bonus
```

---

## ✅ Шалгуурын жагсаалт (Checklist)

### Core
- [ ] `Skill` нь `abstract class`
- [ ] `Skill.cast` нь abstract method
- [ ] `Fireball` — 30 mp, target takes 40
- [ ] `Heal` — 20 mp, caster.heal(30)
- [ ] `Stealth` — 10 mp, no damage
- [ ] 3 дэд анги тус бүр `cast` override

### Stretch
- [ ] `Usable` нь interface
- [ ] `Potion.use` heals 50
- [ ] `Scroll.use` restores mp 30

### Bonus
- [ ] `Usable.announce` default method
- [ ] `HealingPotionSkill extends Skill implements Usable`

---

## 🚫 Түгээмэл алдаанууд

1. **`abstract` орхих** — `class Skill` нь instantiate хийгдэж болно, тест унана
2. **`cast`-д body бичих** — abstract method нь body-гүй (`;` гэж л төгсгөнө)
3. **`interface`-ийн оронд `class` бичих** — `Usable` нь `interface` байх ёстой
4. **`implements` орхих** — `class Potion {}` биш `class Potion implements Usable {}`
5. **Constructor-д `super(...)` орхих** — parent-ын constructor дуудагдахгүй
6. **Default method-д `default` үг орхих** — энгийн abstract method болж буцна
7. **`Character.java`-г өөрчлөх** — өгөгдсөн, хөндөхгүй
