package br.com.sap.erp.modules.base.service;

import br.com.sap.erp.modules.base.domain.entity.Company;
import br.com.sap.erp.modules.base.domain.entity.User;
import br.com.sap.erp.modules.base.repository.CompanyRepository;
import br.com.sap.erp.modules.base.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private Company company;
    private User user;

    @BeforeEach
    void setUp() {
        company = Company.builder()
                .name("Test Company")
                .cnpj("12345678000190")
                .build();
        company.setId(UUID.randomUUID());
        company.setActive(true);

        user = User.builder()
                .name("Test User")
                .email("test@test.com")
                .password("password123")
                .build();
        user.setId(UUID.randomUUID());
        user.setActive(true);
    }

    @Test
    void testCreateUser() {
        when(companyRepository.findById(any(UUID.class))).thenReturn(Optional.of(company));
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User created = userService.create(user, company.getId());

        assertNotNull(created);
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(anyString());
    }

    @Test
    void testLoadUserByUsername() {
        when(userRepository.findActiveByEmail(anyString())).thenReturn(Optional.of(user));

        var userDetails = userService.loadUserByUsername("test@test.com");

        assertNotNull(userDetails);
        assertEquals("test@test.com", userDetails.getUsername());
    }
}
