package vn.edu.ATBMModel.symmetric;

import java.util.ArrayList;
import java.util.StringTokenizer;


public class Hill {
    int[] vietnameseCharacters = new int[257];

    public int[] initTableCharacter() {
        vietnameseCharacters = new int[257];
        int count = 0;
        for (int i = 65; i <= 121; i++) {
            vietnameseCharacters[count] = i;
            count++;
        }
        for (int i = 7840; i <= 7929; i++) {
            vietnameseCharacters[count] = i;
            count++;
        }
        vietnameseCharacters[count] = 32;
        count++;
        for (int i = 192; i <= 300; i++) {
            vietnameseCharacters[count] = i;
            count++;
        }
        return vietnameseCharacters;
    }

    public Hill() {
        this.vietnameseCharacters = initTableCharacter();
    }

    int modulo = vietnameseCharacters.length;

    public int[][] getKey(String data) {
        StringTokenizer st = new StringTokenizer(data, " ");
        int[][] key = new int[2][2];
        if (st.countTokens() != 2 * 2) {
            return null;
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                key[i][j] = getNum(st.nextToken());
            }
        }
        return key;
    }

    private int getNum(String s) {
        for (int i = 0; i < vietnameseCharacters.length; i++) {
            if (s.codePointAt(0) == vietnameseCharacters[i]) {
                return i;
            }
        }
        return 148;
    }

    private static int[][] reverseMatrix(int[][] keyMatrix, int modulo) {
        int detmod68 = (keyMatrix[0][0] * keyMatrix[1][1] - keyMatrix[0][1] * keyMatrix[1][0] + modulo) % modulo; // Calculate the determinant modulo 68
        int factor;

        // Find the factor for which is true that
        // factor * det = 1 mod modulo
        for (factor = 1; factor < modulo; factor++) {
            if ((detmod68 * factor) % modulo == 1) {
                break;
            }
        }

        int[][] reverseMatrix = new int[2][2];

        // Calculate the reverse key matrix elements using the factor found
        reverseMatrix[0][0] = (keyMatrix[1][1] * factor) % modulo;
        reverseMatrix[0][1] = ((modulo - keyMatrix[0][1]) * factor) % modulo;
        reverseMatrix[1][0] = ((modulo - keyMatrix[1][0]) * factor) % modulo;
        reverseMatrix[1][1] = (keyMatrix[0][0] * factor) % modulo;

        return reverseMatrix;
    }

    public String encrypt(int[][] key, String mess) {
        if (mess.length() % 2 == 1) {
            mess += " ";
        }
        ArrayList<Integer> messlist = new ArrayList<>();
        ArrayList<Character> result = new ArrayList<>();
        for (int i = 0; i < mess.length(); i++) {
            messlist.add(getNum(mess.charAt(i) + ""));
        }
        for (int i = 0; i < messlist.size(); i += 2) {

            int x = (key[0][0] * messlist.get(i) + key[0][1] * messlist.get(i + 1)) % modulo;
            int y = (key[1][0] * messlist.get(i) + key[1][1] * messlist.get(i + 1)) % modulo;
            result.add((char) vietnameseCharacters[x]);
            result.add((char) vietnameseCharacters[y]);
        }
        return printResult(result);
    }

    public String decrypt(int[][] key, String mess) {
        ArrayList<Integer> messlist = new ArrayList<>();
        ArrayList<Character> result = new ArrayList<>();
        for (int i = 0; i < mess.length(); i++) {
            messlist.add(getNum(mess.charAt(i) + ""));
        }
        int[][] reverseKey = reverseMatrix(key, modulo);
        for (int i = 0; i < messlist.size(); i += 2) {
            int x = (reverseKey[0][0] * messlist.get(i) + reverseKey[0][1] * messlist.get(i + 1)) % modulo;
            int y = (reverseKey[1][0] * messlist.get(i) + reverseKey[1][1] * messlist.get(i + 1)) % modulo;
            result.add((char) vietnameseCharacters[x]);
            result.add((char) vietnameseCharacters[y]);
        }
        return printResult(result);
    }

    private String printResult(ArrayList<Character> result) {
        String s = "";
        for (int i = 0; i < result.size(); i++) {
            s += result.get(i);
        }
        return s;
    }


    public void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            System.out.print("|");
            for (int j = 0; j < matrix.length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.print("|");
            System.out.println();
        }
    }

    public void printUnicode() {
        System.out.println(vietnameseCharacters.length);
        for (int i = 0; i < vietnameseCharacters.length; i++) {
            System.out.print(vietnameseCharacters[i] + " ");
        }
    }

    public static void main(String[] args) {
        Hill hill = new Hill();

        int[][] key = {{16, 33}, {11, 15}};
        String hillenc = hill.encrypt(key, "okokokokokokokokok");
        System.out.println(hillenc);
        System.out.println(hill.decrypt(key, hillenc));
//        System.out.println("ỹ".codePointAt(0)== (char)7929);
//        for (int i = 192; i<=400;i++){
//            System.out.println((char) i);
//        }
//        System.out.println("Ý".codePointAt(0));
    }
}
