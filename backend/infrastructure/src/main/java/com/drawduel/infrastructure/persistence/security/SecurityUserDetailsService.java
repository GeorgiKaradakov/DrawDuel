package com.drawduel.infrastructure.persistence.security;

import com.drawduel.domain.ports.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {
  private final UserRepository userRepo;

  public UserDetails loadUserByUsername(
      String username) { // it is actually an identifier (both email or username)
    return userRepo
        .findByEmail(username)
        .or(() -> userRepo.findByUsername(username))
        .map(SecurityUser::new)
        .orElseThrow(() -> new RuntimeException("User not found"));
  }
}
