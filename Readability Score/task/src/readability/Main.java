package readability;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        FileTextInfo fileTextInfo = new FileTextInfo(args[0]);
        fileTextInfo.printTextInfo();
        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        Scanner scanner = new Scanner(System.in);
        String option = scanner.nextLine();
        fileTextInfo.printScore(option);
    }

    static class FileTextInfo {
        private final String filePath;

        private int characterNumber = 0;
        private int sentenceNumber = 0;
        private int wordNumber = 0;
        private int syllableNumber = 0;
        private int polysyllableNumber = 0;

        public FileTextInfo(String filePath) {
            this.filePath = filePath;
            processFile();
        }

        public void printTextInfo() {
            System.out.printf("Words: %d\n", wordNumber);
            System.out.printf("Sentences: %d\n", sentenceNumber);
            System.out.printf("Characters: %d\n", characterNumber);
            System.out.printf("Syllables: %d\n", syllableNumber);
            System.out.printf("Polysyllables: %d\n", polysyllableNumber);
        }

        public void printScore(String option) {
            System.out.println(" ");
            switch (option) {
                case "ARI":
                    printARIScoreAndGetAge();
                    break;
                case "FK":
                    printFleschKincaidScoreAndGetAge();
                    break;
                case "SMOG":
                    printSmogScoreAndGetAge();
                    break;
                case "CL":
                    printColemanLiauScoreAndGetAge();
                    break;
                default:
                    printAllTests();
            }
        }

        private void printAllTests() {
            int ageTotal = printARIScoreAndGetAge();
            ageTotal += printFleschKincaidScoreAndGetAge();
            ageTotal += printSmogScoreAndGetAge();
            ageTotal += printColemanLiauScoreAndGetAge();

            double ageAvg = ((double) ageTotal) / 4;
            System.out.println(" ");
            System.out.printf("This text should be understood in average by %.2f year olds.\n", ageAvg);

        }

        private void processFile() {
            File file = new File(this.filePath);
            try (Scanner scanner = new Scanner(file)) {
                scanner.useDelimiter("[!.?]");
                while (scanner.hasNext()) {
                    String sentence = scanner.next();
                    sentenceNumber++;
                    Scanner wordScanner = new Scanner(sentence).useDelimiter("\\s+");
                    while (wordScanner.hasNext()) {
                        String word = wordScanner.next();
                        countSyllables(word);
                        this.wordNumber++;
                    }
                }
                try (Scanner letterScanner = new Scanner(file)) {
                    while (letterScanner.hasNext()) {
                        characterNumber += letterScanner.next().length();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private int printARIScoreAndGetAge() {
            double score = (4.71 * ((double) characterNumber / wordNumber)) + (0.5 * ((double) wordNumber / sentenceNumber)) - 21.43;
            int ageRange = getAge(score);
            System.out.printf("Automated Readability Index: %.2f (about %d year olds).\n", score, ageRange);
            return ageRange;
        }

        private int printFleschKincaidScoreAndGetAge() {
            double score = (0.39 * ((double) wordNumber / sentenceNumber)) + (11.8 * ((double) syllableNumber / wordNumber)) - 15.59;
            int ageRange = getAge(score);
            System.out.printf("Flesch–Kincaid readability tests: %.2f (about %d year olds).\n", score, ageRange);
            return ageRange;
        }

        private int printSmogScoreAndGetAge() {
            double score = (1.043 * Math.sqrt(polysyllableNumber * (30.0 / sentenceNumber))) + 3.1291;
            int ageRange = getAge(score);
            System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d year olds).\n", score, ageRange);
            return ageRange;
        }

        private int printColemanLiauScoreAndGetAge() {
            double score = (0.0588 * (((double) characterNumber / wordNumber) * 100)) - (0.296 * ((double) sentenceNumber / wordNumber) * 100) - 15.8;
            int ageRange = getAge(score);
            System.out.printf("Coleman–Liau index: %.2f (about %d year olds).\n", score, ageRange);
            return ageRange;
        }

        private int getAge(double rawScore) {
            int score = (int) Math.ceil(rawScore);
            switch (score) {
                case 1:
                    return 6;
                case 2:
                    return 7;
                case 3:
                    return 9;
                case 4:
                    return 10;
                case 5:
                    return 11;
                case 6:
                    return 12;
                case 7:
                    return 13;
                case 8:
                    return 14;
                case 9:
                    return 15;
                case 10:
                    return 16;
                case 11:
                    return 17;
                case 12:
                    return 18;
                case 13:
                    return 24;
                default:
                    return 24;
            }
        }

        private void countSyllables(String word) {
            int syllablesCount = SyllableHelper.syllables(word);
            syllableNumber += syllablesCount;
            if (syllablesCount > 2)
                polysyllableNumber++;
        }
    }

    static class TextInfo {
        final String text;

        int sentenceNumber = 0;
        int wordNumber = 0;

        public TextInfo(String text) {
            this.text = text.strip();
            wordNumber = getWordCount();
            sentenceNumber = getSentenceCount();
        }

        private int getWordCount() {
            String regexp = "\\s+";
            String[] arr = text.split(regexp);
            return arr.length;
        }

        private int getSentenceCount() {
            String regexp = "[?.!]";
            String[] arr = text.split(regexp);
            return arr.length;
        }

        public boolean isHardToRead() {
            return ((double) wordNumber / sentenceNumber) > 10;
        }
    }

}
