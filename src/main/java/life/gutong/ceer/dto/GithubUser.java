package life.gutong.ceer.dto;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.dto
 * @ClassName: GithubUser
 * @Description: java类作用描述
 * @Author: ceer
 * @CreateDate: 2019/11/3 21:33
 */
public class GithubUser {
    private String name;
    private Long id;
    private String bio;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
