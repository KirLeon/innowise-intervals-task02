import com.innowise.Intervals;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class IntervalConstructionTest {


  public static List<String[]> INPUT_ARRAY;


  @BeforeAll
  public static void setup() {

    INPUT_ARRAY = List.of(
        new String[]{"M2", "C", "asc"},
        new String[]{"P5", "B", "asc"},
        new String[]{"m2", "Bb", "dsc"},
        new String[]{"M3", "Cb", "dsc"},
        new String[]{"P4", "G#", "dsc"},
        new String[]{"m3", "B", "dsc"},
        new String[]{"m2", "Fb", "asc"},
        new String[]{"M2", "E#", "dsc"},
        new String[]{"P4", "E", "dsc"},
        new String[]{"m2", "D#", "asc"},
        new String[]{"M7", "G", "asc"});
  }


  @Test
  public void checkInputExceptions() {

    //INPUT
    List<String[]> wrongInput = List.of(
        new String[]{"M8", "C", "asc"},
        new String[]{"m2", "B", null},
        new String[]{"p5", "Bb", "dsc"},
        new String[]{"", "Cb", "dsc"},
        new String[]{null, "G#", "dsc"},
        new String[]{"m2", null, "asc"},
        new String[]{"M2"},
        new String[]{"P4", "E###", "dsc"},
        new String[]{"m2", "D#", null},
        new String[]{"M7", "Gb", "as"});

    //EXPECTED
    List<String> expectedErrors = List.of(
        "Cannot identify the interval",
        "Illegal number of elements in input array",
        "Cannot identify the interval",
        "Cannot identify the interval",
        "Cannot identify the interval",
        "Cannot identify the note",
        "Illegal number of elements in input array",
        "Cannot identify the note",
        "Illegal number of elements in input array",
        "Illegal number of elements in input array");

    //ACTUAL
    List<String> actualErrors = wrongInput.stream()
        .map(input -> Assertions.assertThrows(RuntimeException.class,
            () -> Intervals.intervalConstruction(input)).getMessage())
        .toList();

    //ASSERTION
    Assertions.assertEquals(expectedErrors, actualErrors);
  }


  @Test
  public void intervalConstructionTest() {

    //EXPECTED
    List<String> expectedOutput = List.of("D", "F#", "A", "Abb", "D#", "G#", "Gbb", "D#",
        "B", "E", "F#");

    //ACTUAL
    List<String> actualOutput = INPUT_ARRAY.stream().map(Intervals::intervalConstruction).toList();

    //ASSERTION
    Assertions.assertEquals(expectedOutput, actualOutput);
  }


  @Test
  public void countResultNoteNumberTest() {

    //INPUT
    List<Integer> beginningNoteNumbers = List.of(0, 6, 2, 1, 5, 3, 4, 6);
    List<Boolean> descendingModes = List.of(true, false, false, true, false, true, true, false);
    List<Integer> degrees = List.of(3, 6, 8, 4, 7, 5, 2, 2);

    //EXPECTED
    List<Integer> expectedNoteNumbers = List.of(5, 4, 2, 5, 4, 6, 3, 0);

    //ACTUAL
    List<Integer> actualNoteNumbers = IntStream.range(0, beginningNoteNumbers.size()).mapToObj(
        i -> Intervals.countResultNoteNumber(beginningNoteNumbers.get(i), descendingModes.get(i),
            degrees.get(i))
    ).toList();

    //ASSERTION
    Assertions.assertEquals(expectedNoteNumbers, actualNoteNumbers);
  }


  @Test
  public void countRealSemitonesTest() {

    //INPUT
    List<String> notes = List.of("Cb", "Bbb", "Abb", "E##", "E#", "Bb", "D", "G##");
    List<String> secondNotes = List.of("A", "E", "B", "B", "C", "A", "F", "D");
    List<Boolean> descModes = List.of(true, true, false, false, false, false, true,
        false);

    //EXPECTED
    List<Integer> expectedAdditionalSemitones = List.of(2, 5, 4, 5, 7, 11, 9, 5);

    //ACTUAL
    List<Integer> actualAdditionalSemitones = IntStream.range(0, expectedAdditionalSemitones.size())
        .mapToObj(
            i -> Intervals.countRealSemitones(notes.get(i), secondNotes.get(i), descModes.get(i)))
        .toList();

    //ASSERTION
    Assertions.assertEquals(expectedAdditionalSemitones, actualAdditionalSemitones);
  }


  @Test
  public void countSemitonesInIntervalTest() {

    //INPUT
    List<String> intervals = List.of("m2", "M2", "m3", "M3", "P4", "P5", "m6", "M6", "m7",
        "M7", "P8");

    //EXPECTED
    List<Integer> expectedSemitones = List.of(1, 2, 3, 4, 5, 7, 8, 9, 10, 11, 12);

    //ACTUAL
    List<Integer> actualSemitones = intervals.stream()
        .map(Intervals::countSemitonesInInterval)
        .toList();

    //ASSERTION
    Assertions.assertEquals(expectedSemitones, actualSemitones);
  }


}
