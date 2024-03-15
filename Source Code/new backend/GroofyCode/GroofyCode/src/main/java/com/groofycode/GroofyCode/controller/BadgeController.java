package com.groofycode.GroofyCode.controller;

import com.groofycode.GroofyCode.dto.BadgeDTO;
import com.groofycode.GroofyCode.dto.UpdateBadgeDTO;
import com.groofycode.GroofyCode.service.BadgeService;
import com.groofycode.GroofyCode.utilities.ResponseUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.text.BreakIterator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<?> createBadge(@Valid @RequestBody  BadgeDTO badgeDTO) {
//        if(bindingResult.hasErrors()){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("Validation Errors!",bindingResult.getAllErrors()));
//        }
        return badgeService.createBadge(badgeDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBadge(@PathVariable Long id) {

        return badgeService.deleteBadge(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBadge(@PathVariable Long id,@Valid @RequestBody UpdateBadgeDTO badgeDTO){
        return badgeService.updateBadge(id,badgeDTO);
    }


    @ExceptionHandler(BindException.class)
    public ResponseEntity<HashMap<String,List<String>>> handleValidationExceptions(BindException ex) {
       List<String>errors = ex.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        HashMap<String,List<String>>errMap  = new HashMap<>();
        errMap.put("errors",errors);
       return new ResponseEntity<>(errMap,HttpStatus.BAD_REQUEST);
    }
}
