package com.eazybytes.eazyschool.Security;

import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.model.Roles;
import com.eazybytes.eazyschool.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Profile("prod")
public class Authenticationprovider implements org.springframework.security.authentication.AuthenticationProvider {

    @Autowired
    private PersonRepository personRepository; // this is for taking the details from the database, is will run the querry and get the details user entered during the regidtration

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                       // user entered details this is taken by during login
        String email = authentication.getName();
        String pwd = authentication.getCredentials().toString();

           // for executing the querry from the person repository for gettig the details of user, readByEmail get the email from the table whihc we derived querry in Person Repositiory
        Person person = personRepository.readByEmail(email);
          // passwordEncoder.matches(pwd,person.getPwd() :- this line used for (
        //  pwd is user entered raw password during the login and matches method generate the hash value,person.getPwd() this fetching from the dataBase)
        if(null != person && person.getPersonId()>0 && passwordEncoder.matches(pwd,person.getPwd())){
            return new UsernamePasswordAuthenticationToken(email,null, getGrantedAuthorities(person.getRoles()));
        }else{
            throw new BadCredentialsException("Invalid Credentials");
        }


    }
//    SimpleGrantedAuthority extendes the grantedAuthorities
    private List<GrantedAuthority>getGrantedAuthorities(Roles roles){
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + roles.getRoleName()));
        return grantedAuthorities;
    }

    @Override
    public boolean supports(Class<?> authentication) { // check or compare the credinatials with provided details if same then authenticate execuated
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
