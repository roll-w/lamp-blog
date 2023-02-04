const prefix = 'http://localhost:5100/api';
const adminPrefix = 'http://localhost:5100/api/admin';

const passwordLogin = `${prefix}/user/login/password`;
const emailLogin = `${prefix}/user/login/email`;
const logout = `${prefix}/user/logout`;
const register = `${prefix}/user/register`;

const tokenRefresh = `${prefix}/auth/token/r`;

const articleDetails = (userId, articleId, admin) => {
    return `${admin ? adminPrefix : prefix}/${userId}/article/${articleId}`;
}

const articleCreate = (userId, admin) => {
    return `${admin ? adminPrefix : prefix}/${userId}/article`;
}

export default {
    passwordLogin,
    emailLogin,
    logout,
    register,
    tokenRefresh,
    articleCreate,
    articleDetails,
}
