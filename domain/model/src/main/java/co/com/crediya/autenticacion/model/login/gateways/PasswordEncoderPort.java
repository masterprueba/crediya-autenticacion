package co.com.crediya.autenticacion.model.login.gateways;


public interface PasswordEncoderPort {
    Boolean matches(CharSequence raw, String hash);
    String encode(CharSequence raw);
}
