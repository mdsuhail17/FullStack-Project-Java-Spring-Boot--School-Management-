package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.repository.PersonRepository;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class DashboardController {

    @Autowired
    PersonRepository personRepository;


    // custome properties, this is eazyschool.pageSize define in the application.property, we have to use @Value for reading the property
    @Value("${eazyschool.pageSize}")
    private int defaultPageSize;

    @Value("${eazyschool.contact.successMsg}")
    private String message;

//    @Autowired
//    Environment environment;

    @RequestMapping("/dashboard")
    public String displayDashboard(Model model, Authentication authentication, HttpSession session) {
        Person person =  personRepository.readByEmail(authentication.getName());
        model.addAttribute("username", person.getName());
        model.addAttribute("roles", authentication.getAuthorities().toString());

        // display the person is in which class
        if(null != person.getEazyClass() && null != person.getEazyClass().getName()){
            model.addAttribute("enrolledClass", person.getEazyClass().getName());
        }

        session.setAttribute("loggedInPerson", person); // stroing the person info into the session using session.setAttribute
//        logMessages();
        return "dashboard.html";
    }

  // in applcation properties
//     all the logger statement will be print and log,info,warn,debug, error  within code present spring application , not in dashboardcontroller
//    debug=true
    private void logMessages(){
        log.error("Error message from the dashboard page");
        log.warn("Warning message from the dashboard page");
        log.info("Info message from the dashboard page");
        log.debug("debug message from the dashboard page");
        log.trace("trace message from the dashboard page");

        log.error("defaultPageSize value with @Value annotation is : "+defaultPageSize);
        log.error("successMsg value with @Value annotation is : "+message);

//        log.error("defaultPageSize value with Environment is : "+environment.getProperty("eazyschool.pageSize"));
//        log.error("successMsg value with Environment is : "+environment.getProperty("eazyschool.contact.successMsg"));
//        log.error("Java Home environment variable using Environment is : "+environment.getProperty("JAVA_HOME"));


    }

}

