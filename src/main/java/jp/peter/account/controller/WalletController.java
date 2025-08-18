package jp.peter.account.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jp.peter.account.dto.WalletDto;
import jp.peter.account.entity.Wallet;
import jp.peter.account.service.WalletService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping("/wallet")
    public String saveWallet(@ModelAttribute WalletDto walletDto) {

        Wallet saveWallet = new Wallet();
        
        String dateStr = walletDto.getChoosedDate(); 
        String[] parts = dateStr.split("-");

        int year = Integer.parseInt(parts[0]); 
        int month = Integer.parseInt(parts[1]); 
        int day = Integer.parseInt(parts[2]); 

        saveWallet.setYear((short) year);  
        saveWallet.setMonth((byte) month); 
        saveWallet.setDay((byte) day);

        saveWallet.setMemo(walletDto.getMemo());
        saveWallet.setMoney(walletDto.getMoney());
        saveWallet.setType(walletDto.getType());
        saveWallet.setDepositWithdrawal(walletDto.getDepositWithdrawal());
        saveWallet.setCreatedDate(LocalDateTime.now());
        walletService.save(saveWallet);
        return "redirect:/accountBook";
    }

    @GetMapping("wallet")
    public String wallet(@ModelAttribute WalletDto walletDto, Model model) {

        String dateStr = walletDto.getChoosedDate(); 
        String[] parts = dateStr.split("-");

        int year = Integer.parseInt(parts[0]); 
        int month = Integer.parseInt(parts[1]); 
        int day = Integer.parseInt(parts[2]); 

        System.out.println(year);
        System.out.println(month);
        System.out.println(day);

        List<Wallet> list = walletService.findByYearAndMonthAndDay((short) year,(byte) month,(byte) day);
        model.addAttribute("list", list);
        return "/wallet/list";    
    }
}
