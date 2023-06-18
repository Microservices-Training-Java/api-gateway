package com.sub.authen.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1")
public class CoreController {
//    @GetMapping("/core")
//    public Mono<String> helloCore(){
//        return Mono.just("hello core");
//    }
    @GetMapping("/core")
    public String helloCore(){
        return "hello core";
    }
}
