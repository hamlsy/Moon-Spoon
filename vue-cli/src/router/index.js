import Vue from 'vue';
import VueRouter from 'vue-router';
import LoginForm from "@/components/LoginForm.vue";
import SignUpForm from "@/components/SignUpForm.vue";
import MainPage from "@/components/MainPage.vue";
import QuestionTest from "@/components/QuestionTest.vue";
import MyWorkbook from "@/components/MyWorkbook.vue";
import WorkbookDetail from "@/components/WorkbookDetail.vue";

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
        path: '/myWorkbook',
        name: "MyWorkbook",
        component: MyWorkbook
    },
    {
        path: '/workbookDetail',
        name: 'WorkbookDetail',
        component: WorkbookDetail
    },
    {
        path: '/questionTest',
        name: 'QuestionTest',
        component: QuestionTest
    }
];


const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
});

export default router;