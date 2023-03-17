# intervals-task

The application designed in order to help musicians with interval identification and constructing. This app has 2 modes: intervalIdentification, where you shoulp enter 2 notes and the ascending mode (asc/dsc are only possible. Leave empty to make it ascending by default) and the output is interval of them; and the second one is intervalConstruction, where you first enter the interval name (m2, M2, m3, M3, P4, P5, m6, M6, m7, M7, P8 are only possible), then you should enter the beginning note name (example: C##, B, Ab, F#, D, Ebb) and the last one is ascending mode (asc/dsc are only possible. Leave empty to make it ascending by default), the output is the result note. 
!Remember: the array consists only of 2 or 3 elements. If it has 3 of them, the last one should only be 'asc' or 'dsc'. Not empty or null!
Example: intervalConstruction (input -> new String[]{"Bbb", "F#", "dsc"}; output -> "P4"); intervalIdentification (input -> new String[]{"M6", "D#"}; output -> "B#")

Note: the task is in the beta version, so to ise it by yourself you should use the following instruction:
1. Download and installate the IntellijIDEA or any other IDE for Java.
2. Build the project (Ctrl + F9 for IntellijIDEA)
3. Go to folder: src/test/java and fing MainTest.java

![fea6b5b1-4c56-4cd8-b1b3-b5f499eaedfa](https://user-images.githubusercontent.com/113788413/225849530-d44fb4c7-c8b0-4cfd-b278-41548b6f32f8.jpg)

4. There you can see few test annotated with @Test. Feel free to write your own tests: just write "public void", then any name, starting with letter, then "(){}". Between the curly braces create new input with: String[] "input_any_string_name_here" = new String[]{"A", "B", "C"}; change A, B and C here to notes and intervals that you need. Then write ; at the end of the line.
5. Go to the next line and write System.out.println(); In this braces you only need to write Intervals.intervalsConstruction() or Intervals.intervalIdentification(), and put you string_name (1 line higher) to this braces;
6. Check that your method is placing within the curly braces of the class MainTest and it is annotated with @Test.
7. Example: 
...
@Test
public void abc(){
    String[] notes = new String[]{"Ab", "G##"};
    System.out.println(Intervals.intervalIdentification(notes));
  }
...

![efde40cf-0a54-4f04-925a-191dae6718f6](https://user-images.githubusercontent.com/113788413/225849065-f012ffb5-c80b-4c7c-935e-d4cd34e3285f.jpg)

My name is Kirill Levchenko, and I am young Java Software Engineer from Minsk, Belarus. At the moment I take part at internship at Innowise Group.
During internship my responsibility is to grow up as the specialist, develop my soft and hard skills, communication and teamwork.
This repository is destinated especially to keep my tasks, so you can probably find something interesting and useful here.
For any questions, please, contact me: kirleon.dev@gmail.com or https://www.linkedin.com/in/kirleon/

