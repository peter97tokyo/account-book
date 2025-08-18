package jp.peter.account.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jp.peter.account.entity.Wallet;
import jp.peter.account.repository.WalletRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class WalletServiceImpl implements WalletService {
    
    private final WalletRepository walletRepository;

    @Override
    public Wallet save(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    @Override
    public List<Wallet> findAll() {
        return walletRepository.findAll();
    }

    @Override
    public Long findDailyWithdrawalByYearAndMonthAndDayNative(short year, byte month, byte day) {
        return walletRepository.findDailyWithdrawalByYearAndMonthAndDayNative(year, month, day);
    }
    
    @Override
    public Long findDailyDepositByYearAndMonthAndDayNative(short year, byte month, byte day) {
        return walletRepository.findDailyDepositByYearAndMonthAndDayNative(year, month, day);
    }

    @Override
    public Long findTotalInOut(short year, byte month, boolean depositWithdrawal) {
        return walletRepository.findByYearAndMonthAndDepositWithdrawalNative(year, month, depositWithdrawal);
    }

    @Override
    public List<Wallet> findByYearAndMonthAndDay(short year, byte month, byte day) {
        return walletRepository.findByYearAndMonthAndDay(year, month, day);
    }
}
