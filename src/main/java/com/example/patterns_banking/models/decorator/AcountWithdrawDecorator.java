package com.example.patterns_banking.models.decorator;

import com.example.patterns_banking.models.Account;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class AcountWithdrawDecorator extends AccountDecorator{
    private static final double OVERDRAFT_LIMIT = 20000.0;

    public AcountWithdrawDecorator(Account account) {
        super(account);
    }

    @Override
    public void withdraw(double amount){
        double balance = getBalance();
        if (amount <= balance) {
            // Si el retiro es menor o igual al balance, normal
            super.withdraw(amount);
        } else {
            double exceededAmount = amount - balance;

            if (exceededAmount <= OVERDRAFT_LIMIT) {

                super.withdraw(balance); // Retira todo lo que hay
                System.out.println("Se ha excedido el saldo en: $" + exceededAmount);
                System.out.println("El saldo ha quedado en: $0.00");
            } else {

                System.out.println("El retiro excede el límite de sobregiro permitido. Operación cancelada.");
                return;
            }
        }

        System.out.println("Se ha realizado un retiro de: " + amount + " en la cuenta de: " + getAccountNumber() +
                "el saldo final es: " + getBalance()    );
    }

}
