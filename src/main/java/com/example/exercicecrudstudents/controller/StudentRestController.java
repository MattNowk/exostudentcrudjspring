package com.example.exercicecrudstudents.controller;

import com.example.exercicecrudstudents.model.Student;
import com.example.exercicecrudstudents.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentRestController {

    private final IStudentService studentService;

    @Autowired
    public StudentRestController(IStudentService studentService){
        this.studentService = studentService;
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/form")
    public String form(Model model) {
        Student student = new Student();
        model.addAttribute("student", student);
        return "form";
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "list";
    }

    @GetMapping("/details/{id}")
    public String studentDetails(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id);
        if (student != null) {
            model.addAttribute("student", student);
            return "studentDetails";
        } else {
            model.addAttribute("error", "Étudiant non trouvé avec l'ID spécifié.");
            return "list";
        }
    }

    @GetMapping("/edit/{id}")
    public String editStudent(@PathVariable Long id, Model model) {
        // Récupérez les détails de l'étudiant avec l'ID spécifié
        Student student = studentService.getStudentById(id);

        if (student != null) {
            model.addAttribute("student", student);
            return "editStudent"; // Redirigez vers la page d'édition de l'étudiant
        } else {
            // Gérez le cas où l'étudiant n'a pas été trouvé, par exemple, en affichant un message d'erreur
            model.addAttribute("error", "Étudiant non trouvé avec l'ID spécifié.");
            return "list"; // Redirigez vers la liste des étudiants ou une autre page en cas d'erreur
        }
    }


    @GetMapping("/api")
    @ResponseBody
    public List<Student> getAllStudents(){
        return studentService.getAllStudents();
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public Student getStudentById(@PathVariable Long id){
        return studentService.getStudentById(id);
    }

    @PostMapping("/api/student")
    public String createStudent(@ModelAttribute Student student, Model model){
        if (student != null && student.getFirstName() != null && student.getLastName() != null) {
            studentService.createStudent(student);
            return "redirect:/students/list";
        } else {
            model.addAttribute("error", "Veuillez remplir tous les champs du formulaire.");
            return "form";
        }
    }


    @PutMapping("/api/{id}")
    @ResponseBody
    public Student updateStudent(@PathVariable Long id, @RequestBody Student student){
        return studentService.updateStudent(id, student);
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    public void deleteStudent(@PathVariable Long id){
        studentService.deleteStudent(id);
    }
}
