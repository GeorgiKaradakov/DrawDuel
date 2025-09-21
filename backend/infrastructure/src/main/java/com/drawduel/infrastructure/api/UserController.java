package com.drawduel.infrastructure.api;

import com.drawduel.application.ports.CreateUserUseCasePort;
import com.drawduel.application.ports.GetUserByIdUserCasePort;
import com.drawduel.domain.models.User;
import java.time.Instant;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
  private final CreateUserUseCasePort createUser;
  private final GetUserByIdUserCasePort getUserById;

  public UserController(CreateUserUseCasePort createUser, GetUserByIdUserCasePort getUserById) {
    this.createUser = createUser;
    this.getUserById = getUserById;
  }

  public record CreateUserDto(String username, String email, String pass) {}

  public record UserDto(UUID id, String username, String email, Instant createAt) {
    public static UserDto from(User u) {
      return new UserDto(u.getId(), u.getUsername(), u.getEmail(), u.getCreatedAt());
    }
  }

  @PostMapping("create")
  @ResponseStatus(HttpStatus.CREATED)
  public UserDto create(@RequestBody CreateUserDto body) {
    var res =
        createUser.handle(
            new CreateUserUseCasePort.Query(body.username(), body.email(), body.pass()));
    return UserDto.from(res.user());
  }

  @GetMapping("/getUser/{id}")
  public UserDto get(@PathVariable UUID id) {
    var res = getUserById.handle(new GetUserByIdUserCasePort.Query(id));

    return UserDto.from(res.user());
  }
}
