package jp.peter.account.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.peter.account.dto.WalletDto;
import jp.peter.account.entity.Wallet;
import jp.peter.account.service.WalletService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;





@Controller
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/wallet/calendar")
    public String calendar(Model model) {
        Date today = new Date();

        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        
        int year = Integer.valueOf(yearFormat.format(today));
        int month = Integer.valueOf(monthFormat.format(today));
        int day = Integer.valueOf(dayFormat.format(today));
        
        LocalDate startDate = LocalDate.of(year, month, 1);

        DayOfWeek week = startDate.getDayOfWeek();
        int firstDayWeek = week.getValue();

        int lastDay = startDate.lengthOfMonth();
        
        List<List<Integer>> weeks = new ArrayList<>();
        List<Integer> days = new ArrayList<>();
        int getDay = 1;
        
        for(int i = 0; i <= 41; i++) {
            if (firstDayWeek <= i && getDay <= lastDay) {
                days.add(getDay);
                getDay++;
            }else {
                days.add(null);
            }
        }
        
        for (int j = 0; j < 6; j++) {
            List<Integer> weekWithForeach = new ArrayList<>(days.subList(0, 7));            
            weeks.add(weekWithForeach);
            days.subList(0, 7).clear();
        }
    
        Long sumMoney = null;
        byte dayByte = 0;
        Long[] dailyWithdrawal = new Long[lastDay + 1];
        Long[] dailyDeposit = new Long[lastDay + 1];
        
        for (int i = 1; i <= lastDay; i++) {
            dayByte = (byte) i;

            sumMoney = walletService.findDailyWithdrawalByYearAndMonthAndDayNative((short) year, (byte) month, dayByte);
            dailyWithdrawal[i] = sumMoney;    

            sumMoney = walletService.findDailyDepositByYearAndMonthAndDayNative((short) year, (byte) month, dayByte);
            dailyDeposit[i] = sumMoney;
        }

        Long totalDeposit = walletService.findTotalInOut((short) year, (byte) month, true);
        Long totalWithdrawal = walletService.findTotalInOut((short) year, (byte) month, false);

        int prevMonth = month - 1;
        int prevYear = year;

        if (prevMonth == 0) {
            prevMonth = 12;
            prevYear--;
        }

        Long totalWithdrawalPrevMonth = walletService.findTotalInOut((short) prevYear, (byte) prevMonth, false);
        
        Long avgDailyWithdrawalForMonth = walletService.avgDailyWithdrawalForMonth((short)year, (byte) month, false); 
        Long avgDailyWithdrawalForOneYear = walletService.avgDailyWithdrawalForOneYear((short)year, false);


        model.addAttribute("totalDeposit", totalDeposit);
        model.addAttribute("totalWithdrawal", totalWithdrawal);
        model.addAttribute("totalWithdrawalPrevMonth", totalWithdrawalPrevMonth);
        model.addAttribute("avgDailyWithdrawalForMonth", avgDailyWithdrawalForMonth);
        model.addAttribute("avgDailyWithdrawalForOneYear", avgDailyWithdrawalForOneYear);
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("day", day);
        model.addAttribute("firstDayWeek", firstDayWeek);
        model.addAttribute("lastDay", lastDay);
        model.addAttribute("weeks", weeks);
        model.addAttribute("dailyWithdrawal", dailyWithdrawal);
        model.addAttribute("dailyDeposit", dailyDeposit);
        
        return "wallet/calendar"; 
    }

    @PostMapping("/wallet/calendar")
    public String calendar(@RequestParam(required = false) Integer year,
                            @RequestParam(required = false) Integer month,
                            Model model) {

        LocalDate today = LocalDate.now();

        if (year == null || month == null) {
            year = today.getYear();
            month = today.getMonthValue();
        }

        LocalDate startDate = LocalDate.of(year, month, 1);

        DayOfWeek week = startDate.getDayOfWeek();
        int firstDayWeek = week.getValue();

        int lastDay = startDate.lengthOfMonth();

        List<List<Integer>> weeks = new ArrayList<>();
        List<Integer> days = new ArrayList<>();
        int getDay = 1;

        for (int i = 0; i <= 41; i++) {
            if (firstDayWeek <= i && getDay <= lastDay) {
                days.add(getDay);
                getDay++;
            } else {
                days.add(null);
            }
        }

        for (int j = 0; j < 6; j++) {
            List<Integer> weekWithForeach = new ArrayList<>(days.subList(0, 7));
            weeks.add(weekWithForeach);
            days.subList(0, 7).clear();
        }

        Long sumMoney = null;
        short yearShort = year.shortValue(); 
        byte monthByte = month.byteValue();  
        byte dayByte = 0;
        Long[] dailyWithdrawal = new Long[lastDay + 1];
        Long[] dailyDeposit = new Long[lastDay + 1];

        for (int i = 0; i <= lastDay; i++) {
            dayByte = (byte) i;

            sumMoney = walletService.findDailyWithdrawalByYearAndMonthAndDayNative(yearShort, monthByte, dayByte);
            dailyWithdrawal[i] = sumMoney;

            sumMoney = walletService.findDailyDepositByYearAndMonthAndDayNative(yearShort, monthByte, dayByte);
            dailyDeposit[i] = sumMoney;
        }

        Long totalDeposit = walletService.findTotalInOut(yearShort, monthByte, true);
        Long totalWithdrawal = walletService.findTotalInOut(yearShort, monthByte, false);

        int prevMonth = month - 1;
        int prevYear = year;

        if (prevMonth == 0) {
            prevMonth = 12;
            prevYear--;
        }

        Long totalWithdrawalPrevMonth = walletService.findTotalInOut((short) prevYear, (byte) prevMonth, false);
        Long avgDailyWithdrawalForMonth = walletService.avgDailyWithdrawalForMonth(yearShort, monthByte, false);
        Long avgDailyWithdrawalForOneYear = walletService.avgDailyWithdrawalForOneYear(yearShort, false);

        model.addAttribute("totalDeposit", totalDeposit);
        model.addAttribute("totalWithdrawal", totalWithdrawal);
        model.addAttribute("totalWithdrawalPrevMonth", totalWithdrawalPrevMonth);
        model.addAttribute("avgDailyWithdrawalForMonth", avgDailyWithdrawalForMonth);
        model.addAttribute("avgDailyWithdrawalForOneYear", avgDailyWithdrawalForOneYear);
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("firstDayWeek", firstDayWeek);
        model.addAttribute("lastDay", lastDay);
        model.addAttribute("weeks", weeks);
        model.addAttribute("dailyWithdrawal", dailyWithdrawal);
        model.addAttribute("dailyDeposit", dailyDeposit);
        return "wallet/calendar";
    }

    @PostMapping("/wallet/printCalendar")
    public String printCalendar(@RequestParam(required = false) Integer year,
                            @RequestParam(required = false) Integer month,
                            Model model) {

        LocalDate today = LocalDate.now();

        if (year == null || month == null) {
            year = today.getYear();
            month = today.getMonthValue();
        }

        LocalDate startDate = LocalDate.of(year, month, 1);

        DayOfWeek week = startDate.getDayOfWeek();
        int firstDayWeek = week.getValue();

        int lastDay = startDate.lengthOfMonth();

        List<List<Integer>> weeks = new ArrayList<>();
        List<Integer> days = new ArrayList<>();
        int getDay = 1;

        for (int i = 0; i <= 41; i++) {
            if (firstDayWeek <= i && getDay <= lastDay) {
                days.add(getDay);
                getDay++;
            } else {
                days.add(null);
            }
        }

        for (int j = 0; j < 6; j++) {
            List<Integer> weekWithForeach = new ArrayList<>(days.subList(0, 7));
            weeks.add(weekWithForeach);
            days.subList(0, 7).clear();
        }

        Long sumMoney = null;
        short yearShort = year.shortValue(); 
        byte monthByte = month.byteValue();  
        byte dayByte = 0;
        Long[] dailyWithdrawal = new Long[lastDay + 1];
        Long[] dailyDeposit = new Long[lastDay + 1];

        for (int i = 0; i <= lastDay; i++) {
            dayByte = (byte) i;

            sumMoney = walletService.findDailyWithdrawalByYearAndMonthAndDayNative(yearShort, monthByte, dayByte);
            dailyWithdrawal[i] = sumMoney;

            sumMoney = walletService.findDailyDepositByYearAndMonthAndDayNative(yearShort, monthByte, dayByte);
            dailyDeposit[i] = sumMoney;
        }

        Long totalDeposit = walletService.findTotalInOut(yearShort, monthByte, true);
        Long totalWithdrawal = walletService.findTotalInOut(yearShort, monthByte, false);

        int prevMonth = month - 1;
        int prevYear = year;

        if (prevMonth == 0) {
            prevMonth = 12;
            prevYear--;
        }

        Long totalWithdrawalPrevMonth = walletService.findTotalInOut((short) prevYear, (byte) prevMonth, false);
        Long avgDailyWithdrawalForMonth = walletService.avgDailyWithdrawalForMonth(yearShort, monthByte, false);
        Long avgDailyWithdrawalForOneYear = walletService.avgDailyWithdrawalForOneYear(yearShort, false);

        model.addAttribute("totalDeposit", totalDeposit);
        model.addAttribute("totalWithdrawal", totalWithdrawal);
        model.addAttribute("totalWithdrawalPrevMonth", totalWithdrawalPrevMonth);
        model.addAttribute("avgDailyWithdrawalForMonth", avgDailyWithdrawalForMonth);
        model.addAttribute("avgDailyWithdrawalForOneYear", avgDailyWithdrawalForOneYear);
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("firstDayWeek", firstDayWeek);
        model.addAttribute("lastDay", lastDay);
        model.addAttribute("weeks", weeks);
        model.addAttribute("dailyWithdrawal", dailyWithdrawal);
        model.addAttribute("dailyDeposit", dailyDeposit);
        return "wallet/printCalendar";
    }

    @GetMapping("/wallet/list")
    public String list(Model model,
                    @RequestParam(value="page", defaultValue="0") int page,
                    @RequestParam(value="startDate", required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                    @RequestParam(value="endDate", required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                    @RequestParam(value="memo", required=false) String memo,
                    @RequestParam(value="depositWithdrawal", required=false) Boolean depositWithdrawal,
                    @RequestParam(value="type", required=false) String type) {

        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime   = (endDate != null) ? endDate.atTime(23, 59, 59) : null;

        Page<Wallet> paging = walletService.getPage(page, startDateTime, endDateTime, memo, depositWithdrawal, type);

        model.addAttribute("paging", paging);

        int prevPage = paging.hasPrevious() ? page - 1 : 0;
        int nextPage = paging.hasNext() ? page + 1 : paging.getTotalPages() - 1;
        
        List<Integer> pages = IntStream.range(0, paging.getTotalPages())
                                    .boxed()
                                    .collect(Collectors.toList());
        
        
        model.addAttribute("prevPage", prevPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("pages", pages);
        
        model.addAttribute("memo", memo != null ? memo : "");
        model.addAttribute("depositWithdrawal", depositWithdrawal != null ? depositWithdrawal : "");
        model.addAttribute("type", type != null ? type : "");
        model.addAttribute("startDate", startDate != null ? startDate : "");
        model.addAttribute("endDate", endDate != null ? endDate : "");


        return "wallet/list";
    }

    @GetMapping("/wallet/graph")
    public String graph(Model model, @RequestParam(value="year", defaultValue="0") int year) {

        if(year == 0){
            Date today = new Date();
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
            year = Integer.valueOf(yearFormat.format(today));
        }
    
        List<WalletDto> sumDeposit = walletService.sumMoneyByYearAndMonth((short) year, true); // in
        List<WalletDto> sumWithdrawal = walletService.sumMoneyByYearAndMonth((short) year, false); // out

        Long sumDepositOneYear = walletService.sumMoneyForOneYear((short)year, true); // in
        Long avgMonthlyDepositForOneYear = walletService.avgMonthlyMoneyForOneYear((short)year, true); // in

        Long sumWithdrawalOneYear = walletService.sumMoneyForOneYear((short)year, false); // out
        Long avgMonthlyWithdrawalForOneYear = walletService.avgMonthlyMoneyForOneYear((short)year, false); // out
        Long avgDailyWithdrawalForOneYear = walletService.avgDailyWithdrawalForOneYear((short)year, false); // out

        model.addAttribute("sumDeposit", sumDeposit);
        model.addAttribute("sumWithdrawal", sumWithdrawal);

        model.addAttribute("sumDepositOneYear", sumDepositOneYear);
        model.addAttribute("sumWithdrawalOneYear", sumWithdrawalOneYear);

        model.addAttribute("avgMonthlyDepositForOneYear", avgMonthlyDepositForOneYear);
        model.addAttribute("avgMonthlyWithdrawalForOneYear", avgMonthlyWithdrawalForOneYear);
        model.addAttribute("avgDailyWithdrawalForOneYear", avgDailyWithdrawalForOneYear);

        model.addAttribute("year", year);
    
        return "wallet/graph"; 
    }

    @PostMapping("/wallet")
    public ResponseEntity<?> saveWallet(@RequestBody WalletDto walletDto) {
        Wallet saveWallet = new Wallet();

        String dateStr = walletDto.getChoosedDate();
        String[] parts = dateStr.split("-");

        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);

        saveWallet.setYear((short) year);
        saveWallet.setMonth((byte) month);
        saveWallet.setDay((byte) day);

        saveWallet.setInputDate(
            LocalDateTime.of(year, month, day, 0, 0) 
        );

        saveWallet.setMemo(walletDto.getMemo());
        saveWallet.setMoney(walletDto.getMoney());
        saveWallet.setType(walletDto.getType());
        saveWallet.setDepositWithdrawal(walletDto.getDepositWithdrawal());
        saveWallet.setCreatedDate(LocalDateTime.now());

        walletService.save(saveWallet);

        return ResponseEntity.ok().body(Map.of("success", true));
    }


    @GetMapping("wallet")
    public String wallet(@ModelAttribute WalletDto walletDto, Model model) {

        String dateStr = walletDto.getChoosedDate(); 
        String[] parts = dateStr.split("-");

        int year = Integer.parseInt(parts[0]); 
        int month = Integer.parseInt(parts[1]); 
        int day = Integer.parseInt(parts[2]); 

        List<Wallet> list = walletService.findByYearAndMonthAndDay((short) year,(byte) month,(byte) day);
        model.addAttribute("list", list);
        return "/wallet/calendarSubList";    
    }

    @DeleteMapping("wallet/delete")
    @ResponseBody
    public String deleteWallet(@RequestBody WalletDto walletDto) {
        Long id = walletDto.getId();
        walletService.deleteById(id);
        return "wallet history is deleted!!";
    }

    @PostMapping("/wallet/updateForm")
    public String walletUpdateForm(@RequestParam("id") Long id, Model model) { 
        Wallet wallet = walletService.findById(id)
        .orElseThrow(() -> new RuntimeException("Wallet not found"));

        
        String formattedDate = String.format("%04d-%02d-%02d",
                wallet.getYear(), wallet.getMonth(), wallet.getDay());

        model.addAttribute("choosedDate", formattedDate);
        model.addAttribute("wallet", wallet);
        return "/wallet/updateForm";
    }

    @PutMapping("wallet/update")
    public ResponseEntity<?> walletUpdate(@RequestBody WalletDto walletDto) {

        Long id = walletDto.getId();

        Wallet updateWallet = walletService.findById(id)
        .orElseThrow(() -> new RuntimeException("Wallet not found"));

        updateWallet.setDepositWithdrawal(walletDto.getDepositWithdrawal());
        updateWallet.setMemo(walletDto.getMemo());
        updateWallet.setMoney(walletDto.getMoney());
        updateWallet.setType(walletDto.getType());
        updateWallet.setUpdatedDate(LocalDateTime.now());
        walletService.save(updateWallet);

        return ResponseEntity.ok().body(Map.of("success", true));
    }
    
    @GetMapping("/wallet/excel")
    public ResponseEntity<Resource> downloadExcel(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String memo,
            @RequestParam(required = false) Boolean depositWithdrawal,
            @RequestParam(required = false) String type
    ) throws IOException {
        ByteArrayInputStream excelFile = walletService.exportWalletToExcel(startDate, endDate, memo, depositWithdrawal, type);
        InputStreamResource resource = new InputStreamResource(excelFile);

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=wallet_data.xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .body(resource);
    }
    
}
