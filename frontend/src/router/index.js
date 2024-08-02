import Vue from 'vue';
import VueRouter from 'vue-router';
import LoginForm from "@/components/login/LoginForm.vue";
import MainPage from "@/components/MainPage.vue";

import problemTest from "@/components/test/ProblemTest.vue";
import MyWorkbook from "@/components/workbook/MyWorkbook.vue";
import WorkbookDetail from "@/components/workbook/WorkbookDetail.vue";
import ScoringTest from "@/components/test/ScoringTest.vue";
import SignupForm from "@/components/login/SignupForm.vue";

import NoticeForm from "@/components/notice/NoticeForm.vue";
import NoticeDetail from "@/components/notice/NoticeDetail.vue";
import NoticeList from "@/components/notice/NoticeList.vue";
import NoticeUpdateForm from "@/components/notice/NoticeUpdateForm.vue";
import TestPage from "@/components/TestPage.vue";


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
        path: '/workbookDetail/:id',
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
    },
    {
        path: '/noticeList',
        name: 'NoticeForm',
        component: NoticeList
    },
    {
        path: '/notice/:id',
        name: 'NoticeDetail',
        component: NoticeDetail
    },
    {
        path: '/noticeForm',
        name: 'NoticeForm',
        component: NoticeForm
    },
    {
        path: '/noticeUpdateForm',
        name: 'NoticeUpdateForm',
        component: NoticeUpdateForm
    },
    {
        path: '/test',
        name: 'TestPage',
        component: TestPage
    }
];


const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
});

export default router;