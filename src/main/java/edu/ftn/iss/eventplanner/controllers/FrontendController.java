package edu.ftn.iss.eventplanner.controllers;

import jakarta.annotation.security.PermitAll;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FrontendController {

    // should redirect all angular client routes to index.html
    @PermitAll
    @RequestMapping(path = {
            "/",
            "/{path:^(?!api|static|media|photos|profile-photos|event-photo|product-service-photo|images|ws|.*\\..*).*$}",
            "/{path:^(?!api|static|media|photos|profile-photos|event-photo|product-service-photo|images|ws|.*\\..*).*$}/**"
    })
    @SuppressWarnings("unused")
    public String index(@PathVariable(required = false) String path) {
        return "forward:/index.html";
    }

}
