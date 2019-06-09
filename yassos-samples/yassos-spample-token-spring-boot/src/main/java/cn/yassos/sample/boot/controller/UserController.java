package cn.yassos.sample.boot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hylexus
 * Created At 2019-06-07 20:29
 */
@RestController
public class UserController {

    @GetMapping("/user-info")
    public Object userInfo() {
        return "OK";
    }
}
