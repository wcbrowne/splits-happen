import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/*
Given a valid sequence of rolls for one line of American Ten-Pin Bowling, produces the total score for the game.
Input is either stdin or a file.
 */
public class BowlingScore {

    private static final char STRIKE = 'X';
    private static final char SPARE = '/';
    private static final char MISS = '-';


    private String computeScore(final String input) {
        int score = 0;
        int frame = 1;
        boolean firstRoll = true;
        char[] scores = input.toUpperCase().toCharArray();
        for (int i=0; i< scores.length && frame <= 10; i++) {
            if (scores[i] == STRIKE) {
                score += 10;
                score += getScore(scores[i], scores[i+1]);
                score += getScore(scores[i+1], scores[i+2]);
                frame ++;
                firstRoll = true;
            } else if (scores[i] == SPARE) {
                score += getScore(scores[i-1], scores[i]);
                score += getScore(scores[i+1]);
                frame ++;
                firstRoll = true;
            } else if (scores[i] == MISS) {
                if (!firstRoll) {
                    frame ++;
                }
                firstRoll = !firstRoll;
                continue;
            } else {
                score += getScore(scores[i]);
                if (!firstRoll) {
                    frame ++;
                }
                firstRoll = !firstRoll;
            }
        }
        return  Integer.toString(score);
    }

    private int getScore(char currentScore) {
        return getScore('0', currentScore);
    }

    private static int getScore(char previousScore, char currentScore) {
        if (currentScore == STRIKE) {
            return 10;
        } else if (currentScore == SPARE) {
            return 10 - Character.getNumericValue(previousScore);
        } else if (currentScore == MISS) {
            return 0;
        } else {
            return Character.getNumericValue(currentScore);
        }
    }

    public static void main(String[] args) {
        BowlingScore bs = new BowlingScore();
        if (args.length == 1) {
            File dir = new File(args[0]);
            if (dir.isFile()) {
                try (BufferedReader r = Files.newBufferedReader(Paths.get(args[0]), StandardCharsets.UTF_8)) {
                    r.lines().forEach(input -> System.out.println(bs.computeScore(input)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
              System.out.println("Supplied file does not exist or is a directory.");
              return;  
            }
        } else if (args.length == 0) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your scores: ");
            System.out.println(bs.computeScore(scanner.next()));
        } else {
            System.out.println("Please supply a single filename.");
            return;
        }
    }
}