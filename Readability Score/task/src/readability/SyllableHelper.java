package readability;

public class SyllableHelper {
    private static final char[] vowelList = new char[]{'a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U'};

    private static boolean isVowel(char c) {
        for (char value : vowelList) {
            if (value == c) {
                return true;
            }
        }
        return false;
    }

    public static int syllables(String word) {
        char lastChar = word.charAt(word.length() - 1);
        if (word.length() > 1 && !Character.isLetter(lastChar)) {
            word = word.substring(0, word.length() - 2);
        }
        int syllableCount = 0;
        for (int i = 0; i < word.length(); i++) {
            if (isVowel(word.charAt(i))) {
                if (i == 0) {
                    syllableCount++;
                    continue;
                }
                if (isVowel(word.charAt(i - 1))) {
                    continue;
                }

                if (i == word.length() - 1) {
                    if (word.charAt(i) == 'E' || word.charAt(i) == 'e') {
                        continue;
                    }
                }

                syllableCount++;
            }
        }
        return syllableCount > 0 ? syllableCount : 1;
    }
}
