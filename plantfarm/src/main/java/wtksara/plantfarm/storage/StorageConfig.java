package wtksara.plantfarm.storage;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Konfiguracja w celu korzystania z AWS
@Configuration
public class StorageConfig {

    // Dane niezbedne do uwierzytelnienia dostępu do Amazon S3
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String accessSecret;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    // Konfiguracja dostępu do AWS
    public AmazonS3 s3Client() {
        // Tworzenie połączenia klienta, aby uzyskać dostęp do Amazon S3.
        // Wykorzystanie poświadczenia bezpieczeństwa
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, accessSecret);
        // Konfiguracja klienta
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region).build();
    }
}
