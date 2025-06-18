package com.eazybytes.eazyschool.controller;


import com.eazybytes.eazyschool.model.Address;
import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.model.Profile;
import com.eazybytes.eazyschool.repository.PersonRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller("ProfileControllerBean")
public class ProfileController {

    @Autowired
    PersonRepository personRepository;

    @RequestMapping("/displayProfile")
    public ModelAndView displayProfile(Model model, HttpSession session){
          Person person = (Person) session.getAttribute("loggedInPerson"); // fetching the loged in user details by logedInPerson
        Profile profile = new Profile();

          // getting the details from the person and and set to the profile object using setter methods
        profile.setName(person.getName());
        profile.setMobileNumber(person.getMobileNumber());
        profile.setEmail(person.getEmail());
        // if person have address details in database then populate to the profile from the person object
        if(person.getAddress() != null && person.getAddress().getAddressId()>0){
            profile.setAddress1(person.getAddress().getAddress1());
            profile.setAddress2(person.getAddress().getAddress2());
            profile.setCity(person.getAddress().getCity());
            profile.setState(person.getAddress().getState());
            profile.setZipCode(person.getAddress().getZipCode());
        }
        ModelAndView modelAndView = new ModelAndView("profile.html");
        modelAndView.addObject("profile",profile);
        return modelAndView;

    }
      // Explanation
    // @Valid @ModelAttribute : using for validation based on the annotation that we done in the profile model class
    // profile :accepting this parameter for we are sending data inside  a  profile web page
    // errors :  to handle the errors because we are using spring mvc validations
    // HttpSession : fetching the data from the http session and store
    @PostMapping("/updateProfile")
    public String updateProfile(@Valid @ModelAttribute("profile") Profile profile, Errors errors, HttpSession session){

        // if any errors it will return  the profile.html page
     if(errors.hasErrors()){
         return "profile.html";
     }
          // fetching the data from the session note: we stored the session data in  logedInPerson
        Person person = (Person) session.getAttribute("loggedInPerson");

     // setting or updating  the all profile detail
        person.setName(profile.getName());
        person.setEmail(profile.getEmail());
        person.setMobileNumber(profile.getMobileNumber());

        // if person have address inside the database or if dont have we create the empty address object
        if(person.getAddress() ==null || !(person.getAddress().getAddressId()>0)){
            person.setAddress(new Address());
        }
        person.getAddress().setAddress1(profile.getAddress1());
        person.getAddress().setAddress2(profile.getAddress2());
        person.getAddress().setCity(profile.getCity());
        person.getAddress().setState(profile.getState());
        person.getAddress().setZipCode(profile.getZipCode());

        personRepository.save(person);

        //for session have latest data becaise user may go back or do any other so we have to save all latest data into session
        session.setAttribute("loggedInPerson", person);

        return "redirect:/displayProfile";




    }

}
