import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@DisplayName("Lab 11: Skill System (Abstraction)")
public class SkillTest {

    private Character caster;
    private Character target;

    @BeforeEach
    void setUp() {
        caster = new Character("Gandalf", 100, 100);
        target = new Character("Orc", 100, 0);
    }

    // ==================== 🟢 CORE ====================

    @Test
    @Tag("core")
    @DisplayName("Skill класс нь abstract байх ёстой")
    void skillIsAbstract() {
        assertTrue(Modifier.isAbstract(Skill.class.getModifiers()),
            "Skill нь abstract class байх ёстой");
    }

    @Test
    @Tag("core")
    @DisplayName("Skill.cast нь abstract method")
    void skillCastIsAbstract() throws Exception {
        Method m = Skill.class.getDeclaredMethod("cast", Character.class, Character.class);
        assertTrue(Modifier.isAbstract(m.getModifiers()),
            "Skill.cast нь abstract method байх ёстой");
    }

    @Test
    @Tag("core")
    @DisplayName("Fireball нь Skill-аас удамшдаг")
    void fireballExtendsSkill() {
        Fireball fb = new Fireball();
        assertTrue(fb instanceof Skill, "Fireball нь Skill-аас extends хийх ёстой");
    }

    @Test
    @Tag("core")
    @DisplayName("Fireball nameCost: 'Fireball', 30 mp")
    void fireballProperties() {
        Fireball fb = new Fireball();
        assertEquals("Fireball", fb.getName());
        assertEquals(30, fb.getMpCost());
    }

    @Test
    @Tag("core")
    @DisplayName("Fireball.cast: target takes 40, caster mp -30")
    void fireballCast() {
        Fireball fb = new Fireball();
        fb.cast(caster, target);
        assertEquals(60, target.hp, "target hp: 100 - 40 = 60");
        assertEquals(70, caster.mp, "caster mp: 100 - 30 = 70");
    }

    @Test
    @Tag("core")
    @DisplayName("Heal.cast: caster heals 30, mp -20")
    void healCast() {
        Heal h = new Heal();
        caster.hp = 50;
        h.cast(caster, target);
        assertEquals(80, caster.hp, "caster hp: 50 + 30 = 80");
        assertEquals(80, caster.mp, "caster mp: 100 - 20 = 80");
        assertEquals("Heal", h.getName());
        assertEquals(20, h.getMpCost());
    }

    @Test
    @Tag("core")
    @DisplayName("Stealth.cast: no damage, mp -10")
    void stealthCast() {
        Stealth s = new Stealth();
        s.cast(caster, target);
        assertEquals(100, target.hp, "target hp өөрчлөгдөхгүй");
        assertEquals(90, caster.mp, "caster mp: 100 - 10 = 90");
        assertEquals("Stealth", s.getName());
        assertEquals(10, s.getMpCost());
    }

    @Test
    @Tag("core")
    @DisplayName("Fireball нь cast method-ийг өөрөө declare хийсэн байна")
    void fireballDeclaresCast() throws Exception {
        Method m = Fireball.class.getDeclaredMethod("cast", Character.class, Character.class);
        assertNotNull(m, "Fireball.cast method зарлагдсан байх ёстой");
    }

    @Test
    @Tag("core")
    @DisplayName("Heal ба Stealth нь cast method-ийг declare хийсэн")
    void healAndStealthDeclareCast() throws Exception {
        Method hm = Heal.class.getDeclaredMethod("cast", Character.class, Character.class);
        Method sm = Stealth.class.getDeclaredMethod("cast", Character.class, Character.class);
        assertNotNull(hm);
        assertNotNull(sm);
    }

    // ==================== 🟡 STRETCH ====================

    @Test
    @Tag("stretch")
    @DisplayName("Usable нь interface")
    void usableIsInterface() {
        assertTrue(Usable.class.isInterface(),
            "Usable нь interface байх ёстой");
    }

    @Test
    @Tag("stretch")
    @DisplayName("Potion implements Usable, use() heals 50")
    void potionHeals50() {
        Potion p = new Potion();
        assertTrue(p instanceof Usable, "Potion нь Usable implement хийх ёстой");
        caster.hp = 30;
        p.use(caster);
        assertEquals(80, caster.hp, "caster hp: 30 + 50 = 80");
    }

    @Test
    @Tag("stretch")
    @DisplayName("Scroll implements Usable, use() restores 30 mp")
    void scrollRestoresMp() {
        Scroll s = new Scroll();
        assertTrue(s instanceof Usable, "Scroll нь Usable implement хийх ёстой");
        caster.mp = 10;
        s.use(caster);
        assertEquals(40, caster.mp, "caster mp: 10 + 30 = 40");
    }

    // ==================== 🔴 BONUS ====================

    @Test
    @Tag("bonus")
    @DisplayName("Usable.announce default method алдаагүй ажиллана")
    void usableAnnounceDefaultMethod() throws Exception {
        Method m = Usable.class.getMethod("announce");
        assertTrue(m.isDefault(), "announce нь default method байх ёстой");
        // Actually invoke it — Potion doesn't override, inherits default
        Potion p = new Potion();
        assertDoesNotThrow(() -> p.announce(),
            "Potion.announce (default-ээс) алдаагүй ажиллах ёстой");
    }

    @Test
    @Tag("bonus")
    @DisplayName("Класс нь Skill extend хийж, Usable implement хийнэ")
    void classExtendsSkillAndImplementsUsable() throws Exception {
        // Look for any class that satisfies both relationships
        Class<?> target = null;
        String[] candidates = { "HealingPotionSkill", "ComboSkill", "MagicPotion", "HealingScroll" };
        for (String name : candidates) {
            try {
                target = Class.forName(name);
                break;
            } catch (ClassNotFoundException ignored) {}
        }
        assertNotNull(target,
            "Skill extends + Usable implements хийсэн анги байх ёстой (жишээ: HealingPotionSkill)");
        assertTrue(Skill.class.isAssignableFrom(target),
            target.getSimpleName() + " нь Skill-аас extend хийх ёстой");
        assertTrue(Usable.class.isAssignableFrom(target),
            target.getSimpleName() + " нь Usable implement хийх ёстой");
    }
}
