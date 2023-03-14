import com.innowise.Intervals;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class IntervalIdentificationTest {

  public static List<String[]> INPUT_ARRAY;


  @BeforeAll
  public static void setup() {

    INPUT_ARRAY = List.of(
        new String[]{"C", "D"},
        new String[]{"B", "F#", "asc"},
        new String[]{"Fb", "Gbb"},
        new String[]{"G", "F#", "asc"},
        new String[]{"Bb", "A", "dsc"},
        new String[]{"Cb", "Abb", "dsc"},
        new String[]{"G#", "D#", "dsc"},
        new String[]{"E", "B", "dsc"},
        new String[]{"E#", "D#", "dsc"},
        new String[]{"B", "G#", "dsc"});
  }


  @Test
  public void checkInputExceptions() {
    Stream<String[]> wrongInput = Stream.of(
        new String[]{"Вb", "C", "as"},
        new String[]{"D", "B", null},
        new String[]{"F#", "Bb b", "dsc"},
        new String[]{"#G", "Cb", "dsc"},
        new String[]{null, "G#"},
        new String[]{"m2", null, "asc"},
        new String[]{"#", "E#", ""},
        new String[]{"P4", "Ebb", "dsc"},
        new String[]{"m2", "D#", null},
        new String[]{"P8", "Gb", "asc"});

    wrongInput.forEach(input -> Assertions.assertThrows(
        RuntimeException.class,
        () -> Intervals.intervalIdentification(input),
        "Cannot identify the array"
    ));
  }


  @Test
  public void intervalIdentificationTest() {

    //EXPECTED
    List<String> expectedOutput = Stream.of("M2", "P5", "m2", "M7", "m2", "M3", "P4", "P4",
        "M2", "m3").toList();

    //ACTUAL
    List<String> actualOutput = INPUT_ARRAY.stream().map(Intervals::intervalIdentification)
        .toList();

    //ASSERTION
    Assertions.assertEquals(expectedOutput, actualOutput);
  }


  @Test
  public void identifyIntervalByDegreeAndSemitoneTest() {

    //INPUT
    List<Integer> inputSemitones = Stream.of(1, 2, 3, 4, 5, 7, 8, 9, 10, 11, 12).toList();
    List<Integer> inputDegrees = Stream.of(2, 2, 3, 3, 4, 5, 6, 6, 7, 7, 8).toList();
    List<String> expectedIntervals = Stream.of("m2", "M2", "m3", "M3", "P4", "P5", "m6", "M6", "m7",
        "M7", "P8").toList();

    //ACTUAL
    List<String> actualIntervals = IntStream.range(0, inputSemitones.size())
        .mapToObj(i -> Intervals.identifyIntervalByDegreeAndSemitone(inputDegrees.get(i),
            inputSemitones.get(i)
        ))
        .toList();

    //ASSERTION
    Assertions.assertEquals(expectedIntervals, actualIntervals);
  }


  @Test
  public void countRealSemitonesTest() {
    //INPUT
    List<String> notes = Stream.of("Cb", "Bbb", "Abb", "E##", "E#", "Bb", "D#", "G#").toList();
    List<String> secondNotes = Stream.of("Abb", "Eb", "B#", "B", "C", "A", "F#", "Db").toList();
    List<Boolean> descModes = Stream.of(true, true, false, false, false, false, true,
        false).toList();

    //EXPECTED
    List<Integer> expectedAdditionalSemitones = Stream.of(4, 6, 5, 5, 7, 11, 9, 5)
        .collect(Collectors.toList());

    //ACTUAL
    List<Integer> actualAdditionalSemitones = IntStream.range(0, expectedAdditionalSemitones.size())
        .map(i -> Intervals.countRealSemitones(notes.get(i), secondNotes.get(i), descModes.get(i)))
        .boxed()
        .collect(Collectors.toList());

    //ASSERTION
    Assertions.assertEquals(expectedAdditionalSemitones, actualAdditionalSemitones);
  }


  @Test
  public void countDegreeTest() {

    //INPUT
    List<String> notes = Arrays.asList("D", "F", "F", "G", "D", "B", "A", "G");
    List<String> secondNotes = Arrays.asList("A", "E", "B", "B", "D", "A", "E", "D");
    List<Boolean> descModes = Arrays.asList(false, true, false, true, true, false, true, false);

    //EXPECTED
    List<Integer> expectedAdditionalDegrees = Stream.of(5, 2, 4, 6, 8, 7, 4, 5).toList();

    //ACTUAL
    List<Integer> actualAdditionalDegrees = IntStream.range(0, notes.size())
        .mapToObj(i -> Intervals.countDegree(notes.get(i), secondNotes.get(i), descModes.get(i)))
        .collect(Collectors.toList());

    //ASSERTION
    Assertions.assertEquals(expectedAdditionalDegrees, actualAdditionalDegrees);
  }
}
