package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.entities.BlockedUser;
import edu.ftn.iss.eventplanner.entities.User;
import edu.ftn.iss.eventplanner.repositories.BlockedUserRepository;
import edu.ftn.iss.eventplanner.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlockedUserService {
    private final UserRepository userRepository;
    private final BlockedUserRepository blockedUserRepository;

    public BlockedUser blockUser(Integer blockerId, Integer blockedId) {
        if (blockerId.equals(blockedId)) {
            throw new IllegalArgumentException("User cannot block themselves.");
        }

        User blocker = userRepository.findById(blockerId)
                .orElseThrow(() -> new RuntimeException("Blocker not found"));
        User blocked = userRepository.findById(blockedId)
                .orElseThrow(() -> new RuntimeException("Blocked user not found"));

        boolean alreadyBlocked = blockedUserRepository.existsByBlockerAndBlocked(blocker, blocked);
        if (alreadyBlocked) {
            throw new IllegalStateException("User is already blocked.");
        }

        BlockedUser blockedUser = new BlockedUser();
        blockedUser.setBlocker(blocker);
        blockedUser.setBlocked(blocked);
        blockedUser.setBlockedAt(LocalDateTime.now());

        blockedUserRepository.save(blockedUser);
        return blockedUser;
    }

    public List<Integer> getBlockedUsers(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        List<BlockedUser> blockedUsers = blockedUserRepository.findByBlocker(user.get());
        return blockedUsers.stream().map(blockedUser -> blockedUser.getBlocked().getId()).collect(Collectors.toList());
    }

}