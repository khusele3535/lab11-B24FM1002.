#!/bin/bash

# ============================================================
# Lab 11 — Skill System (Abstraction) Test Runner
# ============================================================

RED='\033[91m'
GREEN='\033[92m'
YELLOW='\033[93m'
CYAN='\033[96m'
MAGENTA='\033[95m'
BOLD='\033[1m'
RESET='\033[0m'
DIM='\033[2m'

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
ROOT_DIR="$(dirname "$SCRIPT_DIR")"
LIB_DIR="$ROOT_DIR/lib"
BUILD_DIR="$ROOT_DIR/build/skills"

JUNIT_JAR="junit-platform-console-standalone-1.10.2.jar"
JUNIT_URL="https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.10.2/$JUNIT_JAR"

SRC_DIR="$ROOT_DIR/assignments/skills"
TEST="$ROOT_DIR/tests/SkillTest.java"

# ─── Arguments ───
TAG_FILTER=""
if [ "$1" = "--tag" ] && [ -n "$2" ]; then
    TAG_FILTER="$2"
fi

# ─── JUnit татах ───
download_junit() {
    if [ ! -f "$LIB_DIR/$JUNIT_JAR" ]; then
        echo -e "${CYAN}JUnit jar татаж байна...${RESET}"
        mkdir -p "$LIB_DIR"
        curl -sL "$JUNIT_URL" -o "$LIB_DIR/$JUNIT_JAR"
        if [ $? -ne 0 ]; then
            echo -e "${RED}JUnit jar татаж чадсангүй!${RESET}"
            exit 1
        fi
        echo -e "${GREEN}JUnit jar амжилттай татагдлаа.${RESET}"
    fi
}

