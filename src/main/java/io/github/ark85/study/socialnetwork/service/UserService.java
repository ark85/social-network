package io.github.ark85.study.socialnetwork.service;

import io.github.ark85.study.socialnetwork.configuration.ImportProperties;
import io.github.ark85.study.socialnetwork.model.User;
import io.github.ark85.study.socialnetwork.model.api.request.UserCreateRequest;
import io.github.ark85.study.socialnetwork.model.api.response.UserCreateResponse;
import io.github.ark85.study.socialnetwork.model.api.response.UserGetResponse;
import io.github.ark85.study.socialnetwork.repository.UserRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Validated
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImportProperties importProperties;

    @Override
    public @NullMarked UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserById(UUID.fromString(username));
    }

    public User getUserById(UUID id) {
        User user = userRepository.getUserById(id);
        if (user == null) {
            throw new UsernameNotFoundException("User is not found.");
        }
        return user;
    }

    public UserCreateResponse registerUser(UserCreateRequest userCreateRequest) {
        User user = new User(userCreateRequest);
        String passwordHash = passwordEncoder.encode(userCreateRequest.getPassword());
        user.setPasswordHash(passwordHash);
        return new UserCreateResponse(userRepository.createUser(user));
    }

    public List<UserGetResponse> searchUsersByFirstNameAndSecondName(
            @NotBlank String firstName, @NotBlank String secondName) {
        List<User> users = userRepository.searchUsersByFirstNameAndSecondName(firstName, secondName);
        return users.stream().map(UserGetResponse::new).toList();
    }

    public void importUsers(MultipartFile usersFile) throws IOException {
        log.info("Start importUsers with batchSize = {}", importProperties.getBatchSize());
        List<User> usersToCreate = new ArrayList<>(importProperties.getBatchSize());
        // generate any password
        String passwordHash = passwordEncoder.encode(UUID.randomUUID().toString());
        try (
                Reader reader = new InputStreamReader(usersFile.getInputStream(), StandardCharsets.UTF_8);
                CSVParser parser = CSVFormat.DEFAULT.builder()
                        .setHeader()
                        .setSkipHeaderRecord(true).get()
                        .parse(reader)
        ) {
            for (CSVRecord userRecord : parser) {
                User user = createUserFromCsvRecord(userRecord, passwordHash);
                usersToCreate.add(user);

                if (usersToCreate.size() == importProperties.getBatchSize()) {
                    userRepository.createAll(usersToCreate);
                    usersToCreate.clear();
                }
            }
        }

        if (!usersToCreate.isEmpty()) {
            userRepository.createAll(usersToCreate);
        }
        log.info("importUsers is successfully finished");
    }

    private User createUserFromCsvRecord(CSVRecord userRecord, String commonPasswordHash) {
        String[] firstSecondName = userRecord.get(0).split("\\s+");
        return new User(
                null,
                firstSecondName[0],
                firstSecondName[1],
                commonPasswordHash,
                LocalDate.parse(userRecord.get(1)),
                "",
                userRecord.get(2)
        );
    }
}
