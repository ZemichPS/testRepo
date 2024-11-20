package by.zemich.userms.controller;

import by.zemich.userms.controller.request.RegisterRequest;
import by.zemich.userms.controller.request.UpdateUserRequest;
import by.zemich.userms.controller.response.UserFullResponse;
import by.zemich.userms.dao.entity.User;
import by.zemich.userms.service.UserRestService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserRestService userRestService;

    @PostMapping
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request) {
        User registered = userRestService.register(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(registered.getId())
                .toUri();
        return ResponseEntity.created(location).contentType(MediaType.APPLICATION_JSON).build();
    }

    @GetMapping
    public ResponseEntity<Page<UserFullResponse>> findAll(
            @RequestParam(name = "page_number", defaultValue = "0") int pageNumber,
            @RequestParam(name = "page_size", defaultValue = "10") int pageSize,
            @RequestParam(name = "sort_by", defaultValue = "registerAt") String sortBy
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        Page<UserFullResponse> response = userRestService.findAll(pageable);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserFullResponse> partialUpdate(
            @PathVariable UUID userId,
            @RequestBody UpdateUserRequest updateUserRequest) {
        UserFullResponse userUpdated = userRestService.partialUpdate(userId, updateUserRequest);
        return ResponseEntity.ok(userUpdated);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserFullResponse> findById(@PathVariable UUID userId) {
        UserFullResponse response = userRestService.findById(userId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{userId}/deactivation")
    public ResponseEntity<UserFullResponse> deactivate(@PathVariable UUID userId) {
        UserFullResponse response = userRestService.deactivate(userId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{userId}/activation")
    public ResponseEntity<UserFullResponse> activate(@PathVariable UUID userId) {
        UserFullResponse response = userRestService.activate(userId);
        return ResponseEntity.ok(response);
    }
}
