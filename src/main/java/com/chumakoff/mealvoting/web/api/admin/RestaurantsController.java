package com.chumakoff.mealvoting.web.api.admin;

import com.chumakoff.mealvoting.dto.RestaurantCreateDTO;
import com.chumakoff.mealvoting.dto.RestaurantResponseDTO;
import com.chumakoff.mealvoting.dto.RestaurantUpdateDTO;
import com.chumakoff.mealvoting.model.Restaurant;
import com.chumakoff.mealvoting.repository.RestaurantRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/admin/restaurants")
@RequiredArgsConstructor
public class RestaurantsController {
    private final RestaurantRepository repository;

    @GetMapping
    public List<RestaurantResponseDTO> list() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name"))
                .stream().map(RestaurantResponseDTO::buildFromEntity).toList();
    }

    @GetMapping("/{id}")
    public RestaurantResponseDTO get(@PathVariable("id") Long id) {
        var restaurant = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return RestaurantResponseDTO.buildFromEntity(restaurant);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantResponseDTO create(@Valid @RequestBody RestaurantCreateDTO dto) {
        var restaurant = repository.save(new Restaurant(dto.name()));
        return RestaurantResponseDTO.buildFromEntity(restaurant);
    }

    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public RestaurantResponseDTO update(@PathVariable("id") Long id, @Valid @RequestBody RestaurantUpdateDTO dto) {
        Restaurant restaurant = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        restaurant.setName(dto.name());

        Restaurant updatedRestaurant = repository.save(restaurant);
        return RestaurantResponseDTO.buildFromEntity(updatedRestaurant);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
