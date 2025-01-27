package com.chumakoff.mealvoting.web.api;

import com.chumakoff.mealvoting.config.security.AuthUser;
import com.chumakoff.mealvoting.dto.VoteCreateDTO;
import com.chumakoff.mealvoting.dto.VoteResponseDTO;
import com.chumakoff.mealvoting.model.Vote;
import com.chumakoff.mealvoting.repository.VoteRepository;
import com.chumakoff.mealvoting.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/vote")
@RequiredArgsConstructor
@Tag(name = "Vote")
public class VoteController {
    private final VoteService voteService;
    private final VoteRepository repository;

    @GetMapping
    @Operation(summary = "Get today's vote.", description = "Returns today's vote for an authenticated user.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Vote found.",
                            content = {@Content(schema = @Schema(implementation = VoteResponseDTO.class))}),
                    @ApiResponse(responseCode = "204", description = "Vote not found", content = @Content)
            }
    )
    public ResponseEntity<VoteResponseDTO> get(@AuthenticationPrincipal AuthUser authUser) {
        return repository.findByUserIdAndMealDate(authUser.getUserId(), LocalDate.now())
                .map((vote) -> new ResponseEntity<>(VoteResponseDTO.buildFromEntity(vote), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update today's vote.", description = "Updates today's vote for an authenticated user.")
    public VoteResponseDTO update(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody VoteCreateDTO dto) {
        Vote vote = voteService.registerVote(authUser.getUserId(), dto.restaurantId(), LocalDateTime.now());
        return VoteResponseDTO.buildFromEntity(vote);
    }
}
