package lesson2;

import kotlin.NotImplementedError;
import kotlin.Pair;

import java.io.*;
import java.util.*;

@SuppressWarnings("unused")
public class JavaAlgorithms {
    /**
     * Получение наибольшей прибыли (она же -- поиск максимального подмассива)
     * Простая
     * <p>
     * Во входном файле с именем inputName перечислены цены на акции компании в различные (возрастающие) моменты времени
     * (каждая цена идёт с новой строки). Цена -- это целое положительное число. Пример:
     * <p>
     * 201
     * 196
     * 190
     * 198
     * 187
     * 194
     * 193
     * 185
     * <p>
     * Выбрать два момента времени, первый из них для покупки акций, а второй для продажи, с тем, чтобы разница
     * между ценой продажи и ценой покупки была максимально большой. Второй момент должен быть раньше первого.
     * Вернуть пару из двух моментов.
     * Каждый момент обозначается целым числом -- номер строки во входном файле, нумерация с единицы.
     * Например, для приведённого выше файла результат должен быть Pair(3, 4)
     * <p>
     * В случае обнаружения неверного формата файла бросить любое исключение.
     * Трудоемкость алгоритма - O(N)
     * * Ресурсоемкость - O(N)
     */
    static public Pair<Integer, Integer> optimizeBuyAndSell(String inputName) throws IOException {
        BufferedReader fileIn = new BufferedReader(new FileReader(inputName));
        Pair<Integer, Integer> pair = null;
        List<Integer> list = new ArrayList<>();
        String str;
        int max = 0;
        int diff1 = 0;
        int diff2 = 0;
        int nextIndx = 0;
        boolean isIncre = true;
        while ((str = fileIn.readLine()) != null) {
            list.add(Integer.parseInt(str));
        }
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(j) < list.get(i)) {
                    nextIndx = j;
                    break;
                } else {
                    diff1 = diff2;
                    diff2 = list.get(j) - list.get(i);
                    if (diff2 > max) {
                        max = diff2;
                        pair = new Pair<>(i + 1, j + 1);
                        nextIndx = j + 1;
                    }
                    if (isIncre) {
                        nextIndx = j;
                        if (diff2 < diff1) isIncre = false;
                    }
                }
            }
            i = nextIndx - 1;
            isIncre = true;
        }
        return pair;
    }

    /**
     * Задача Иосифа Флафия.
     * Простая
     * <p>
     * Образовав круг, стоят menNumber человек, пронумерованных от 1 до menNumber.
     * <p>
     * 1 2 3
     * 8   4
     * 7 6 5
     * <p>
     * Мы считаем от 1 до choiceInterval (например, до 5), начиная с 1-го человека по кругу.
     * Человек, на котором остановился счёт, выбывает.
     * <p>
     * 1 2 3
     * 8   4
     * 7 6 х
     * <p>
     * Далее счёт продолжается со следующего человека, также от 1 до choiceInterval.
     * Выбывшие при счёте пропускаются, и человек, на котором остановился счёт, выбывает.
     * <p>
     * 1 х 3
     * 8   4
     * 7 6 Х
     * <p>
     * Процедура повторяется, пока не останется один человек. Требуется вернуть его номер (в данном случае 3).
     * <p>
     * 1 Х 3
     * х   4
     * 7 6 Х
     * <p>
     * 1 Х 3
     * Х   4
     * х 6 Х
     * <p>
     * х Х 3
     * Х   4
     * Х 6 Х
     * <p>
     * Х Х 3
     * Х   х
     * Х 6 Х
     * <p>
     * Х Х 3
     * Х   Х
     * Х х Х
     */
    static public int josephTask(int menNumber, int choiceInterval) {
        throw new NotImplementedError();
    }

    /**
     * Наибольшая общая подстрока.
     * Средняя
     * <p>
     * Дано две строки, например ОБСЕРВАТОРИЯ и КОНСЕРВАТОРЫ.
     * Найти их самую длинную общую подстроку -- в примере это СЕРВАТОР.
     * Если общих подстрок нет, вернуть пустую строку.
     * При сравнении подстрок, регистр символов *имеет* значение.
     * Если имеется несколько самых длинных общих подстрок одной длины,
     * вернуть ту из них, которая встречается раньше в строке first.
     * Трудоемкость алгоритма - O(N*m)
     * * Ресурсоемкость - O(N*m)
     */
    static public String longestCommonSubstring(String firs, String second) {
        int lenghtFirst = firs.length();
        int lenghtSecd = second.length();
        int max = 0;
        int indexRow = 0;
        String longestSubString = "";
        int[][] count = new int[lenghtFirst][lenghtSecd];
        for (int i = 0; i < lenghtFirst; i++) {
            for (int j = 0; j < lenghtSecd; j++) {
                if (firs.charAt(i) == second.charAt(j)) {
                    if (i == 0 || j == 0) {
                        count[i][j] = 1;
                    } else {
                        count[i][j] = count[i - 1][j - 1] + 1;
                    }
                    if (count[i][j] > max) {
                        max = count[i][j];
                        indexRow = i;
                    }
                }
            }
        }
        while (max > 0) {
            longestSubString = firs.charAt(indexRow) + longestSubString;
            indexRow--;
            max--;
        }
        return longestSubString;

    }

    /**
     * Число простых чисел в интервале
     * Простая
     * <p>
     * Рассчитать количество простых чисел в интервале от 1 до limit (включительно).
     * Если limit <= 1, вернуть результат 0.
     * <p>
     * Справка: простым считается число, которое делится нацело только на 1 и на себя.
     * Единица простым числом не считается.
     * Трудоемкость алгоритма - O(NlogN)
     * * Ресурсоемкость - O(N)
     */

    static public int calcPrimesNumber(int limit) {
        int res = 0;
        boolean[] isPrime = new boolean[limit + 1];
        if (limit < 2) return 0;
        for (int i = 2; i < limit + 1; i++) {
            isPrime[i] = true;
        }
        for (int i = 2; i < limit + 1; i++)
            if (isPrime[i]) {
                for (int j = 2 * i; j < limit + 1; j += i)
                    isPrime[j] = false;
            }

        for (int i = 2; i < limit + 1; i++) {
            if (isPrime[i]) res++;
        }
        return res;
    }

    /**
     * Балда
     * Сложная
     * <p>
     * В файле с именем inputName задана матрица из букв в следующем формате
     * (отдельные буквы в ряду разделены пробелами):
     * <p>
     * И Т Ы Н
     * К Р А Н
     * А К В А
     * <p>
     * В аргументе words содержится множество слов для поиска, например,
     * ТРАВА, КРАН, АКВА, НАРТЫ, РАК.
     * <p>
     * Попытаться найти каждое из слов в матрице букв, используя правила игры БАЛДА,
     * и вернуть множество найденных слов. В данном случае:
     * ТРАВА, КРАН, АКВА, НАРТЫ
     * <p>
     * И т Ы Н     И т ы Н
     * К р а Н     К р а н
     * А К в а     А К В А
     * <p>
     * Все слова и буквы -- русские или английские, прописные.
     * В файле буквы разделены пробелами, строки -- переносами строк.
     * Остальные символы ни в файле, ни в словах не допускаются.
     * Трудоемкость алгоритма - O(m*n+k*h) k количество words , h средная длина word
     * Ресурсоемкость - O(m*n) где m,n размер матрицы
     */
    static public Set<String> baldaSearcher(String inputName, Set<String> words) throws IOException {
        Set<String> result = new HashSet<>();
        int row = 0;
        int col = 0;
        String str;
        List<String[]> linesStr = new ArrayList<>();
        BufferedReader fileIn = new BufferedReader(new FileReader(inputName));
        while ((str = fileIn.readLine()) != null) {
            String[] string = str.split(" ");
            linesStr.add(string);
            row++;
        }
        col = linesStr.get(0).length;
        Map<String, List<Pair<Integer, Integer>>> letter = new HashMap<>();

        String[][] matrix = new String[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                matrix[i][j] = linesStr.get(i)[j];
                if (!letter.containsKey(matrix[i][j])) {
                    List<Pair<Integer, Integer>> listIndex = new ArrayList<>();
                    listIndex.add(new Pair<>(i, j));
                    letter.put(matrix[i][j], listIndex);
                } else {
                    letter.get(matrix[i][j]).add(new Pair<>(i, j));
                }
            }
        }
        for (String word : words) {
            String firstLetter = String.valueOf(word.charAt(0));
            if (letter.containsKey(firstLetter)) {
                for (Pair<Integer, Integer> pairIndex : letter.get(firstLetter)) {
                    if (check(matrix, pairIndex.getFirst(), pairIndex.getSecond(), word.substring(1))) {
                        result.add(word);
                        break;
                    }
                }
            }
        }
        return result;
    }

    static private boolean check(String[][] matrix, int row, int col, String word) {
        if (word.length() == 0) return true;
        if (row > 0 && matrix[row - 1][col].equals(word.valueOf(word.charAt(0)))) {
            if (check(matrix, row - 1, col, word.substring(1))) return true;
        }
        if (col > 0 && matrix[row][col - 1].equals(word.valueOf(word.charAt(0)))) {
            if (check(matrix, row, col - 1, word.substring(1))) return true;
        }
        if (row < matrix.length - 1 && matrix[row + 1][col].equals(word.valueOf(word.charAt(0)))) {
            if (check(matrix, row + 1, col, word.substring(1))) return true;
        }
        if (col < matrix[0].length - 1 && matrix[row][col + 1].equals(word.valueOf(word.charAt(0)))) {
            if (check(matrix, row, col + 1, word.substring(1))) return true;
        }
        return false;

    }
}