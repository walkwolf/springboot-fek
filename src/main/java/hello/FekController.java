package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class FekController {

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name,
                           Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping("/normal")
    @ResponseBody
    public String test() {
        log.info("A normal request income.");
        return "normal";
    }

    @GetMapping("/fail")
    @ResponseBody
    public String fail() {
        int a = 10;
        int b = 0;
        try {
            System.out.println(a / b);
        } catch (Exception e) {
            log.error("执行错误", e);
        }

        return "test";
    }
}
