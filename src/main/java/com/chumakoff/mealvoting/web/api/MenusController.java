package com.chumakoff.mealvoting.web.api;

import com.chumakoff.mealvoting.dto.MenuResponseDTO;
import com.chumakoff.mealvoting.model.Menu;
import com.chumakoff.mealvoting.repository.MenuRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
@Tag(name = "Menus")
public class MenusController {
    private final MenuRepository repository;

    @GetMapping
    @Operation(summary = "Get restaurant daily lunch menus.",
            description = "Menus also include a restaurant information.")
    public List<MenuResponseDTO> list(
            @RequestParam("date")
            @Nullable
            @Parameter(description = "Filter menus by date")
            LocalDate date
    ) {
        var sort = Sort.by(Sort.Direction.ASC, "id");
        List<Menu> menus;

        if (date == null) {
            menus = repository.findAll(sort);
        } else {
            menus = repository.findAllByDate(date, sort);
        }
        return menus.stream().map(MenuResponseDTO::buildFromEntity).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a lunch menu by ID.")
    public MenuResponseDTO get(@PathVariable("id") Long id) {
        Menu menu = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return MenuResponseDTO.buildFromEntity(menu);
    }
}
