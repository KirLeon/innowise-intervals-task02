import com.innowise.Intervals;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class IntervalIdentificationTest {

  public static List<String[]> inputArray;

  @BeforeAll
  public static void setup() {

    inputArray = new ArrayList<>();

    Stream<String[]> inputArrayStream = Stream.of(
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

    inputArrayStream.forEach(inputArray::add);
  }

  @Test
  public void intervalIdentificationTest() {

    List<String> expectedOutput = new ArrayList<>();
    List<String> actualOutput = new ArrayList<>();

    //EXPECTED
    Stream<String> expectedOutputStream = Stream.of("M2", "P5", "m2", "M7", "m2", "M3", "P4", "P4",
        "M2", "m3");
    expectedOutputStream.forEach(expectedOutput::add);

    //ACTUAL
    inputArray.forEach(inputData -> {
      String currentOutput = Intervals.intervalIdentification(inputData);
      actualOutput.add(currentOutput);
    });

    Assertions.assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void countRealSemitonesTest() {
    List<Integer> expectedAdditionalSemitones = new ArrayList<>();
    List<Integer> actualAdditionalSemitones = new ArrayList<>();

    List<String> notes = new ArrayList<>();
    List<String> secondNotes = new ArrayList<>();
    List<Boolean> descModes = new ArrayList<>();

    //EXPECTED
    Stream<Integer> expectedAdditionalSemitonesStream = Stream.of(4, 6, 5, 5, 7, 11, 9, 5);
    expectedAdditionalSemitonesStream.forEach(expectedAdditionalSemitones::add);
    IntStream iteratorStream = IntStream.range(0, expectedAdditionalSemitones.size());

    //INPUT
    Stream<String> notesStream = Stream.of("Cb", "Bbb", "Abb", "E##", "E#", "Bb", "D#", "G#");
    Stream<String> secondNoteStream = Stream.of("Abb", "Eb", "B#", "B", "C", "A", "F#", "Db");
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
  public void countDegreeTest() {

    List<Integer> expectedAdditionalDegrees = new ArrayList<>();
    List<Integer> actualAdditionalDegrees = new ArrayList<>();

    List<String> notes = new ArrayList<>();
    List<String> secondNotes = new ArrayList<>();
    List<Boolean> descModes = new ArrayList<>();

    //EXPECTED
    Stream<Integer> expectedAdditionalDegreesStream = Stream.of(5, 2, 4, 6, 8, 7, 4, 5);
    expectedAdditionalDegreesStream.forEach(expectedAdditionalDegrees::add);

    IntStream iteratorStream = IntStream.range(0, expectedAdditionalDegrees.size());

    //INPUT
    Stream<String> notesStream = Stream.of("D", "F", "F", "G", "D", "B", "A", "G");
    Stream<String> secondNoteStream = Stream.of("A", "E", "B", "B", "D", "A", "E", "D");
    Stream<Boolean> descModesStream = Stream.of(false, true, false, true, true, false, true,
        false);

    notesStream.forEach(notes::add);
    secondNoteStream.forEach(secondNotes::add);
    descModesStream.forEach(descModes::add);

    //ACTUAL
    iteratorStream.forEach(i -> {
      int currentDegree = Intervals.countDegree(notes.get(i), secondNotes.get(i), descModes.get(i));
      actualAdditionalDegrees.add(currentDegree);
    });

    Assertions.assertEquals(expectedAdditionalDegrees, actualAdditionalDegrees);
  }
}
