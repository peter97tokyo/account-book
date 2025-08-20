package jp.peter.account.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jp.peter.account.entity.Wallet;

@Service 
public interface WalletService {
    
    Wallet save(Wallet wallet);
    
    List<Wallet> findAll();

    Long findDailyWithdrawalByYearAndMonthAndDayNative(short year, byte month, byte day);

    Long findDailyDepositByYearAndMonthAndDayNative(short year, byte month, byte day);

    Long findTotalInOut(short year, byte month, boolean depositWithdrawal);

    List<Wallet> findByYearAndMonthAndDay(short year, byte month, byte day);

    void deleteById(Long id);
}
