package com.github.githubreposervice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.githubreposervice.entity.GitHubRepo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * GitHub repository Mapper interface
 */
@Mapper
public interface GitHubRepoMapper extends BaseMapper<GitHubRepo> {
    
    /**
     * Query by repository full name
     * 
     * @param fullName Repository full name
     * @return GitHub repository entity
     */
    GitHubRepo selectByFullName(@Param("fullName") String fullName);
    
    /**
     * Query by owner and repository name
     * 
     * @param owner Repository owner
     * @param repoName Repository name
     * @return GitHub repository entity
     */
    GitHubRepo selectByOwnerAndRepo(@Param("owner") String owner, 
                                     @Param("repoName") String repoName);
}
