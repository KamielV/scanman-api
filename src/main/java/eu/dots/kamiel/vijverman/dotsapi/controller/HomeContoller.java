/*
 */
package eu.dots.kamiel.vijverman.dotsapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HomeContoller {

    @RequestMapping("/gc")
    public String gc() {
        System.gc();
        return "Running garbage collector";
    }
    

}

