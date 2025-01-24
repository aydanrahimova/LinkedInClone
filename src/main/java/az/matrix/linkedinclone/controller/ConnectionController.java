package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.response.ConnectionResponse;
import az.matrix.linkedinclone.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/connections")
@RequiredArgsConstructor
public class ConnectionController {
    private final ConnectionService connectionService;

    @GetMapping
    public Page<ConnectionResponse> getConnectionsOfUser(@RequestParam Long userId,@PageableDefault Pageable pageable) {
        return connectionService.getConnectionsOfUser(userId,pageable);
    }

    @PostMapping
    public ConnectionResponse sendConnectionRequest(@RequestParam Long receiverId) {
        return connectionService.sendConnectionRequest(receiverId);
    }

    @PatchMapping("/{id}/accept")
    public ConnectionResponse acceptConnectionRequest(@PathVariable Long id) {
        return connectionService.acceptConnectionRequest(id);
    }

    @PatchMapping("/{id}/reject")
    public ConnectionResponse rejectConnectionRequest(@PathVariable Long id) {
        return connectionService.rejectConnectionRequest(id);
    }

    @PatchMapping("/{id}")
    public void deleteConnection(@PathVariable Long id) {
        connectionService.deleteConnection(id);
    }
}
