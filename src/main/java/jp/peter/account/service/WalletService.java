package jp.peter.account.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import jp.peter.account.dto.WalletDto;
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

    Optional<Wallet> findById(Long id);

    Page<Wallet> getPage(int page, LocalDateTime startDate, LocalDateTime endDate, String memo, Boolean depositWithdrawal, String type);

    List<WalletDto> sumMoneyByYearAndMonth(short year, boolean depositWithdrawal);

    Long sumMoneyForOneYear(short year, boolean depositWithdrawal);
    
    Long avgMoneyForOneYear(short year, boolean depositWithdrawal);
    
}
