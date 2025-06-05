package jp.peter.account.controller;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        

        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("day", day);
        model.addAttribute("firstDayWeek", firstDayWeek);
        model.addAttribute("lastDay", lastDay);
        model.addAttribute("weeks", weeks);
        
        return "accountBook"; 
    }
}
