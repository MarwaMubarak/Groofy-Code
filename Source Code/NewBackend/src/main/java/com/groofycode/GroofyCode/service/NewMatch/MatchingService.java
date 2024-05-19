//package com.groofycode.GroofyCode.service.NewMatch;
//
//import com.groofycode.GroofyCode.model.NewMatch.*;
//import com.groofycode.GroofyCode.model.User.UserModel;
//import com.groofycode.GroofyCode.repository.NewMatch.MatchRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.time.Duration;
//import java.time.Instant;
//import java.util.List;
//import java.util.concurrent.ConcurrentLinkedQueue;
//
//@Service
//public class MatchingService {
//
//
//    @Autowired
//    private MatchRepository matchRepository;
//
//    private ConcurrentLinkedQueue<UserModel> queue;
//    private ConcurrentLinkedQueue<UserModel> waitingQueue;
//
//    public MatchingService() {
//        queue = new ConcurrentLinkedQueue<>();
//        waitingQueue = new ConcurrentLinkedQueue<>();
//    }
//
//
////    public synchronized MatchModel findMatch(UserModel user, String matchType) {
////        queue.remove(user);
////        UserModel matchUser = queue.poll(); // Get and remove the first user in the queue
////
////        if(matchUser == null) {
////            user.setTimestamp(Instant.now());
////            waitingQueue.add(user);
////            queue.add(user);
////            return null;
////        }
////        MatchModel match = null;
////        if (matchType.equalsIgnoreCase("Solo")) {
////            match = new SoloMatch(user);
////        } else if (matchUser != null) {
////            waitingQueue.remove(user);
////            waitingQueue.remove(matchUser);
////
////            if (matchType.equalsIgnoreCase("Casual")) {
////                match = new CasualMatch(user, matchUser);
////            } else if (matchType.equalsIgnoreCase("Official")) {
////                match = new OfficialMatch(user, matchUser, calculateRank(user, matchUser), calculateRank(matchUser, user));
////            }
////
////        }
////
////        if (match != null) {
////            return matchRepository.save(match);
////        }
////
////        return null;
////    }
//
//    private int calculateRank(UserModel user1, UserModel user2) {
//        // Implement your rank calculation logic here
//        return 200;
//    }
//
//    public synchronized int getQueueSize() {
//        return queue.size();
//    }
//
////    @Scheduled(fixedRate = 5000)
////    public synchronized void checkTimeouts() {
////        Instant now = Instant.now();
////        waitingQueue.forEach(user -> {
////            if (Duration.between(user.getTimestamp(), now).toMinutes() >= 2) {
////                waitingQueue.remove(user);
////                queue.remove(user);
////                // Notify timeout
////                System.out.println("Timeout for user: " + user.getUsername());
////            }
////        });
////    }
//
//    public List<MatchModel> getMatchesForUser(Long userId) {
//        return matchRepository.findByUserId(userId);
//    }
//}
