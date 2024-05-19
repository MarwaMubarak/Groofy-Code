//package com.groofycode.GroofyCode.repository.NewMatch;
//
//import com.groofycode.GroofyCode.model.NewMatch.MatchModel;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.util.List;
//
//public interface MatchRepository extends JpaRepository<MatchModel, Long> {
//
//    @Query("SELECT m FROM Match m WHERE m.user1.id = :userId OR m.user2.id = :userId")
//    List<MatchModel> findByUserId(@Param("userId") Long userId);
//}
