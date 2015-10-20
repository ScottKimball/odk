package org.motechproject.odk.web;


import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.Verification;
import org.motechproject.odk.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/verify")
public class VerifyController {

    @Autowired
    private VerificationService verificationService;

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/kobo", method = RequestMethod.POST)
    public Verification verifyKobo(@RequestBody Configuration configuration) {
        return verificationService.verifyKobo(configuration);
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/ona", method = RequestMethod.POST)
    public Verification verifyOna(@RequestBody Configuration configuration) {
        return verificationService.verifyOna(configuration);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/odk", method = RequestMethod.POST)
    public Verification verifyOdk(@RequestBody Configuration configuration) {
        return verificationService.verifyOdk(configuration);
    }
}
