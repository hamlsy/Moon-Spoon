import Vue from 'vue';
import VueRouter from 'vue-router';
import LoginForm from "@/components/LoginForm.vue";
import SignUpForm from "@/components/SignUpForm.vue";
import MainPage from "@/components/MainPage.vue";
import MyWorkBook from "@/components/MyWorkBook.vue";
import WorkBookDetail from "@/components/WorkBookDetail.vue";

Vue.use(VueRouter);

const routes = [
    {
        path:'/',
        redirect: "/mainPage",
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
    },
    {
        path: '/mainPage',
        name: "MainPage",
        component: MainPage
    },
    {
        path: '/myWorkBook',
        name: "MyWorkBook",
        component: MyWorkBook
    },
    {
        path: '/workBookDetail',
        name: 'WorkBookDetail',
        component: WorkBookDetail
    }
];


const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
});

export default router;