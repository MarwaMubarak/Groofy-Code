package com.groofycode.GroofyCode.service.Game;


import com.google.firebase.database.utilities.Pair;
import com.groofycode.GroofyCode.dto.FriendMatchInvitationDTO;
import com.groofycode.GroofyCode.dto.Game.*;
import com.groofycode.GroofyCode.dto.MatchPlayerDTO;
import com.groofycode.GroofyCode.dto.Notification.NotificationDTO;
import com.groofycode.GroofyCode.dto.Game.PlayerDTO;
import com.groofycode.GroofyCode.dto.Game.ProblemDTO;
import com.groofycode.GroofyCode.dto.TeamMatchInvitationDTO;
import com.groofycode.GroofyCode.dto.User.UserInfo;
import com.groofycode.GroofyCode.dto.PlayerDisplayDTO;
import com.groofycode.GroofyCode.model.Game.*;
import com.groofycode.GroofyCode.model.Notification.MatchNotificationModel;
import com.groofycode.GroofyCode.model.Notification.NotificationType;
import com.groofycode.GroofyCode.model.Team.TeamMember;
import com.groofycode.GroofyCode.model.Team.TeamModel;
import com.groofycode.GroofyCode.model.User.UserModel;
import com.groofycode.GroofyCode.repository.*;
import com.groofycode.GroofyCode.repository.Game.*;
import com.groofycode.GroofyCode.service.CodeforcesSubmissionService;
import com.groofycode.GroofyCode.service.NotificationService;
import com.groofycode.GroofyCode.service.ProblemPicker;
import com.groofycode.GroofyCode.utilities.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class GameService {

    private final GameRepository gameRepository;

    private final UserRepository userRepository; // Assuming you have a UserRepository

    private final PlayerSelection playerSelection;

    private final NotificationService notificationService;

    private final NotificationRepository notificationRepository;

    private final SubmissionRepository submissionRepository;

    private final SimpMessagingTemplate messagingTemplate;

    private final CodeforcesSubmissionService codeforcesSubmissionService;

    private final MatchStatusMapper matchStatusMapper;

    private final ModelMapper modelMapper;

    private final ProblemParser problemParser;

    private final ProblemPicker problemPicker;

    private final RatingSystemCalculator ratingSystemCalculator;

    private final MatchInvitationService matchInvitationService;

    private final ProgProblemRepository progProblemRepository;

    private final TeamMatchInvitationRepository teamMatchInvitationRepository;

    private final BeatAFriendRepository beatAFriendMatchRepository;

    private final FriendMatchInvitationNotificationRepository friendMatchInvitationNotificationRepository;

    private final FriendMatchInvitationRepository friendMatchInvitationRepository;

    private final MatchInvitationRepository matchInvitationRepository;

    private final BlobConverter blobConverter;

    private final GameHistoryRepository gameHistoryRepository;

//    private final MatchScheduler matchScheduler;

    @Autowired
    public GameService(GameRepository gameRepository, UserRepository userRepository, PlayerSelection playerSelection,
                       NotificationService notificationService, SubmissionRepository submissionRepository, SimpMessagingTemplate messagingTemplate,
                       ProblemParser problemParser, CodeforcesSubmissionService codeforcesSubmissionService,
                       NotificationRepository notificationRepository, MatchStatusMapper matchStatusMapper, ModelMapper modelMapper,
                       GameHistoryRepository gameHistoryRepository,

                       ProblemPicker problemPicker, RatingSystemCalculator ratingSystemCalculator, MatchInvitationService matchInvitationService, ProgProblemRepository progProblemRepository, TeamMatchInvitationRepository teamMatchInvitationRepository,
                       BeatAFriendRepository beatAFriendMatchRepository, FriendMatchInvitationNotificationRepository friendMatchInvitationNotificationRepository, FriendMatchInvitationRepository friendMatchInvitationRepository, MatchInvitationRepository matchInvitationRepository) {

        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.playerSelection = playerSelection;
        this.notificationService = notificationService;
        this.submissionRepository = submissionRepository;
        this.messagingTemplate = messagingTemplate;
        this.problemParser = problemParser;
        this.codeforcesSubmissionService = codeforcesSubmissionService;
        this.notificationRepository = notificationRepository;
        this.matchStatusMapper = matchStatusMapper;
        this.modelMapper = modelMapper;
        this.problemPicker = problemPicker;
        this.ratingSystemCalculator = ratingSystemCalculator;
        this.progProblemRepository = progProblemRepository;
//        this.matchScheduler = matchScheduler;
        this.matchInvitationService = matchInvitationService;
        this.teamMatchInvitationRepository = teamMatchInvitationRepository;
        this.beatAFriendMatchRepository = beatAFriendMatchRepository;
        this.friendMatchInvitationNotificationRepository = friendMatchInvitationNotificationRepository;
        this.friendMatchInvitationRepository = friendMatchInvitationRepository;
        this.matchInvitationRepository = matchInvitationRepository;
        this.gameHistoryRepository = gameHistoryRepository;
        blobConverter = new BlobConverter();
    }

    public ResponseEntity<Object> getSubmissions(Long gameId) {
        List<Submission> submissions = submissionRepository.findByGameId(gameId);
        List<SubmissionDTO> submissionDTOS = submissions.stream().map(sub -> {
            SubmissionDTO subDTO = modelMapper.map(sub, SubmissionDTO.class);
            Duration duration = Duration.between(sub.getSubmissionTime(), LocalDateTime.now());
            subDTO.setSubmissionTime(duration.getSeconds() / 60 + " min");
            subDTO.setVerdict(matchStatusMapper.getStatusIntToString().get(sub.getResult()));
            return subDTO;
        }).toList();
        return ResponseEntity.ok(ResponseUtils.successfulRes("Submissions retrieved successfully", submissionDTOS));
    }

    public ResponseEntity<Object> findAllGames(Integer page) throws Exception {
        try {
            if (page == null || page < 0) page = 0;
            PageRequest pageRequest = PageRequest.of(page, 10);
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel user = userRepository.findByUsername(userInfo.getUsername());
            List<GameHistory> gameHistories = gameHistoryRepository.findByUserIdOrderByGameDateDesc(user.getId(), pageRequest).getContent();
            List<GameHistoryDTO> gameDTOS = gameHistories.stream().map(gameHistory -> modelMapper.map(gameHistory, GameHistoryDTO.class)).toList();
            return ResponseEntity.ok(ResponseUtils.successfulRes("Games retrieved successfully", gameDTOS));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public ResponseEntity<Object> findRankedMatch() throws Exception {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel player = userRepository.findByUsername(userInfo.getUsername());

        if (playerSelection.isInQueue(player.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("Player already in queue", null));
        }

        PlayerDTO playerDTO = modelMapper.map(player, PlayerDTO.class);

        int expectedRating = problemPicker.expectedRatingPlayer(playerDTO);

        MatchPlayerDTO matchPlayerDTO = new MatchPlayerDTO(player.getId(), expectedRating, expectedRating);
        playerSelection.addPlayerToRankedQueue(matchPlayerDTO);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ResponseUtils.successfulRes("Added to waiting list", null)); // No available match found yet
    }

    public ResponseEntity<Object> findCasualMatch() throws Exception {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel player = userRepository.findByUsername(userInfo.getUsername());

        if (playerSelection.isInQueue(player.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("Player already in queue", null));
        }

        PlayerDTO playerDTO = modelMapper.map(player, PlayerDTO.class);

        int expectedRating = problemPicker.expectedRatingPlayer(playerDTO);

        MatchPlayerDTO matchPlayerDTO = new MatchPlayerDTO(player.getId(), expectedRating, expectedRating);
        playerSelection.addPlayerToCausalQueue(matchPlayerDTO);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ResponseUtils.successfulRes("Added to waiting list", null)); // No available match found yet
    }

    public ResponseEntity<Object> findVelocityMatch() throws Exception {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel player = userRepository.findByUsername(userInfo.getUsername());

        if (playerSelection.isInQueue(player.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("Player already in queue", null));
        }

        PlayerDTO playerDTO = modelMapper.map(player, PlayerDTO.class);

        int expectedRating = problemPicker.expectedRatingPlayer(playerDTO);

        MatchPlayerDTO matchPlayerDTO = new MatchPlayerDTO(player.getId(), expectedRating, expectedRating);
        playerSelection.addPlayerToVelocityQueue(matchPlayerDTO);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ResponseUtils.successfulRes("Added to waiting list", null)); // No available match found yet
    }

    public ResponseEntity<Object> findSoloMatch() {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel player1 = userRepository.findByUsername(userInfo.getUsername());

        if (playerSelection.isInQueue(player1.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("Player already in queue", null));
        }
        try {

            PlayerDTO playerDTO = modelMapper.map(player1, PlayerDTO.class);
            List<ProgProblem> problemsFromUserModel = player1.getSolvedProblems();

            List<ProblemDTO> problemDTOs = mapProblemsToDTOs(problemsFromUserModel);

//            problemDTOs.add(new ProblemDTO("A", 0, "4", "A", 800));

            String problemURL = (String) problemPicker.pickProblem(playerDTO, problemDTOs).getBody();
            List<UserModel> players1 = List.of(player1);

            // Create a new SoloMatch and save it to the repository
            LocalDateTime endTime = LocalDateTime.now().plusMinutes(60);
            SoloMatch soloMatch = new SoloMatch(players1, problemURL, LocalDateTime.now(), endTime, 60.0);


            soloMatch = gameRepository.save(soloMatch);

            playerSelection.activeSoloDuration(soloMatch.getId());

            ProblemDTO problemDTO = problemParser.parseCodeforcesUrl(problemURL);

            ResponseEntity<Object> problemParsed = problemParser.parseFullProblem(problemDTO.getContestId(), problemDTO.getIndex());

            // Convert the SoloMatch entity to its corresponding DTO
            SoloMatchDTO soloMatchDTO = new SoloMatchDTO(soloMatch, problemParsed.getBody());

            player1.setExistingGameId(soloMatch.getId());
            userRepository.save(player1);

            // Return a success response with the SoloMatchDTO
            return ResponseEntity.ok(ResponseUtils.successfulRes("Match started successfully", soloMatchDTO));
        } catch (Exception e) {
            // Handle any exceptions that may occur during the match creation or saving process
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Error starting match", e.getMessage()));
        }
    }

    public ResponseEntity<Object> leaveQueue() {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel player = userRepository.findByUsername(userInfo.getUsername());
            String queueType = playerSelection.whichQueue(player.getId());
            if (queueType.equals("Ranked")) {
                playerSelection.leaveRankedQueue(player.getId());
            } else if (queueType.equals("Casual")) {
                playerSelection.leaveCasualQueue(player.getId());
            } else if (queueType.equals("Velocity")) {
                playerSelection.leaveVelocityQueue(player.getId());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("Player not in queue", null));
            }
            return ResponseEntity.ok(ResponseUtils.successfulRes("Player removed from queue successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Error removing player from queue", e.getMessage()));
        }
    }

    public ResponseEntity<Object> leaveRankedQueue() {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel player = userRepository.findByUsername(userInfo.getUsername());

            if (playerSelection.isInRankedQueue(player.getId())) {
                playerSelection.leaveRankedQueue(player.getId());
                return ResponseEntity.ok(ResponseUtils.successfulRes("Player removed from queue successfully", null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("Player not in queue", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Error removing player from queue", e.getMessage()));
        }
    }

    public ResponseEntity<Object> leaveCasualQueue() {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel player = userRepository.findByUsername(userInfo.getUsername());

            if (playerSelection.isInCasualQueue(player.getId())) {
                playerSelection.leaveCasualQueue(player.getId());
                return ResponseEntity.ok(ResponseUtils.successfulRes("Player removed from queue successfully", null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("Player not in queue", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Error removing player from queue", e.getMessage()));
        }
    }

    public ResponseEntity<Object> leaveVelocityQueue() {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel player = userRepository.findByUsername(userInfo.getUsername());

            if (playerSelection.isInVelocityQueue(player.getId())) {
                playerSelection.leaveVelocityQueue(player.getId());
                return ResponseEntity.ok(ResponseUtils.successfulRes("Player removed from queue successfully", null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("Player not in queue", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Error removing player from queue", e.getMessage()));
        }
    }

    public ResponseEntity<Object> getUserQueue() {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel player = userRepository.findByUsername(userInfo.getUsername());
        String queueType = playerSelection.whichQueue(player.getId());
        return ResponseEntity.ok(ResponseUtils.successfulRes(queueType, null));
    }

    public ResponseEntity<Object> getUserRankedQueue() {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel player = userRepository.findByUsername(userInfo.getUsername());
        if (playerSelection.isInRankedQueue(player.getId())) {
            return ResponseEntity.ok(ResponseUtils.successfulRes("YES", null));
        } else {
            return ResponseEntity.ok(ResponseUtils.successfulRes("NO", null));
        }
    }

    public ResponseEntity<Object> getUserCasualQueue() {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel player = userRepository.findByUsername(userInfo.getUsername());
        if (playerSelection.isInCasualQueue(player.getId())) {
            return ResponseEntity.ok(ResponseUtils.successfulRes("YES", null));
        } else {
            return ResponseEntity.ok(ResponseUtils.successfulRes("NO", null));
        }
    }

    public ResponseEntity<Object> getUserVelocityQueue() {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel player = userRepository.findByUsername(userInfo.getUsername());
        if (playerSelection.isInVelocityQueue(player.getId())) {
            return ResponseEntity.ok(ResponseUtils.successfulRes("YES", null));
        } else {
            return ResponseEntity.ok(ResponseUtils.successfulRes("NO", null));
        }
    }

    public ResponseEntity<Object> getMatch(Long id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("ID must not be null", null));
        }

        Optional<Game> optionalGame = gameRepository.findById(id);
        if (optionalGame.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Game not found", null));
        }

        try {
            Game game = optionalGame.get();

            ProblemDTO problemDTO = problemParser.parseCodeforcesUrl(game.getProblemUrl());

            ResponseEntity<Object> problemParsed = problemParser.parseFullProblem(problemDTO.getContestId(), problemDTO.getIndex());


            GameDTO gameDTO;

            if (game instanceof RankedMatch) {
                gameDTO = new RankedMatchDTO((RankedMatch) game, problemParsed.getBody());
            } else if (game instanceof SoloMatch) {
                gameDTO = new SoloMatchDTO((SoloMatch) game, problemParsed.getBody());
            } else if (game instanceof CasualMatch) {
                gameDTO = new CasualMatchDTO((CasualMatch) game, problemParsed.getBody());
            } else if (game instanceof BeatAFriend) {
                gameDTO = new BeatAFriendDTO((BeatAFriend) game, problemParsed.getBody());
            } else if (game instanceof VelocityMatch) {
                gameDTO = new VelocityMatchDTO((VelocityMatch) game, problemParsed.getBody());
            } else if (game instanceof TeamMatch) {
                ProblemDTO problemDTO2 = problemParser.parseCodeforcesUrl(((TeamMatch) game).getProblemUrl2());
                ProblemDTO problemDTO3 = problemParser.parseCodeforcesUrl(((TeamMatch) game).getProblemUrl3());

                ResponseEntity<Object> problem2Parsed = problemParser.parseFullProblem(problemDTO2.getContestId(), problemDTO2.getIndex());
                ResponseEntity<Object> problem3Parsed = problemParser.parseFullProblem(problemDTO3.getContestId(), problemDTO3.getIndex());

                gameDTO = new TeamMatchDTO((TeamMatch) game, problemParsed.getBody(), problem2Parsed.getBody(), problem3Parsed.getBody());
            } else {
                gameDTO = new GameDTO(game);
            }
            gameDTO.setPlayers1Ids(game.getPlayers1().stream().map(UserModel::getId).collect(Collectors.toList()));
            gameDTO.setPlayers2Ids(game.getPlayers2().stream().map(UserModel::getId).collect(Collectors.toList()));

//            gameDTO.setProblems1ID(List.of({}));
            return ResponseEntity.ok(ResponseUtils.successfulRes("Match started successfully", gameDTO));
        } catch (
                Exception e) {
            // Handle any exceptions that may occur during the match creation or saving process
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Error getting match", e.getMessage()));
        }
    }
//    public List<Boolean> setProblemsSolvedForATeam(List<UserModel> users,Game game){
//        List<Boolean> problemsSolved = new ArrayList<>();
//
//    }

    public ResponseEntity<Object> getMatchInvitation(Long id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("ID must not be null", null));
        }

        Optional<MatchInvitation> optionalInvitation = matchInvitationRepository.findById(id);
        if (optionalInvitation.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Match invitation not found", null));
        }

        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel currUser = userRepository.findByUsername(userInfo.getUsername());


        try {
            MatchInvitation invitation = optionalInvitation.get();

            if (invitation instanceof FriendMatchInvitation) {
                UserModel User1;
                UserModel User2;
                if (currUser.getId().equals(invitation.getSender().getId())) {
                    User1 = invitation.getSender();
                    User2 = invitation.getReceiver();
                } else {
                    User1 = invitation.getReceiver();
                    User2 = invitation.getSender();
                }

                FriendMatchInvitation friendMatchInvitation = (FriendMatchInvitation) invitation;
                FriendMatchInvitationDTO dto = new FriendMatchInvitationDTO(friendMatchInvitation);

                PlayerDisplayDTO player1 = new PlayerDisplayDTO(User1.getUsername(), User1.getAccountColor(), User1.getPhotoUrl());
                PlayerDisplayDTO player2 = new PlayerDisplayDTO(User2.getUsername(), User2.getAccountColor(), User2.getPhotoUrl());

                dto.setTeam1Players(List.of(player1));
                dto.setTeam2Players(List.of(player2));
                dto.setSender(invitation.getSender().getId().equals(currUser.getId()));

                return ResponseEntity.ok(ResponseUtils.successfulRes("Friend match invitation retrieved successfully", dto));
            } else if (invitation instanceof TeamMatchInvitation) {
                TeamModel team1;
                TeamModel team2;

                if (((TeamMatchInvitation) invitation).getTeam1().getMembers().stream().anyMatch(user -> user.getId().equals(currUser.getId()))) {
                    team1 = ((TeamMatchInvitation) invitation).getTeam1();
                    team2 = ((TeamMatchInvitation) invitation).getTeam2();
                } else {
                    team2 = ((TeamMatchInvitation) invitation).getTeam1();
                    team1 = ((TeamMatchInvitation) invitation).getTeam2();
                }

                List<PlayerDisplayDTO> team1Players = team1.getMembers().stream().map(teamMember -> {
                    UserModel user = teamMember.getUser();
                    return new PlayerDisplayDTO(user.getUsername(), user.getAccountColor(), user.getPhotoUrl());
                }).toList();

                List<PlayerDisplayDTO> team2Players = team2.getMembers().stream().map(teamMember -> {
                    UserModel user = teamMember.getUser();
                    return new PlayerDisplayDTO(user.getUsername(), user.getAccountColor(), user.getPhotoUrl());
                }).toList();


                TeamMatchInvitation teamMatchInvitation = (TeamMatchInvitation) invitation;
                TeamMatchInvitationDTO dto = new TeamMatchInvitationDTO(teamMatchInvitation);

                dto.setTeam1Players(team1Players);
                dto.setTeam2Players(team2Players);
                dto.setSender(invitation.getSender().getId().equals(currUser.getId()));

                return ResponseEntity.ok(ResponseUtils.successfulRes("Team match invitation retrieved successfully", dto));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("Unknown invitation type", null));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Error getting match invitation", e.getMessage()));
        }
    }

    public ResponseEntity<Object> leaveMatch(Long gameId) {
        Game game = gameRepository.findById(gameId).orElse(null);
        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtils.unsuccessfulRes("Match not found", null));
        }

        List<UserModel> players1 = game.getPlayers1();
        List<UserModel> players2 = game.getPlayers2();

        // Check if both sides have at least one player
        if ((players1.isEmpty() || players2.isEmpty()) && !game.getGameType().getValue().equals(GameType.SOLO.getValue())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtils.unsuccessfulRes("Match is already incomplete", null));
        }

        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel leavingPlayer = userRepository.findByUsername(userInfo.getUsername());

        List<UserModel> remainingPlayers;
        List<UserModel> leavingPlayers;

        // Determine which side the leaving player is on
        if (players1.contains(leavingPlayer)) {
            leavingPlayers = players1;
            remainingPlayers = players2;
        } else if (players2.contains(leavingPlayer)) {
            leavingPlayers = players2;
            remainingPlayers = players1;
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtils.unsuccessfulRes("Player is not part of this match", null));
        }

        // Create and save the notification
        MatchNotificationModel matchNotification = new MatchNotificationModel();
        matchNotification.setBody("Your opponent has left the match.");
        matchNotification.setSender(leavingPlayer);
        matchNotification.setCreatedAt(new Date());
        matchNotification.setGame(game);
        matchNotification.setNotificationType(NotificationType.Leave_Game);

        notificationRepository.save(matchNotification);

        // Update the game status or perform necessary logic
        game.setGameStatus(GameStatus.OPPONENT_LEFT);
        game.setEndTime(LocalDateTime.now());
        gameRepository.save(game);


//        if(leavingPlayers.size() == 1 && remainingPlayers.size() == 1) {
//            // Update the player's rating and save
//            leavingPlayers.get(0).setUser_rating(leavingPlayers.get(0).getUser_rating() - 30);
//            userRepository.save(leavingPlayers.get(0));
//        }

        // Create and send the notification DTO using setters
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setBody(matchNotification.getBody());
        notificationDTO.setSender(matchNotification.getSender().getUsername());
        notificationDTO.setNotificationType(matchNotification.getNotificationType());
        notificationDTO.setCreatedAt(matchNotification.getCreatedAt());
        notificationDTO.setRead(matchNotification.isRead());
        notificationDTO.setId(matchNotification.getId());
        notificationDTO.setColor(matchNotification.getSender().getAccountColor());

        String gameType;
        if (game instanceof RankedMatch) {
            gameType = "Ranked";
        } else if (game instanceof SoloMatch) {
            gameType = "Solo";

        } else if (game instanceof CasualMatch) {
            gameType = "Casual";
        } else if (game instanceof VelocityMatch) {
            gameType = "Velocity";
        } else if (game instanceof BeatAFriend) {
            gameType = "Friendly";
        } else if (game instanceof TeamMatch) {
            gameType = "Team";
        } else {
            gameType = "Unknown";
        }

        // Send notification to remaining players
        for (UserModel remainingPlayer : remainingPlayers) {
            remainingPlayer.setExistingGameId(null);

            List<Submission> submissions = submissionRepository.findByUserId(remainingPlayer.getId(), gameId);
            List<SubmissionDTO> submissionDTOS = submissions.stream().map(sub -> {
                SubmissionDTO subDTO = modelMapper.map(sub, SubmissionDTO.class);
                subDTO.setVerdict(matchStatusMapper.getStatusIntToString().get(sub.getResult()));
                return subDTO;
            }).toList();
            GameResultDTO gameResultDTO = new GameResultDTO(gameType, "Cancelled", remainingPlayer.getUser_rating(), submissionDTOS);

            GameHistory gameHistory1 = new GameHistory();
            gameHistory1.setGameDate(game.getStartTime());
            gameHistory1.setGameType(gameType);
            gameHistory1.setGameResult("Cancelled");
            gameHistory1.setRatingChange(0);
            gameHistory1.setNewRating(remainingPlayer.getUser_rating());
            gameHistory1.setUserId(remainingPlayer.getId());
            gameHistoryRepository.save(gameHistory1);

            ProblemDTO problemDTO1 = problemPicker.getProblemByUrl(game.getProblemUrl());
            ProgProblem progProblem1 = modelMapper.map(problemDTO1, ProgProblem.class);

            Map<Integer, Consumer<UserModel>> ratingIncrementer = createRatingIncrementers();

            ratingIncrementer.getOrDefault(problemDTO1.getRating(), player -> {
            }).accept(remainingPlayer);

            progProblem1.setUser(remainingPlayer);
            remainingPlayer.getSolvedProblems().add(progProblem1);

            userRepository.save(remainingPlayer);
//            messagingTemplate.convertAndSendToUser(remainingPlayer.getUsername(), "/notification", notificationDTO);
            messagingTemplate.convertAndSendToUser(remainingPlayer.getUsername(), "/games", gameResultDTO);
        }

        GameResultDTO leavingPlayerGameResult = new GameResultDTO();

        for (UserModel lp : leavingPlayers) {
            lp.setExistingGameId(null);
            int newRating = lp.getUser_rating();
            if (gameType.equals("Ranked")) {
                newRating -= 30;
            }


            GameHistory gameHistory1 = new GameHistory();
            gameHistory1.setGameDate(game.getStartTime());
            gameHistory1.setGameType(gameType);
            gameHistory1.setGameResult("Left");
            gameHistory1.setRatingChange(newRating - lp.getUser_rating());
            gameHistory1.setNewRating(newRating);
            gameHistory1.setUserId(lp.getId());
            gameHistoryRepository.save(gameHistory1);

            lp.setUser_rating(newRating);
            lp.setUser_max_rating(Math.max(lp.getUser_max_rating(), newRating));

            ProblemDTO problemDTO1 = problemPicker.getProblemByUrl(game.getProblemUrl());
            ProgProblem progProblem1 = modelMapper.map(problemDTO1, ProgProblem.class);

            Map<Integer, Consumer<UserModel>> ratingIncrementer = createRatingIncrementers();


            ratingIncrementer.getOrDefault(problemDTO1.getRating(), player -> {
            }).accept(lp);

            progProblem1.setUser(lp);
            lp.getSolvedProblems().add(progProblem1);

            userRepository.save(lp);

            List<Submission> submissions = submissionRepository.findByUserId(lp.getId(), gameId);
            List<SubmissionDTO> submissionDTOS = submissions.stream().map(sub -> {
                SubmissionDTO subDTO = modelMapper.map(sub, SubmissionDTO.class);
                subDTO.setVerdict(matchStatusMapper.getStatusIntToString().get(sub.getResult()));
                return subDTO;
            }).toList();

            GameResultDTO gameResultDTO = new GameResultDTO(gameType, "Cancelled", lp.getUser_rating(), submissionDTOS);
            if (!lp.getId().equals(leavingPlayer.getId())) {
                messagingTemplate.convertAndSendToUser(lp.getUsername(), "/games", gameResultDTO);
            } else {
                leavingPlayerGameResult = gameResultDTO;
            }
        }

        return ResponseEntity.ok(ResponseUtils.successfulRes("Left the match successfully", leavingPlayerGameResult));
    }

    public ResponseEntity<Object> getUserHistory() {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel user = userRepository.findByUsername(userInfo.getUsername());

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("User not found", null));
            }

            List<Game> games = gameRepository.findGamesByUser(user);

            List<GameResultDTO> gameResultDTOs = games.stream().map(game -> {
                try {
                    ProblemDTO problemDTO = problemParser.parseCodeforcesUrl(game.getProblemUrl());
                    ResponseEntity<Object> problemParsed = problemParser.parseFullProblem(problemDTO.getContestId(), problemDTO.getIndex());
                    Object parsedProblem = problemParsed.getBody();

                    GameDTO gameDTO;
                    if (game instanceof SoloMatch) {
                        gameDTO = new SoloMatchDTO((SoloMatch) game, parsedProblem);
                    } else if (game instanceof RankedMatch) {
                        gameDTO = new RankedMatchDTO((RankedMatch) game, parsedProblem);
                    } else if (game instanceof CasualMatch) {
                        gameDTO = new CasualMatchDTO((CasualMatch) game, parsedProblem);
                    } else if (game instanceof VelocityMatch) {
                        gameDTO = new VelocityMatchDTO((VelocityMatch) game, parsedProblem);
                    } else {
                        gameDTO = new GameDTO(game); // Default DTO if no specific type matches
                    }

                    List<Submission> submissions = submissionRepository.findByUserId(user.getId(), game.getId());
                    List<SubmissionDTO> submissionDTOS = submissions.stream().map(sub -> {
                        SubmissionDTO subDTO = modelMapper.map(sub, SubmissionDTO.class);
                        subDTO.setVerdict(matchStatusMapper.getStatusIntToString().get(sub.getResult()));
                        return subDTO;
                    }).toList();

                    return new GameResultDTO(game.getGameType().toString(), "Completed", user.getUser_rating(), submissionDTOS);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null; // Or handle the exception appropriately
                }
            }).filter(dto -> dto != null).toList();

            return ResponseEntity.ok(ResponseUtils.successfulRes("User history retrieved successfully", gameResultDTOs));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("An error occurred while retrieving user history", null));
        }
    }

    public ResponseEntity<Object> submitCode(Long gameId, ProblemSubmitDTO problemSubmitDTO) throws Exception {
        Game game = gameRepository.findById(gameId).orElse(null);
        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Match not found", null));
        }

        if (game.getGameStatus() != null && game.getGameStatus().getValue().equals(GameStatus.FINISHED.getValue())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("Match is already finished", null));
        }

        if (game instanceof RankedMatch) {
            return submitCodeteam2team(game, problemSubmitDTO);
        } else if (game instanceof TeamMatch) {
            return submitCodeteam2team(game, problemSubmitDTO);
        } else if (game instanceof CasualMatch) {
            return submitCodeteam2team(game, problemSubmitDTO);
        } else if (game instanceof VelocityMatch) {
            return submitCodeteam2team(game, problemSubmitDTO);
        } else if (game instanceof BeatAFriend) {
            return submitCodeteam2team(game, problemSubmitDTO);
        } else if (game instanceof SoloMatch) {
            return submitCodeSolo(game, problemSubmitDTO);
        } else return null;
    }

    private ResponseEntity<Object> submitCodeSolo(Game game, ProblemSubmitDTO problemSubmitDTO) throws Exception {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel submittingPlayer = userRepository.findByUsername(userInfo.getUsername());

        // Ensure that the submitting player is one of the players in the game
        if (!game.getPlayers1().get(0).getId().equals(submittingPlayer.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseUtils.unsuccessfulRes("You do not own this game", null));
        }

        Integer verdict = codeforcesSubmissionService.submitCode(game.getProblemUrl(), problemSubmitDTO);
        String codeForceResponse = matchStatusMapper.getStatusIntToString().get(verdict); // default response, simulate the actual submission process

        Submission submission = new Submission(game, submittingPlayer, blobConverter.convertObjectToBlob(problemSubmitDTO.getCode()), problemSubmitDTO.getLanguage(), LocalDateTime.now(), verdict, game.getProblemUrl());
        submissionRepository.save(submission);


        if (codeForceResponse.equals("Accepted")) {
            // End the game
            game.setGameStatus(GameStatus.FINISHED);
            game.setEndTime(LocalDateTime.now());
            gameRepository.save(game);

            storeGameHistory(List.of(submittingPlayer), List.of(), game);

            ProblemDTO problemDTO = problemPicker.getProblemByUrl(game.getProblemUrl());

            ProgProblem progProblem = modelMapper.map(problemDTO, ProgProblem.class);
            progProblem.setUser(submittingPlayer);
            submittingPlayer.getSolvedProblems().add(progProblem);

            submittingPlayer.setExistingGameId(null);
            userRepository.save(submittingPlayer);

            List<Submission> submissions = submissionRepository.findByUserId(submittingPlayer.getId(), game.getId());
            List<SubmissionDTO> submissionDTOS = submissions.stream().map(sub -> {
                SubmissionDTO submissionDTO = modelMapper.map(sub, SubmissionDTO.class);
                Duration duration = Duration.between(game.getStartTime(), sub.getSubmissionTime());
                submissionDTO.setSubmissionTime(duration.getSeconds() / 60 + " min");
                submissionDTO.setVerdict(matchStatusMapper.getStatusIntToString().get(sub.getResult()));
                return submissionDTO;
            }).toList();
            GameResultDTO gameResultDTO = new GameResultDTO("Solo", "You Won", submittingPlayer.getUser_rating(), submissionDTOS);

            messagingTemplate.convertAndSendToUser(submittingPlayer.getUsername(), "/games", gameResultDTO);
            return ResponseEntity.ok(ResponseUtils.successfulRes("Match", null));
        }

        return ResponseEntity.ok(ResponseUtils.successfulRes("Code submitted successfully", codeForceResponse));
    }

    private Map<Integer, Consumer<UserModel>> createRatingIncrementers() {
        Map<Integer, Consumer<UserModel>> ratingIncrementers = new HashMap<>();

        ratingIncrementers.put(800, player -> player.setRate_800_cnt(player.getRate_800_cnt() + 1));
        ratingIncrementers.put(900, player -> player.setRate_900_cnt(player.getRate_900_cnt() + 1));
        ratingIncrementers.put(1000, player -> player.setRate_1000_cnt(player.getRate_1000_cnt() + 1));
        ratingIncrementers.put(1100, player -> player.setRate_1100_cnt(player.getRate_1100_cnt() + 1));
        ratingIncrementers.put(1200, player -> player.setRate_1200_cnt(player.getRate_1200_cnt() + 1));
        ratingIncrementers.put(1300, player -> player.setRate_1300_cnt(player.getRate_1300_cnt() + 1));
        ratingIncrementers.put(1400, player -> player.setRate_1400_cnt(player.getRate_1400_cnt() + 1));
        ratingIncrementers.put(1500, player -> player.setRate_1500_cnt(player.getRate_1500_cnt() + 1));
        ratingIncrementers.put(1600, player -> player.setRate_1600_cnt(player.getRate_1600_cnt() + 1));
        ratingIncrementers.put(1700, player -> player.setRate_1700_cnt(player.getRate_1700_cnt() + 1));
        ratingIncrementers.put(1800, player -> player.setRate_1800_cnt(player.getRate_1800_cnt() + 1));
        ratingIncrementers.put(1900, player -> player.setRate_1900_cnt(player.getRate_1900_cnt() + 1));
        ratingIncrementers.put(2000, player -> player.setRate_2000_cnt(player.getRate_2000_cnt() + 1));
        ratingIncrementers.put(2100, player -> player.setRate_2100_cnt(player.getRate_2100_cnt() + 1));
        ratingIncrementers.put(2200, player -> player.setRate_2200_cnt(player.getRate_2200_cnt() + 1));
        ratingIncrementers.put(2300, player -> player.setRate_2300_cnt(player.getRate_2300_cnt() + 1));
        ratingIncrementers.put(2400, player -> player.setRate_2400_cnt(player.getRate_2400_cnt() + 1));
        ratingIncrementers.put(2500, player -> player.setRate_2500_cnt(player.getRate_2500_cnt() + 1));
        ratingIncrementers.put(2600, player -> player.setRate_2600_cnt(player.getRate_2600_cnt() + 1));
        ratingIncrementers.put(2700, player -> player.setRate_2700_cnt(player.getRate_2700_cnt() + 1));
        ratingIncrementers.put(2800, player -> player.setRate_2800_cnt(player.getRate_2800_cnt() + 1));
        ratingIncrementers.put(2900, player -> player.setRate_2900_cnt(player.getRate_2900_cnt() + 1));
        ratingIncrementers.put(3000, player -> player.setRate_3000_cnt(player.getRate_3000_cnt() + 1));
        ratingIncrementers.put(3100, player -> player.setRate_3100_cnt(player.getRate_3100_cnt() + 1));
        ratingIncrementers.put(3200, player -> player.setRate_3200_cnt(player.getRate_3200_cnt() + 1));
        ratingIncrementers.put(3300, player -> player.setRate_3300_cnt(player.getRate_3300_cnt() + 1));
        ratingIncrementers.put(3400, player -> player.setRate_3400_cnt(player.getRate_3400_cnt() + 1));
        ratingIncrementers.put(3500, player -> player.setRate_3500_cnt(player.getRate_3500_cnt() + 1));

        return ratingIncrementers;
    }

    private void storeGameHistory(List<UserModel> submittingPlayers, List<UserModel> remainingPlayers, Game game) {
        String gameType;
        if (game instanceof RankedMatch) {
            gameType = "Ranked";
        } else if (game instanceof CasualMatch) {
            gameType = "Casual";
        } else if (game instanceof VelocityMatch) {
            gameType = "Velocity";
        } else if (game instanceof BeatAFriend) {
            gameType = "Friendly";
        } else if (game instanceof TeamMatch) {
            gameType = "Team";
        } else {
            gameType = "Solo";
        }
        if (gameType.equals("Ranked")) {
            int player1Rating = submittingPlayers.get(0).getUser_rating();
            int player2Rating = remainingPlayers.get(0).getUser_rating();
            int newPlayer1Rating = ratingSystemCalculator.calculateDeltaRating(player2Rating - player1Rating, player1Rating, 'W');
            int newPlayer2Rating = ratingSystemCalculator.calculateDeltaRating(player1Rating - player2Rating, player2Rating, 'L');
            submittingPlayers.get(0).setUser_max_rating(Math.max(submittingPlayers.get(0).getUser_max_rating(), newPlayer1Rating));
            submittingPlayers.get(0).setUser_rating(newPlayer1Rating);
            submittingPlayers.get(0).setWins(submittingPlayers.get(0).getWins() + 1);
            submittingPlayers.get(0).setExistingGameId(null);
            remainingPlayers.get(0).setUser_max_rating(Math.max(remainingPlayers.get(0).getUser_max_rating(), newPlayer2Rating));
            remainingPlayers.get(0).setUser_rating(newPlayer2Rating);
            remainingPlayers.get(0).setLosses(remainingPlayers.get(0).getLosses() + 1);
            remainingPlayers.get(0).setExistingGameId(null);

            ProblemDTO problemDTO = problemPicker.getProblemByUrl(game.getProblemUrl());
            ProgProblem progProblem = modelMapper.map(problemDTO, ProgProblem.class);

            Map<Integer, Consumer<UserModel>> ratingIncrementer = createRatingIncrementers();

            // Increment the count based on problem rating
            ratingIncrementer.getOrDefault(problemDTO.getRating(), player -> {
            }).accept(submittingPlayers.get(0));

            // Associate problem with user and save
            progProblem.setUser(submittingPlayers.get(0));
            submittingPlayers.get(0).getSolvedProblems().add(progProblem);

            userRepository.save(submittingPlayers.get(0));
            userRepository.save(remainingPlayers.get(0));


            GameHistory gameHistory1 = new GameHistory();
            gameHistory1.setGameDate(game.getStartTime());
            gameHistory1.setGameType("Ranked");
            gameHistory1.setGameResult("Win");
            gameHistory1.setRatingChange(newPlayer1Rating - player1Rating);
            gameHistory1.setNewRating(newPlayer1Rating);
            gameHistory1.setUserId(submittingPlayers.get(0).getId());
            gameHistoryRepository.save(gameHistory1);

            GameHistory gameHistory2 = new GameHistory();
            gameHistory2.setGameDate(game.getStartTime());
            gameHistory2.setGameType("Ranked");
            gameHistory2.setGameResult("Lose");
            gameHistory2.setRatingChange(newPlayer2Rating - player2Rating);
            gameHistory2.setNewRating(newPlayer2Rating);
            gameHistory2.setUserId(remainingPlayers.get(0).getId());
            gameHistoryRepository.save(gameHistory2);
        } else {
            for (UserModel sP : submittingPlayers) {
                GameHistory gameHistory1 = new GameHistory();
                gameHistory1.setGameDate(game.getStartTime());
                gameHistory1.setGameType(gameType);
                gameHistory1.setGameResult("Win");
                gameHistory1.setRatingChange(0);
                gameHistory1.setNewRating(sP.getUser_rating());
                gameHistory1.setUserId(sP.getId());
                gameHistoryRepository.save(gameHistory1);
                sP.setExistingGameId(null);

                ProblemDTO problemDTO = problemPicker.getProblemByUrl(game.getProblemUrl());
                ProgProblem progProblem = modelMapper.map(problemDTO, ProgProblem.class);

                Map<Integer, Consumer<UserModel>> ratingIncrementer = createRatingIncrementers();

                // Increment the count based on problem rating
                ratingIncrementer.getOrDefault(problemDTO.getRating(), player -> {
                }).accept(sP);

                // Associate problem with user and save
                progProblem.setUser(sP);
                sP.getSolvedProblems().add(progProblem);

                userRepository.save(sP);

            }
            for (UserModel lp : remainingPlayers) {
                GameHistory gameHistory2 = new GameHistory();
                gameHistory2.setGameDate(game.getStartTime());
                gameHistory2.setGameType(gameType);
                gameHistory2.setGameResult("Lose");
                gameHistory2.setRatingChange(0);
                gameHistory2.setNewRating(lp.getUser_rating());
                gameHistory2.setUserId(lp.getId());
                gameHistoryRepository.save(gameHistory2);

                ProblemDTO problemDTO = problemPicker.getProblemByUrl(game.getProblemUrl());
                ProgProblem progProblem = modelMapper.map(problemDTO, ProgProblem.class);

                Map<Integer, Consumer<UserModel>> ratingIncrementer = createRatingIncrementers();

                // Increment the count based on problem rating
                ratingIncrementer.getOrDefault(problemDTO.getRating(), player -> {
                }).accept(lp);

                // Associate problem with user and save
                progProblem.setUser(lp);
                lp.getSolvedProblems().add(progProblem);

                userRepository.save(lp);
            }
        }
    }

    private ResponseEntity<Object> submitCodeteam2team(Game game, ProblemSubmitDTO problemSubmitDTO) throws Exception {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel submittingPlayer = userRepository.findByUsername(userInfo.getUsername());

        List<UserModel> players1 = game.getPlayers1();
        List<UserModel> players2 = game.getPlayers2();

        // Determine the remaining player
        List<UserModel> remainingPlayers;
        List<UserModel> submittingPlayers;

        if (players1.contains(submittingPlayer)) {
            submittingPlayers = players1;
            remainingPlayers = players2;
        } else if (players2.contains(submittingPlayer)) {
            submittingPlayers = players2;
            remainingPlayers = players1;
        } else {
            throw new IllegalArgumentException("Submitting player is not part of this game");
        }

        String problemURL = game.getProblemUrl();

        if (game instanceof TeamMatch) {
            if (problemSubmitDTO.getProblemNumber() == 2) {
                problemURL = ((TeamMatch) game).getProblemUrl2();
            } else if (problemSubmitDTO.getProblemNumber() == 3) {
                problemURL = ((TeamMatch) game).getProblemUrl3();
            }
        }


        Integer verdict = codeforcesSubmissionService.submitCode(problemURL, problemSubmitDTO);

        String codeForceResponse = matchStatusMapper.getStatusIntToString().get(verdict); // Default response, simulate the actual submission process

        Submission submission = new Submission(game, submittingPlayer, blobConverter.convertObjectToBlob(problemSubmitDTO.getCode()), problemSubmitDTO.getLanguage(), LocalDateTime.now(), verdict, problemURL);
        submissionRepository.save(submission);
        if (codeForceResponse.equals("Accepted")) {
            if (!(game instanceof TeamMatch) || isAllAccepted(submittingPlayers, game)) {

                // End the game and notify the remaining player that they lost
                game.setGameStatus(GameStatus.FINISHED);
                game.setEndTime(LocalDateTime.now());
                gameRepository.save(game);
            }

            storeGameHistory(submittingPlayers, remainingPlayers, game);

            if (submittingPlayers.size() == 1 && remainingPlayers.size() == 1) {
                // Update delta of the players
                int newPlayer1Rating = submittingPlayers.get(0).getUser_rating();
                int newPlayer2Rating = remainingPlayers.get(0).getUser_rating();

                List<Submission> submissions = game.getSubmissions();
                List<SubmissionDTO> player1Submissions = new ArrayList<>();
                List<SubmissionDTO> player2Submissions = new ArrayList<>();

                submissions.forEach(sub -> {
                    SubmissionDTO submissionDTO = modelMapper.map(sub, SubmissionDTO.class);
                    Duration duration = Duration.between(game.getStartTime(), sub.getSubmissionTime());
                    submissionDTO.setSubmissionTime(duration.getSeconds() / 60 + " min");
                    submissionDTO.setVerdict(matchStatusMapper.getStatusIntToString().get(sub.getResult()));

                    if (sub.getUser().getId().equals(submittingPlayers.get(0).getId())) {
                        player1Submissions.add(submissionDTO);
                    } else {
                        player2Submissions.add(submissionDTO);
                    }
                });

                GameResultDTO player1GameResult = new GameResultDTO("Ranked", "You Won", newPlayer1Rating, player1Submissions);
                GameResultDTO player2GameResult = new GameResultDTO("Ranked", "You Lost", newPlayer2Rating, player2Submissions);

                messagingTemplate.convertAndSendToUser(submittingPlayers.get(0).getUsername(), "/games", player1GameResult);
                messagingTemplate.convertAndSendToUser(remainingPlayers.get(0).getUsername(), "/games", player2GameResult);

            }
            else if (isAllAccepted(submittingPlayers, game)) {
                for (UserModel sP : submittingPlayers) {
                    sP.setExistingGameId(null);
                    userRepository.save(sP);
                    List<Submission> submissions = submissionRepository.findByUserId(sP.getId(), game.getId());
                    List<SubmissionDTO> submissionDTOS = submissions.stream().map(sub -> {
                        SubmissionDTO subDTO = modelMapper.map(sub, SubmissionDTO.class);
                        subDTO.setVerdict(matchStatusMapper.getStatusIntToString().get(sub.getResult()));
                        return subDTO;
                    }).toList();

                    GameResultDTO gameResultDTO = new GameResultDTO("Team", "You Won", sP.getUser_rating(), submissionDTOS);
                    messagingTemplate.convertAndSendToUser(sP.getUsername(), "/games", gameResultDTO);
                }
                for (UserModel rP : remainingPlayers) {
                    rP.setExistingGameId(null);
                    userRepository.save(rP);
                    List<Submission> submissions = submissionRepository.findByUserId(rP.getId(), game.getId());
                    List<SubmissionDTO> submissionDTOS = submissions.stream().map(sub -> {
                        SubmissionDTO subDTO = modelMapper.map(sub, SubmissionDTO.class);
                        subDTO.setVerdict(matchStatusMapper.getStatusIntToString().get(sub.getResult()));
                        return subDTO;
                    }).toList();

                    GameResultDTO gameResultDTO = new GameResultDTO("Team", "You Lost", rP.getUser_rating(), submissionDTOS);
                    messagingTemplate.convertAndSendToUser(rP.getUsername(), "/games", gameResultDTO);
                }
            }

            return ResponseEntity.ok(ResponseUtils.successfulRes("Match", null));
        } else {
            return ResponseEntity.ok(ResponseUtils.successfulRes("Code submitted successfully", codeForceResponse));
        }
    }

    private boolean isAllAccepted(List<UserModel> users, Game game) {
        Set<String> st = new HashSet<>();
        for (UserModel user : users) {
            List<Submission> submissions = submissionRepository.findByUserId(user.getId(), game.getId());
            for (Submission submission : submissions) {
                st.add(submission.getProblemUrl());
            }
        }
        return (st.size() == 3);
    }


    private MatchNotificationModel createNotification(String body, UserModel sender, UserModel receiver, Game game, NotificationType type) {
        MatchNotificationModel notification = new MatchNotificationModel();
        notification.setBody(body);
        notification.setSender(sender);
        notification.setCreatedAt(new Date());
        notification.setReceiver(receiver);
        notification.setGame(game);
        notification.setNotificationType(type);
        notificationRepository.save(notification);
        return notification;
    }

    private List<String> get3problems(TeamModel team1, TeamModel team2) throws Exception {

        List<UserModel> users = new ArrayList<>();
        users.addAll(team1.getMembers().stream().map(TeamMember::getUser).toList());
        users.addAll(team2.getMembers().stream().map(TeamMember::getUser).toList());

        List<Pair<Integer, PlayerDTO>> players = new ArrayList<>();

        for (UserModel user : users) {
            PlayerDTO playerDTO = modelMapper.map(user, PlayerDTO.class);
            players.add(new Pair<>(user.getUser_rating(), playerDTO));
        }

        //sorting players array
        players.sort((a, b) -> {
            return a.getFirst().compareTo(b.getFirst());
        });

        List<ProblemDTO> problemDTOs = new ArrayList<>();

        for (UserModel user : users) {
            problemDTOs.addAll(user.getSolvedProblems().stream().map(progProblem -> {
                return modelMapper.map(progProblem, ProblemDTO.class);
            }).toList());
        }

        PlayerDTO medium = players.get(2).getSecond();
        String problemURL2 = (String) problemPicker.pickProblem(medium, problemDTOs).getBody();
        problemDTOs.add(problemPicker.getProblemByUrl(problemURL2));

        String problemURL1 = (String) problemPicker.pickProblem(medium.getUser_rating() - 300, problemDTOs).getBody();
        problemDTOs.add(problemPicker.getProblemByUrl(problemURL1));

        String problemURL3 = (String) problemPicker.pickProblem(medium.getUser_rating() + 300, problemDTOs).getBody();

        List<String> problems = new ArrayList<>();
        problems.add(problemURL1);
        problems.add(problemURL2);
        problems.add(problemURL3);
        return problems;
    }

    public ResponseEntity<Object> createTeamMatch(Long invitationId) {
        try {

            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel user = userRepository.findByUsername(userInfo.getUsername());

            TeamMatchInvitation teamMatchInvitation = teamMatchInvitationRepository.findById(invitationId).orElse(null);

            if (teamMatchInvitation == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseUtils.unsuccessfulRes("Invitation not found", null));
            }

            TeamModel team1 = teamMatchInvitation.getTeam1();
            TeamModel team2 = teamMatchInvitation.getTeam2();

            UserModel admin1 = team1.getCreator();
            UserModel admin2 = team2.getCreator();


            // Ensure the user is the owner of at least one of the teams
            if (!user.getId().equals(admin1.getId()) && !user.getId().equals(admin2.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ResponseUtils.unsuccessfulRes("You are not an admin for either team", null));
            }

            // Ensure the user is the owner of team1
            if (!user.getId().equals(admin1.getId())) {
                // Swap team1 and team2
                TeamModel temp = team1;
                team1 = team2;
                team2 = temp;

                // Reassign admins after swap
                admin1 = team1.getCreator();
                admin2 = team2.getCreator();
            }


            List<UserModel> team1Users = team1.getMembers().stream()
                    .map(TeamMember::getUser)
                    .collect(Collectors.toList());
            List<UserModel> team2Users = team2.getMembers().stream()
                    .map(TeamMember::getUser)
                    .collect(Collectors.toList());


            List<UserModel> commonUsers = new ArrayList<>(team1Users);
            commonUsers.retainAll(team2Users);

            boolean hasIntersection = !commonUsers.isEmpty();
            if (hasIntersection) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseUtils.unsuccessfulRes("Teams cannot have common members", null));
            }

            List<TeamMatchInvitation> existingInvitation = teamMatchInvitationRepository.findByTeams(team1, team2);

            if (existingInvitation.get(0).isAccepted()) {

                List<String> problems = get3problems(team1, team2);
                String problemURL = problems.get(0); // Default problem URL
                String problemURL2 = problems.get(1); // Default problem URL
                String problemURL3 = problems.get(2); // Default problem URL
                // Create the TeamMatch
                LocalDateTime endTime = LocalDateTime.now().plusMinutes(60);
                final TeamMatch teamMatch = new TeamMatch(team1, team2,
                        problemURL,
                        problemURL2,
                        problemURL3, LocalDateTime.now(), endTime, 60.0);

                teamMatch.setPlayers1(team1Users);
                teamMatch.setPlayers2(team2Users);

                gameRepository.save(teamMatch); // Save the team match immediately

                // Update each player's existing game ID
                team1Users.forEach(player -> player.setExistingGameId(teamMatch.getId()));
                team1Users.forEach(player -> player.setExistingInvitationId(null));
                team2Users.forEach(player -> player.setExistingGameId(teamMatch.getId()));
                team2Users.forEach(player -> player.setExistingInvitationId(null));
                userRepository.saveAll(team1Users);
                userRepository.saveAll(team2Users);

                // Convert the TeamMatch entity to its corresponding DTO
                ProblemDTO problemDTO = problemParser.parseCodeforcesUrl(problemURL);
                ProblemDTO problemDTO2 = problemParser.parseCodeforcesUrl(problemURL2);
                ProblemDTO problemDTO3 = problemParser.parseCodeforcesUrl(problemURL3);

                ResponseEntity<Object> problemParsed = problemParser.parseFullProblem(problemDTO.getContestId(), problemDTO.getIndex());
                ResponseEntity<Object> problemParsed2 = problemParser.parseFullProblem(problemDTO2.getContestId(), problemDTO2.getIndex());
                ResponseEntity<Object> problemParsed3 = problemParser.parseFullProblem(problemDTO3.getContestId(), problemDTO3.getIndex());

                TeamMatchDTO teamMatchDTO = new TeamMatchDTO(teamMatch, problemParsed.getBody(), problemParsed2.getBody(), problemParsed3.getBody());

                // Notify all players about the match start
                for (UserModel player : team1Users) {
                    if (!player.getUsername().equals(userInfo.getUsername())) {
                        messagingTemplate.convertAndSendToUser(player.getUsername(), "/games",
                                ResponseUtils.successfulRes("Team Match started successfully", teamMatchDTO));
                    }
                }
                for (UserModel player : team2Users) {
                    if (!player.getUsername().equals(userInfo.getUsername())) {
                        messagingTemplate.convertAndSendToUser(player.getUsername(), "/games",
                                ResponseUtils.successfulRes("Team Match started successfully", teamMatchDTO));
                    }
                }
                matchInvitationService.deleteInvitation(team1, team2);

                return ResponseEntity.ok(ResponseUtils.successfulRes("Team Match started successfully", teamMatchDTO));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseUtils.unsuccessfulRes("Team has not accepted the invitation", null));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.unsuccessfulRes("Error creating Team Match", e.getMessage()));
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResponseEntity<Object> createBeatAFriendMatch(Long invitationId) throws Exception {
        try {
            FriendMatchInvitation friendMatchInvitation = friendMatchInvitationRepository.findById(invitationId).orElse(null);

            if (friendMatchInvitation == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Invitation not found", null));
            }

            UserModel sender = friendMatchInvitation.getSender();
            UserModel receiver = friendMatchInvitation.getReceiver();

            if (friendMatchInvitation.getReceiver() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Receiver not found", null));
            }

            UserModel player1 = userRepository.findByUsername(sender.getUsername());
            UserModel opponent = userRepository.findByUsername(receiver.getUsername());

            if (friendMatchInvitation.isAccepted()) {
                PlayerDTO playerDTO = modelMapper.map(player1, PlayerDTO.class);
                PlayerDTO opponentDTO = modelMapper.map(opponent, PlayerDTO.class);

                List<ProblemDTO> solvedProblemsPlayer = player1.getSolvedProblems().stream().map(
                        progProblem -> modelMapper.map(progProblem, ProblemDTO.class)).toList();
                List<ProblemDTO> solvedProblemsOpponent = opponent.getSolvedProblems().stream().map(
                        progProblem -> modelMapper.map(progProblem, ProblemDTO.class)).toList();

                String problemURL = (String) problemPicker.pickProblem(playerDTO, solvedProblemsPlayer, opponentDTO, solvedProblemsOpponent).getBody();

                LocalDateTime endTime = LocalDateTime.now().plusMinutes(60);
                BeatAFriend beatAFriendMatch = new BeatAFriend(List.of(sender), List.of(receiver), problemURL, LocalDateTime.now(), endTime, 60.0);
                beatAFriendMatchRepository.save(beatAFriendMatch);

                player1.setExistingGameId(beatAFriendMatch.getId());
                opponent.setExistingGameId(beatAFriendMatch.getId());

                player1.setExistingInvitationId(null);
                opponent.setExistingInvitationId(null);

                userRepository.save(player1);
                userRepository.save(opponent);

                // Prepare DTO for response
                assert problemURL != null;
                ProblemDTO problemDTO = problemParser.parseCodeforcesUrl(problemURL);
                ResponseEntity<Object> problemParsed = problemParser.parseFullProblem(problemDTO.getContestId(), problemDTO.getIndex());
                BeatAFriendDTO beatAFriendDTO = new BeatAFriendDTO(beatAFriendMatch, problemParsed.getBody());

                // Notify sender and receiver about the match start
                messagingTemplate.convertAndSendToUser(sender.getUsername(), "/games",
                        ResponseUtils.successfulRes("Friendly match started successfully", beatAFriendDTO));
                messagingTemplate.convertAndSendToUser(receiver.getUsername(), "/games",
                        ResponseUtils.successfulRes("Friendly match started successfully", beatAFriendDTO));

                Boolean notifyIsDeleted = matchInvitationService.deleteInvitation(player1, opponent);

                if (!notifyIsDeleted) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Failed to create a game", null));
                }

                return ResponseEntity.ok(ResponseUtils.successfulRes("Beat a Friend match started successfully", beatAFriendDTO));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("Friend has not accepted the invitation", null));
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public List<ProblemDTO> mapProblemsToDTOs(List<ProgProblem> problems) {
        return problems.stream()
                .map(problem -> modelMapper.map(problem, ProblemDTO.class))
                .collect(Collectors.toList());
    }
}