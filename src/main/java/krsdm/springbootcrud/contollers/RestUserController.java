package krsdm.springbootcrud.contollers;

import krsdm.springbootcrud.models.User;
import krsdm.springbootcrud.restExсeptions.NoSuchUserExeption;
import krsdm.springbootcrud.restExсeptions.UserExistException;
import krsdm.springbootcrud.service.RoleService;
import krsdm.springbootcrud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class RestUserController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    RestUserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public List<User> allUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("{id}")
    public User userById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            throw new NoSuchUserExeption(String.format("User with id=%s does not exist", id));
        }
        return user;
    }

    @PostMapping
    public User newUser(@ModelAttribute User user) {
        // если юзер с таким именем уже есть, сообщим об этом
        if (userService.getUserByName(user.getName()) != null) {
            throw new UserExistException(String.format("User with name \"%s\" already exist!", user.getName()));
        }
        user.setRoles(user.getRoles().stream()
                .map(role -> roleService.getByName(role.getName()))
                .collect(Collectors.toSet()));
        userService.saveUser(user);
        return user;
    }

    @PutMapping
    public User updateUser(@ModelAttribute User user) {
        // если имя было измененно и юзер с таким именем уже есть, сообщим об этом
        if (!user.getName().equals(userService.getUserById(user.getId()).getName()) &&
                userService.getUserByName(user.getName()) != null) {
            throw new UserExistException(String.format("User with name \"%s\" already exist!", user.getName()));
        }
        user.setRoles(user.getRoles().stream()
                .map(role -> roleService.getByName(role.getName()))
                .collect(Collectors.toSet()));
        userService.updateUser(user);
        return user;
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable Long id) {
        // если такого юзера нет, сообщим об этом
        if (userService.getUserById(id) == null) {
            throw new NoSuchUserExeption(String.format("User with id %s does not exist", id));
        }
        userService.removeUser(id);
    }
}
