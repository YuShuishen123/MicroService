package java_web.configclient2.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Value("${name}")
    String name;
    @GetMapping("/getName")
    public String name(){
        return name;
    }
}
