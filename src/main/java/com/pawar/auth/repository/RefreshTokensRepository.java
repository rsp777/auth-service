package com.pawar.auth.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.pawar.auth.entity.RefreshTokens;
import com.pawar.auth.exception.TokenNotFoundException;

@Repository
public interface RefreshTokensRepository extends JpaRepository<RefreshTokens, Long> {

    @Query("SELECT rt FROM RefreshTokens rt WHERE rt.user.userId = :id AND rt.revoked = false")
    Optional<RefreshTokens> findByUser_Id(Long id) throws TokenNotFoundException;

    @Query("SELECT rt FROM RefreshTokens rt WHERE rt.token = :token")
    Optional<RefreshTokens> findBytoken(String token);

    // @Modifying
    // @NamedNativeQuery(value = "update RefreshTokens set revoked = :revoked where
    // id = :id")
    // Optional<?> revoked(@Param("revoked") Boolean revoked,@Param("id") Long id);

}
