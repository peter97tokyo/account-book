

package jp.peter.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.peter.account.entity.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    
}