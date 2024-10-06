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

package space.lingu.lamp.security.authorization.adapter

import org.springframework.security.access.expression.SecurityExpressionRoot
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations
import org.springframework.security.core.Authentication
import space.lingu.lamp.security.authorization.AuthorizationScope
import java.util.function.Supplier

/**
 * @author RollW
 */
class ScopeBasedMethodSecurityExpressionRoot :
    SecurityExpressionRoot,
    MethodSecurityExpressionOperations {
    constructor(authentication: Authentication) : super(authentication)

    constructor(authenticationSupplier: Supplier<Authentication>) : super(authenticationSupplier)

    fun hasScope(scope: AuthorizationScope) : Boolean {
        return hasScope(scope.scope)
    }

    fun hasScope(scope: String) : Boolean {
        return hasAuthority(scope)
    }

    fun hasAnyScope(vararg scopes: AuthorizationScope) : Boolean {
        return scopes.any { hasScope(it) }
    }

    fun hasScopes(vararg scopes: AuthorizationScope) : Boolean {
        return scopes.all { hasScope(it) }
    }

    private var filterObject: Any? = null
    private var returnObject: Any? = null
    private var target: Any? = null

    override fun setFilterObject(filterObject: Any?) {
        this.filterObject = filterObject
    }

    override fun getFilterObject(): Any? {
        return filterObject
    }

    override fun setReturnObject(returnObject: Any?) {
        this.returnObject = returnObject
    }

    override fun getReturnObject(): Any? {
        return returnObject
    }

    override fun getThis(): Any? {
        return target
    }

    fun setThis(target: Any?) {
        this.target = target
    }
}