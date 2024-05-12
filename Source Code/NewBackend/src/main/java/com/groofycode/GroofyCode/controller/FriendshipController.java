package com.groofycode.GroofyCode.controller;
import com.groofycode.GroofyCode.service.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/friends")
public class FriendshipController {
    @Autowired
    private FriendshipService friendshipService;


    @PostMapping("/send")
    public ResponseEntity<Object> sendRequest(@RequestParam Long friendId) throws Exception {
       return  friendshipService.sendRequest(friendId);

    }

    @PutMapping("/accept")
    public ResponseEntity<Object> acceptRequest(@RequestParam Long friendId) throws Exception {
        return friendshipService.acceptRequest(friendId);

    }

    @DeleteMapping("/reject")
    public ResponseEntity<Object> rejectRequest(@RequestParam Long friendId) throws Exception {
       return friendshipService.rejectRequest(friendId);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteFriend(@RequestParam Long friendId) throws Exception {
        return friendshipService.deleteFriend(friendId);
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<Object> cancelRequest(@RequestParam Long friendId) throws Exception {
        return friendshipService.cancelRequest(friendId);
    }

    @GetMapping("/accepted")
    public ResponseEntity<Object> getAcceptedPage( @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws Exception {
        return friendshipService.getAcceptedPage(page,size);
    }

    @GetMapping("/pending")
    public ResponseEntity<Object> getPendingPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws Exception {
        return friendshipService.getPendingPage(page,size);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws Exception {
        return friendshipService.getAllPage(  page,size);
    }

    @GetMapping("/accepted_cnt")
    public ResponseEntity<Object> getAcceptedCount() throws Exception {
        return friendshipService.getAcceptedCount();
    }

    @GetMapping("/pending_cnt")
    public ResponseEntity<Object> getPendingCount() throws Exception {
        return friendshipService.getPendingCount();
    }

    @GetMapping("/all_cnt")
    public ResponseEntity<Object> getAllCount() throws Exception {
        return friendshipService.getAllCount();
    }



}
