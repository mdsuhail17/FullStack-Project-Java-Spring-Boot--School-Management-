package com.eazybytes.eazyschool.controller;


import com.eazybytes.eazyschool.model.Courses;
import com.eazybytes.eazyschool.model.EazyClass;
import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.repository.CoursesRepository;
import com.eazybytes.eazyschool.repository.EazyClassRepository;
import com.eazybytes.eazyschool.repository.PersonRepository;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    EazyClassRepository eazyClassRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    CoursesRepository coursesRepository;

@RequestMapping("/displayClasses")
public ModelAndView displayClasses(Model model){
    List<EazyClass> eazyClasses = eazyClassRepository.findAll();  // retriving the data from the data base  using the findAll()method present in the curdrepo
    ModelAndView modelAndView = new ModelAndView("classes.html");
    modelAndView.addObject("eazyClasses", eazyClasses);// after retriving displaying in to the view
    modelAndView.addObject("eazyClass", new EazyClass()); // because admin can have option for creating or deleting the new class so we are creating new object of the eaztClass

    return modelAndView;
}


// adding the new class
@RequestMapping("/addNewClass")
 public ModelAndView addNewClass(Model model, @ModelAttribute("eazyClass") EazyClass eazyClass){
    eazyClassRepository.save(eazyClass);  // sending the deta into the dataBase using repositiry class
    // returning the same page for displaying the class , so above line we have inserted the data and
    //we have to retriveit on the same page so we are retriving the data from above method on /displayClasses
    ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayClasses");
    return  modelAndView;
 }

 // deleting the class
@RequestMapping("/deleteClass")
public ModelAndView deleteClass(Model model, @RequestParam int id ){ // @RequestParam int id becaouse in html page we are passing class id which is need to be deleted

     Optional<EazyClass> eazyClass =  eazyClassRepository.findById(id);// from that id we are finding the class
    for(Person person : eazyClass.get().getPersons()){
        person.setEazyClass(null);// removing the references from the person from the eazyclss
        personRepository.save(person);
    }
    // deleting the class by id and returning the displayClasses
    eazyClassRepository.deleteById(id);
    ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayClasses");
    return modelAndView;
}

@GetMapping("/displayStudents")
public ModelAndView displayStudents(Model model, @RequestParam int classId, HttpSession session,
                                    @RequestParam(value = "error",required = false) String error
                                    ){
    String errorMessage = null;
   ModelAndView modelAndView  = new ModelAndView("students.html");
   Optional<EazyClass> eazyClass = eazyClassRepository.findById(classId);// get the class details by findById() by request parm classId
   modelAndView.addObject("eazyClass", eazyClass.get());
   modelAndView.addObject("person", new Person()); // for new object
   session.setAttribute("eazyClass", eazyClass.get()); // details of the class eazyclass add so we are storing into teh session

   if(error != null){
       errorMessage = " invalid email entred";
       modelAndView.addObject("errorMessage", errorMessage);
   }
    return modelAndView;
}

@PostMapping("/addStudent")
public ModelAndView addStudent(Model model, @ModelAttribute("person") Person person, HttpSession session){
    ModelAndView modelAndView = new ModelAndView();
    EazyClass eazyClass = (EazyClass) session.getAttribute("eazyClass");
    Person personEntity = personRepository.readByEmail(person.getEmail()); // if email enterd by user is valid it stores intp personentity
     // if email id is null
    if(personEntity== null  || !(personEntity.getPersonId()>0)){
        modelAndView.setViewName("redirect:/admin/displayStudent?classId="+ eazyClass.getClassId() + "&error=true");
        return modelAndView;
    }
    // linking  person class and eazyclass
    personEntity.setEazyClass(eazyClass); // setting  eazyclass detail into my person entiry
    personRepository.save(personEntity); // saving into the database
    eazyClass.getPersons().add(personEntity); // personemtity in to eazyclass
    eazyClassRepository.save(eazyClass);
    modelAndView.setViewName("redirect:/admin/displayStudents?classId="+eazyClass.getClassId());
    return modelAndView;
}

