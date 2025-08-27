

package jp.peter.account.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.peter.account.entity.Wallet;
import java.util.List;
import java.util.Optional;


public interface WalletRepository extends JpaRepository<Wallet, Long> {

       @Query(value = "SELECT SUM(money) FROM wallet WHERE year = :year AND month = :month AND day = :day AND deposit_withdrawal = FALSE", nativeQuery = true)
       Long findDailyWithdrawalByYearAndMonthAndDayNative(@Param("year") short year, @Param("month") byte month, @Param("day") byte day);

       @Query(value = "SELECT SUM(money) FROM wallet WHERE year = :year AND month = :month AND day = :day AND deposit_withdrawal = TRUE", nativeQuery = true)
       Long findDailyDepositByYearAndMonthAndDayNative(@Param("year") short year, @Param("month") byte month, @Param("day") byte day);

       @Query(value = "SELECT SUM(money) FROM wallet WHERE year = :year AND month = :month AND deposit_withdrawal = :deposit_withdrawal", nativeQuery = true)
       Long findByYearAndMonthAndDepositWithdrawalNative(@Param("year") short year, @Param("month") byte month, @Param("deposit_withdrawal") boolean depositWithdrawal);

       List<Wallet> findByYearAndMonthAndDay(short year, byte month, byte day);

       void deleteById(Long id);
       
}