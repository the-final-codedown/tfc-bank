package fr.polytech.al.tfc.savings.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/savings")
public class SavingsController {

    private final SavingsObserver savingsObserver;

    @Autowired
    public SavingsController(SavingsObserver savingsObserver) {
        this.savingsObserver = savingsObserver;
    }

    @PostMapping
    public ResponseEntity<String> startComputingSavings() {
        savingsObserver.computeSavings();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
