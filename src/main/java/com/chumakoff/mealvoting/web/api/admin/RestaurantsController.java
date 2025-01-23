package com.chumakoff.mealvoting.web.api.admin;

import com.chumakoff.mealvoting.dto.RestaurantCreateDTO;
import com.chumakoff.mealvoting.dto.RestaurantResponseDTO;
import com.chumakoff.mealvoting.dto.RestaurantUpdateDTO;
import com.chumakoff.mealvoting.model.Restaurant;
import com.chumakoff.mealvoting.repository.RestaurantRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.chumakoff.mealvoting.helper.RepositoryHelper.getOrThrow;

@RestController
@RequestMapping("/api/admin/restaurants")
@RequiredArgsConstructor
@Tag(name = "[ADMIN] Restaurants")
public class RestaurantsController {
    private final RestaurantRepository repository;

    @GetMapping
    @Operation(summary = "Get restaurants.")
    public List<RestaurantResponseDTO> list() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name"))
                .stream().map(RestaurantResponseDTO::buildFromEntity).toList();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new restaurant.")
    public RestaurantResponseDTO create(@Valid @RequestBody RestaurantCreateDTO dto) {
        var restaurant = repository.save(new Restaurant(dto.name()));
        return RestaurantResponseDTO.buildFromEntity(restaurant);
    }

    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a restaurant.")
    public RestaurantResponseDTO update(@PathVariable("id") Long id, @Valid @RequestBody RestaurantUpdateDTO dto) {
        Restaurant restaurant = getOrThrow(repository.findById(id), id, Restaurant.class);
        restaurant.setName(dto.name());

        Restaurant updatedRestaurant = repository.save(restaurant);
        return RestaurantResponseDTO.buildFromEntity(updatedRestaurant);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a restaurant.")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