# ─── Compile ───
compile_all() {
    mkdir -p "$BUILD_DIR"
    echo -e "${DIM}[1/3] Skill source файлуудыг компайл хийж байна...${RESET}"
    javac -d "$BUILD_DIR" "$SRC_DIR"/*.java 2>&1
    if [ $? -ne 0 ]; then
        echo -e "${RED}  ✗ Skills компайл алдаатай!${RESET}"
        exit 1
    fi
    echo -e "${GREEN}  ✓ Skills source компайл амжилттай${RESET}"

    echo -e "${DIM}[2/3] SkillTest.java компайл хийж байна...${RESET}"
    javac -cp "$BUILD_DIR:$LIB_DIR/$JUNIT_JAR" -d "$BUILD_DIR" "$TEST" 2>&1
    if [ $? -ne 0 ]; then
        echo -e "${RED}  ✗ Тест компайл алдаатай!${RESET}"
        exit 1
    fi
    echo -e "${GREEN}  ✓ Тест компайл амжилттай${RESET}"
}

# ─── Run tests by tag ───
run_tag() {
    local tag="$1"
    local output
    output=$(java -jar "$LIB_DIR/$JUNIT_JAR" \
        --class-path "$BUILD_DIR" \
        --select-class SkillTest \
        --include-tag "$tag" \
        --disable-ansi-colors 2>&1)

    local passed failed total
    passed=$(echo "$output" | grep -oE '[0-9]+ tests successful' | grep -oE '[0-9]+' | head -1)
    failed=$(echo "$output" | grep -oE '[0-9]+ tests failed' | grep -oE '[0-9]+' | head -1)
    passed=${passed:-0}
    failed=${failed:-0}
    total=$((passed + failed))

    echo "$passed $total"

    # Print failures (to stderr so it doesn't pollute captured output)
    if [ "$failed" -gt 0 ]; then
        echo "$output" | grep -E "✗|FAIL|expected:|but was:" | head -10 | while read -r line; do
            echo -e "${RED}    $line${RESET}" >&2
        done
    fi
}

# ─── Main ───
main() {
    echo -e "${BOLD}${MAGENTA}"
    echo "  ╔═══════════════════════════════════════════════╗"
    echo "  ║  🐉 Lab 11 — Skill System Abstraction        ║"
    echo "  ╚═══════════════════════════════════════════════╝"
    echo -e "${RESET}"

    download_junit
    compile_all

    echo ""
    echo -e "${DIM}[3/3] Тест ажиллуулж байна...${RESET}"
    echo ""

    if [ -n "$TAG_FILTER" ]; then
        # Single-tag mode
        result=$(run_tag "$TAG_FILTER")
        passed=$(echo "$result" | awk '{print $1}')
        total=$(echo "$result" | awk '{print $2}')
        if [ "$total" -gt 0 ] && [ "$passed" = "$total" ]; then
            echo -e "${GREEN}  ✓ [$TAG_FILTER] $passed/$total tests passed${RESET}"
        else
            echo -e "${YELLOW}  △ [$TAG_FILTER] $passed/$total tests passed${RESET}"
        fi
        echo ""
        exit 0
    fi

    # Full run — score all tiers
    core_result=$(run_tag "core")
    stretch_result=$(run_tag "stretch")
    bonus_result=$(run_tag "bonus")

    core_p=$(echo "$core_result" | awk '{print $1}')
    core_t=$(echo "$core_result" | awk '{print $2}')
    stretch_p=$(echo "$stretch_result" | awk '{print $1}')
    stretch_t=$(echo "$stretch_result" | awk '{print $2}')
    bonus_p=$(echo "$bonus_result" | awk '{print $1}')
    bonus_t=$(echo "$bonus_result" | awk '{print $2}')

    # Calculate weighted score
    score=$(awk -v cp="$core_p" -v ct="$core_t" \
                -v sp="$stretch_p" -v st="$stretch_t" \
                -v bp="$bonus_p" -v bt="$bonus_t" \
                'BEGIN {
                  core   = (ct > 0) ? (cp/ct)*60 : 0;
                  stretch= (st > 0) ? (sp/st)*30 : 0;
                  bonus  = (bt > 0) ? (bp/bt)*10 : 0;
                  printf "%.1f", core + stretch + bonus
                }')

    # Pretty-print per-tier
    print_tier() {
        local label="$1" p="$2" t="$3" weight="$4"
        local pts
        pts=$(awk -v p="$p" -v t="$t" -v w="$weight" \
              'BEGIN { if (t>0) printf "%.1f", (p/t)*w; else print "0.0" }')
        local color="$GREEN"
        local icon="✓"
        if [ "$p" != "$t" ]; then color="$YELLOW"; icon="△"; fi
        if [ "$p" = "0" ] && [ "$t" != "0" ]; then color="$RED"; icon="✗"; fi
        printf "  ${color}${icon} [%-8s] %s/%s tests passed  → %s/%s${RESET}\n" \
               "$label" "$p" "$t" "$pts" "$weight"
    }

    echo ""
    echo -e "${BOLD}${CYAN}─────────────────────────────────────────${RESET}"
    print_tier "core"    "$core_p"    "$core_t"    60
    print_tier "stretch" "$stretch_p" "$stretch_t" 30
    print_tier "bonus"   "$bonus_p"   "$bonus_t"   10
    echo -e "${BOLD}${CYAN}─────────────────────────────────────────${RESET}"
    echo -e "${BOLD}  НИЙТ ОНОО: ${MAGENTA}${score} / 100${RESET}"
    echo ""

    # AI Detection — scan all student-writable source files
    echo -e "${DIM}AI Detection шалгаж байна...${RESET}"
    for f in "$SRC_DIR/Skill.java" "$SRC_DIR/Fireball.java" "$SRC_DIR/Heal.java" \
             "$SRC_DIR/Stealth.java" "$SRC_DIR/Usable.java" \
             "$SRC_DIR/Potion.java" "$SRC_DIR/Scroll.java"; do
        if [ -f "$f" ]; then
            python3 "$SCRIPT_DIR/ai_detector.py" "$f"
        fi
    done
}

main "$@"
