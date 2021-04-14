package vip.xiaonuo.sys.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.mail.MailAccount;
import vip.xiaonuo.common.context.constant.ConstantContextHolder;
import vip.xiaonuo.core.email.MailSender;
import vip.xiaonuo.core.email.modular.SimpleMailSender;
import vip.xiaonuo.common.pojo.email.EmailConfigs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 邮件发送控制器
 *
 * @author yubaoshan
 * @date 2020/6/6 22:27
 */
@Configuration
public class MailSenderConfig {

    /**
     * 邮件发射器
     *
     * @author yubaoshan
     * @date 2020/6/9 23:13
     */
    @Bean
    public MailSender mailSender() {
        EmailConfigs emailConfigs = ConstantContextHolder.getEmailConfigs();
        MailAccount mailAccount = new MailAccount();
        BeanUtil.copyProperties(emailConfigs, mailAccount);
        return new SimpleMailSender(mailAccount);
    }

}
