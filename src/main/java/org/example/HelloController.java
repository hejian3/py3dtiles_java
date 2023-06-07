package org.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/convert")
    public String convert(@RequestParam String input, @RequestParam String output) {
        ConvertUtils.exec(input, output);
        return"OK";
    }

    @GetMapping("/test")
    public String convert() {
        String input = "/root/las/202305161756050332_202305161756110548.las";
        String output = "/root/las1";
        ConvertUtils.exec(input, output);
        return"OK";
    }
}
