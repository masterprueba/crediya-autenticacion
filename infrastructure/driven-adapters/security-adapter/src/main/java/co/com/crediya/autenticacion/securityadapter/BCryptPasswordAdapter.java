package co.com.crediya.autenticacion.securityadapter;

import co.com.crediya.autenticacion.model.login.gateways.PasswordEncoderPort;
import org.springframework.stereotype.Component;

import static org.springframework.security.crypto.bcrypt.BCrypt.checkpw;

@Component
public class BCryptPasswordAdapter implements PasswordEncoderPort {
    @Override
    public Boolean matches(CharSequence raw, String hash) {
        return checkpw(raw.toString(), hash);
    }
}
