# 🐉 Lab 11 — Abstraction: Skill System

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)
![JUnit](https://img.shields.io/badge/JUnit-5-green?logo=junit5)
![Auto-Grader](https://img.shields.io/badge/Auto--Grader-Enabled-blue)
![AI Detection](https://img.shields.io/badge/AI%20Detection-Enabled-red)

> Dungeon of OOP-ын баатарт одоо олон төрлийн ур чадвар хэрэгтэй — Fireball нь дайсныг түлнэ, Heal нь биеийг эдгээнэ, Stealth нь сүүдэрт нуугдана. Бүгд "skill" боловч тус бүр өөр өөрөөр `cast()` хийгдэнэ. Яаж нэг ойлголтоор бүгдийг нэгтгэх вэ? Хариулт нь **abstraction** — `abstract class Skill` нь зөвхөн **юу** хийхийг зарлана, дэд анги нь **яаж** хийхийг хэрэгжүүлнэ. Үүн дээр `interface` нэмж, зөвхөн төрлийг нь гэрээлж, олон төрлийн inheritance-ийг эмуляц хийнэ.

## 📚 Суралцах материал

- **Теори:** [`UEFA-OPP-resources/docs/week-11-abstraction/`](https://github.com/UEFA-OPP/UEFA-OPP-resources/tree/main/docs/week-11-abstraction)
- **Git workflow заавар:** [`UEFA-OPP-resources/docs/git-workflow/`](https://github.com/UEFA-OPP/UEFA-OPP-resources/tree/main/docs/git-workflow)

## 🏗️ Хавтасны бүтэц

```
lab11-template/
├── README.md                          # Энэ файл
├── .gitignore
├── assignments/
│   └── skills/
│       ├── Character.java             # ← Өгөгдсөн (бүү өөрчил)
│       ├── Skill.java                 # ← Та энд код бичнэ (abstract class)
│       ├── Fireball.java              # ← Та энд код бичнэ
│       ├── Heal.java                  # ← Та энд код бичнэ
│       ├── Stealth.java               # ← Та энд код бичнэ
│       ├── Usable.java                # ← Та энд код бичнэ (interface)
│       ├── Potion.java                # ← Та энд код бичнэ
│       ├── Scroll.java                # ← Та энд код бичнэ
│       └── README.md                  # Даалгаврын дэлгэрэнгүй заавар
├── tests/
│   └── SkillTest.java                 # JUnit 5 тестүүд (бүү өөрчил)
├── scripts/
│   ├── run_tests.sh                   # Тест ажиллуулах скрипт
│   └── ai_detector.py                 # AI илрүүлэгч
└── .github/workflows/grade.yml        # GitHub Actions автомат шалгагч
```

## 🚀 Лаб хийх заавар (Алхам алхмаар)

### Алхам 1: Repo-г Fork хийх

1. Браузераар [`UEFA-OPP/lab11-template`](https://github.com/UEFA-OPP/lab11-template) руу орно
2. Баруун дээд буланд **Fork** товч дарна
3. Owner-ээр өөрийн account-ийг сонгоод **Create fork** дарна
4. Одоо `https://github.com/<таны-username>/lab11-template` гэсэн хуулбартай боллоо

### Алхам 2: Компьютер дээрээ Clone хийх

```bash
git clone https://github.com/<таны-username>/lab11-template.git
cd lab11-template
```

> SSH key тохируулсан бол `git@github.com:<таны-username>/lab11-template.git` ашиглаж болно.

### Алхам 3: Өөрийн нэрээр branch үүсгэх

```bash
# Жишээ: git checkout -b lab11/bat-erdene
git checkout -b lab11/<өөрийн-нэр>
```

> **Яагаад branch вэ?** `main` branch-д шууд push хийвэл PR үүсгэх боломжгүй. Заавал шинэ branch дээр ажиллана.

### Алхам 4: Даалгаврын зааврыг унших

```bash
cat assignments/skills/README.md
```

Энд `Skill` abstract class-ын бүтэц, `Usable` interface-ийн гэрээ, гурван skill-ийн дэлгэрэнгүй шаардлагыг бичсэн байгаа.

### Алхам 5: Код бичих

`assignments/skills/` доторх бүх `// TODO` комментыг өөрийн кодоор соль. Ядаж дараах түвшнүүдийг хийж үзнэ үү:

- 🟢 **Core (60 оноо)** — `abstract Skill`, `Fireball`, `Heal`, `Stealth`
- 🟡 **Stretch (30 оноо)** — `Usable` interface, `Potion`, `Scroll`
- 🔴 **Bonus (10 оноо)** — default method, multi-inheritance of type

### Алхам 6: Локал тест ажиллуулах

```bash
# Бүх тестийг ажиллуулах
bash scripts/run_tests.sh

# Тодорхой tier дангаар шалгах
bash scripts/run_tests.sh --tag core
bash scripts/run_tests.sh --tag stretch
bash scripts/run_tests.sh --tag bonus
```

**Жишээ output:**
```
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  Lab 11: Skill System
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
[core]    ✓ 9/9 tests passed  → 60.0/60
[stretch] ✓ 3/3 tests passed  → 30.0/30
[bonus]   △ 1/2 tests passed  → 5.0/10
─────────────────────────────────────
НИЙТ ОНОО: 95.0 / 100
```

### Алхам 7: Commit хийх

```bash
git add assignments/
git commit -m "Implement Skill System - <your name>"
```

> **Анхаар:** `tests/`, `scripts/`, `.github/` хавтсуудыг өөрчлөх/commit хийх хэрэггүй. Зөвхөн `assignments/skills/` доторх файлуудаа илгээнэ.
>
> Commit message-ийг **англиар** бичнэ (course convention).

### Алхам 8: GitHub руу Push хийх

```bash
git push origin lab11/<өөрийн-нэр>
```

### Алхам 9: Pull Request (PR) үүсгэх

1. `https://github.com/<таны-username>/lab11-template` руу орно
2. Шар өнгийн **"Compare & pull request"** товч дарна
3. Товч байхгүй бол: **Pull requests** → **New pull request**
   - **base repository:** `UEFA-OPP/lab11-template` | **base:** `main`
   - **head repository:** `<таны-username>/lab11-template` | **compare:** `lab11/<өөрийн-нэр>`
4. PR title-д **өөрийн нэр, бүлгийг** бичнэ. Жишээ: `Bat-Erdene - SE401`
5. **Create pull request** дарна

### Алхам 10: Автомат шалгалтын дүнг харах

PR үүсгэсний дараа GitHub Actions автоматаар ажиллана:

1. PR хуудасны доод талд **Checks** хэсэг гарна
2. ⏳ = ажиллаж байна | ✅ = амжилттай | ❌ = алдаатай
3. **Details** дарж дэлгэрэнгүй дүнг харна
4. PR-т автоматаар коммент бичигдэнэ:

| Tier | Tests | Score |
|------|-------|-------|
| 🟢 Core | 9/9 | 60.0 / 60 |
| 🟡 Stretch | 3/3 | 30.0 / 30 |
| 🔴 Bonus | 1/2 | 5.0 / 10 |
| **Total** | | **95.0 / 100** |
| AI Detection | | ✅ LOW (6) |

> **Алдаатай бол?** Кодоо засаад дахин commit + push хийнэ. PR автоматаар шинэчлэгдэж, тест дахин ажиллана.

## 📊 Оноо тооцох систем

| Tier | Жин | Тайлбар |
|------|-----|---------|
| 🟢 **Core** | **60%** | `abstract class Skill`, 3 дэд skill, cast хэрэгжүүлэлт |
| 🟡 **Stretch** | **30%** | `interface Usable`, `Potion`, `Scroll` |
| 🔴 **Bonus** | **10%** | `default` method, нэг анги нь class+interface хоёроос |

**Формула:**
```
score = (core_passed / core_total) * 60
      + (stretch_passed / stretch_total) * 30
      + (bonus_passed / bonus_total) * 10
```

## 🤖 AI Detection policy

AI detector кодын 11 шалгуурыг шинжилж оноо өгнө (0-121):

| Оноо | Түвшин | Үр дагавар |
|------|--------|------------|
| 0-19 | ✅ **LOW** | Асуудалгүй. Сайн! |
| 20-39 | ⚠️ **MEDIUM** | Багш кодыг шалгана. Хариулт хүсч магадгүй. |
| 40+ | 🚨 **HIGH** | Онооноос **50% хасна**. Повторный submission шаардлагатай. |

## ⚠️ Дүрэм

1. **Тест файлыг өөрчлөхгүй** — `tests/SkillTest.java`-г хөндөхгүй
2. **`Character.java`-г өөрчлөхгүй** — өгөгдсөн dependency
3. **Зөвхөн өөрийн бичих ёстой файлуудад код бичнэ**
4. **AI ашиглахгүй** — ChatGPT, Copilot, Claude, Gemini зэргийг хэрэглэхгүй
5. **Өөрийн branch дээр ажиллана** (`main` биш)
6. **Deadline-аа баримтална** — хожимдуулсан submission оноо хасагдана
7. **Commit message, код — англиар** | **Коммент — англи/монгол хамаагүй**

## 🛠️ Шаардлага

- **Java 17+** — `java -version` гэж шалгана
- **Python 3.11+** — `python3 --version` (AI detector ажиллуулахад)
- **Bash** — тест скрипт ажиллуулахад
- **curl** — JUnit jar автомат татахад
- **Git** — clone, commit, push хийхэд

## 📞 Асуулт байвал

Багшаасаа асуу. Discord / classroom channel-аар бичиж болно. Амжилт хүсье, адвенчурер! 🗡️🛡️
