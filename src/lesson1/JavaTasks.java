package lesson1;

import java.io.*;
import java.util.*;

import kotlin.NotImplementedError;

@SuppressWarnings("unused")
public class JavaTasks {
    /**
     * Сортировка времён
     * <p>
     * Простая
     * (Модифицированная задача с сайта acmp.ru)
     * <p>
     * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС,
     * каждый на отдельной строке. Пример:
     * <p>
     * 13:15:19
     * 07:26:57
     * 10:00:03
     * 19:56:14
     * 13:15:19
     * 00:40:31
     * <p>
     * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
     * сохраняя формат ЧЧ:ММ:СС. Одинаковые моменты времени выводить друг за другом. Пример:
     * <p>
     * 00:40:31
     * 07:26:57
     * 10:00:03
     * 13:15:19
     * 13:15:19
     * 19:56:14
     * <p>
     * В случае обнаружения неверного формата файла бросить любое исключение.
     * <p>
     * Трудоемкость алгоритма - O(NlogN)
     * Ресурсоемкость - O(N)
     */


    private static Integer toSec(String str) {
        String[] sub = str.split(":");
        return (Integer.parseInt(sub[0]) * 3600 + Integer.parseInt(sub[1]) * 60 + Integer.parseInt(sub[2]));
    }

    static public void sortTimes(String inputName, String outputName) throws IOException {
        BufferedReader fileIn = new BufferedReader(new FileReader(inputName));
        BufferedWriter fileOut = new BufferedWriter(new FileWriter(outputName));
        String line;
        List<Integer> listSec = new ArrayList<>();
        Map<Integer, String> mapTime = new HashMap<>();
        while ((line = fileIn.readLine()) != null) {
            if (!line.matches("^([0-9][0-9]:[0-9][0-9]:[0-9][0-9]$)")) throw new IOException();
            mapTime.put(toSec(line), line);
            listSec.add(toSec(line));
        }
        int[] nSec = listSec.stream().mapToInt(i -> i).toArray();
        Sorts.quickSort(nSec);
        for (int e : nSec) {
            fileOut.write(mapTime.get(e) + "\n");
        }
        fileOut.close();

    }

    /**
     * Сортировка адресов
     * <p>
     * Средняя
     * <p>
     * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
     * где они прописаны. Пример:
     * <p>
     * Петров Иван - Железнодорожная 3
     * Сидоров Петр - Садовая 5
     * Иванов Алексей - Железнодорожная 7
     * Сидорова Мария - Садовая 5
     * Иванов Михаил - Железнодорожная 7
     * <p>
     * Людей в городе может быть до миллиона.
     * <p>
     * Вывести записи в выходной файл outputName,
     * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
     * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
     * <p>
     * Железнодорожная 3 - Петров Иван
     * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
     * Садовая 5 - Сидоров Петр, Сидорова Мария
     * <p>
     * В случае обнаружения неверного формата файла бросить любое исключение.
     * <p>
     * Трудоемкость алгоритма - O(NlogN)
     * Ресурсоемкость - O(N)
     */
    static public void sortAddresses(String inputName, String outputName) throws IOException {
        BufferedReader fileIn = new BufferedReader(new FileReader(inputName));
        BufferedWriter fileOut = new BufferedWriter(new FileWriter(outputName));
        String line;
        Map<String, TreeSet<String>> mapAdrs = new TreeMap<>();
        while ((line = fileIn.readLine()) != null) {
            String[] sub = line.split(" - ");
            String name = sub[0];
            String adr = sub[1];
            if (!mapAdrs.containsKey(adr)) {
                TreeSet<String> setName = new TreeSet<>();
                setName.add(name);
                mapAdrs.put(adr, setName);
            } else {
                mapAdrs.get(adr).add(name);
            }
        }
        for (String key : mapAdrs.keySet()) {
            TreeSet<String> nameSet = mapAdrs.get(key);
            String[] nameList = mapAdrs.get(key).toArray(new String[mapAdrs.get(key).size()]);
            String name = nameList[0];
            if (nameList.length > 1) {
                for (int i = 1; i < nameList.length; i++) name = name + ", " + nameList[i];
            }
            fileOut.write("\n" + key + " - " + name);
        }
        fileOut.close();

    }

    /**
     * Сортировка температур
     * <p>
     * Средняя
     * (Модифицированная задача с сайта acmp.ru)
     * <p>
     * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
     * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
     * Например:
     * <p>
     * 24.7
     * -12.6
     * 121.3
     * -98.4
     * 99.5
     * -12.6
     * 11.0
     * <p>
     * Количество строк в файле может достигать ста миллионов.
     * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
     * Повторяющиеся строки сохранить. Например:
     * <p>
     * -98.4
     * -12.6
     * -12.6
     * 11.0
     * 24.7
     * 99.5
     * 121.3
     * <p>
     * Трудоемкость алгоритма - O(N+k)
     * Ресурсоемкость - O(N)
     */

    static public void sortTemperatures(String inputName, String outputName) throws IOException {
        BufferedReader fileIn = new BufferedReader(new FileReader(inputName));
        BufferedWriter fileOut = new BufferedWriter(new FileWriter(outputName));
        ArrayList<Integer> list = new ArrayList<>();
        String tpt;
        while ((tpt = fileIn.readLine()) != null) {
            int d = (int) (Double.parseDouble(tpt) * 10) + 2730;
            list.add(d);
        }
        int[] indexTemp = new int[7731];
        for (int i = 0; i < list.size(); i++) {
            indexTemp[list.get(i)]++;
        }
        for (int i = 0; i < 7730; i++) {
            indexTemp[i + 1] += indexTemp[i];
        }
        int[] sortTemp = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            int number = list.get(i);
            int index = indexTemp[list.get(i)];
            sortTemp[index - 1] = number;
            indexTemp[list.get(i)]--;
        }
        for (int a : sortTemp) {
            double b = (double) (a - 2730) / 10;
            fileOut.write(String.valueOf(b) + "\n");
        }
        fileOut.close();
    }


    /**
     * Сортировка последовательности
     * <p>
     * Средняя
     * (Задача взята с сайта acmp.ru)
     * <p>
     * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
     * <p>
     * 1
     * 2
     * 3
     * 2
     * 3
     * 1
     * 2
     * <p>
     * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
     * а если таких чисел несколько, то найти минимальное из них,
     * и после этого переместить все такие числа в конец заданной последовательности.
     * Порядок расположения остальных чисел должен остаться без изменения.
     * <p>
     * 1
     * 3
     * 3
     * 1
     * 2
     * 2
     * 2
     */
    static public void sortSequence(String inputName, String outputName) {
        throw new NotImplementedError();
    }

    /**
     * Соединить два отсортированных массива в один
     * <p>
     * Простая
     * <p>
     * Задан отсортированный массив first и второй массив second,
     * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
     * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
     * <p>
     * first = [4 9 15 20 28]
     * second = [null null null null null 1 3 9 13 18 23]
     * <p>
     * Результат: second = [1 3 4 9 9 13 15 20 23 28]
     */
    static <T extends Comparable<T>> void mergeArrays(T[] first, T[] second) {
        throw new NotImplementedError();
    }
}
