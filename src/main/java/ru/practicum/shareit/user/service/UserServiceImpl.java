package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;


    @Override
    public Collection<UserDto> getAllUsersDto() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::jpaToDto)
                .toList();
    }

    @Override
    public UserDto getUserDtoById(Long userId) {
        return UserMapper.jpaToDto(userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с таким id не найден")));
    }

    @Override
    public UserDto addNewUser(UserDto userDto) {
        try {
            return UserMapper.jpaToDto(userRepository.save(UserMapper.dtoToJpa(userDto)));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Пользователь с email = '%s' уже существует".formatted(userDto.getEmail()));
        }
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User userToUpd = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с таким id не найден"));

        if (!userRepository.findByEmail(userDto.getEmail())
                .stream()
                .filter(userFromDB -> userFromDB.getEmail().equals(userDto.getEmail()))
                .allMatch(userFromDB -> userFromDB.getId().equals(userId))) {
            throw new ConflictException("Пользователь с email = '%s' уже существует".formatted(userDto.getEmail()));
        }

        userToUpd.setId(userId);

        if (Objects.nonNull(userDto.getName())
            && !userDto.getName().isBlank()
            && !userToUpd.getName().equals(userDto.getName()))
            userToUpd.setName(userDto.getName());

        if (Objects.nonNull(userDto.getEmail())
            && !userDto.getEmail().isBlank()
            && !userToUpd.getEmail().equals(userDto.getEmail()))
            userToUpd.setEmail(userDto.getEmail());

        return UserMapper.jpaToDto(userRepository.save(userToUpd));
    }

    @Override
    public void deleteUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("Пользователь с id %d не найден".formatted(userId));
        }
        userRepository.deleteById(userId);
    }
}
