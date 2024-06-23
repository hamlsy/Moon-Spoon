import Vue from 'vue';
import VueRouter from 'vue-router';
import LoginForm from "@/components/LoginForm.vue";
import SignUpForm from "@/components/SignUpForm.vue";

Vue.use(VueRouter);

const routes = [
    {
        path:'/',
        redirect: "/user/login",
    },
    {
        path: '/user/login',
        name: 'Login',
        component: LoginForm
    },
    {
        path: '/user/signUp',
        name: 'SignUp',
        component: SignUpForm
    }
];


const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
});

export default router;