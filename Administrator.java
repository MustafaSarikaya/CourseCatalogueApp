package projcet;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class Administrator extends User {

    protected LinkedList<Course> courseList;//may be protected data
    private LinkedList<Student> studentList;
    private LinkedList<Instructor> InstructorList;

    public Administrator(String firstName, String lastName, String emailAddress, String passWord) {
        super(firstName, lastName, emailAddress, passWord);
    }

    public Instructor createInstructor(String firstName, String lastName, String emailAddress, String passWord) {
        Instructor result = new Instructor(firstName, lastName, emailAddress, passWord);
        return result;
    }

    public void addInstructor(Instructor instructor) {
        InstructorList.add(instructor);
    }

    public void deleteInstructor(String instructorEmailAddress) {
        for (ListIterator<Instructor> iter = InstructorList.listIterator(); iter.hasNext(); ) {
            if (iter.next().getEmailAddress() == instructorEmailAddress) {
                iter.remove();
            }
        }
    }


    public Student createStudent(String firstName, String lastName, String emailAddress, String passWord) {
        Student result = new Student(firstName, lastName, emailAddress, passWord);
        return result;
    }

    public void addStudent(Student student) {
        studentList.add(student);
    }

    public void deleteStudent(String studentEmailAddress) {
        for (ListIterator<Student> iter = studentList.listIterator(); iter.hasNext(); ) {
            if (iter.next().getEmailAddress() == studentEmailAddress) {
                iter.remove();
            }
        }
    }

    public Course creatCourse(String courseCode, String courseName) {
        Course result = new Course(courseCode, courseName);
        return result;
    }

    public void addCourse(Course course) {
        courseList.add(course);
    }

    public void deleteCourse(String courseCode) {
        for (ListIterator<Course> iter = courseList.listIterator(); iter.hasNext(); ) {
            if (iter.next().getCourseCode() == courseCode) {
                iter.remove();
            }
        }
    }

    public void setcourseCode(Course course, String courseCode){course.setCourseCode(courseCode);}

    public void setCourseName(Course course, String courseName){course.setCourseName(courseName);}

    public Course searchCourseByCode(String courseCode){
        for (ListIterator<Course> iter = courseList.listIterator(); iter.hasNext(); ) {
            if (iter.next().getCourseCode() == courseCode) {
                return iter.next();
            }
        }
        throw new NoSuchElementException("cannot find this course!");
    }

    public Course searchCourseByName(String courseName){
        for (ListIterator<Course> iter = courseList.listIterator(); iter.hasNext(); ) {
            if (iter.next().getCourseName() == courseName) {
                return iter.next();
            }
        }
        throw new NoSuchElementException("cannot find this course!");
    }

    public LinkedList<Course> getCourseList(){
        return courseList;
    }

//    public LinkedList<Student> getStudentList(){//private info, may need modify
//        return studentList;
//    }
//
//    public LinkedList<Instructor> getInstructorList(){
//        return InstructorList;
//    }
}
