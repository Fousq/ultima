package com.example.rbapp.supervisor.contoller;

import com.example.rbapp.api.service.registration.RegistrationRequest;
import com.example.rbapp.supervisor.contoller.api.SupervisorResponse;
import com.example.rbapp.supervisor.service.SupervisorRegistrationService;
import com.example.rbapp.supervisor.service.SupervisorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supervisor")
@RequiredArgsConstructor
public class SupervisorController {

    private final SupervisorRegistrationService supervisorRegistrationService;
    private final SupervisorService supervisorService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody RegistrationRequest registrationRequest) {
        supervisorRegistrationService.register(registrationRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<SupervisorResponse> getList() {
        return supervisorService.getAll();
    }

    @GetMapping("/{id}")
    public SupervisorResponse getById(@PathVariable("id") Long id) {
        return supervisorService.getById(id);
    }
}
