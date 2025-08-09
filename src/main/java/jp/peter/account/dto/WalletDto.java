package jp.peter.account.dto;

import java.time.LocalDateTime;

import org.antlr.v4.runtime.misc.NotNull;

import jp.peter.account.entity.Wallet;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WalletDto {

    private Long id;

    @NotNull
    private Boolean depositWithdrawal;

    private String memo;

    @NotNull
    private Long money;

    @NotNull
    private String type;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

}
