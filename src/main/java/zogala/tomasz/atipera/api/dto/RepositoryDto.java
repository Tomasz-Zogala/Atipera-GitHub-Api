package zogala.tomasz.atipera.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class RepositoryDto {
    private String name;
    private String owner;
    private List<BranchDto> branches;
}