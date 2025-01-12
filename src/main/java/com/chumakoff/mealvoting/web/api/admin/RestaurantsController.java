package com.chumakoff.mealvoting.web.api.admin;

import com.chumakoff.mealvoting.model.Restaurant;
import com.chumakoff.mealvoting.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/restaurants")
@RequiredArgsConstructor
public class RestaurantsController {
    private final RestaurantRepository repository;

    @GetMapping
    public List<RestaurantDTO> list() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name"))
                .stream().map(RestaurantDTO::buildFromEntity).toList();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantDTO create(@RequestBody RestaurantCreateDTO restaurantCreateDto) {
        var restaurant = repository.save(restaurantCreateDto.buildEntity());
        return RestaurantDTO.buildFromEntity(restaurant);
    }

    private record RestaurantDTO(Long id, String name) {
        public static RestaurantDTO buildFromEntity(Restaurant restaurant) {
            return new RestaurantDTO(restaurant.getId(), restaurant.getName());
        }
    }

    private record RestaurantCreateDTO(String name) {
        public Restaurant buildEntity() {
            return new Restaurant(name);
        }
    }
}
