package domashka.student;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

public class StudentTest {
    @Test
    @DisplayName("Creating a student")
    public void createStudent(){
        Student stud = new Student("Petya");
        stud.addGrade(2);
        stud.addGrade(5);
        Assertions.assertEquals(stud.getName(), "Petya");
        Assertions.assertEquals(stud.getGrades(), List.of(2,5));
    }

    @RepeatedTest(value = 4, name = "Adding a correct grades")
    public void gradesInRange(RepetitionInfo repetitionInfo){
        Student stud = new Student("Petya");
        stud.addGrade(repetitionInfo.getCurrentRepetition()+1);
        Assertions.assertEquals(stud.getGrades().get(0), repetitionInfo.getCurrentRepetition()+1);
    }

    @ParameterizedTest
    @MethodSource("domashka.student.GradeSource#ints")
    @DisplayName("Adding incorrect grades")
    public void gradesNotInRange(int x) {
        Student stud = new Student("vasia");
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                stud.addGrade(x));
    }

    @ParameterizedTest
    @MethodSource("domashka.student.GradeSource#ints")
    @DisplayName("Checking encapsulation")
    public void gradesNotInRangeEncapsulation(int x) {
        Student stud = new Student("vasia");
        stud.getGrades().add(x);
        Assertions.assertEquals(stud.getGrades(), List.of() );
    }

    @Test
    @DisplayName("Positive test of the equals method")
    public void equalsStudent(){
        Student stud = new Student("Anna");
        stud.addGrade(5);
        stud.addGrade(2);

        Student stud2 = new Student("Anna");
        stud2.addGrade(5);
        stud2.addGrade(2);

        Assertions.assertTrue(stud.equals(stud2));
        Assertions.assertTrue(stud2.equals(stud));
    }

    @Test
    @DisplayName("Negative test of the equals method")
    public void notEqualsStudent(){
        Student stud = new Student("Anna");
        stud.addGrade(5);
        stud.addGrade(2);

        Student stud2 = new Student("Anna");
        stud2.addGrade(3);
        stud2.addGrade(2);

        Student stud3 = new Student("Anton");
        stud2.addGrade(5);
        stud2.addGrade(2);

        Assertions.assertFalse(stud.equals(stud2));
        Assertions.assertFalse(stud.equals(stud3));
    }

    @Test
    @DisplayName("Checking the method toString")
    public void checkingName(){
        Student stud = new Student("Petya");
        Assertions.assertEquals(stud.toString(), "Student{" + "name=" + "Petya" + ", marks=[]}");
    }
}
