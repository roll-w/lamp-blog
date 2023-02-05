const base = 'localhost:5100';

export const baseUrl = `http://${base}`;
export const wsBaseUrl = `ws://${base}`;

const prefix = `${baseUrl}/api`;
const adminPrefix = `${baseUrl}/api/admin`;

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

const userList = `${adminPrefix}/users`;

const systemErrorLog = `${adminPrefix}/error/records`;

export default {
    passwordLogin,
    emailLogin,
    logout,
    register,
    tokenRefresh,
    articleCreate,
    articleDetails,

    userList,
    systemErrorLog,
}
