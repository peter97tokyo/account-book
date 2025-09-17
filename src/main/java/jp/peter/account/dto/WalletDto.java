package jp.peter.account.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WalletDto extends SearchDto{

    private Long id;

    private String choosedDate;

    private Boolean depositWithdrawal;

    private String memo;

    private Long money;

    private String type;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private Long year;

    private Long month;

}
