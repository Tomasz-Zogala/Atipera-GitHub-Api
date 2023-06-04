package zogala.tomasz.atipera.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zogala.tomasz.atipera.api.dto.BranchDto;
import zogala.tomasz.atipera.api.dto.RepositoryDto;
import zogala.tomasz.atipera.github.*;
import zogala.tomasz.atipera.github.exceptions.RepositoryNotFoundException;
import zogala.tomasz.atipera.github.exceptions.UserNotFoundException;
import zogala.tomasz.atipera.middleware.RequestException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/github/users")
public class GitHubUsersController {

    @Autowired
    private GitHubService userService;

    @GetMapping(value = "/{username}/repositories", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RepositoryDto> getUserRepositories(@PathVariable("username") final String username) {
        try {
            return this.doGetUserRepositories(username);
        } catch (UserNotFoundException | RepositoryNotFoundException e) {
            throw new RequestException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    private List<RepositoryDto> doGetUserRepositories(final String username) throws UserNotFoundException, RepositoryNotFoundException {
        final List<RepositoryDto> repositoryDtos = new ArrayList<>();
        final List<Repository> repositories = this.userService.getUserRepositories(username);
        for (final Repository repository : repositories) {
            final var repositoryDto = new RepositoryDto();
            repositoryDto.setName(repository.getName());
            repositoryDto.setOwner(username);

            final List<BranchDto> branchDtos = this.userService.getRepositoryBranches(repository.getId()).stream()
                    .map(this::mapBranch)
                    .toList();
            repositoryDto.setBranches(branchDtos);

            repositoryDtos.add(repositoryDto);
        }
        return repositoryDtos;
    }

    private BranchDto mapBranch(final Branch branch) {
        final var branchDto = new BranchDto();
        branchDto.setName(branch.getName());
        branchDto.setHeadSha(branch.getHeadSha());
        return branchDto;
    }
}