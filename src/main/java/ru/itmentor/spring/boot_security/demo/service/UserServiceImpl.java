package ru.itmentor.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmentor.spring.boot_security.demo.Repository.RoleRepository;
import ru.itmentor.spring.boot_security.demo.Repository.UserRepository;
import ru.itmentor.spring.boot_security.demo.configs.WebSecurityConfig;
import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    @Override
    public User addUser(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String bCryptPassword = encoder.encode(user.getPassword());
        user.setPassword(bCryptPassword);
        return userRepository.saveAndFlush(user);
    }

    @Transactional
    @Override
    public void delete(User user) {
        userRepository.deleteById(user.getId());
    }

    public void deleteUserById(long id) {
        Optional<User> userDelete = findById(id);

        if (userDelete.isPresent()) {
            User user = userDelete.get();
            delete(user);
        } else {
            throw new IllegalArgumentException("Invalid user Id:" + id);
        }
    }

    public User findUserById(long id) {
        return findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public Set<Role> getRole(Set<String> rolesId) {
        Set<Role> roles = new HashSet<>();
        for (String id : rolesId) {
            roles.add(getRoleById(Long.parseLong(id)));
        }
        return roles;
    }

    public Role getRoleById(long id) {
        return roleRepository.getRoleById(id);
    }
}
