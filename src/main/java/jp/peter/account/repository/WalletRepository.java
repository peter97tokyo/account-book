

package jp.peter.account.repository;




import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.peter.account.entity.Wallet;
import java.util.List;


public interface WalletRepository extends JpaRepository<Wallet, Long>, JpaSpecificationExecutor<Wallet> {

       @Query(value = "SELECT SUM(money) FROM wallet WHERE year = :year AND month = :month AND day = :day AND deposit_withdrawal = FALSE", nativeQuery = true)
       Long findDailyWithdrawalByYearAndMonthAndDayNative(@Param("year") short year, @Param("month") byte month, @Param("day") byte day);

       @Query(value = "SELECT SUM(money) FROM wallet WHERE year = :year AND month = :month AND day = :day AND deposit_withdrawal = TRUE", nativeQuery = true)
       Long findDailyDepositByYearAndMonthAndDayNative(@Param("year") short year, @Param("month") byte month, @Param("day") byte day);

       @Query(value = "SELECT SUM(money) FROM wallet WHERE year = :year AND month = :month AND deposit_withdrawal = :deposit_withdrawal", nativeQuery = true)
       Long findByYearAndMonthAndDepositWithdrawalNative(@Param("year") short year, @Param("month") byte month, @Param("deposit_withdrawal") boolean depositWithdrawal);

       List<Wallet> findByYearAndMonthAndDay(short year, byte month, byte day);

       void deleteById(Long id);

       Page<Wallet> findAll(Pageable pageable);
       
       @Query(value = "SELECT year, month, SUM(money) FROM wallet WHERE deposit_withdrawal = :deposit_withdrawal AND year = :year GROUP BY year, month", nativeQuery = true)
       List<Object[]> sumMoneyByYearAndMonth(@Param("year") short year, @Param("deposit_withdrawal") boolean depositWithdrawal);

       @Query(value = "SELECT SUM(money) FROM wallet WHERE year = :year AND deposit_withdrawal = :deposit_withdrawal", nativeQuery = true)
       Long sumMoneyByDepositWithdrawal(@Param("year") short year, @Param("deposit_withdrawal") boolean depositWithdrawal);

       @Query(value = """
              SELECT AVG(month_total)
              FROM (
                     SELECT SUM(money) AS month_total
                     FROM wallet
                     WHERE YEAR(input_date) = :year
                     AND deposit_withdrawal = :deposit_withdrawal
                     GROUP BY YEAR(input_date), MONTH(input_date)
              ) t;
              """, nativeQuery = true)
       Long avgMonthlyMoneyForOneYear(@Param("year") short year, @Param("deposit_withdrawal") boolean depositWithdrawal);

       @Query(value = """
              SELECT AVG(daily_total)
              FROM (
                     SELECT SUM(money) AS daily_total
                     FROM wallet
                     WHERE YEAR(input_date) = :year
                     AND deposit_withdrawal = :deposit_withdrawal
                     GROUP BY DATE(input_date)
              ) t
              """, nativeQuery = true)
       Long avgDailyWithdrawalForOneYear(@Param("year") short year, @Param("deposit_withdrawal") boolean depositWithdrawal);

       @Query(value = """
              SELECT AVG(daily_total)
              FROM (
                     SELECT SUM(money) AS daily_total
                     FROM wallet
                     WHERE YEAR(input_date) = :year
                     AND MONTH(input_date) = :month
                     AND deposit_withdrawal = :deposit_withdrawal
                     GROUP BY DATE(input_date)
              ) t
              """, nativeQuery = true)
       Long avgDailyWithdrawalForMonth(@Param("year") short year, @Param("month") byte month, @Param("deposit_withdrawal") boolean depositWithdrawal);

}      
