package az.matrix.linkedinclone.scheduler;

import az.matrix.linkedinclone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class Scheduler {

    private final UserService userService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteUserTask() {
        userService.deleteUser();
    }

}
