#!/bin/bash

NUM_BOARDS=10  # 🟢 Parametric: number of boards per size

echo "Cleaning old results..."
rm -rf results/* snapshots/* boards/*

echo "Compiling Java..."
javac -d bin src/game/*.java src/players/*.java
if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    exit 1
fi

echo "Generating boards..."
mkdir -p boards snapshots results

# ✅ Define board sizes to test
BOARD_SIZES=(10 25 50)

for size in "${BOARD_SIZES[@]}"; do
    for ((i = 1; i <= NUM_BOARDS; i++)); do
        java -cp bin game.InstanceGenerator "boards/board_${size}x${size}_$i.dat" $size
    done
done

echo "Running tests..."
> results/TotalScores.txt

while IFS= read -r studentID; do
    totalPercentage=0
    playerLogFile="results/Player${studentID}.log"
    > "$playerLogFile"

    for size in "${BOARD_SIZES[@]}"; do
        for ((i = 1; i <= NUM_BOARDS; i++)); do
            boardFile="boards/board_${size}x${size}_$i.dat"
            echo "Testing $studentID on $boardFile..."

            # ⏱️ Compute timeout dynamically per board size (capped at 300 seconds)
            timeoutSeconds=$(awk "BEGIN { t = (0.4 * $size * $size) + 2; print (t > 300 ? 300 : int(t)) }")

            output=$(timeout ${timeoutSeconds}s java -cp bin game.Tester "$boardFile" "$studentID")
            exit_code=$?

            score=1
            percentage=0.00

            if [ $exit_code -eq 0 ]; then
                score_line=$(echo "$output" | tail -n 1)
                score=$(echo "$score_line" | awk '{print $2}')
                if [[ "$score" =~ ^[0-9]+$ ]]; then
                    maxScore=$((size * size))
                    percentage=$(awk "BEGIN {printf \"%.2f\", 100 * $score / $maxScore}")
                    echo "$size x $size - Game $i: $score ($percentage%)" >> "$playerLogFile"
                else
                    echo "$size x $size - Game $i: invalid score ($score_line)" >> "$playerLogFile"
                fi
            else
                echo "$size x $size - Game $i: 1 (timeout/crash) ($percentage%)" >> "$playerLogFile"
            fi

            totalPercentage=$(awk "BEGIN {print $totalPercentage + $percentage}")
        done
    done

    totalGames=$(( ${#BOARD_SIZES[@]} * NUM_BOARDS ))
    avgPercentage=$(awk "BEGIN {printf \"%.2f\", $totalPercentage / $totalGames}")
    echo "$studentID $avgPercentage%" >> results/TotalScores.txt
done < students.txt

echo "Done! Check 'results/TotalScores.txt' and 'snapshots/' for full gameplay."
