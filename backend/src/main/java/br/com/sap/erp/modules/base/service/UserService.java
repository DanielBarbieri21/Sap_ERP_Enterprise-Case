package br.com.sap.erp.modules.base.service;

import br.com.sap.erp.core.domain.TenantContext;
import br.com.sap.erp.modules.base.domain.entity.Company;
import br.com.sap.erp.modules.base.domain.entity.User;
import br.com.sap.erp.modules.base.repository.CompanyRepository;
import br.com.sap.erp.modules.base.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            CompanyRepository companyRepository,
            @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findActiveByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
    }

    @Transactional
    public User create(User user, UUID companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        user.setCompany(company);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (companyId != null) {
            user.setTenantId(companyId); // Assumindo que tenantId = companyId
        }
        
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        UUID tenantId = TenantContext.getCurrentTenantId();
        if (tenantId != null) {
            return userRepository.findAllByTenantId(tenantId);
        }
        return userRepository.findAll().stream()
                .filter(u -> u.getActive() != null && u.getActive())
                .toList();
    }

    @Transactional
    public User update(UUID id, User userData) {
        User user = findById(id);
        
        user.setName(userData.getName());
        user.setPhone(userData.getPhone());
        user.setAvatarUrl(userData.getAvatarUrl());
        
        if (userData.getPassword() != null && !userData.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userData.getPassword()));
        }
        
        return userRepository.save(user);
    }

    @Transactional
    public void delete(UUID id) {
        User user = findById(id);
        user.softDelete();
        userRepository.save(user);
    }

    @Transactional
    public void updateLastLogin(UUID id) {
        User user = findById(id);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
    }
}
