package sp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SecondPartTasks {

    private SecondPartTasks() {}

    // Найти строки из переданных файлов, в которых встречается указанная подстрока.
    public static List<String> findQuotes(List<String> paths, CharSequence sequence) {
        try {
            return paths.stream().flatMap(path -> {
                try {
                    return Files.lines(Paths.get(path));
                } catch (IOException e) {
                    throw new RuntimeException("Can't open " + path, e);
                }
            }).filter(s -> s.contains(sequence)).collect(Collectors.toList());
        } catch (RuntimeException e) {
            e.printStackTrace();
            return null;
        }
    }

    // В квадрат с длиной стороны 1 вписана мишень.
    // Стрелок атакует мишень и каждый раз попадает в произвольную точку квадрата.
    // Надо промоделировать этот процесс с помощью класса java.util.Random и посчитать,
    // какова вероятность попасть в мишень.
    public static double piDividedBy4() {
        final long COUNT = 100000;
        final double radius = 0.5;
        Random random = new Random();
        long pos = Stream.generate(() ->
                Math.pow(random.nextDouble() - radius, 2) + Math.pow(random.nextDouble() - radius, 2) < Math.pow(radius, 2)).
                limit(COUNT).filter(s -> s).count();
        return (double) pos / COUNT;
    }

    // Дано отображение из имени автора в список с содержанием его произведений.
    // Надо вычислить, чья общая длина произведений наибольшая.
    public static String findPrinter(Map<String, List<String>> compositions) {
        return compositions.entrySet().stream().max(
                Comparator.comparing(
                        e -> e.getValue().stream().mapToInt(String::length).sum()
                )
        ).get().getKey();
    }

    // Вы крупный поставщик продуктов. Каждая торговая сеть делает вам заказ в виде Map<Товар, Количество>.
    // Необходимо вычислить, какой товар и в каком количестве надо поставить.
    public static Map<String, Integer> calculateGlobalOrder(List<Map<String, Integer>> orders) {
        return orders.stream().flatMap(o -> o.entrySet().stream()).
                collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a + b
                ));
    }
}
