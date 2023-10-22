package vn.edu.ATBMModel.symmetric;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.StringTokenizer;


public class Hill {
    public double[][] getKey(int n, String data) {
        StringTokenizer st = new StringTokenizer(data, " ");
        double[][] key = new double[n][n];
        if (st.countTokens() != n * n) {
            return null;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                key[i][j] = Double.parseDouble(st.nextToken());
            }
        }
        return key;
    }

    public double[][] encrypt(double[][] key, double[][] messVector) {
        RealMatrix keyMatrix = new Array2DRowRealMatrix(key);
        RealMatrix messMatrix = new Array2DRowRealMatrix(messVector);
        RealMatrix cipherMatrix = keyMatrix.multiply(messMatrix);
        return cipherMatrix.getData();
    }

    public double[][] decrypt(double[][] key, double[][] cipherVector) {
        RealMatrix keyMatrix = new Array2DRowRealMatrix(key);
        RealMatrix cipherMatrix = new Array2DRowRealMatrix(cipherVector);
        RealMatrix inverseKeyMatrix = MatrixUtils.inverse(keyMatrix);
        RealMatrix messMatrix = inverseKeyMatrix.multiply(cipherMatrix);
        return messMatrix.getData();
    }

    public String hillCipherEnc(String key, String mess, int n) {
        String result = "";
        int count = 0;
        double[][] messVector = new double[n][1];
        while (count <= mess.length()) {
            for (int i = 0; i < n; i++) {
                try {
                    messVector[i][0] = mess.charAt(i + count) - 65;
                } catch (Exception e) {
                    messVector[i][0] = 0;
//                messVector[i][0] = mess.charAt(i + count) - 65;
                }
            }
            count = count + n;
            double[][] enc = encrypt(getKey(n, key), messVector);
            for (int i = 0; i < enc.length; i++) {
                System.out.println(enc[i][0] % 26);
                result += (char) ((int) (enc[i][0] % 26) + 65);
                System.out.println(result);
            }
        }
        return result;
    }


    public void printMatrix(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            System.out.print("|");
            for (int j = 0; j < matrix.length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.print("|");
            System.out.println();
        }
    }

    public static void main(String[] args) {
        String data = "6 24 1 13 16 10 20 17 15";
        double[][] key = new double[3][3];
        Hill hill = new Hill();
        key = hill.getKey(3, data);
        hill.printMatrix(key);
        String mess = "ACTACTACTA";
        System.out.println(hill.hillCipherEnc(data, mess, 3));

//        System.out.println('a'- 0);


//        double[][] key = {{6, 24, 1}, {13, 16, 10}, {20, 17, 15}};
//        double[][] plaintext = {{0}, {2}, {19}};
//        int[][] ciphertext = new int[3][1];
//        int[][] plaintext1 = new int[3][1];
//        RealMatrix keyMatrix = new Array2DRowRealMatrix(key);
//
//        RealMatrix inverseKeyMatrix = MatrixUtils.inverse(keyMatrix);
//
//        RealMatrix plaintextMatrix = new Array2DRowRealMatrix(plaintext);
//
//        RealMatrix ciphertextMatrix = keyMatrix.multiply(plaintextMatrix);
//
//        ciphertext = new int[ciphertextMatrix.getRowDimension()][ciphertextMatrix.getColumnDimension()];
//
//        for (int i = 0; i < ciphertextMatrix.getRowDimension(); i++) {
//            for (int j = 0; j < ciphertextMatrix.getColumnDimension(); j++) {
//                ciphertext[i][j] = (int) ciphertextMatrix.getEntry(i, j);
//                System.out.print(ciphertext[i][j] % 26 + " ");
//            }
//            System.out.println();
//        }
//        RealMatrix plaintextMatrix1 = inverseKeyMatrix.multiply(ciphertextMatrix);
//        plaintext1 = new int[plaintextMatrix1.getRowDimension()][plaintextMatrix1.getColumnDimension()];
//        for (int i = 0; i < plaintextMatrix1.getRowDimension(); i++) {
//            for (int j = 0; j < plaintextMatrix1.getColumnDimension(); j++) {
//                plaintext1[i][j] = (int) plaintextMatrix1.getEntry(i, j);
//                System.out.print(plaintext1[i][j] % 26 + " ");
//            }
//            System.out.println();
//        }
    }
}
