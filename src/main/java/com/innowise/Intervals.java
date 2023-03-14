package com.innowise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Intervals {

  public static final List<String> NOTE_LIST = new ArrayList<>(7);

  //array of intervals counting from the note C to the other notes
  public static final int[] SEMITONE_POSITIONS = new int[]{0, 2, 4, 5, 7, 9, 11};
  private static final String[] ACCIDENTAL_LIST = new String[]{"##", "#", "", "b", "bb"};


  static {
    //inserting notes
    NOTE_LIST.addAll(Arrays.asList("C", "D", "E", "F", "G", "A", "B"));
  }


  public static String intervalConstruction(String[] args) {

    validatorIntervalConstructionInput(args);

    String interval = args[0];
    String note = args[1];

    boolean descendMode = (args.length == 3 && args[2].equals("dsc"));
    int requiredDegrees = Character.digit(interval.charAt(1), 10);

    int beginningNoteNumber = NOTE_LIST.indexOf(note.substring(0, 1));
    int resultNoteNumber = countResultNoteNumber(beginningNoteNumber, descendMode, requiredDegrees);
    String resultNote = NOTE_LIST.get(resultNoteNumber);

    int requiredSemitones = countSemitonesInInterval(interval);
    int realSemitones = countRealSemitones(note, resultNote, descendMode);
    int semitoneAmount = realSemitones - requiredSemitones;

    //going upside down through accidentalList in case the mode is "dsc"
    return descendMode ? resultNote + ACCIDENTAL_LIST[2 - semitoneAmount]
        : resultNote + ACCIDENTAL_LIST[semitoneAmount + 2];
  }


  public static String intervalIdentification(String[] args) {

    validateIntervalIdentificationInput(args);

    String firstNote = args[0];
    String secondNote = args[1];

    boolean descendMode = (args.length == 3 && Objects.equals(args[2], "dsc"));

    int degreeAmount = countDegree(firstNote, secondNote, descendMode);
    int semitoneAmount = countRealSemitones(firstNote, secondNote, descendMode);

    return identifyIntervalByDegreeAndSemitone(degreeAmount, semitoneAmount);
  }


  public static int countResultNoteNumber(int beginningNoteNumber, boolean descendMode,
      int degree) {

    //case P8 -> full-cycle, returning the beginning note
    if (degree == 8) {
      return beginningNoteNumber;
    }

    if (descendMode) {

      //checking whether the result note lying lower than the beginning one
      return beginningNoteNumber - degree + 1 >= 0 ? beginningNoteNumber - degree + 1
          : 7 - (degree - (beginningNoteNumber + 1));
    } else {

      //checking whether the result mode lying higher than the beginning one
      return beginningNoteNumber + degree - 1 < 7 ? beginningNoteNumber + degree - 1
          : degree - (7 - beginningNoteNumber) - 1;
    }
  }


  public static int countSemitonesInInterval(String interval) {

    int degrees = Character.digit(interval.charAt(1), 10);

    //case the interval is P8 we instantly return 12
    if (degrees == 8) {
      return 12;
    }

    //for m (minor) counting from 12th semitone upside down, for others - from the 1st semitone
    return (interval.charAt(0) == 'm') ? 12 - SEMITONE_POSITIONS[7 - (degrees - 1)]
        : SEMITONE_POSITIONS[degrees - 1];
  }


  public static int countRealSemitones(String firstNote, String secondNote, boolean descendMode) {

    int firstNoteNumber = NOTE_LIST.indexOf(firstNote.substring(0, 1));
    int secondNoteNumber = NOTE_LIST.indexOf(secondNote.substring(0, 1));

    int semitonesByFirstNote = countAdditionalSemitones(firstNote, descendMode);
    int semitonesBySecondNote = -1 * countAdditionalSemitones(secondNote, descendMode);

    int realSemitones;

    //case we can just find straight distance between 2 notes (Second <- First) || (First -> Second)
    if ((descendMode && firstNoteNumber > secondNoteNumber) || (!descendMode &&
        firstNoteNumber < secondNoteNumber)) {

      realSemitones = Math.abs(
          SEMITONE_POSITIONS[firstNoteNumber] - SEMITONE_POSITIONS[secondNoteNumber]);
    } else {

      //replacing descending from the lower (L) note to the higher one (H) to ascending from H to L
      if (firstNoteNumber < secondNoteNumber) {
        int tempNoteIndex = firstNoteNumber;
        firstNoteNumber = secondNoteNumber;
        secondNoteNumber = tempNoteIndex;
      }

      realSemitones =
          12 - SEMITONE_POSITIONS[firstNoteNumber] + SEMITONE_POSITIONS[secondNoteNumber];
    }
    return realSemitones + semitonesByFirstNote + semitonesBySecondNote;
  }


  public static int countAdditionalSemitones(String note, boolean descendMode) {

    //if the note is empty or doesn't contain any accidentals, not any additional semitones required
    if (note == null || note.length() < 2) {
      return 0;
    }

    //by default (asc mode), ## means we have 2 extra notes, and bb means we need 2 more
    int additionalSemitones = Stream.of("##", "#", "", "b", "bb")
        .toList()
        .indexOf(note.substring(1)) - 2;

    //descending mode inverts this numbers vice versa
    return descendMode ? additionalSemitones * -1 : additionalSemitones;
  }


  public static int countDegree(String firstNote, String secondNote, boolean descendMode) {

    int firstNoteNumber = NOTE_LIST.indexOf(firstNote.substring(0, 1));
    int secondNoteNumber = NOTE_LIST.indexOf(secondNote.substring(0, 1));

    if (firstNoteNumber == secondNoteNumber) {
      return 8;
    }

    //checking note positions and descendingMode. Here: firstNote (F), secondNote(S), mode (<- / ->)
    return (!descendMode && firstNoteNumber < secondNoteNumber) || (descendMode
        && firstNoteNumber > secondNoteNumber)

        //case we can just find straight distance between 2 notes (F -> S) or (S <- F)
        ? Math.abs(firstNoteNumber - secondNoteNumber) + 1

        //case we can subtract degrees between this 2 notes from all degrees (->S F->) or (<-F S<-)
        : 7 - Math.abs(firstNoteNumber - secondNoteNumber) + 1;
  }


  public static String identifyIntervalByDegreeAndSemitone(int degree, int semitones) {

    if (degree == 8 && semitones != 12) {
      throw new RuntimeException("Cannot identify the interval");
    }
    //case degree is 4, 5, or 8 we can instantly return "P" (division remainder is 0 or 1)
    if (degree % 4 < 2) {
      return "P" + degree;
    } else {

      int groupId = degree - 2;

      if (groupId < 2) {

        //this formula works for groups with 2 and 3 degrees
        return (groupId * 2 + 1 == semitones) ? "m" + degree : "M" + degree;
      } else {

        //this formula works for groups with 6 and 7 degrees
        return (groupId * 2 == semitones) ? "m" + degree : "M" + degree;
      }
    }
  }


  public static boolean validatorIntervalConstructionInput(String[] notes) {

    //checking correct size
    if (notes.length < 2 || notes.length > 3) {
      throw new RuntimeException("Illegal number of elements in input array");
    }

    //checking necessary presence of correct interval
    int intervalMatch = (int)
        Stream.of("m2", "M2", "m3", "M3", "P4", "P5", "m6", "M6", "m7", "M7", "P8")
            .filter(interval -> Objects.equals(interval, notes[0]))
            .count();

    if (intervalMatch < 1) {
      throw new RuntimeException("Cannot identify the interval");
    }

    //counting input note matches with all the possible one (1 required)
    int noteMatchAmount = countNoteMatches(notes[1]);

    if (noteMatchAmount < 1) {
      throw new RuntimeException("Cannot identify the note");
    }

    if (notes.length == 3 &&
        !(Objects.equals(notes[2], "asc") || Objects.equals(notes[2], "dsc"))
    ) {
      throw new RuntimeException("Illegal number of elements in input array");
    }

    return true;
  }

  public static boolean validateIntervalIdentificationInput(String[] notes) {

    //checking correct size
    if (notes.length < 2 || notes.length > 3) {
      throw new RuntimeException("Illegal number of elements in input array");
    }

    //counting input note matches with all the possible one (2 required)
    int noteMatchAmount = countNoteMatches(notes[0], notes[1]);

    if (noteMatchAmount != 2) {
      throw new RuntimeException("Cannot identify the interval");
    }

    //checking counting mode "asc/dsc" in case that input has more than 2 attributes
    if (notes.length == 3 &&
        !(Objects.equals(notes[2], "asc") || Objects.equals(notes[2], "dsc"))
    ) {
      throw new RuntimeException("Illegal number of elements in input array");
    }

    return true;
  }

  public static int countNoteMatches(String... notes) {

    return (int) NOTE_LIST.stream()
        //counting the number of matches of the input notes with all the possible one
        .flatMap(note -> Arrays.stream(ACCIDENTAL_LIST)
            .map(accidental -> note + accidental))
        .filter(possibleNote -> Arrays.asList(notes)
            .contains(possibleNote))
        .count();
  }
}