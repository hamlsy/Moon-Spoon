import Vue from 'vue';
import VueRouter from 'vue-router';
import LoginForm from "@/components/LoginForm.vue";
import MainPage from "@/components/MainPage.vue";
import problemTest from "@/components/ProblemTest.vue";
import MyWorkbook from "@/components/MyWorkbook.vue";
import WorkbookDetail from "@/components/WorkbookDetail.vue";
import ScoringTest from "@/components/ScoringTest.vue";
import SignupForm from "@/components/SignupForm.vue";

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
        path: '/user/signup',
        name: 'Signup',
        component: SignupForm
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
        path: '/problemTest',
        name: 'problemTest',
        component: problemTest
    },
    {
        path: '/scoringTest',
        name: 'ScoringTest',
        component: ScoringTest
    }
];


const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
});

export default router;