package jp.peter.account.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import jp.peter.account.dto.WalletDto;
import jp.peter.account.entity.Wallet;
import jp.peter.account.repository.WalletRepository;
import jp.peter.account.specification.WalletSpecification;
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
    public Page<Wallet> getPage(int page,
                                LocalDateTime startDate,
                                LocalDateTime endDate,
                                String memo,
                                Boolean depositWithdrawal,
                                String type) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("inputDate").descending());

        Specification<Wallet> spec = Specification
            .where(WalletSpecification.inputDateBetween(startDate, endDate))
            .and(WalletSpecification.memoContains(memo))
            .and(WalletSpecification.depositWithdrawalEquals(depositWithdrawal))
            .and(WalletSpecification.typeContains(type));

        return walletRepository.findAll(spec, pageable);
    }

    @Override
    public List<WalletDto> sumMoneyByYearAndMonth(short year, boolean depositWithdrawal) {
        List<Object[]> items = walletRepository.sumMoneyByYearAndMonth(year, depositWithdrawal);
        List<WalletDto> result = new ArrayList<>();
        WalletDto wallet;
        int startNumber = 0;
        int startNumberNeeds = 0;

        if (!items.isEmpty()) {
            startNumber = ((Number) items.get(0)[1]).intValue();
            if (startNumber > 1) {
                startNumberNeeds = startNumber - 1;
            }
        }

        // items가 비어 있어도 안전하게 null 또는 기본값 채우기
        for (int i = 0; i < startNumberNeeds; i++) {
            result.add(null); // 필요하면 WalletDto에 기본값 넣어도 됨
        }

        for (Object[] item : items) {
            wallet = new WalletDto();
            wallet.setYear(((Number) item[0]).longValue());
            wallet.setMonth(((Number) item[1]).longValue());
            wallet.setMoney(((Number) item[2]).longValue());
            result.add(wallet);
        }

        // result가 12개 미만이면 null 또는 기본값으로 채우기
        while (result.size() < 12) {
            result.add(null); // 필요시 기본 WalletDto 생성 가능
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
