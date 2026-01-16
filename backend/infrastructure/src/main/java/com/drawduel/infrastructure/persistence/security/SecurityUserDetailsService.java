package com.drawduel.infrastructure.persistence.security;

import com.drawduel.domain.ports.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {
  private final UserRepository userRepo;

  public UserDetails loadUserById(UUID userId) {
    return userRepo
        .findById(userId)
        .map(SecurityUser::new)
        .orElseThrow(() -> new RuntimeException("User not found"));
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'loadUserByUsername'");
  }
}
