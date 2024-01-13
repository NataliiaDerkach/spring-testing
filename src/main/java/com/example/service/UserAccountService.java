package com.example.service;

import com.example.dao.UserAccountRepository;
import com.example.dao.UserRepository;
import com.example.entity.User;
import com.example.entity.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class UserAccountService {

    @Autowired
    UserAccountRepository userAccountRepository;
    @Autowired
    private UserRepository userRepository;

    public UserAccount saveUserAccount(UserAccount userAccount) {
        return userAccountRepository.save(userAccount);
    }

    public UserAccount getUserAccountById(User user) {
        return userAccountRepository.findById(user.getId()).orElse(null);
    }

    public UserAccount updateBalance(User user, BigDecimal amount) {

        UserAccount account = new UserAccount();
        List<UserAccount> users = userAccountRepository.findAll();
        for (UserAccount u : users) {
            if (u.getUserId().getId() == user.getId()) {
                account.setId(u.getId());
                account.setBalance(amount);
                account.setUserId(user);
                saveUserAccount(account);
            }
        }
        return account;
    }

    @Transactional
    public BigDecimal getUserBalance(User user) {
        BigDecimal userBalance=BigDecimal.valueOf(0);
        List<UserAccount> users = userAccountRepository.findAll();
        for (UserAccount u : users) {
            if (u.getUserId().getId() == user.getId()) {
                userBalance= u.getBalance();
            }
        }
        return userBalance;
    }
}
