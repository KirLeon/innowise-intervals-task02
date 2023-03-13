package com.innowise;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class Intervals {

  public static final List<String> noteList = new ArrayList<>(7);

  //array of intervals counting from the note C to the other notes
  public static final int[] semitonePositions = {0, 2, 4, 5, 7, 9, 11};


  static {
    Stream<String> streamOfNotes = Stream.of("C", "D", "E", "F", "G", "A", "B");
    streamOfNotes.forEach(noteList::add);
  }


  public static String intervalConstruction(String[] args) {

    validatorIntervalConstructionInput(args);

    String interval = args[0];
    String note = args[1];

    boolean descendMode = (args.length == 3 && Objects.equals(args[2], "dsc"));
    int requiredDegrees = Character.digit(interval.charAt(1), 10);

    int beginningNoteNumber = noteList.indexOf(note.substring(0, 1));
    int resultNoteNumber = countResultNoteNumber(beginningNoteNumber, descendMode, requiredDegrees);

    String resultNote = noteList.get(resultNoteNumber);

    //String firstNote, String secondNote, boolean descendMode
    int requiredSemitones = countSemitonesInInterval(interval);
    int realSemitones = countRealSemitones(note, resultNote, descendMode);

    int semitoneAmount = realSemitones - requiredSemitones;

    String[] accidentalList = new String[]{"##", "#", "", "b", "bb"};

    //going upside down accidentalList in case the mode is "dsc"
    return descendMode ? resultNote + accidentalList[2 - semitoneAmount]
        : resultNote + accidentalList[semitoneAmount + 2];
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
    return (interval.charAt(0) == 'm') ? 12 - semitonePositions[7 - (degrees - 1)]
        : semitonePositions[degrees - 1];
  }

  //String firstNote, String secondNote, boolean descendMode
  public static int countRealSemitones(String firstNote, String secondNote, boolean descendMode) {

    int firstNoteNumber = noteList.indexOf(firstNote.substring(0, 1));

    int secondNoteNumber = noteList.indexOf(secondNote.substring(0, 1));

    int semitonesByFirstNote;
    int semitonesBySecondNote;

    semitonesByFirstNote = countAdditionalSemitones(firstNote, descendMode);
    semitonesBySecondNote = countAdditionalSemitones(secondNote, descendMode) * -1;

    int realSemitones;

    //case we can just find straight distance between 2 notes (Second <- First) || (First -> Second)
    if ((descendMode && firstNoteNumber > secondNoteNumber) || (!descendMode &&
        firstNoteNumber < secondNoteNumber)) {

      realSemitones = Math.abs(
          semitonePositions[firstNoteNumber] - semitonePositions[secondNoteNumber]);
    } else {

      //we can replace descending from the lower (L) note to the higher one (H) to ascending from H to L
      if (firstNoteNumber < secondNoteNumber) {
        int tempNoteIndex = firstNoteNumber;
        firstNoteNumber = secondNoteNumber;
        secondNoteNumber = tempNoteIndex;
      }

      realSemitones = 12 - semitonePositions[firstNoteNumber] + semitonePositions[secondNoteNumber];
    }
    return realSemitones + semitonesByFirstNote + semitonesBySecondNote;
  }


  public static int countAdditionalSemitones(String note, boolean descendMode) {
    if (note == null) {
      return 0;
    }
    int noteLength = note.length();
    int additionalSemitones = 0;

    //counting additional semitones (if accidentals are present)
    if (noteLength >= 2) {
      List<String> accidentalList = new ArrayList<>();

      Stream<String> accidentalsStream = Stream.of("##", "#", "", "b", "bb");
      accidentalsStream.forEach(accidentalList::add);

      //by default (asc mode), ## means we have 2 extra notes, and bb means we need 2 more
      additionalSemitones = accidentalList.indexOf(note.substring(1, noteLength)) - 2;

      //descending mode inverts this numbers vice versa
      if (descendMode) {
        additionalSemitones *= -1;
      }
    }
    return additionalSemitones;
  }


  public static String identifyIntervalByDegreeAndSemitone(int degree, int semitones) {

    //case degree is 4, 5, or 8 we can instantly return "P"
    if ((degree % 4 == 0 || degree % 4 == 1)) {
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


  public static int countDegree(String firstNote, String secondNote, boolean descendMode) {

    int firstNoteNumber = noteList.indexOf(firstNote.substring(0, 1));
    int secondNoteNumber = noteList.indexOf(secondNote.substring(0, 1));

    if (firstNoteNumber == secondNoteNumber) {
      return 8;
    }

    //case we can just find straight distance between 2 notes
    if ((descendMode && firstNoteNumber > secondNoteNumber) || (!descendMode &&
        firstNoteNumber < secondNoteNumber)) {
      return Math.abs(secondNoteNumber - firstNoteNumber) + 1;
    }

    //we can replace descending from the lower (L) note to the higher one (H) to ascending from H to L
    if (firstNoteNumber < secondNoteNumber) {
      int tempNoteIndex = firstNoteNumber;
      firstNoteNumber = secondNoteNumber;
      secondNoteNumber = tempNoteIndex;
    }
    //going from the first note to the last one (B) and then to the second one
    return 7 - firstNoteNumber + secondNoteNumber + 1;
  }

  public static boolean validatorIntervalConstructionInput(String[] notes) {

    String exceptionMessage = "Cannot identify the interval";

    //checking correct size
    if (notes.length < 2 || notes.length > 3) {
      throw new RuntimeException(exceptionMessage);
    }

    //checking necessary presence of correct interval
    Stream<String> intervalsStream = Stream.of("m2", "M2", "m3", "M3", "P4", "P5", "m6", "M6", "m7",
        "M7", "P8");
    int intervalMatch = (int) intervalsStream
        .filter(interval -> Objects.equals(interval, notes[0]))
        .count();

    if (intervalMatch < 1) {
      throw new RuntimeException(exceptionMessage);
    }

    //counting input note matches with all the possible one (1 required)
    int noteMatchAmount = countNoteMatches(notes[1]);

    if (noteMatchAmount < 1) {
      throw new RuntimeException(exceptionMessage);
    }

    if (notes.length == 3 &&
        !(Objects.equals(notes[2], "asc") || Objects.equals(notes[2], "dsc"))
    ) {
      throw new RuntimeException(exceptionMessage);
    }

    return true;
  }

  public static boolean validateIntervalIdentificationInput(String[] notes) {

    String exceptionMessage = "Cannot identify the interval";

    //checking correct size
    if (notes.length < 2 || notes.length > 3) {
      throw new RuntimeException(exceptionMessage);
    }

    //counting input note matches with all the possible one (2 required)
    int noteMatchAmount = countNoteMatches(notes[0], notes[1]);

    if (noteMatchAmount != 2) {
      throw new RuntimeException(exceptionMessage);
    }

    //checking counting mode "asc/dsc" in case that input has more than 2 attributes
    if (notes.length == 3 &&
        !(Objects.equals(notes[2], "asc") || Objects.equals(notes[2], "dsc"))
    ) {
      throw new RuntimeException(exceptionMessage);
    }

    return true;
  }

  public static int countNoteMatches(String... notes) {

    //using atomic var to count amount of matches input notes with all the possible one
    AtomicLong noteMatchAmount = new AtomicLong();

    noteList.forEach(note -> {

      Stream<String> streamOfAccidentals = Stream.of("bb", "b", "", "#", "##");

      noteMatchAmount.addAndGet(streamOfAccidentals

          //filter throw the input notes in the search of match through all possible notes
          .filter(accidental -> {
                for (String currentInputNote : notes) {
                  if (Objects.equals(note + accidental, currentInputNote)) {
                    return Objects.equals(note + accidental, currentInputNote);
                  }
                }
                return false;
              }
          )
          .count());
    });

    return (int) noteMatchAmount.get();
  }
}