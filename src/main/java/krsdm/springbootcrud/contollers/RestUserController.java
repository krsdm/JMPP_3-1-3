package krsdm.springbootcrud.contollers;

import krsdm.springbootcrud.models.User;
import krsdm.springbootcrud.restExсeptions.NoSuchUserExeption;
import krsdm.springbootcrud.service.RoleService;
import krsdm.springbootcrud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
//@CrossOrigin (origins = "http://localhost:63342/")
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
        user.setRoles(user.getRoles().stream()
                .map(role -> roleService.getByName(role.getName()))
                .collect(Collectors.toSet()));
        userService.saveUser(user);
        return user;
    }

    @PutMapping
    public User updateUser(@ModelAttribute User user) {
        user.setRoles(user.getRoles().stream()
                .map(role -> roleService.getByName(role.getName()))
                .collect(Collectors.toSet()));
        userService.updateUser(user);
        return user;
    }

    @DeleteMapping("{id}")
    public String deleteUser(@PathVariable Long id) {
        if (userService.getUserById(id) == null) {
            throw new NoSuchUserExeption(String.format("User with id=%s does not exist", id));
        }
        userService.removeUser(id);
        return String.format("User with id=%s was removed", id);
    }

/*
    @GetMapping
    public String userList(@AuthenticationPrincipal User activeUser, Model model) {
        model.addAttribute("roles", roleService.getRoles());
        model.addAttribute("user", activeUser);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("newuser", new User());
        return "admin/admin";
    }

    @PostMapping
    public String createUser(@AuthenticationPrincipal User activeUser,
                             @ModelAttribute("newuser") @Valid User newuser,
                             BindingResult bindingResult, Model model) {

        // Если юзер с таким именем уже существует, сообщим об этом
        if (userService.getUserByName(newuser.getName()) != null) {
            model.addAttribute("usernameError", String.format("User with first name \"%s\" is already exist!", newuser.getName()));
            model.addAttribute("roles", roleService.getRoles());
            model.addAttribute("user", activeUser);
            return "admin/admin";
        }

        // Иначе достаем для юзера по указанным именам роли из базы и сохраняем
        newuser.setRoles(newuser.getRoles().stream()
                .map(role -> roleService.getByName(role.getName()))
                .collect(Collectors.toSet()));
        userService.saveUser(newuser);
        return "redirect:/admin";
    }

    @PatchMapping("{id}/edit")
    public String updateUser(@AuthenticationPrincipal User activeUser,
                             @ModelAttribute("user") User user, Model model) {

        // если имя было изменено и юзер с таким именем уже существует, сообщим об этом
        if (!user.getName().equals(userService.getUserById(user.getId()).getName()) &&
                userService.getUserByName(user.getName()) != null) {
            model.addAttribute("usernameError", String.format("User with first name \"%s\" is already exist!", user.getName()));
            model.addAttribute("roles", roleService.getRoles());
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("newuser", new User());
            model.addAttribute("user", activeUser);
            return "admin/admin";
        }

        // иначе достаем для юзера по указанным именам роли из базы и сохраняем
        user.setRoles(user.getRoles().stream()
                .map(role -> roleService.getByName(role.getName()))
                .collect(Collectors.toSet()));
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("{id}")
    public String deleteUser(@AuthenticationPrincipal User activeUser, @PathVariable("id") long id) {
        userService.removeUser(id);
        // если admin удалил сам себя, нужно его разлогинить
        if (id == activeUser.getId()) {
            return "redirect:/logout";
        }
        return "redirect:/admin";
    }
*/

}
