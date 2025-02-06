
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Getter
@AllArgsConstructor
@Builder
public class UserLombok extends AuthorizationUser {
    private String email;
    private String password;
    private String name;
}
