import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BowlingGame {

    private int score = 0;

    private int[][] scoresPerRoll;

    private boolean hasGameEnded = false;

    private boolean isBonusRoll = false;

    private int curFrame = 0;

    private int bowlInFrame = 0;

    private int numBonusRollsLeft = 0;

    public BowlingGame() {
        scoresPerRoll = new int[10][2];
    }

    public void roll(int numPins) {
        if (numPins > 10 || (!isBonusRoll && bowlInFrame == 1 && numPins > 10 - scoresPerRoll[curFrame][0])
                || (isBonusRoll && ((bowlInFrame == 2 && scoresPerRoll[curFrame][0] == 10 && numPins > (10 - scoresPerRoll[curFrame][1])) ||
                (bowlInFrame == 1 && scoresPerRoll[curFrame][0] != 10 && numPins > (10 - scoresPerRoll[curFrame][0]))))) {
            System.out.println("Number of pins struck down in the current roll cannot be more than whats remaining");
            return;
        }
        if (hasGameEnded) {
            System.out.println("Game has ended");
            return;
        }

        score += numPins;

        if (!isBonusRoll) {
            scoresPerRoll[curFrame][bowlInFrame] = numPins;
        } else {
            // this is a bonus roll
            numBonusRollsLeft--;
        }

        // handle spare and strike scenarios
        if (curFrame > 0 && curFrame < 9) {
            if (curFrame > 1 && scoresPerRoll[curFrame - 1][0] == 10 && scoresPerRoll[curFrame - 2][0] == 10) {
                // prev 2 strikes
                score += numPins;
            }
            if (scoresPerRoll[curFrame - 1][0] == 10) {
                // prev strike
                score += numPins;
            } else if (bowlInFrame == 0 && scoresPerRoll[curFrame - 1][0] + scoresPerRoll[curFrame - 1][1] == 10) {
                // prev spare
                score += numPins;
            }
        }

        // last frame
        if (curFrame == 9) {
            // update scores
            if (scoresPerRoll[curFrame - 1][0] == 10 && bowlInFrame < 2) {
                score += numPins;
            } else if ((scoresPerRoll[curFrame - 1][0] + scoresPerRoll[curFrame - 1][1] == 10) && bowlInFrame == 0) {
                score += numPins;
            } else if (scoresPerRoll[curFrame][0] == 10 && bowlInFrame == 2) {
                score += numPins;
            }

            // update pointers and counts
            if (scoresPerRoll[9][0] == 10) {
                numBonusRollsLeft = 2;
                isBonusRoll = true;
            } else if (bowlInFrame == 1 && (scoresPerRoll[9][0] + scoresPerRoll[9][1] == 10)) {
                numBonusRollsLeft = 1;
                isBonusRoll = true;
            }
        }

        // update pointers
        if (numPins == 10 && numBonusRollsLeft == 0) {
            bowlInFrame = 0;
            curFrame++;
        } else if (bowlInFrame == 1 && numBonusRollsLeft == 0) {
            curFrame++;
            bowlInFrame = 0;
        } else {
            bowlInFrame++;
        }

        if (curFrame == 10) {
            // set game ends
            hasGameEnded = true;
        }
    }

    public int score() {
        return score;
    }


    private static int playGame(List<Integer> rolls) {
        BowlingGame bowlingGame = new BowlingGame();
        for (Integer pinsToStrike : rolls) {
            bowlingGame.roll(pinsToStrike);
        }

        return bowlingGame.score();
    }

    public static void main(String[] args) {
        System.out.println("Welcome to Bowling club");

        int[] pinsStruckdown1 = {4, 2, 10, 5, 5, 4, 2, 4, 6, 10, 9, 1, 4, 5, 3, 7, 4, 6, 5};
        List<Integer> pinsStruck1 = IntStream.of(pinsStruckdown1).boxed().collect(Collectors.toList());
        int score1 = playGame(pinsStruck1);
        int expectedScore1 = 138;
        System.out.println("Expected score : " + expectedScore1 + " and actual score " + score1);


        int[] pinsStruckdown2 = {10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10};
        int expectedScore2 = 300;
        List<Integer> pinsStruck2 = IntStream.of(pinsStruckdown2).boxed().collect(Collectors.toList());
        int score2 = playGame(pinsStruck2);
        System.out.println("Expected score : " + expectedScore2 + " and actual score " + score2);

        int[] pinsStruckdown3 = {10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 5, 5};
        int expectedScore3 = 280;
        List<Integer> pinsStruck3 = IntStream.of(pinsStruckdown3).boxed().collect(Collectors.toList());
        int score3 = playGame(pinsStruck3);
        System.out.println("Expected score : " + expectedScore3 + " and actual score " + score3);
    }
}
