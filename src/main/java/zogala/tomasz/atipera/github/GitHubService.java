package zogala.tomasz.atipera.github;

import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.util.Strings;
import org.kohsuke.github.GHBranch;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import zogala.tomasz.atipera.github.exceptions.GitHubConnectionException;
import zogala.tomasz.atipera.github.exceptions.RepositoryNotFoundException;
import zogala.tomasz.atipera.github.exceptions.UserNotFoundException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@Component
public class GitHubService {
    private static final Logger LOGGER = Logger.getLogger(GitHubService.class.getName());

    @Value("${github.username:}")
    private String gitHubUsername;
    @Value("${github.token:}")
    private String gitHubToken;

    private GitHub github;

    @PostConstruct
    private void initGitHubClient() {
        if (Strings.isBlank(this.gitHubUsername) || Strings.isBlank(this.gitHubToken)) {
            LOGGER.info("GitHub authentication is incomplete - using anonymous access");
            try {
                this.github = GitHub.connectAnonymously();
            } catch (IOException e) {
                throw new GitHubConnectionException(e);
            }
            return;
        }

        try {
            this.github = GitHub.connect(this.gitHubUsername, this.gitHubToken);
        } catch (IOException e) {
            throw new GitHubConnectionException(e);
        }
    }

    public List<Repository> getUserRepositories(final String username) throws UserNotFoundException {
        try {
            return this.github.getUser(username).getRepositories().values().stream()
                    .filter(repo -> !repo.isFork())
                    .map(this::mapRepository)
                    .toList();
        } catch (FileNotFoundException e) {
            throw new UserNotFoundException(String.format("User '%s' does not exist", username));
        } catch (IOException e) {
            throw new GitHubConnectionException(e);
        }
    }

    private Repository mapRepository(final GHRepository repositoryDto) {
        final var repository = new Repository();
        repository.setId(repositoryDto.getId());
        repository.setName(repositoryDto.getName());
        return repository;
    }

    public List<Branch> getRepositoryBranches(final long repositoryId) throws RepositoryNotFoundException {
        try {
            return this.github.getRepositoryById(repositoryId).getBranches().values().stream()
                    .map(this::mapBranch)
                    .toList();
        } catch (FileNotFoundException e) {
            throw new RepositoryNotFoundException(String.format("Repository with ID %d does not exist", repositoryId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Branch mapBranch(final GHBranch branchDto) {
        final var branch = new Branch();
        branch.setName(branchDto.getName());
        branch.setHeadSha(branchDto.getSHA1());
        return branch;
    }
}