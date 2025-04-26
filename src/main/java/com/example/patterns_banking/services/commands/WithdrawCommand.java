package com.example.patterns_banking.services.commands;

import com.example.patterns_banking.models.Account;
import com.example.patterns_banking.models.decorator.AccountNotificationDecorator;
import com.example.patterns_banking.models.decorator.AcountWithdrawDecorator;
import com.example.patterns_banking.repositories.IAccountRepository;

import java.util.Optional;

/**
 * Command for withdrawing money from an account.
 */
public class WithdrawCommand implements ICommand<Account> {
    
    private final Long accountId;
    private final double amount;
    private final IAccountRepository accountRepository;
    
    /**
     * Constructor for WithdrawCommand.
     * 
     * @param accountId The ID of the account
     * @param amount The amount to withdraw
     * @param accountRepository The account repository
     */
    public WithdrawCommand(Long accountId, double amount, IAccountRepository accountRepository) {
        this.accountId = accountId;
        this.amount = amount;
        this.accountRepository = accountRepository;
    }
    
    /**
     * Execute the command to withdraw money from an account.
     * 
     * @return The updated account
     * @throws IllegalArgumentException if the account is not found, the amount is negative, or there are insufficient funds
     */
    @Override
    public Account execute() {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        Optional<Account> accountOpt = accountRepository.findById(accountId);
        if (accountOpt.isEmpty()) {
            throw new IllegalArgumentException("Account not found with ID: " + accountId);
        }

        Account account = accountOpt.get();
        Account accountDec = new AcountWithdrawDecorator(account);
        accountDec.withdraw(amount);

        return accountRepository.save(accountDec);
    }
}