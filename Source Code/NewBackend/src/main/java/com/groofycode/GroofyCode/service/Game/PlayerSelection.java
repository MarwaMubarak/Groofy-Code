package com.groofycode.GroofyCode.service.Game;

import com.groofycode.GroofyCode.dto.Game.RankedMatchDTO;
import com.groofycode.GroofyCode.dto.MatchPlayerDTO;
import com.groofycode.GroofyCode.dto.MatchPlayersDTO;
import com.groofycode.GroofyCode.dto.PlayerDTO;
import com.groofycode.GroofyCode.dto.ProblemDTO;
import com.groofycode.GroofyCode.model.Game.RankedMatch;
import com.groofycode.GroofyCode.model.User.UserModel;
import com.groofycode.GroofyCode.repository.Game.GameRepository;
import com.groofycode.GroofyCode.repository.UserRepository;
import com.groofycode.GroofyCode.service.ProblemPicker;
import com.groofycode.GroofyCode.utilities.ResponseUtils;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class PlayerSelection {

    private final CopyOnWriteArrayList<MatchPlayerDTO> waitingPlayers = new CopyOnWriteArrayList<>();
    private final AtomicBoolean isProcessing = new AtomicBoolean(false);
    private final ProblemPicker problemPicker;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final SimpMessagingTemplate messagingTemplate;


    @Autowired
    public PlayerSelection(ProblemPicker problemPicker, ModelMapper modelMapper, UserRepository userRepository, GameRepository gameRepository,
                           SimpMessagingTemplate messagingTemplate) {
        this.problemPicker = problemPicker;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public boolean isInQueue(Long playerId) {
        return waitingPlayers.stream()
                .anyMatch(player -> player.getId().equals(playerId));
    }

    public boolean leaveQueue(Long playerId) {
        Optional<MatchPlayerDTO> playerToRemove = waitingPlayers.stream()
                .filter(player -> player.getId().equals(playerId))
                .findFirst();
        if (playerToRemove.isPresent()) {
            waitingPlayers.remove(playerToRemove.get());
            return true;  // Successfully removed the player from the queue
        }
        return false;  // Player was not found in the queue
    }


    public void addPlayerToQueue(MatchPlayerDTO player) {
        boolean wasEmpty = waitingPlayers.isEmpty();
        if(isInQueue(player.getId())) {
            return;
        }
        waitingPlayers.add(player);
        if (wasEmpty) {
            // Trigger processing if this is the first player to join an empty queue
            isProcessing.set(true);
        }
    }

    @Scheduled(fixedDelay = 1000)  // Run every second
    public void processPlayers() throws Exception {
        if (!isProcessing.get() || waitingPlayers.isEmpty()) {
            // Stop processing if there are no players
            isProcessing.set(false);
            return;
        }

        // Implement your matchmaking logic here
        if (waitingPlayers.size() >= 2) {  // Example: Need at least two players to match
            List<MatchPlayerDTO> matchedPlayers = selectPlayersForMatch();

            UserModel player1 = userRepository.fetchById(matchedPlayers.get(0).getId());
            UserModel opponent = userRepository.fetchById(matchedPlayers.get(1).getId());


            assert player1 != null;
            List<UserModel> players1 = List.of(player1);
            assert opponent != null;
            List<UserModel> players2 = List.of(opponent);

            PlayerDTO playerDTO = modelMapper.map(player1, PlayerDTO.class);
            PlayerDTO opponentDTO = modelMapper.map(opponent, PlayerDTO.class);

            List<ProblemDTO> solvedProblemsPlayer = player1.getSolvedProblems().stream().map(
                    progProblem -> modelMapper.map(progProblem, ProblemDTO.class)).toList();
            List<ProblemDTO> solvedProblemsOpponent = opponent.getSolvedProblems().stream().map(
                    progProblem -> modelMapper.map(progProblem, ProblemDTO.class)).toList();

            String problemUrl = (String) problemPicker.pickProblem(playerDTO, solvedProblemsPlayer, opponentDTO, solvedProblemsOpponent).getBody();

            RankedMatch rankedMatch = new RankedMatch(players1, players2, problemUrl, LocalDateTime.now(), LocalDateTime.now().plusMinutes(60), 60.0);


            rankedMatch = gameRepository.save(rankedMatch);

            RankedMatchDTO rankedMatchDTO = new RankedMatchDTO(rankedMatch, null);
            player1.setExistingGameId(rankedMatch.getId());
            opponent.setExistingGameId(rankedMatch.getId());
            userRepository.save(player1);
            userRepository.save(opponent);

            messagingTemplate.convertAndSendToUser(player1.getUsername(), "/games", ResponseUtils.successfulRes("Match started successfully", rankedMatchDTO));
            messagingTemplate.convertAndSendToUser(opponent.getUsername(), "/games", ResponseUtils.successfulRes("Match started successfully", rankedMatchDTO));

            waitingPlayers.removeAll(matchedPlayers);
        }
    }

    @Scheduled(fixedDelay = 5000)  // Run every second
    public void updatingExpectedRatingPlayers() {
        if (!isProcessing.get() || waitingPlayers.isEmpty()) {
            // Stop processing if there are no players
            isProcessing.set(false);
            return;
        }
        for (int i = 0; i < waitingPlayers.size(); i++) {
            waitingPlayers.get(i).setExpectedRatingL(waitingPlayers.get(i).getExpectedRatingL() - 10);
            waitingPlayers.get(i).setExpectedRatingR(waitingPlayers.get(i).getExpectedRatingR() + 10);
        }
    }


    private List<MatchPlayerDTO> selectPlayersForMatch() {
        // Placeholder: select the first two players
        // Implement your own logic based on ratings or other criteria
        List<MatchPlayerDTO> playersForMatch = new ArrayList<>();
        for (int i = 1; i < waitingPlayers.size(); i++) {
            int l1 = waitingPlayers.get(i).getExpectedRatingL();
            int r1 = waitingPlayers.get(i).getExpectedRatingR();
            int l2 = waitingPlayers.get(0).getExpectedRatingL();
            int r2 = waitingPlayers.get(0).getExpectedRatingR();
            if (l1 > l2) {
                int tempL = l1;
                int tempR = r1;
                l1 = l2;
                r1 = r2;
                l2 = tempL;
                r2 = tempR;
            }
            if (l2 <= r1 || (l1 <= r2 && r2 <= r1)) {
                playersForMatch.add(waitingPlayers.get(i));
                playersForMatch.add(waitingPlayers.get(0));
                break;
            }
        }
        return playersForMatch;
    }
}