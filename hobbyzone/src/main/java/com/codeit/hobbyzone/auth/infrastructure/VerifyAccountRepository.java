package com.codeit.hobbyzone.auth.infrastructure;

import com.codeit.hobbyzone.auth.domain.VerifyAccount;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerifyAccountRepository extends JpaRepository<VerifyAccount, Long> {

    Optional<VerifyAccount> findByEmail(String email);

    void deleteAllByEmail(String email);
}
