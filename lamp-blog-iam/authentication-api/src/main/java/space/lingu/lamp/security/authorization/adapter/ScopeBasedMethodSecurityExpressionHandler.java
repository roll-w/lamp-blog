/*
 * Copyright (C) 2023 RollW
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.lingu.lamp.security.authorization.adapter;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.TypeLocator;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import java.util.function.Supplier;

/**
 * @author RollW
 */
public class ScopeBasedMethodSecurityExpressionHandler extends
        DefaultMethodSecurityExpressionHandler {
    private TypeLocator typeLocator;

    public ScopeBasedMethodSecurityExpressionHandler() {
    }

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(
            Authentication authentication, MethodInvocation invocation) {
        ScopeBasedMethodSecurityExpressionRoot root =
                new ScopeBasedMethodSecurityExpressionRoot(authentication);
        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setTrustResolver(getTrustResolver());
        root.setRoleHierarchy(getRoleHierarchy());
        root.setDefaultRolePrefix(getDefaultRolePrefix());
        root.setThis(invocation.getThis());
        return root;
    }

    @Override
    public StandardEvaluationContext createEvaluationContextInternal(Authentication auth, MethodInvocation mi) {
        StandardEvaluationContext ctx = super.createEvaluationContextInternal(auth, mi);
        ctx.setRootObject(createSecurityExpressionRoot(auth, mi));
        setupContext(ctx);
        return ctx;
    }

    @Override
    public EvaluationContext createEvaluationContext(Supplier<Authentication> authentication, MethodInvocation mi) {
        StandardEvaluationContext ctx = (StandardEvaluationContext)
                super.createEvaluationContext(authentication, mi);
        ctx.setRootObject(createSecurityExpressionRoot(authentication.get(), mi));
        setupContext(ctx);
        return ctx;
    }

    public TypeLocator getTypeLocator() {
        return typeLocator;
    }

    public void setTypeLocator(TypeLocator typeLocator) {
        this.typeLocator = typeLocator;
    }

    private void setupContext(StandardEvaluationContext ctx) {
        ctx.setTypeLocator(typeLocator);
    }
}
