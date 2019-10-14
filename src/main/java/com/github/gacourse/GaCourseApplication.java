package com.github.gacourse;

import io.undertow.Undertow;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.SecurityConstraint;
import io.undertow.servlet.api.SecurityInfo;
import io.undertow.servlet.api.TransportGuaranteeType;
import io.undertow.servlet.api.WebResourceCollection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.undertow.UndertowBuilderCustomizer;
import org.springframework.boot.web.embedded.undertow.UndertowDeploymentInfoCustomizer;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GaCourseApplication {

    public static void main(String[] args) {
        SpringApplication.run(GaCourseApplication.class, args);
    }

    /**
     * https服务端口
     */
    @Value("${server.port}")
    private Integer httpsPort;

    @Bean
    public ServletWebServerFactory servletContainer() {
        UndertowServletWebServerFactory serverFactory = new UndertowServletWebServerFactory();
        serverFactory.addBuilderCustomizers(new UndertowBuilderCustomizer() {
            @Override
            public void customize(Undertow.Builder builder) {
                builder.addHttpListener(httpsPort + 1, "0.0.0.0");
            }
        });
        // 开启HTTP自动跳转至HTTPS
        serverFactory.addDeploymentInfoCustomizers(new UndertowDeploymentInfoCustomizer() {
            @Override
            public void customize(DeploymentInfo deploymentInfo) {
                deploymentInfo.addSecurityConstraint(new SecurityConstraint()
                        .addWebResourceCollection(new WebResourceCollection().addUrlPattern("/*"))
                        .setTransportGuaranteeType(TransportGuaranteeType.CONFIDENTIAL)
                        .setEmptyRoleSemantic(SecurityInfo.EmptyRoleSemantic.PERMIT))
                        .setConfidentialPortManager(exchange -> httpsPort);
            }
        });
        return serverFactory;
    }
}
