package jp.peter.account.controller;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import jp.peter.account.entity.Wallet;
import jp.peter.account.service.WalletService;

@Controller
public class AccountBookController {

    @Autowired
    private WalletService walletService;
    
    @GetMapping("/accountBook")
    public String accountBook(Model model) {
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
        Long[] sumMoneyArr = new Long[lastDay + 1];
        for (int i = 1; i <= lastDay; i++) {
            dayByte = (byte) i;
            sumMoney = walletService.findTotalMoneyByYearAndMonthAndDayNative((short) year, (byte) month, dayByte);
            sumMoneyArr[i] = sumMoney;    
        }
        
        
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("day", day);
        model.addAttribute("firstDayWeek", firstDayWeek);
        model.addAttribute("lastDay", lastDay);
        model.addAttribute("weeks", weeks);
        model.addAttribute("sumMoneyArr", sumMoneyArr);
        
        return "accountBook"; 
    }

    @PostMapping("/accountBook")
    public String accountBook(@RequestParam(required = false) Integer year,
                            @RequestParam(required = false) Integer month,
                            Model model) {
        // 현재 날짜 기준 기본값 설정
        LocalDate today = LocalDate.now();

        if (year == null || month == null) {
            year = today.getYear();
            month = today.getMonthValue();
        }

        // 해당 월의 1일
        LocalDate startDate = LocalDate.of(year, month, 1);

        // 요일 (1=월요일, 7=일요일)
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
        Long[] sumMoneyArr = new Long[lastDay + 1];
        for (int i = 0; i <= lastDay; i++) {
            dayByte = (byte) i;
            sumMoney = walletService.findTotalMoneyByYearAndMonthAndDayNative(yearShort, monthByte, dayByte);
            sumMoneyArr[i] = sumMoney;
        }

        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("firstDayWeek", firstDayWeek);
        model.addAttribute("lastDay", lastDay);
        model.addAttribute("weeks", weeks);
        model.addAttribute("sumMoneyArr", sumMoneyArr);
        return "accountBook";
    }

}
