<template>
  <div class="main-page">
    <button class="button button--ujarak button--border-thin button--text-thick">테스트 시작</button>
  </div>
</template>

<script>
export default {
  name: 'MainPage',
  data() {
    return {
      currentCategory: 'recent',
      hoveredWorkbook: null,
      workbooks: [
        // 여기에 실제 데이터를 넣으세요
        { id: 1, title: "수학 기초", author: "김철수", date: "2024-08-01", commentCount: 5, likes: 10 },
        { id: 2, title: "영어 문법", author: "이영희", date: "2024-08-02", commentCount: 3, likes: 8 },
        // ... 더 많은 문제집 데이터
      ]
    }
  },
  computed: {
    filteredWorkbooks() {
      // 실제로는 여기서 카테고리에 따라 필터링을 구현해야 합니다
      return this.workbooks;
    }
  },
  methods: {
    setCategory(category) {
      this.currentCategory = category;
    },
    goToWorkbookDetail(id) {
      // 실제로는 여기서 라우팅을 구현해야 합니다
      console.log(`문제집 ${id}의 상세 페이지로 이동`);
    },
    likeWorkbook(id) {
      // 실제로는 여기서 좋아요 기능을 구현해야 합니다
      console.log(`문제집 ${id}에 좋아요`);
    }
  }
}
</script>

<style scoped>

.button {
  float: left;
  min-width: 150px;
  max-width: 250px;
  display: block;
  margin: 1em;
  padding: 1em 2em;
  border: none;
  background: none;
  color: inherit;
  vertical-align: middle;
  position: relative;
  font-size: 1rem;
  z-index: 1;
  -webkit-backface-visibility: hidden;
  -moz-osx-font-smoothing: grayscale;
  border: 1px solid;
  font-weight: 600;
}

.button--ujarak {
  -webkit-transition: border-color 0.4s, color 0.4s;
  transition: border-color 0.4s, color 0.4s;
}
.button--ujarak::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: #37474f;
  z-index: -1;
  opacity: 0;
  -webkit-transform: scale3d(0.7, 1, 1);
  transform: scale3d(0.7, 1, 1);
  -webkit-transition: -webkit-transform 0.4s, opacity 0.4s;
  transition: transform 0.4s, opacity 0.4s;
  -webkit-transition-timing-function: cubic-bezier(0.2, 1, 0.3, 1);
  transition-timing-function: cubic-bezier(0.2, 1, 0.3, 1);
}
.button--ujarak.button--round-s::before {
  border-radius: 2px;
}
.button--ujarak.button--inverted::before {
  background: #7986CB;
}
.button--ujarak,
.button--ujarak::before {
  -webkit-transition-timing-function: cubic-bezier(0.2, 1, 0.3, 1);
  transition-timing-function: cubic-bezier(0.2, 1, 0.3, 1);
}
.button--ujarak:hover {
  color: #fff;
  border-color: #37474f;
}
.button--ujarak.button--inverted:hover {
  color: #37474F;
  border-color: #fff;
}
.button--ujarak:hover::before {
  opacity: 1;
  -webkit-transform: translate3d(0, 0, 0);
  transform: translate3d(0, 0, 0);
}
.main-page{
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f0f0;
  margin: 0;
}
</style>