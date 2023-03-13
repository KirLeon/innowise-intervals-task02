import com.innowise.Intervals;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class IntervalConstructionTest {

  public static List<String[]> inputArray;

  @BeforeAll
  public static void setup() {

    inputArray = new ArrayList<>();
    Stream<String[]> inputArrayStream = Stream.of(
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

    inputArrayStream.forEach(inputArray::add);
  }

  @Test
  public void checkInputExceptions() {
    Stream<String[]> wrongInput = Stream.of(
        new String[]{"M8", "C", "asc"},
        new String[]{"P3", "B", null},
        new String[]{"p5", "Bb", "dsc"},
        new String[]{"", "Cb", "dsc"},
        new String[]{null, "G#", "dsc"},
        new String[]{"m2", null, "asc"},
        new String[]{"M2", "E#", ""},
        new String[]{"P4", "E###", "dsc"},
        new String[]{"m2", "D#", null},
        new String[]{"M7", "Gb", "as"});

    wrongInput.forEach(input -> Assertions.assertThrows(
        RuntimeException.class,
        () -> Intervals.intervalConstruction(input),
        "Cannot identify the array"
    ));
  }

  @Test
  public void countResultNoteNumberTest() {

    List<Integer> actualNoteNumbers = new ArrayList<>();
    List<Integer> expectedNoteNumbers = new ArrayList<>();

    List<Integer> beginningNoteNumbers = new ArrayList<>();
    List<Boolean> descendingModes = new ArrayList<>();
    List<Integer> degrees = new ArrayList<>();

    //EXPECTED
    Stream<Integer> expectedNoteNumbersStream = Stream.of(5, 4, 2, 5, 4, 6, 3, 0);
    expectedNoteNumbersStream.forEach(expectedNoteNumbers::add);

    //INPUT
    Stream<Integer> beginningNoteNumbersStream = Stream.of(0, 6, 2, 1, 5, 3, 4, 6);
    Stream<Boolean> descendingModesStream = Stream.of(true, false, false, true, false, true, true,
        false);
    Stream<Integer> degreesStream = Stream.of(3, 6, 8, 4, 7, 5, 2, 2);

    beginningNoteNumbersStream.forEach(beginningNoteNumbers::add);
    descendingModesStream.forEach(descendingModes::add);
    degreesStream.forEach(degrees::add);

    IntStream iteratorStream = IntStream.range(0, expectedNoteNumbers.size());

    //ACTUAL
    iteratorStream.forEach(i -> {
      int currentNoteNumber = Intervals.countResultNoteNumber(beginningNoteNumbers.get(i),
          descendingModes.get(i), degrees.get(i));
      actualNoteNumbers.add(currentNoteNumber);
    });

    Assertions.assertEquals(expectedNoteNumbers, actualNoteNumbers);
  }

  @Test
  public void countSemitonesInIntervalTest() {

    List<Integer> actualSemitones = new ArrayList<>();
    List<Integer> expectedSemitones = new ArrayList<>();

    //EXPECTED
    Stream<Integer> semitonesStream = Stream.of(1, 2, 3, 4, 5, 7, 8, 9, 10, 11, 12);
    semitonesStream.forEach(expectedSemitones::add);

    //INPUT
    Stream<String> intervalsStream = Stream.of("m2", "M2", "m3", "M3", "P4", "P5", "m6", "M6", "m7",
        "M7", "P8");

    //ACTUAL
    intervalsStream.forEach(
        interval -> actualSemitones.add(Intervals.countSemitonesInInterval(interval)));

    Assertions.assertEquals(expectedSemitones, actualSemitones);
  }

  @Test
  public void countRealSemitonesTest() {

    List<Integer> expectedAdditionalSemitones = new ArrayList<>();
    List<Integer> actualAdditionalSemitones = new ArrayList<>();

    List<String> notes = new ArrayList<>();
    List<String> secondNotes = new ArrayList<>();
    List<Boolean> descModes = new ArrayList<>();

    //EXPECTED
    Stream<Integer> expectedAdditionalSemitonesStream = Stream.of(2, 5, 4, 5, 7, 11, 9, 5);
    expectedAdditionalSemitonesStream.forEach(expectedAdditionalSemitones::add);
    IntStream iteratorStream = IntStream.range(0, expectedAdditionalSemitones.size());

    //INPUT
    Stream<String> secondNoteStream = Stream.of("A", "E", "B", "B", "C", "A", "F", "D");
    Stream<String> notesStream = Stream.of("Cb", "Bbb", "Abb", "E##", "E#", "Bb", "D", "G##");
    Stream<Boolean> descModesStream = Stream.of(true, true, false, false, false, false, true,
        false);

    secondNoteStream.forEach(secondNotes::add);
    notesStream.forEach(notes::add);
    descModesStream.forEach(descModes::add);

    //ACTUAL
    iteratorStream.forEach(i -> {
      int currentSemitone = Intervals.countRealSemitones(notes.get(i),
          secondNotes.get(i), descModes.get(i));
      actualAdditionalSemitones.add(currentSemitone);
    });

    Assertions.assertEquals(expectedAdditionalSemitones, actualAdditionalSemitones);

  }

  @Test
  public void intervalConstructionTest() {

    List<String> expectedOutput = new ArrayList<>();
    List<String> actualOutput = new ArrayList<>();

    //EXPECTED
    Stream<String> expectedOutputStream = Stream.of("D", "F#", "A", "Abb", "D#", "G#", "Gbb", "D#",
        "B", "E", "F#");
    expectedOutputStream.forEach(expectedOutput::add);

    //ACTUAL
    inputArray.forEach(inputData -> {
      String currentOutput = Intervals.intervalConstruction(inputData);
      actualOutput.add(currentOutput);
    });

    Assertions.assertEquals(expectedOutput, actualOutput);
  }
}
