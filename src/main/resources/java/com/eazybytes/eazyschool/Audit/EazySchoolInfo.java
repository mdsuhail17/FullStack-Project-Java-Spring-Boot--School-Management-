package com.eazybytes.eazyschool.Audit;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EazySchoolInfo implements InfoContributor {


    @Override
    public void contribute(Info.Builder builder) {
        Map<String, String> eazyMap = new HashMap<String, String>();
        eazyMap.put("App Name", "EazySchool");
        eazyMap.put("App Description", "A acchool web Application for Students and Admin");
        eazyMap.put("Developed By", "MD Suhail");
        eazyMap.put("For More Info Contact","9353649419");
        builder.withDetail("eazyschool-info", eazyMap);

    }
}
