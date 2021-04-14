package vip.xiaonuo.common.pojo.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

/**
 * 用来包装一下角色名称
 *
 * @author yubaoshan
 * @date 2020/4/10 13:08
 */
@Data
@AllArgsConstructor
public class SnowyAuthority implements GrantedAuthority {

    private String authority;

    @Override
    public String getAuthority() {
        return authority;
    }

}
