package edu.twt.rehuixiangshudong.controller;

import edu.twt.rehuixiangshudong.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
}
