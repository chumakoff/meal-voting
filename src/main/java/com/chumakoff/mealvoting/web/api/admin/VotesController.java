package com.chumakoff.mealvoting.web.api.admin;

import com.chumakoff.mealvoting.dto.VoteResponseDTO;
import com.chumakoff.mealvoting.model.Vote;
import com.chumakoff.mealvoting.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/votes")
@RequiredArgsConstructor
@Tag(name = "[ADMIN] Votes")
public class VotesController {
    private final VoteService voteService;

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
}
