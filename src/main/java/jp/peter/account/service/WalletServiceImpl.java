package jp.peter.account.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import jp.peter.account.dto.WalletDto;
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

    @Override
    public void deleteById(Long id) {
        walletRepository.deleteById(id);
    }

    @Override
    public Optional<Wallet> findById(Long id) {
        return walletRepository.findById(id);
    }

    @Override
    public Page<Wallet> getPage(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return walletRepository.findAll(pageable);
    }

    @Override
    public List<WalletDto> sumMoneyByYearAndMonth(short year, boolean depositWithdrawal) {
        List<Object[]> items = walletRepository.sumMoneyByYearAndMonth(year, depositWithdrawal);
        List<WalletDto> result = new ArrayList<WalletDto>();
        WalletDto wallet = null;
        int startNumber = 0;
        int startNumberNeeds = 0;
        if (items != null) {
            startNumber = ((Number) items.get(0)[1]).intValue(); 
            if (startNumber > 1) {
                startNumberNeeds = startNumber - 1;
            }
        }
        for (int i = 0; i < startNumberNeeds; i++) {
            result.add(null);
        }
        for (Object[] item : items) {
            wallet = new WalletDto();
            wallet.setYear(((Number) item[0]).longValue());
            wallet.setMonth(((Number) item[1]).longValue());
            wallet.setMoney(((Number) item[2]).longValue());
            result.add(wallet);
        }
        if (result.size() < 12) {
            int lastNumberNeeds = 12 - result.size();
            for (int i = 1; i <= lastNumberNeeds; i++) {
                result.add(null);
            }
        }
        return result;
    }

    @Override
    public Long sumMoneyForOneYear(short year, boolean depositWithdrawal) {
        return walletRepository.sumMoneyByDepositWithdrawal(year, depositWithdrawal);
    }
    
    @Override
    public Long avgMoneyForOneYear(short year, boolean depositWithdrawal) {
        return walletRepository.avgMoneyByDepositWithdrawal(year, depositWithdrawal);
    }
    
}
