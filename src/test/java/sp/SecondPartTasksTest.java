package sp;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

import static sp.SecondPartTasks.*;


public class SecondPartTasksTest {

    @Test
    public void testFindQuotes() {
        List<String> strings = Arrays.asList("Hello from the other side", "Hello from the outside",
                "Hello, can you hear me?", "Hello, it's me", "helloHellohello", "Hello, world!");
        String[] files = {"src/test/resources/test1.txt", "src/test/resources/test2.txt"};
        strings.sort(Comparator.<String>naturalOrder());
        List<String> ans = findQuotes(Arrays.asList(files), "Hello");
        ans.sort(Comparator.<String>naturalOrder());
        assertEquals(strings, ans);
        assertEquals(null, findPrinter(Collections.emptyMap()));
    }

    @Test
    public void testPiDividedBy4() {
        assertEquals(piDividedBy4(), Math.PI / 4, 0.005);
    }

    @Test
    public void testFindPrinter() {
        List<String> eminems = Arrays.asList(
                "Look, I was gonna go easy on you not to hurt your feelings",
                "But I'm only going to get this one chance",
                "Something's wrong, I can feel it",
                "Just a feeling I've got"
        );

        List<String> rihannas = Arrays.asList(
                "Shine bright like a diamond",
                "Find light in the beautiful sea",
                "I choose to be happy",
                "You and I, you and I",
                "We’re like diamonds in the sky"
        );

        List<String> lordes = Arrays.asList(
                "The image of me that you see is distorted, twisted, broken, fractured, isolated - miles out to sea.",
                "I don't want a separate place.",
                "I need to feel safe, not thrown away, away, away, away.",
                "And I will push, push, to tear down the walls.",
                "Of this box you put me in.",
                "Because you don't understand."
        );

        Map<String, List<String>> map = new HashMap<>();

        map.put("Eminem", eminems);
        map.put("Rihanna", rihannas);
        assertEquals("Eminem", findPrinter(map));

        map.put("Lorde", lordes);
        assertEquals("Lorde", findPrinter(map));
    }

    @Test
    public void testCalculateGlobalOrder() {
        Map<String, Integer> alco = new HashMap<>();
        alco.put("vine", 10);
        alco.put("beer", 100);
        alco.put("tequila", 17);
        Map<String, Integer> rusAl = new HashMap<>();
        rusAl.put("liqueur", 55);
        rusAl.put("beer", 114);
        rusAl.put("vine", 89);

        Map<String, Integer> order = new HashMap<>();
        order.put("vine", 89 + 10);
        order.put("beer", 114 + 100);
        order.put("liqueur", 55);
        order.put("tequila", 17);

        Map<String, Integer> answer = calculateGlobalOrder(Arrays.asList(alco, rusAl));
        assertEquals(Collections.emptyMap(), calculateGlobalOrder(Collections.emptyList()));
        assertEquals(order, answer);
    }
}