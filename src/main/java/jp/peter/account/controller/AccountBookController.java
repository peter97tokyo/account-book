package jp.peter.account.controller;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.peter.account.dto.WalletDto;
import jp.peter.account.entity.Wallet;
import jp.peter.account.service.WalletService;

@Controller
public class AccountBookController {

    @Autowired
    private WalletService walletService;
    
    @GetMapping("/accountBook/calendar")
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
        
        model.addAttribute("totalDeposit", totalDeposit);
        model.addAttribute("totalWithdrawal", totalWithdrawal);
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("day", day);
        model.addAttribute("firstDayWeek", firstDayWeek);
        model.addAttribute("lastDay", lastDay);
        model.addAttribute("weeks", weeks);
        model.addAttribute("dailyWithdrawal", dailyWithdrawal);
        model.addAttribute("dailyDeposit", dailyDeposit);
        
        return "accountBook/calendar"; 
    }

    @PostMapping("/accountBook/calendar")
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
        
        model.addAttribute("totalDeposit", totalDeposit);
        model.addAttribute("totalWithdrawal", totalWithdrawal);
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("firstDayWeek", firstDayWeek);
        model.addAttribute("lastDay", lastDay);
        model.addAttribute("weeks", weeks);
        model.addAttribute("dailyWithdrawal", dailyWithdrawal);
        model.addAttribute("dailyDeposit", dailyDeposit);
        return "accountBook/calendar";
    }

    @GetMapping("/accountBook/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page) {
        Page<Wallet> paging = walletService.getPage(page);
        model.addAttribute("paging", paging);
        return "accountBook/list"; 
    }

    @GetMapping("/accountBook/graph")
    public String graph(Model model) {
        Date today = new Date();

        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

        int year = Integer.valueOf(yearFormat.format(today));

        List<WalletDto> sumDeposit = walletService.sumMoneyByYearAndMonth((short) year, true); // in
        List<WalletDto> sumWithdrawal = walletService.sumMoneyByYearAndMonth((short) year, false); // out
        
        model.addAttribute("sumDeposit", sumDeposit);
        model.addAttribute("sumWithdrawal", sumWithdrawal);
        model.addAttribute("year", year);
    
        return "accountBook/graph"; 
    }
}
