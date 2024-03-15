package com.groofycode.GroofyCode.controller;

import com.groofycode.GroofyCode.dto.BadgeDTO;
import com.groofycode.GroofyCode.service.BadgeService;
import com.groofycode.GroofyCode.utilities.ResponseUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.text.BreakIterator;
import java.util.List;

@RestController
@RequestMapping("/badges")
public class BadgeController {
    @Autowired
    private BadgeService badgeService;

    @GetMapping
    public ResponseEntity<?> getAllBadges() {

        return badgeService.getAllBadges();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBadgeById(@PathVariable Long id) {
        return badgeService.getBadgeById(id);
    }

    @PostMapping("")
    public ResponseEntity<?> createBadge(@Valid @RequestBody  BadgeDTO badgeDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("Validation Errors!",bindingResult.getAllErrors()));
        }
        return badgeService.createBadge(badgeDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBadge(@PathVariable Long id) {

        return badgeService.deleteBadge(id);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append("; "));
        return new ResponseEntity<>(errorMessage.toString(), HttpStatus.FORBIDDEN);
    }
}
