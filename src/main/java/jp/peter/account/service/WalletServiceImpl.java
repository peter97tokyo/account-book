package jp.peter.account.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jp.peter.account.entity.Wallet;
import jp.peter.account.repository.WalletRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class WalletServiceImpl implements WalletService {
    
    private final WalletRepository walletRepository;

    @Override
    public Wallet save(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    @Override
    public List<Wallet> findAll() {
        return walletRepository.findAll();
    }
}
