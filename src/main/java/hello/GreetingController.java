package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping("/decTest")
    public String decTest(@RequestParam(name = "params", required = false, defaultValue = "") String params, Model model) {

        String key = "test";

        String result="解密异常";


                //非空解密
        if (!StringUtils.isEmpty(params)) {

            try {
                System.out.println("收到请求:params " + params + " :解密结果 "+AESUtil.decrypt(key, params));
                result=AESUtil.decrypt(key, params);
            } catch (Exception e) {
                e.printStackTrace();

            }


        }

        model.addAttribute("params", params);
        model.addAttribute("result", result);
        return "decTest";
    }


}
