package krsdm.springbootcrud.contollers;

import krsdm.springbootcrud.models.User;
import krsdm.springbootcrud.service.RoleService;
import krsdm.springbootcrud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    MainController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/login")
    public String formlogin() {
        return "login";
    }

    @GetMapping("/admin")
    public String userList(@AuthenticationPrincipal User activeUser, Model model) {
        model.addAttribute("roles", roleService.getRoles());
        model.addAttribute("user", activeUser);
        model.addAttribute("newuser", new User());
        return "admin/admin";
    }

    @GetMapping("/user")
    public String userProfile(@AuthenticationPrincipal User activeUser, Model model) {
        model.addAttribute("user", activeUser);
        return "user";
    }
}