@GetMapping("/deleteStudent")
public ModelAndView deleteStudent(Model model, @RequestParam int personId, HttpSession session){

    EazyClass eazyClass= (EazyClass) session.getAttribute("eazyClass"); // getting the eazycalss details which stored into the session
     Optional<Person> person =  personRepository.findById(personId);//fethicng the person based on the personid

    //breaking the relationship bw person and eazyclass
    person.get().setEazyClass(null);
    eazyClass.getPersons().remove(person.get());// revemoving the person
    EazyClass eazyClasssaved =  eazyClassRepository.save(eazyClass); //saving into data
    session.setAttribute("eazyClass", eazyClasssaved);// setting the same object or a latest object ofter deleting the student
    ModelAndView modelAndView= new ModelAndView("redirect:/admin/displayStudents?classId="+eazyClass.getClassId());
    return modelAndView;
}

 // for courses
@GetMapping("/displayCourses")
public ModelAndView displayCourses(Model model){

    //1.findByOrderByName(); this is used from course  reposetry class that is (static sorting) by defaiul assending
    //2.findByOrderByNameDesc::  this is used from course  reposetry class that is (static sorting)  print the course in decending
    // this line commented for this is static sorting so we commented, for bulding the dynamic sorting
//    List<Courses> courses = coursesRepository.findByOrderByNameDesc(); // first finding the course present in the database if there display to view


    // dynamic sorting using sort  :: (Sort.by("name").descending().and(...)); we can use and()
List<Courses> courses = coursesRepository.findAll(Sort.by("name").ascending());

    ModelAndView modelAndView = new ModelAndView("courses_secure.html");
    modelAndView.addObject("courses", courses); //sending the fetched courses to the view to the object courses
    modelAndView.addObject("course", new Courses()); // new object ofa new entity , to bring the course info ui to the backend
    return modelAndView;
}
@PostMapping("/addNewCourse")
public ModelAndView addNewCourse(Model model, @ModelAttribute("course") Courses course){
    ModelAndView modelAndView = new ModelAndView();
    coursesRepository.save(course); // saving the coureses comming from the UI
    modelAndView.setViewName("redirect:/admin/displayCourses");
    return modelAndView;

}
@GetMapping("/viewStudents")
public ModelAndView viewStudents(Model model, @RequestParam int id, HttpSession session,
                                 @RequestParam( required = false) String error){
    String errorMessage = null;
    ModelAndView modelAndView = new ModelAndView("course_students.html");
    Optional<Courses> courses = coursesRepository.findById(id); // fetching the courses present in the data base bu course id
    modelAndView.addObject("courses", courses.get()); // sending avobe course to UI
    modelAndView.addObject("person", new Person()); // admin add new studets so we send the new object
    session.setAttribute("courses",courses.get());// courses abject that we got from the database and storing into session

    if( error != null){
        errorMessage = "invalid email entred";

        modelAndView.addObject("errorMessage",errorMessage);
    }
    return modelAndView;
}
@PostMapping("/addStudentToCourse")
public ModelAndView addStudentToCourse(Model model, @ModelAttribute("person") Person person, HttpSession session){

    ModelAndView modelAndView = new ModelAndView();
           Courses courses =(Courses) session.getAttribute("courses"); // getting the details from the session(loded the course details from the session )
                Person personENtity =  personRepository.readByEmail(person.getEmail());//fetching the person details from the database from the mail entered by the admin

    // if person entity is null or because of the email is wrong
    if(personENtity == null || !(personENtity.getPersonId()>0)){
        modelAndView.setViewName("redirect:/admin/viewStudents?id=" +courses.getCourseId() +"&error=true");
        return modelAndView;
    }

    personENtity.getCourses().add(courses); // calling the personemtity and calling the all courses and add only one course
    courses.getPersons().add(personENtity);
    personRepository.save(personENtity);// latest  courses object setting
    session.setAttribute("courses", courses);
    modelAndView.setViewName("redirect:/viewStudents?id=" + courses.getCourseId());
    return modelAndView;
}
@GetMapping("/deleteStudentFromCourse")
public ModelAndView deleteStudentFromCourse(Model model, @RequestParam int personId, HttpSession session){
    Courses courses = (Courses) session.getAttribute("courses"); //  which course : focusing on the current course, getting the current course from the session
    Optional<Person> person = personRepository.findById(personId); // fetching the person details by person id
    person.get().getCourses().remove(courses); // getting courses  removing
    courses.getPersons().remove(person);
    personRepository.save(person.get()); // saving in database
    session.setAttribute("courses", courses); // setting latest object
    ModelAndView modelAndView = new ModelAndView(" redirect:/admin/viewStudents?id=" + courses.getCourseId());
    return modelAndView;
}
}
