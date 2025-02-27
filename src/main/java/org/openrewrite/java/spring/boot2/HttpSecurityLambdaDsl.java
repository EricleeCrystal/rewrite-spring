/*
 * Copyright 2024 the original author or authors.
 * <p>
 * Licensed under the Moderne Source Available License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://docs.moderne.io/licensing/moderne-source-available-license
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openrewrite.java.spring.boot2;

import org.openrewrite.ExecutionContext;
import org.openrewrite.Preconditions;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.java.search.UsesType;

import java.util.Arrays;
import java.util.Collection;

public final class HttpSecurityLambdaDsl extends Recipe {

    private static final String FQN_HTTP_SECURITY = "org.springframework.security.config.annotation.web.builders.HttpSecurity";

    private static final Collection<String> APPLICABLE_METHOD_NAMES = Arrays.asList(
            "anonymous", "authorizeHttpRequests", "authorizeRequests", "cors", "csrf", "exceptionHandling", "formLogin",
            "headers", "httpBasic", "jee", "logout", "oauth2Client", "oauth2Login", "oauth2ResourceServer",
            "openidLogin", "portMapper", "rememberMe", "requestCache", "requestMatchers", "requiresChannel",
            "saml2Login", "securityContext", "servletApi", "sessionManagement", "x509");

    @Override
    public String getDisplayName() {
        return "Convert `HttpSecurity` chained calls into Lambda DSL";
    }

    @Override
    public String getDescription() {
        return "Converts `HttpSecurity` chained call from Spring Security pre 5.2.x into new lambda DSL style calls and removes `and()` methods.";
    }

    @Override
    public TreeVisitor<?, ExecutionContext> getVisitor() {
        return Preconditions.check(
                new UsesType<>(FQN_HTTP_SECURITY, true),
                new ConvertToSecurityDslVisitor<>(FQN_HTTP_SECURITY, APPLICABLE_METHOD_NAMES)
        );
    }

}
