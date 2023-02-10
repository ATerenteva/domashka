package domashka.student;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.List;

public class StudentTest {

    @RepeatedTest(value = 4, name = "Adding a correct grades")
    public void gradesInRange(RepetitionInfo repetitionInfo){
        Student stud = new Student("Petya");
        CheckGrade checkGrade = Mockito.mock(CheckGrade.class);
        Mockito.when(checkGrade.checkGrade(repetitionInfo.getCurrentRepetition()+1))
                .thenReturn(repetitionInfo.getCurrentRepetition()+1 >= 2 && repetitionInfo.getCurrentRepetition()+1 <= 5);
        stud.setCheckGrade(checkGrade);
        stud.addGrade(repetitionInfo.getCurrentRepetition()+1);
        Assertions.assertEquals(stud.getGrades().get(0), repetitionInfo.getCurrentRepetition()+1);
    }

    @ParameterizedTest
    @MethodSource("domashka.student.GradeSource#ints")
    @DisplayName("Adding incorrect grades")
    public void gradesNotInRange(int x) {
        Student stud = new Student("vasia");
        CheckGrade checkGrade = Mockito.mock(CheckGrade.class);
        Mockito.when(checkGrade.checkGrade(x))
                .thenReturn(x >= 2 && x <= 5);
        stud.setCheckGrade(checkGrade);
       Assertions.assertThrows(IllegalArgumentException.class, () ->
                stud.addGrade(x));
    }
}
