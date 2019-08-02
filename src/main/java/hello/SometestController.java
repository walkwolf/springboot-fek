package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class SometestController {
    Logger logger = LoggerFactory.getLogger(SometestController.class);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");

    @GetMapping("/test")
    public String test() {

        String date = sdf.format(new Date());
        logger.info(date);
        return date;
    }

    @GetMapping("/fail")
    public String fail() {
        int a = 10;
        int b = 0;
        try {
            System.out.println(a / b);
        } catch (Exception e) {
            logger.error("执行错误", e);
        }

        return "test";
    }

    @GetMapping("/consoleout")
    public String consoleout() {
        System.out.println("this is a 控制台输出啊哈哈哈");
        return "test";
    }
}
