package domashka.student;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@EqualsAndHashCode
public class Student {

    @Setter
    CheckGrade checkGrade;
    @Getter
    @Setter
    private String name;
    private List grades = new ArrayList<>();

    public Student(String name) {
        this.name = name;
    }

    public List getGrades() {
        return new ArrayList<>(grades);
    }

    @SneakyThrows
    public void addGrade(int grade) {
        if(!checkGrade.checkGrade(grade)){
            throw new IllegalArgumentException(grade + " is wrong grade");
        }
        grades.add(grade);
    }
}