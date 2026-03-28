package ru.jobtracker.auth_service.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.jobtracker.auth_service.model.OAuth2Client;

@Repository
public interface OAuth2ClientRepository extends JpaRepository<OAuth2Client, UUID> {
  Optional<OAuth2Client> findByClientId(String clientId);
}
