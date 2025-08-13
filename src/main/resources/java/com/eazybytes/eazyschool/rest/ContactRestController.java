package com.eazybytes.eazyschool.rest;


import com.eazybytes.eazyschool.constants.EazySchoolConstants;
import com.eazybytes.eazyschool.model.Contact;
import com.eazybytes.eazyschool.model.Response;
import com.eazybytes.eazyschool.repository.ContactRepository;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.CachedIntrospectionResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/api/contact", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}) // they can [roduce the response the json and xml
@CrossOrigin(origins = "*") //used to  please allow the communication regardlesst of hostname or portname
public class ContactRestController {

   @Autowired
    ContactRepository contactRepository;

//    @ResponseBody send the all java object data into json
// @RequestBody  spring covert the json request assign the java object
     // restService
    // the data is coverted which is present in the java pojo class is converted into json
    // this conversation is done by the internally spring, take help from jackson they will cobert the java pojo class to the json
   @GetMapping("/getMessageByStatus")
//   @ResponseBody// get the data  and doest  recive the any view // commented because we are using restController , which is playes the both roles ResponseBody and controller
   public List<Contact> getMessageStatus(@RequestParam(name = "status") String status){
       return contactRepository.findByStatus(status);
   }


   @GetMapping("/getAllMessagesByStatus")
//   @ResponseBody
   // @RequestBody we use this for sending through the body rather the the parameter
   public List<Contact> getAllMessagesByStatus(@RequestBody Contact contact){
     if(null != contact && null != contact.getStatus()){
         return contactRepository.findByStatus(contact.getStatus());
     }else {
         return List.of();  // returning empty list
     }

   }

//    @RequestHeader  to accept or fecth the header information we use this
   @PostMapping("/saveMsg")
   public ResponseEntity<Response> saveMsg(@RequestHeader("invocationFrom") String invocationForm,
                                           @Valid @RequestBody Contact contact){
       // @Valid what we are perform validation on contact that also perfrom in this data (for spring rest service validations)
       //@RequestBody accepect the entire body
       //@RequestHeader accepcting header
       log.info(String.format("Header invocationFrom = %s",invocationForm )); // to know  who is invocating
       contactRepository.save(contact);
       Response response = new Response();
       response.setStatusCode("200");
       response.setStatusMsg("Message Saved successfully ");
       return ResponseEntity          // for header info and status info we use response entity and what is tge data type of body
               .status(HttpStatus.CREATED) // operation is succcessfull
               .header("isMsgSaved", "true")  // header info
               .body(response); // send the object

   }
@DeleteMapping("/deleteMSg") // we are deleteing the contact message from the dataBase
   public ResponseEntity<Response> deleteMsg(RequestEntity<Contact> requestEntity){  // RequestEntity : for fetch the th all body and headers infor that some one is going to send whever invockring the  perticular APi

     HttpHeaders headers= requestEntity.getHeaders(); // identify all the headers or get the multiple headers
     headers.forEach((key, value) -> {  // we can get the header key and value
         log.info(String.format(
                 "Header '%s' = %s", key, value.stream().collect(Collectors.joining("|"))));
     });

     Contact contact = requestEntity.getBody(); // fetch the requestBody
     contactRepository.deleteById(contact.getContactId()); // deleting the message by contact id
     Response response = new Response();
     response.setStatusCode("200");
     response.setStatusMsg("successfully deleted");
     return ResponseEntity.status(HttpStatus.OK).body(response);
   }

   //updating the sttus by contact id
@PatchMapping("/closeMsg") // we use @PatchMapping when we are partillyb  updating the column
   public  ResponseEntity<Response> closeMsg(@RequestBody Contact contactReq){
       Response response = new Response();
    Optional<Contact> contact = contactRepository.findById(contactReq.getContactId());
    if(contact.isPresent()){
        contact.get().setStatus(EazySchoolConstants.CLOSE);
        contactRepository.save(contact.get());
    }else{
        response.setStatusCode("400");
        response.setStatusMsg("Invalid contact id recived");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
    response.setStatusCode("200");
    response.setStatusMsg("message successfully closed");
    return ResponseEntity.status(HttpStatus.OK).body(response);

   }

}
