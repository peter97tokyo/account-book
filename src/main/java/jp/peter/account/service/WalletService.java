package jp.peter.account.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jp.peter.account.entity.Wallet;

@Service 
public interface WalletService {
    
    Wallet save(Wallet wallet);
    
    List<Wallet> findAll();

    Long findTotalWithdrawalByYearAndMonthAndDayNative(short year, byte month, byte day);

    Long findTotalDepositByYearAndMonthAndDayNative(short year, byte month, byte day);
    
}
