package com.example.controller;

import com.example.entity.User;
import com.example.entity.UserAccount;
import com.example.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/userAccount")
public class UserAccountController {

    @Autowired
    private UserAccountService userAccountService;

    @PostMapping("/addUserAccount")
    public UserAccount addUserAccount(@RequestBody UserAccount userAccount) {
        return userAccountService.saveUserAccount(userAccount);
    }

    @GetMapping("/userAccount")
    public UserAccount getUserAccountById(@PathVariable User user) {
        return userAccountService.getUserAccountById(user);
    }

    @PutMapping("/updateUserAccount")
    public UserAccount updateUserAccountBalance(@RequestBody User user, BigDecimal amount) {
        return userAccountService.updateBalance(user, amount);
    }

    @Transactional
    @GetMapping("/balance/{userId}")
    public BigDecimal getUserBalanceById(@PathVariable ("userId") User userId) {
        return userAccountService.getUserBalance(userId);

    }
}
