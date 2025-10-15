package jp.peter.account.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

        while (result.size() < 12) {
            result.add(null); 
        }
        return result;

    }

    @Override
    public Long sumMoneyForOneYear(short year, boolean depositWithdrawal) {
        return walletRepository.sumMoneyByDepositWithdrawal(year, depositWithdrawal);
    }
    
    @Override
    public Long avgMonthlyMoneyForOneYear(short year, boolean depositWithdrawal) {
        return walletRepository.avgMonthlyMoneyForOneYear(year, depositWithdrawal);
    }

    @Override
    public Long avgDailyWithdrawalForOneYear(short year, boolean depositWithdrawal) {
        return walletRepository.avgDailyWithdrawalForOneYear(year, depositWithdrawal);
    }
    
    @Override
    public Long avgDailyWithdrawalForMonth(short year, byte month, boolean depositWithdrawal) {
        return walletRepository.avgDailyWithdrawalForMonth(year, month, depositWithdrawal);
    }

    @Override
    public List<Wallet> getExcel(LocalDateTime startDate,
                                LocalDateTime endDate,
                                String memo,
                                Boolean depositWithdrawal,
                                String type) {
        Specification<Wallet> spec = Specification
            .where(WalletSpecification.inputDateBetween(startDate, endDate))
            .and(WalletSpecification.memoContains(memo))
            .and(WalletSpecification.depositWithdrawalEquals(depositWithdrawal))
            .and(WalletSpecification.typeContains(type));
        return walletRepository.findAll(spec, Sort.by("inputDate").descending());
    }

    public ByteArrayInputStream exportWalletToExcel(LocalDateTime startDate,
                                                LocalDateTime endDate,
                                                String memo,
                                                Boolean depositWithdrawal,
                                                String type) throws IOException {
        List<Wallet> walletList = getExcel(startDate, endDate, memo, depositWithdrawal, type);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = (Sheet) workbook.createSheet("Wallet Data");
            Row headerRow = ((org.apache.poi.ss.usermodel.Sheet) sheet).createRow(0);
            String[] headers = {"Date", "Deposit/Withdrawal", "Type", "Money", "Memo"};
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowNum = 1;
            for (Wallet w : walletList) {
                Row row = ((org.apache.poi.ss.usermodel.Sheet) sheet).createRow(rowNum++);
                row.createCell(0).setCellValue(w.getInputDate().toString());
                row.createCell(1).setCellValue(w.getDepositWithdrawal() ? "Deposit" : "Withdrawal");
                row.createCell(2).setCellValue(w.getType());
                row.createCell(3).setCellValue(w.getMoney().doubleValue());
                row.createCell(4).setCellValue(w.getMemo());
            }

            for (int i = 0; i < headers.length; i++) {
                ((org.apache.poi.ss.usermodel.Sheet) sheet).autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

}
