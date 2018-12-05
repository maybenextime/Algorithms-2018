package lesson6;

import kotlin.NotImplementedError;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class JavaDynamicTasks {
    /**
     * Наибольшая общая подпоследовательность.
     * Средняя
     * <p>
     * Дано две строки, например "nematode knowledge" и "empty bottle".
     * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
     * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
     * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
     * Если общей подпоследовательности нет, вернуть пустую строку.
     * При сравнении подстрок, регистр символов *имеет* значение.
     * Трудоемкость алгоритма - O(m*n) где m : second.length() ; n : first.length()
     * Ресурсоемкость - O(m*n)
     */
    public static String longestCommonSubSequence(String first, String second) {

        int m = second.length();
        int n = first.length();
        if (m == 0 || n == 0) return "";
        int[][] count = new int[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (first.charAt(j) == second.charAt(i)) {
                    if (i == 0 || j == 0) {
                        count[i][j] = 1;
                    } else count[i][j] = count[i - 1][j - 1] + 1;
                } else if (i == 0 || j == 0) {
                    if (i != 0) count[i][j] = count[i - 1][j];
                    else if (j != 0) count[i][j] = count[i][j - 1];
                    else count[i][j] = 0;
                } else
                    count[i][j] = Math.max(count[i - 1][j], count[i][j - 1]);
            }
        }
        int index = count[m - 1][n - 1];
        String longest = "";
        if (index == 0) return "";
        int i = m - 1, j = n - 1;
        while (i >= 0 && j >= 0) {
            if (first.charAt(j) == second.charAt(i)) {
                longest = String.valueOf(first.charAt(j)) + longest;
                i--;
                j--;
                index--;
            } else {
                if (i > 0 && j > 0) {
                    if (count[i - 1][j] > count[i][j - 1])
                        i--;
                    else
                        j--;
                } else {
                    if (i != 0) i--;
                    else if (j != 0) j--;
                    else break;
                }

            }
        }
        return longest;
    }

    /**
     * Наибольшая возрастающая подпоследовательность
     * Средняя
     * <p>
     * Дан список целых чисел, например, [2 8 5 9 12 6].
     * Найти в нём самую длинную возрастающую подпоследовательность.
     * Элементы подпоследовательности не обязаны идти подряд,
     * но должны быть расположены в исходном списке в том же порядке.
     * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
     * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
     * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
     */
    public static List<Integer> longestIncreasingSubSequence(List<Integer> list) {
        throw new NotImplementedError();
    }

    /**
     * Самый короткий маршрут на прямоугольном поле.
     * Сложная
     * <p>
     * В файле с именем inputName задано прямоугольное поле:
     * <p>
     * 0 2 3 2 4 1
     * 1 5 3 4 6 2
     * 2 6 2 5 1 3
     * 1 4 3 2 6 2
     * 4 2 3 1 5 0
     * <p>
     * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
     * В каждой клетке записано некоторое натуральное число или нуль.
     * Необходимо попасть из верхней левой клетки в правую нижнюю.
     * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
     * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
     * <p>
     * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
     * Трудоемкость алгоритма - O(m*n) где m , n : размер матрицы
     * Ресурсоемкость - O(m*n)
     */
    public static int shortestPathOnField(String inputName) throws IOException {
        String str;
        int row = 0;
        int col = 0;
        List<String[]> linesStr = new ArrayList<>();
        BufferedReader fileIn = new BufferedReader(new FileReader(inputName));
        while ((str = fileIn.readLine()) != null) {
            String[] string = str.split(" ");
            linesStr.add(string);
            row++;
        }
        col = linesStr.get(0).length;

        int[][] matrix = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                matrix[i][j] = Integer.parseInt(linesStr.get(i)[j]);
            }
        }
        int[][] value = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (i == 0 && j == 0) value[i][j] = matrix[i][j];
                else {
                    if (i == 0) value[i][j] = value[i][j - 1] + matrix[i][j];
                    else if (j == 0) value[i][j] = value[i - 1][j] + matrix[i][j];
                    else
                        value[i][j] = Math.min(value[i][j - 1], Math.min(value[i - 1][j], value[i - 1][j - 1])) + matrix[i][j];
                }
            }
        }

        return value[row - 1][col - 1];

    }
}
