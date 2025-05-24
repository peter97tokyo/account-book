package jp.peter.account.controller;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountBookController {
    
    @GetMapping("/accountBook")
    public String accountBook(Model model) {
        Date today = new Date();

        SimpleDateFormat yearFormat = new SimpleDateFormat("YYYY");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        
        int year = Integer.valueOf(yearFormat.format(today));
        int month = Integer.valueOf(monthFormat.format(today));
        int day = Integer.valueOf(dayFormat.format(today));
        
        LocalDate startDate = LocalDate.of(year, month, 1);

        DayOfWeek week = startDate.getDayOfWeek();
        int iWeek = week.getValue();

        int lastDay = startDate.lengthOfMonth();
        
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("day", day);
        model.addAttribute("week", iWeek);
        model.addAttribute("lastDay", lastDay);
        
        return "accountBook"; 
    }
}
