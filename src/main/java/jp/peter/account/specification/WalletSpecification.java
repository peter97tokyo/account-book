

package jp.peter.account.specification;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import jp.peter.account.entity.Wallet;

public class WalletSpecification {

    public static Specification<Wallet> inputDateBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, cb) -> {
            if (start != null && end != null) {
                return cb.between(root.get("inputDate"), start, end);
            } else if (start != null) {
                return cb.greaterThanOrEqualTo(root.get("inputDate"), start);
            } else if (end != null) {
                return cb.lessThanOrEqualTo(root.get("inputDate"), end);
            }
            return null;
        };
    }

    public static Specification<Wallet> memoContains(String memo) {
        return (root, query, cb) ->
            (memo != null && !memo.isEmpty())
                ? cb.like(root.get("memo"), "%" + memo + "%")
                : null;
    }

    public static Specification<Wallet> depositWithdrawalEquals(Boolean depositWithdrawal) {
        return (root, query, cb) ->
            depositWithdrawal != null
                ? cb.equal(root.get("depositWithdrawal"), depositWithdrawal)
                : null;
    }

    public static Specification<Wallet> typeContains(String type) {
        return (root, query, cb) ->
            (type != null && !type.isEmpty())
                ? cb.like(root.get("type"), "%" + type + "%")
                : null;
    }
}
