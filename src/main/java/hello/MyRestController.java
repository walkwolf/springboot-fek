package hello;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MyRestController {
    @PostMapping("/haha")
    public Map getMap(@RequestBody Map rmap) {
        System.out.println("name:" + rmap.get("name"));
        System.out.println("age:" + rmap.get("age"));
        Map map = new HashMap<>();
        map.put("key", rmap.get("name"));
        map.put("v", rmap.get("age"));
        return map;
    }
}
