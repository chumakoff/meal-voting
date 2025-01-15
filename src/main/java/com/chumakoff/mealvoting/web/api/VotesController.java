package com.chumakoff.mealvoting.web.api;

import com.chumakoff.mealvoting.config.security.AuthUser;
import com.chumakoff.mealvoting.dto.VoteResponseDTO;
import com.chumakoff.mealvoting.model.Vote;
import com.chumakoff.mealvoting.repository.VoteRepository;
import com.chumakoff.mealvoting.service.VoteService;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Votes")
public class VotesController {
    private final VoteService voteService;
    private final VoteRepository repository;

    @GetMapping
    @Operation(summary = "Get votes.", description = "Votes can be filtered by a date or a user.")
    public List<VoteResponseDTO> list(
            @RequestParam("date")
            @Nullable
            @Parameter(description = "Filter votes by date")
            LocalDate date,
            @RequestParam("user_id")
            @Nullable
            @Parameter(description = "Filter votes by user ID")
            Long userId
    ) {
        List<Vote> votes = voteService.findAll(date, userId, Sort.by(Sort.Direction.ASC, "id"));
        return votes.stream().map(VoteResponseDTO::buildFromEntity).toList();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new vote for an authenticated user.", description = "A vote is counted for the current day.")
    public VoteResponseDTO create(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody VoteCreateDTO dto) {
        Vote vote = voteService.registerVote(authUser.getUserId(), dto.restaurantId());
        return VoteResponseDTO.buildFromEntity(vote);
    }

    private record VoteCreateDTO(@NotNull @JsonProperty("restaurant_id") Long restaurantId) {
    }
}
