import com.innowise.Intervals;
import java.util.ArrayList;
import java.util.List;
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
}
