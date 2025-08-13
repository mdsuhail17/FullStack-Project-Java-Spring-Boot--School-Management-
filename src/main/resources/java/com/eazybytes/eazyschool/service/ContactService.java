package com.eazybytes.eazyschool.service;

import com.eazybytes.eazyschool.config.EazySchoolProbs;
import com.eazybytes.eazyschool.constants.EazySchoolConstants;
import com.eazybytes.eazyschool.model.Contact;
import com.eazybytes.eazyschool.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/*
@Slf4j, is a Lombok-provided annotation that will automatically generate an SLF4J
Logger static property in the class at compilation time.
* */
@Slf4j
@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    EazySchoolProbs eazySchoolProbs;

    /**
     * Save Contact Details into DB
     * @param contact
     * @return boolean
     */
    public boolean saveMessageDetails(Contact contact){
        boolean isSaved = false;
        contact.setStatus(EazySchoolConstants.OPEN);

        Contact saveContact = contactRepository.save(contact);
        if( null != saveContact && saveContact.getContactId()> 0) {
            isSaved = true;
        }
        return isSaved;
    }

    public Page<Contact> findMsgsWithOpenStatus(int  pageNum,String sortField, String sortDir){
//        int pageSize =5 ; // commented because we are doing by properties (application properties)

        // this is for we are doing page size through  using application.properties
        int pageSize = eazySchoolProbs.getPageSize(); //get the size from eazySchoolProbs
        if(null !=eazySchoolProbs.getContact() && null!=eazySchoolProbs.getContact().get("pageSize")){
            pageSize = Integer.parseInt(eazySchoolProbs.getContact().get("pageSize").trim());
        }
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize,
                sortDir.equals("asc") ? Sort.by(sortField).ascending()
                        : Sort.by(sortField).descending());
        Page<Contact> msgPage = contactRepository.findByStatusWithQuerry(
                EazySchoolConstants.OPEN,pageable
        );
        return msgPage;

    }

    public boolean updateMsgStatus(int contactId){
        boolean isUpdated = false;

            // this code for @Querry
                 int rows = contactRepository.updateStatusById(EazySchoolConstants.CLOSE,contactId);
                 if(rows > 0){
                     isUpdated = true;
                 }

                 // this code is fpor fetching and updating
//             Optional<Contact> contact=contactRepository.findById(contactId);
//             contact.ifPresent(contact1 -> {
//                 contact1.setStatus(EazySchool
//
//                 Constants.CLOSE);
//
//             });
//
//             Contact updateContact = contactRepository.save(contact.get());
//             if(null != updateContact && updateContact.getUpdatedBy()!=null){
//                 isUpdated = true;
//             }
        return isUpdated;
    }

}
