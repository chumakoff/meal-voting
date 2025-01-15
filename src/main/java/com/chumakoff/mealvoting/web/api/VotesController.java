package com.chumakoff.mealvoting.web.api;

import com.chumakoff.mealvoting.config.security.AuthUser;
import com.chumakoff.mealvoting.dto.VoteResponseDTO;
import com.chumakoff.mealvoting.model.Vote;
import com.chumakoff.mealvoting.repository.VoteRepository;
import com.chumakoff.mealvoting.service.VoteService;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/votes")
@RequiredArgsConstructor
public class VotesController {
    private final VoteService voteService;
    private final VoteRepository repository;

    @GetMapping
    public List<VoteResponseDTO> list(
            @RequestParam("date") @Nullable LocalDate date,
            @RequestParam("user_id") @Nullable Long userId
    ) {
        List<Vote> votes = voteService.findAll(date, userId, Sort.by(Sort.Direction.ASC, "id"));
        return votes.stream().map(VoteResponseDTO::buildFromEntity).toList();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public VoteResponseDTO create(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody VoteCreateDTO dto) {
        Vote vote = voteService.registerVote(authUser.getUserId(), dto.restaurantId());
        return VoteResponseDTO.buildFromEntity(vote);
    }

    private record VoteCreateDTO(@NotNull @JsonProperty("restaurant_id") Long restaurantId) {
    }
}
