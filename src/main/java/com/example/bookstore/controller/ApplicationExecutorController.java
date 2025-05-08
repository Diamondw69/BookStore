package com.example.bookstore.controller;

import com.example.bookstore.dto.ApplicationExecutorRequest;
import com.example.bookstore.service.StateMachineProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/process")
public class ApplicationExecutorController {

    private final StateMachineProcessService stateMachineProcessService;


    @PostMapping("/order")
    public void processApplicationCertificate(@RequestBody ApplicationExecutorRequest request) {
        stateMachineProcessService.processApplication(request);
    }

}
