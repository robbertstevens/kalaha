package rocks.robbert.kalaha.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import rocks.robbert.kalaha.model.Board;

import javax.servlet.http.HttpSession;

@Controller
public class HomepageController {

    @RequestMapping(value ="/")
    public String homepage(Model model) {
        return "index.html";
    }
}
