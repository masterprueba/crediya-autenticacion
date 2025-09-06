package co.com.crediya.autenticacion.securityadapter;

import co.com.crediya.autenticacion.model.login.gateways.PasswordEncoderPort;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordAdapter implements PasswordEncoderPort {
    @Override
    public Boolean matches(CharSequence raw, String hash) {
        return BCrypt.checkpw(raw.toString(), hash);
    }
    @Override
    public String encode(CharSequence raw) {
        return BCrypt.hashpw(raw.toString(), BCrypt.gensalt() );
    }
}
