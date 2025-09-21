package co.com.crediya.autenticacion.config;


import co.com.crediya.autenticacion.secrets.AwsRdsSecret;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.Option;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.r2dbc.spi.ConnectionFactories.get;
import static io.r2dbc.spi.ConnectionFactoryOptions.*;
import static io.r2dbc.pool.PoolingConnectionFactoryProvider.MAX_SIZE;

@Configuration
public class R2dbcSecretConfig {

    @Bean
    public ConnectionFactory connectionFactory(AwsRdsSecret s) {
        String db = "dbAutenticacion";
        String url = String.format("r2dbc:mysql://%s:%d/%s", s.getHost(), s.getPort(), db);

        ConnectionFactoryOptions opts = ConnectionFactoryOptions.parse(url)
                .mutate()
                .option(USER, s.getUsername())
                .option(PASSWORD, s.getPassword())
                .option(Option.valueOf("sslMode"), s.getSslMode() == null ? "REQUIRED" : s.getSslMode())
                .option(MAX_SIZE, 20)
                .build();

        return get(opts);
    }
}
